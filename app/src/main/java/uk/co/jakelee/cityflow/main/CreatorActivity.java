package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.StorageHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Text;

public class CreatorActivity extends Activity {
    private boolean displayImported = false;
    final private static int INTENT_CAMERA = 1234;
    final private static int INTENT_FILE = 1235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateText();
        populatePuzzles();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.myPuzzles)).setText(Text.get("CREATOR_CREATED"));
        ((TextView) findViewById(R.id.othersPuzzles)).setText(Text.get("CREATOR_IMPORTED"));
        ((TextView) findViewById(R.id.importFromCamera)).setText(Text.get("WORD_IMPORT"));
        ((TextView) findViewById(R.id.importFromFile)).setText(Text.get("WORD_IMPORT"));
        ((TextView) findViewById(R.id.newPuzzle)).setText(Text.get("CREATOR_NEW_PUZZLE"));
    }

    public void populatePuzzles() {
        LinearLayout puzzleContainer = ((LinearLayout)findViewById(R.id.puzzleContainer));
        populatePuzzles(puzzleContainer);
        updateTabDisplay();
    }

    public void populatePuzzles(LinearLayout puzzleContainer) {
        final CreatorActivity activity = this;
        puzzleContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 20);

        List<Puzzle> puzzles = Puzzle.getCustomPuzzles(displayImported);
        for (final Puzzle puzzle : puzzles) {
            PuzzleCustom puzzleCustom = puzzle.getCustomData();
            View inflatedView = inflater.inflate(R.layout.custom_puzzle_preview, null);
            RelativeLayout othersPuzzle = (RelativeLayout) inflatedView.findViewById(R.id.puzzleLayout);

            // If own tested level, or imported completed level, display green.
            boolean greenBackground = (!displayImported && puzzleCustom.hasBeenTested()) || (displayImported && puzzle.hasCompletionStar());
            othersPuzzle.setBackgroundResource(greenBackground ? R.drawable.ui_panel_green : R.drawable.ui_panel_grey);
            othersPuzzle.findViewById(R.id.deleteButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    AlertDialogHelper.confirmPuzzleDeletion(activity, puzzle);
                }
            });

            othersPuzzle.findViewById(R.id.moreButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CustomInfoActivity.class);
                    intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });

            if (displayImported) {
                ((TextView) othersPuzzle.findViewById(R.id.actionButton)).setText(R.string.icon_play);
                othersPuzzle.findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                        intent.putExtra(Constants.INTENT_PUZZLE_TYPE, true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                });
            } else {
                ((TextView) othersPuzzle.findViewById(R.id.actionButton)).setText(R.string.icon_edit);
                othersPuzzle.findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                });
            }

            ((TextView)othersPuzzle.findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());

            puzzleContainer.addView(othersPuzzle, lp);
        }

        findViewById(R.id.newPuzzleWrapper).setVisibility(displayImported ? View.GONE : View.VISIBLE);
        findViewById(R.id.importPuzzle).setVisibility(displayImported ? View.VISIBLE : View.GONE);
    }

    public void newPuzzle(View v) {
        AlertDialogHelper.puzzleCreationOptions(this);
    }

    public void importFromCamera(View v) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, INTENT_CAMERA);
        } catch (Exception e) {
            Toast.makeText(this, "Scanning QR codes requires a barcode reader to be installed!", Toast.LENGTH_SHORT).show();
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    public void importFromFile(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), INTENT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String puzzleString = "";
            if (requestCode == INTENT_CAMERA) {
                puzzleString = data.getStringExtra("SCAN_RESULT");
            } else if (requestCode == INTENT_FILE) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    puzzleString = StorageHelper.readQRImage(bitmap);
                } catch (Exception e) {
                    AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.FILE_IMPORT_FAIL));
                }
            }

            if (!puzzleString.equals("") && PuzzleShareHelper.importPuzzleString(puzzleString, false)) {
                AlertHelper.success(this, Text.get("ALERT_PUZZLE_IMPORTED"));
            } else if (requestCode == INTENT_CAMERA) {
                AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.CAMERA_IMPORT_FAIL));
            } else if (requestCode == INTENT_FILE) {
                AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.FILE_IMPORT_FAIL));
            }
        }
    }

    public void changeTab(View v) {
        if (v.getTag().equals("displayOwn") && displayImported) {
            displayImported = false;
        } else if (v.getTag().equals("displayImported") && !displayImported) {
            displayImported = true;
        }

        populatePuzzles();
        updateTabDisplay();
    }

    public void updateTabDisplay() {
        ((TextView)findViewById(R.id.myPuzzles)).setTextColor(displayImported ? Color.GRAY : Color.BLACK);
        ((TextView)findViewById(R.id.othersPuzzles)).setTextColor(displayImported ? Color.BLACK : Color.GRAY);
    }
}

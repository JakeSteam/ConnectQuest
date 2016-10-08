package uk.co.jakelee.cityflow.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aitorvs.android.allowme.AllowMeActivity;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.PermissionHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.StorageHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Text;

public class ExportActivity extends AllowMeActivity {
    private Puzzle puzzle;
    private PuzzleCustom puzzleCustom;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        int puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);
        puzzle = Puzzle.getPuzzle(puzzleId);
        puzzleCustom = puzzle.getCustomData();

        populateCard();
    }

    private void populateCard() {
        ((TextView)findViewById(R.id.saveCard)).setText(Text.get("DIALOG_BUTTON_SAVE"));
        ((TextView)findViewById(R.id.shareCard)).setText(Text.get("DIALOG_BUTTON_SHARE"));

        ((TextView)findViewById(R.id.puzzleName)).setText(puzzle.getName());
        ((TextView)findViewById(R.id.puzzleAuthor)).setText("By: " + puzzleCustom.getAuthor());

        ((ImageView)findViewById(R.id.puzzleImage)).setImageDrawable(dh.getCustomPuzzleDrawable(puzzle.getPuzzleId()));
        ((TextView)findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());

        String export = PuzzleShareHelper.getPuzzleString(puzzle);
        StorageHelper.fillWithQrDrawable((ImageView)findViewById(R.id.puzzleQrCode), export);
    }

    public void save(View view) {
        PermissionHelper.runIfPossible(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Runnable() {
            @Override
            public void run() {
                save();
            }
        });
    }

    public void share(View view) {
        PermissionHelper.runIfPossible(Manifest.permission.WRITE_EXTERNAL_STORAGE, new Runnable() {
            @Override
            public void run() {
                share();
            }
        });
    }

    private void save() {
        String filename = StorageHelper.saveCardImage(this, puzzle.getPuzzleId());
        if (filename.equals("")) {
            AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.CARD_NOT_SAVED));
        } else {
            AlertHelper.success(this, Text.get("ALERT_CARD_SAVED"));
        }
    }

    private void share() {
        String filename = StorageHelper.saveCardImage(this, puzzle.getPuzzleId());

        if (filename.equals("")) {
            AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.CARD_NOT_SAVED));
        } else {
            Uri bmpUri = Uri.parse(filename);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            intent.setType("image/png");
            startActivity(intent);
        }
    }
}

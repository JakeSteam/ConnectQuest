package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.RandomHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Text;

public class CreatorActivity extends Activity {
    private boolean displayOthers = false;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        dh = DisplayHelper.getInstance(this);
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
        ((TextView) findViewById(R.id.importPuzzle)).setText(Text.get("CREATOR_IMPORT_PUZZLE"));
        ((TextView) findViewById(R.id.newPuzzle)).setText(Text.get("CREATOR_NEW_PUZZLE"));
    }

    public void populatePuzzles() {
        LinearLayout puzzleContainer = ((LinearLayout)findViewById(R.id.puzzleContainer));
        populatePuzzles(puzzleContainer);
        updateTabDisplay();
    }

    public void populatePuzzles(LinearLayout puzzleContainer) {
        puzzleContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        List<Puzzle> puzzles = Puzzle.getCustomPuzzles(displayOthers);
        for (final Puzzle puzzle : puzzles) {
            PuzzleCustom puzzleCustom = puzzle.getCustomData();
            View inflatedView = inflater.inflate(R.layout.custom_puzzle_preview, null);
            RelativeLayout othersPuzzle = (RelativeLayout) inflatedView.findViewById(R.id.puzzleLayout);

            othersPuzzle.findViewById(R.id.deleteButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    puzzle.safelyDelete();
                    populatePuzzles();
                }
            });

            othersPuzzle.findViewById(R.id.moreButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CustomInfoActivity.class);
                    intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                    startActivity(intent);
                }
            });

            if (displayOthers) {
                ((TextView) othersPuzzle.findViewById(R.id.actionButton)).setText(R.string.icon_play);
                othersPuzzle.findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                        intent.putExtra(Constants.INTENT_PUZZLE_TYPE, puzzle.getPackId() == 0);
                        startActivity(intent);
                    }
                });
            } else {
                ((TextView) othersPuzzle.findViewById(R.id.actionButton)).setText(R.string.icon_edit);
                othersPuzzle.findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
                        startActivity(intent);
                    }
                });
            }

            ((TextView)othersPuzzle.findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());

            puzzleContainer.addView(othersPuzzle);
        }

        findViewById(R.id.newPuzzle).setVisibility(displayOthers ? View.GONE : View.VISIBLE);
        findViewById(R.id.importPuzzle).setVisibility(displayOthers ? View.VISIBLE : View.GONE);
    }

    public void newPuzzle(View v) {
        AlertDialogHelper.puzzleCreationOptions(this);
    }

    public void importPuzzle(View v) {
        Puzzle puzzle = Puzzle.getPuzzle(RandomHelper.getNumber(98, 101));
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        PuzzleShareHelper.importPuzzleString(backup, false);
        populatePuzzles();
    }

    public void showInfo(View v) {

    }

    public void changeTab(View v) {
        displayOthers = !displayOthers;
        populatePuzzles();
        updateTabDisplay();
    }

    public void updateTabDisplay() {
        ((TextView)findViewById(R.id.myPuzzles)).setTextColor(displayOthers ? Color.GRAY : Color.BLACK);
        ((TextView)findViewById(R.id.othersPuzzles)).setTextColor(displayOthers ? Color.BLACK : Color.GRAY);
    }
}

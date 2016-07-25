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
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.RandomHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Puzzle_Custom;

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

        populatePuzzles();
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
            Puzzle_Custom puzzleCustom = puzzle.getCustomData();
            View inflatedView = inflater.inflate(displayOthers ? R.layout.custom_puzzle_preview_other : R.layout.custom_puzzle_preview_mine, null);
            RelativeLayout othersPuzzle = (RelativeLayout) inflatedView.findViewById(R.id.puzzleLayout);

            TextView deleteButton = (TextView) othersPuzzle.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    puzzle.safelyDelete();
                    populatePuzzles();
                }
            });

            TextView infoButton = (TextView) othersPuzzle.findViewById(R.id.infoButton);
            infoButton.setTag(puzzle.getPuzzleId());

            TextView playButton = (TextView) othersPuzzle.findViewById(R.id.playButton);
            playButton.setTag(R.id.puzzleId, puzzle.getPuzzleId());
            playButton.setTag(R.id.puzzleIsCustom, puzzle.getPackId() == 0);
            if (displayOthers) {
                playButton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                        intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag(R.id.puzzleId));
                        intent.putExtra(Constants.INTENT_PUZZLE_TYPE, (boolean) v.getTag(R.id.puzzleIsCustom));
                        startActivity(intent);
                    }
                });
            }


            ((TextView)othersPuzzle.findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());

            puzzleContainer.addView(othersPuzzle);
        }

        TextView importButton = dh.createTextView(displayOthers ? "Import Puzzle" : "Create Puzzle", 25, Color.BLACK);
        importButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Puzzle puzzle = Puzzle.getPuzzle(RandomHelper.getNumber(98, 101));
                String backup = PuzzleShareHelper.getPuzzleString(puzzle);
                PuzzleShareHelper.importPuzzleString(backup);
                populatePuzzles();
            }
        });
        puzzleContainer.addView(importButton);
    }

    public void populateMyPuzzles(LinearLayout puzzleContainer) {
        puzzleContainer.removeAllViews();

        puzzleContainer.addView(dh.createTextView("Create Puzzle", 25, Color.BLACK));
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

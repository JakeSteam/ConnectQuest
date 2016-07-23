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
        if (displayOthers) {
            populateOthersPuzzles(puzzleContainer);
        } else {
            populateMyPuzzles(puzzleContainer);
        }
        updateTabDisplay();
    }

    public void populateOthersPuzzles(LinearLayout puzzleContainer) {
        puzzleContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        List<Puzzle> puzzles = Puzzle.getOthersCustomPuzzles();
        for (Puzzle puzzle : puzzles) {
            Puzzle_Custom puzzleCustom = puzzle.getCustomData();
            View inflatedView = inflater.inflate(R.layout.custom_puzzle_preview_other, null);
            RelativeLayout othersPuzzle = (RelativeLayout) inflatedView.findViewById(R.id.puzzleLayout);
            othersPuzzle.setTag(R.id.puzzleId, puzzle.getPuzzleId());
            othersPuzzle.setTag(R.id.puzzleIsCustom, puzzle.getPackId() == 0);

            othersPuzzle.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag(R.id.puzzleId));
                    intent.putExtra(Constants.INTENT_PUZZLE_TYPE, (boolean) v.getTag(R.id.puzzleIsCustom));
                    startActivity(intent);
                }
            });

            ((TextView)othersPuzzle.findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());
            ((TextView)othersPuzzle.findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());
            ((TextView)othersPuzzle.findViewById(R.id.puzzleAuthor)).setText(puzzleCustom.getAuthor());

            puzzleContainer.addView(othersPuzzle);
        }
    }

    public void populateMyPuzzles(LinearLayout puzzleContainer) {

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

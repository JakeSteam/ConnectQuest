package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;

public class CustomInfoActivity extends Activity {
    private Puzzle puzzle;
    private PuzzleCustom puzzleCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_info);

        Intent intent = getIntent();
        int puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);
        if (puzzleId > 0) {
            puzzle = Puzzle.getPuzzle(puzzleId);
            puzzleCustom = puzzle.getCustomData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayPuzzleInfo();
    }

    private void displayPuzzleInfo() {
        ((TextView) findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());
        ((TextView) findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());
        ((TextView) findViewById(R.id.puzzleAuthor)).setText("By: " + puzzleCustom.getAuthor());
        ((TextView) findViewById(R.id.puzzleCreatedDate)).setText("Added: " + DateHelper.displayTime(puzzleCustom.getDateAdded(), DateHelper.date));
    }

    public void closePopup (View v) {
        this.finish();
    }
}

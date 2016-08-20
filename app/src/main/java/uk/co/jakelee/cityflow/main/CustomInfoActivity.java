package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Text;

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

        populateText();
        displayPuzzleInfo();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.puzzleName)).setText(Text.get("WORD_NAME"));
        ((TextView) findViewById(R.id.puzzleDesc)).setText(Text.get("WORD_DESCRIPTION"));
    }

    public void redisplayInfo() {
        puzzleCustom = puzzle.getCustomData();
        displayPuzzleInfo();
    }

    private void displayPuzzleInfo() {
        ((TextView) findViewById(R.id.puzzleName)).setText(puzzleCustom.getName());
        ((TextView) findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());
        ((TextView) findViewById(R.id.puzzleAuthor)).setText("By: " + (puzzleCustom.isOriginalAuthor() ? "You!" : puzzleCustom.getAuthor()));
        ((TextView) findViewById(R.id.puzzleCreatedDate)).setText("Added: " + DateHelper.displayTime(puzzleCustom.getDateAdded(), DateHelper.date));

        ((TextView) findViewById(R.id.actionButton)).setText(puzzleCustom.isOriginalAuthor() ? R.string.icon_edit : R.string.icon_play);
    }

    public void performAction(View v) {
        if (puzzleCustom.isOriginalAuthor()) {
            Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
            intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
            intent.putExtra(Constants.INTENT_PUZZLE_TYPE, puzzle.getPackId() == 0);
            startActivity(intent);
        }
    }

    public void copyPuzzle(View v) {
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        PuzzleShareHelper.importPuzzleString(backup, true);
    }

    public void exportPuzzle(View v) {
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);

        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, backup);
        startActivity(Intent.createChooser(intent, "Save puzzle backup to"));
    }

    public void editPuzzleName(View v) {
        AlertDialogHelper.changePuzzleInfo(this, puzzleCustom, false);
    }

    public void editPuzzleDesc(View v) {
        AlertDialogHelper.changePuzzleInfo(this, puzzleCustom, true);
    }

    public void closePopup (View v) {
        this.finish();
    }
}

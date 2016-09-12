package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
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
        redisplayInfo();
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
        ((TextView) findViewById(R.id.puzzleName)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());
        ((TextView) findViewById(R.id.puzzleDesc)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.puzzleAuthor)).setText(puzzleCustom.isOriginalAuthor() ? Setting.getString(Constants.SETTING_PLAYER_NAME) : puzzleCustom.getAuthor());
        ((TextView) findViewById(R.id.puzzleCreatedDate)).setText(DateHelper.displayTime(puzzleCustom.getDateAdded(), DateHelper.date));
        ((TextView) findViewById(R.id.puzzleBestMoves)).setText(puzzle.getBestMovesText());
        ((TextView) findViewById(R.id.puzzleBestTime)).setText(DateHelper.getPuzzleTimeString(puzzle.getBestTime()));

        ((TextView) findViewById(R.id.puzzleNameText)).setText(Text.get("UI_PUZZLE_NAME"));
        ((TextView) findViewById(R.id.puzzleDescText)).setText(Text.get("UI_PUZZLE_DESC"));
        ((TextView) findViewById(R.id.puzzleAuthorText)).setText(Text.get("UI_PUZZLE_BY"));
        ((TextView) findViewById(R.id.puzzleCreatedDateText)).setText(Text.get("UI_PUZZLE_DATE_ADDED"));
        ((TextView) findViewById(R.id.puzzleBestMovesText)).setText(Text.get("UI_PUZZLE_BEST_MOVES"));
        ((TextView) findViewById(R.id.puzzleBestTimeText)).setText(Text.get("UI_PUZZLE_BEST_TIME"));

        if (!puzzleCustom.isOriginalAuthor()) {
            findViewById(R.id.starWrapper).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.puzzleStarComplete)).setImageResource(puzzle.hasCompletionStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
            ((ImageView) findViewById(R.id.puzzleStarMoves)).setImageResource(puzzle.hasMovesStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
            ((ImageView) findViewById(R.id.puzzleStarTime)).setImageResource(puzzle.hasTimeStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
            ((TextView) findViewById(R.id.puzzleStarsText)).setText(Text.get("UI_PUZZLE_STARS"));
        }
        ((TextView) findViewById(R.id.exportButton)).setTextColor(puzzleCustom.hasBeenTested() ? Color.BLACK : Color.LTGRAY);
    }

    public void playPuzzle(View v) {
        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
        intent.putExtra(Constants.INTENT_PUZZLE_TYPE, puzzle.getPackId() == 0);
        startActivity(intent);
        this.finish();
    }

    public void copyPuzzle(View v) {
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        PuzzleShareHelper.importPuzzleString(backup, true);
        AlertHelper.success(this, String.format(Text.get("ALERT_PUZZLE_COPIED"), puzzle.getName()));
    }

    public void exportPuzzle(View v) {
        if (puzzleCustom.isOriginalAuthor() && !puzzleCustom.hasBeenTested()) {
            AlertHelper.error(this, ErrorHelper.get(ErrorHelper.Error.PUZZLE_NOT_TESTED));
            return;
        }

        AlertHelper.info(this, Text.get("PUZZLE_EXPORT_START"));

        /*String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        Intent intent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, backup);
        startActivity(Intent.createChooser(intent, Text.get("UI_EXPORT_PUZZLE_HINT")));*/
        String backup = PuzzleShareHelper.getPuzzleSQL(puzzle);
        backup.toString();
    }

    public void editPuzzleName(View v) {
        if (puzzleCustom.isOriginalAuthor()) {
            AlertDialogHelper.changePuzzleInfo(this, puzzleCustom, false);
        }
    }

    public void editPuzzleDesc(View v) {
        if (puzzleCustom.isOriginalAuthor()) {
            AlertDialogHelper.changePuzzleInfo(this, puzzleCustom, true);
        }
    }

    public void closePopup (View v) {
        this.finish();
    }
}

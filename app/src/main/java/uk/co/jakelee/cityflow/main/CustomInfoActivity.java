package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
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

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
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

        if (puzzleCustom.isOriginalAuthor()) {
            ((TextView) findViewById(R.id.puzzleName)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            ((TextView) findViewById(R.id.puzzleName)).setTextColor(ContextCompat.getColor(this, R.color.blue));
            ((TextView) findViewById(R.id.puzzleDesc)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            ((TextView) findViewById(R.id.puzzleDesc)).setTextColor(ContextCompat.getColor(this, R.color.blue));
        } else {
            findViewById(R.id.starWrapper).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.puzzleName)).setTextColor(Color.BLACK);
            ((TextView) findViewById(R.id.puzzleDesc)).setTextColor(Color.BLACK);
            ((TextView) findViewById(R.id.starCompletion)).setText(puzzle.hasCompletionStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);
            ((TextView) findViewById(R.id.starTime)).setText(puzzle.hasTimeStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);
            ((TextView) findViewById(R.id.starMoves)).setText(puzzle.hasMovesStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);
            ((TextView) findViewById(R.id.puzzleStarsText)).setText(Text.get("UI_PUZZLE_STARS"));
        }
        ((TextView) findViewById(R.id.exportButton)).setTextColor((puzzleCustom.hasBeenTested() || !puzzleCustom.isOriginalAuthor()) ? Color.BLACK : Color.LTGRAY);
    }

    public void playPuzzle(View v) {
        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
        intent.putExtra(Constants.INTENT_IS_CUSTOM, puzzle.getPackId() == 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
    }

    public void copyPuzzle(View v) {
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        PuzzleShareHelper.importPuzzleString(backup, true);
        AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("ALERT_PUZZLE_COPIED"), puzzle.getName()));
    }

    public void exportPuzzle(View v) {
        if (puzzleCustom.isOriginalAuthor() && !puzzleCustom.hasBeenTested()) {
            AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.PUZZLE_NOT_TESTED));
            return;
        }

        Intent intent = new Intent(getApplicationContext(), ExportActivity.class);
        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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

    public void closePopup(View v) {
        this.finish();
    }
}

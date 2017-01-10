package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Text;

import static uk.co.jakelee.cityflow.R.id.puzzleButton;

public class PackActivity extends Activity {
    public Puzzle selectedPuzzle = new Puzzle();
    private View selectedPuzzleView;
    private DisplayHelper dh;
    private Pack selectedPack;
    private int lastCompletedPuzzle = 0;
    private int firstPackPuzzle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        selectedPack = Pack.getPack(intent.getIntExtra(Constants.INTENT_PACK, 0));
    }

    private void populateText() {
        selectedPack = Pack.getPack(selectedPack.getPackId());
        ((TextView) findViewById(R.id.packName)).setText(selectedPack.getName());
        ((TextView) findViewById(R.id.totalMoves)).setText(selectedPack.getCurrentMoves() == 0 ? Text.get("WORD_NA") : Integer.toString(selectedPack.getCurrentMoves()));
        ((TextView) findViewById(R.id.totalTime)).setText(DateHelper.displayTime((long) selectedPack.getCurrentTime(), DateHelper.time));
        ((TextView) findViewById(R.id.totalStars)).setText(selectedPack.getCurrentStars() + "/" + selectedPack.getMaxStars());
        ((TextView) findViewById(R.id.bestTime)).setText(Text.get("METRIC_BEST_TIME"));
        ((TextView) findViewById(R.id.bestMoves)).setText(Text.get("METRIC_BEST_MOVES"));
        ((TextView) findViewById(R.id.tilesEarned)).setText(Text.get("METRIC_TILES_EARNED"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.lastCompletedPuzzle = 0;

        selectedPuzzle = Puzzle.getPuzzle(selectedPuzzle.getPuzzleId());
        populatePuzzles();
        populateText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    public void updateSelectedPuzzle(Puzzle newPuzzle, View newPuzzleView) {
        selectedPuzzleView.setBackgroundColor(ContextCompat.getColor(this, R.color.ltltgrey));

        newPuzzleView.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        selectedPuzzleView = newPuzzleView;
        selectedPuzzle = newPuzzle;

        showPuzzleInfo();
    }

    public void populatePuzzles() {
        TableLayout puzzleContainer = (TableLayout) findViewById(R.id.puzzleContainer);
        puzzleContainer.removeAllViews();

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        int margins = dh.dpToPixel(5);
        layoutParams.setMargins(margins, margins, margins, margins);

        List<Puzzle> puzzles = selectedPack.getPuzzles();
        int numPuzzles = puzzles.size();
        TableRow row = new TableRow(this);
        for (int puzzleIndex = 1; puzzleIndex <= numPuzzles; puzzleIndex++) {
            Puzzle puzzle = puzzles.get(puzzleIndex - 1);
            if (selectedPuzzle == null || selectedPuzzle.getPuzzleId() == 0) {
                firstPackPuzzle = puzzle.getPuzzleId();
                selectedPuzzle = puzzle;
            }

            if (puzzle.hasCompletionStar()) {
                lastCompletedPuzzle = puzzle.getPuzzleId();
            }

            boolean isSelected = selectedPuzzle.getPuzzleId() == puzzle.getPuzzleId();
            View button = dh.createPuzzleSelectButton(this, puzzle.getPuzzleId(), puzzle, isSelected, puzzle.getPuzzleId() == firstPackPuzzle || puzzle.getPuzzleId() <= lastCompletedPuzzle + 1);
            if (selectedPuzzleView == null) {
                selectedPuzzleView = button;
            }
            row.addView(button, layoutParams);

            if (puzzleIndex % 4 == 0 || puzzleIndex == numPuzzles) {
                puzzleContainer.addView(row);
                row = new TableRow(this);
            }
        }

        showPuzzleInfo();
    }

    public void showPuzzleInfo() {
        ((ImageView) findViewById(R.id.puzzleImage)).setImageDrawable(dh.getPuzzleDrawable(selectedPuzzle.getPuzzleId(), selectedPuzzle.hasCompletionStar()));
        findViewById(R.id.puzzleImageQuestion).setVisibility(selectedPuzzle.hasCompletionStar() ? View.INVISIBLE : View.VISIBLE);
        ((TextView) findViewById(R.id.puzzleName)).setText(selectedPuzzle.getName());

        ((TextView) findViewById(R.id.starCompletion)).setText(selectedPuzzle.hasCompletionStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);
        ((TextView) findViewById(R.id.starTime)).setText(selectedPuzzle.hasTimeStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);
        ((TextView) findViewById(R.id.starMoves)).setText(selectedPuzzle.hasMovesStar() ? R.string.icon_star_filled : R.string.icon_star_unfilled);

        ((TextView) findViewById(R.id.puzzleBestTime)).setText(DateHelper.getPuzzleTimeString(selectedPuzzle.getBestTime()));
        ((TextView) findViewById(R.id.puzzleBestTime)).setTextColor(selectedPuzzle.hasTimeStar() ? Color.YELLOW : Color.BLACK);

        ((TextView) findViewById(R.id.puzzleBestMoves)).setText(selectedPuzzle.getBestMovesText());
        ((TextView) findViewById(R.id.puzzleBestMoves)).setTextColor(selectedPuzzle.hasMovesStar() ? Color.YELLOW : Color.BLACK);

        int numTiles = selectedPuzzle.getUnlockableTiles().size();
        ((TextView) findViewById(R.id.puzzleTilesUnlocked)).setText((selectedPuzzle.hasCompletionStar() ? numTiles : 0) + " / " + numTiles);
        ((TextView) findViewById(R.id.puzzleTilesUnlocked)).setTextColor(selectedPuzzle.hasCompletionStar() ? Color.YELLOW : Color.BLACK);

        ((TextView) findViewById(puzzleButton)).setText(Text.get((selectedPuzzle.getPuzzleId() == firstPackPuzzle || selectedPuzzle.getPuzzleId() <= lastCompletedPuzzle + 1) ?
                "WORD_START" :
                "WORD_LOCKED"));
    }

    public void startPuzzle(View v) {
        if (selectedPuzzle.getPuzzleId() == firstPackPuzzle || selectedPuzzle.getPuzzleId() <= lastCompletedPuzzle + 1) {
            Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
            intent.putExtra(Constants.INTENT_PUZZLE, selectedPuzzle.getPuzzleId());
            intent.putExtra(Constants.INTENT_IS_CUSTOM, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    public void openLeaderboard(View v) {
        if (GooglePlayHelper.IsConnected() && selectedPack.getTimeLeaderboard() != null && selectedPack.getMovesLeaderboard() != null) {
            AlertDialogHelper.openPackLeaderboard(this, selectedPack.getTimeLeaderboard(), selectedPack.getMovesLeaderboard());
        }
    }
}

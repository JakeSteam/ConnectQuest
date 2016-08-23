package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Text;

public class PackActivity extends Activity {
    private DisplayHelper dh;
    private Pack selectedPack;
    public Puzzle selectedPuzzle = new Puzzle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        selectedPack = Pack.getPack(intent.getIntExtra(Constants.INTENT_PACK, 0));

        populateText();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.bestTime)).setText(Text.get("METRIC_BEST_TIME"));
        ((TextView) findViewById(R.id.bestMoves)).setText(Text.get("METRIC_BEST_MOVES"));
        ((TextView) findViewById(R.id.tilesEarned)).setText(Text.get("METRIC_TILES_EARNED"));
        ((TextView) findViewById(R.id.puzzleButton)).setText(Text.get("WORD_START"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedPuzzle = Puzzle.getPuzzle(selectedPuzzle.getPuzzleId());
        populatePuzzles();
    }

    public void populatePuzzles() {
        TableLayout puzzleContainer = (TableLayout) findViewById(R.id.puzzleContainer);
        puzzleContainer.removeAllViews();

        List<Puzzle> puzzles = selectedPack.getPuzzles();
        int numPuzzles = puzzles.size();
        TableRow row = new TableRow(this);
        boolean lastLevelCompleted = true;
        for (int puzzleIndex = 1; puzzleIndex <= numPuzzles; puzzleIndex++) {
            Puzzle puzzle = puzzles.get(puzzleIndex - 1);
            if (selectedPuzzle == null || selectedPuzzle.getPuzzleId() == 0) {
                selectedPuzzle = puzzle;
            }
            boolean isSelected = selectedPuzzle.getPuzzleId() == puzzle.getPuzzleId();
            row.addView(dh.createPuzzleSelectButton(this, puzzle.getPuzzleId(), puzzle, isSelected, lastLevelCompleted));

            if (puzzleIndex % 4 == 0 || puzzleIndex == numPuzzles) {
                puzzleContainer.addView(row);
                row = new TableRow(this);
            }

            lastLevelCompleted = puzzle.hasCompletionStar();
        }

        showPuzzleInfo();
    }

    public void showPuzzleInfo() {
        final Activity activity = this;
        ((ImageView) findViewById(R.id.puzzleImage)).setImageDrawable(dh.getPuzzleDrawable(selectedPuzzle.getPuzzleId(), selectedPuzzle.hasCompletionStar()));
        findViewById(R.id.puzzleImageQuestion).setVisibility(selectedPuzzle.hasCompletionStar() ? View.INVISIBLE : View.VISIBLE);
        ((TextView) findViewById(R.id.puzzleName)).setText(selectedPuzzle.getName());
        ((ImageView) findViewById(R.id.starCompletion)).setImageResource(selectedPuzzle.hasCompletionStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
        ((ImageView) findViewById(R.id.starTime)).setImageResource(selectedPuzzle.hasTimeStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
        ((ImageView) findViewById(R.id.starMoves)).setImageResource(selectedPuzzle.hasMovesStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);

        ((TextView) findViewById(R.id.puzzleBestTime)).setText(DateHelper.getPuzzleTimeString(selectedPuzzle.getBestTime()));
        ((TextView) findViewById(R.id.puzzleBestTime)).setTextColor(selectedPuzzle.hasTimeStar() ? Color.YELLOW : Color.BLACK);

        ((TextView) findViewById(R.id.puzzleBestMoves)).setText(Integer.toString(selectedPuzzle.getBestMoves()));
        ((TextView) findViewById(R.id.puzzleBestMoves)).setTextColor(selectedPuzzle.hasMovesStar() ? Color.YELLOW : Color.BLACK);

        int numTiles = selectedPuzzle.getUnlockableTiles().size();
        ((TextView) findViewById(R.id.puzzleTilesUnlocked)).setText((selectedPuzzle.hasCompletionStar() ? numTiles : 0) + " / " + numTiles);
        findViewById(R.id.puzzleButton).setTag(R.id.puzzleId, selectedPuzzle.getPuzzleId());
        findViewById(R.id.puzzleButton).setTag(R.id.puzzleIsCustom, false);
        findViewById(R.id.puzzleButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PuzzleActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag(R.id.puzzleId));
                intent.putExtra(Constants.INTENT_PUZZLE_TYPE, (boolean) v.getTag(R.id.puzzleIsCustom));
                startActivity(intent);
            }
        });
    }
}

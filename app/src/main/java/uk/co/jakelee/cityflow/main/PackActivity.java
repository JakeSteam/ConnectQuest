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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refreshing puzzle, so that stats update
        selectedPuzzle = Puzzle.getPuzzle(selectedPuzzle.getPuzzleId());
        populatePuzzles();
    }

    public void populatePuzzles() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        int margin = dh.dpToPixel(7);
        params.setMargins(margin, margin, margin, margin);

        TableLayout puzzleContainer = (TableLayout) findViewById(R.id.puzzleContainer);
        puzzleContainer.removeAllViews();

        List<Puzzle> puzzles = selectedPack.getPuzzles();
        int numPuzzles = puzzles.size();
        TableRow row = new TableRow(this);
        for (int puzzleIndex = 1; puzzleIndex <= numPuzzles; puzzleIndex++) {
            Puzzle puzzle = puzzles.get(puzzleIndex - 1);
            if (selectedPuzzle == null || selectedPuzzle.getPuzzleId() == 0) {
                selectedPuzzle = puzzle;
            }
            boolean isSelected = selectedPuzzle.getPuzzleId() == puzzle.getPuzzleId();
            row.addView(dh.createPuzzleSelectImageView(this, puzzle.getPuzzleId(), puzzle, isSelected), params);

            if (puzzleIndex % 6 == 0 || puzzleIndex == numPuzzles) {
                puzzleContainer.addView(row);
                row = new TableRow(this);
            }
        }

        showPuzzleInfo();
    }

    public void showPuzzleInfo() {
        final Activity activity = this;
        ((ImageView) findViewById(R.id.puzzleImage)).setImageDrawable(dh.getPuzzleDrawable(selectedPuzzle.getPuzzleId(), selectedPuzzle.hasCompletionStar()));
        findViewById(R.id.puzzleImageQuestion).setVisibility(selectedPuzzle.hasCompletionStar() ? View.INVISIBLE : View.VISIBLE);
        ((TextView) findViewById(R.id.puzzleName)).setText(selectedPuzzle.getName());
        ((TextView) findViewById(R.id.puzzleStars)).setText(selectedPuzzle.getStarString());

        ((TextView) findViewById(R.id.puzzleBestTime)).setText(DateHelper.getPuzzleTimeString(selectedPuzzle.getBestTime()));
        ((TextView) findViewById(R.id.puzzleBestTime)).setTextColor(selectedPuzzle.hasTimeStar() ? Color.YELLOW : Color.BLACK);

        ((TextView) findViewById(R.id.puzzleBestMoves)).setText(Integer.toString(selectedPuzzle.getBestMoves()));
        ((TextView) findViewById(R.id.puzzleBestMoves)).setTextColor(selectedPuzzle.hasMovesStar() ? Color.YELLOW : Color.BLACK);

        ((TextView) findViewById(R.id.puzzleTilesUnlocked)).setText(Integer.toString(selectedPuzzle.getTilesUnlocked().size()));
        findViewById(R.id.puzzleButton).setTag(selectedPuzzle.getPuzzleId());
        findViewById(R.id.puzzleButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PuzzleActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag());
                startActivity(intent);
            }
        });
    }
}

package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
        LinearLayout puzzleContainer = (LinearLayout) findViewById(R.id.puzzleContainer);
        puzzleContainer.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(7, 7, 7, 7);

        List<Puzzle> puzzles = selectedPack.getPuzzles();
        for (final Puzzle puzzle : puzzles) {
            if (selectedPuzzle == null || selectedPuzzle.getPuzzleId() == 0) {
                selectedPuzzle = puzzle;
            }
            puzzleContainer.addView(dh.createPuzzleSelectImageView(this, puzzle.getPuzzleId(), puzzle), layoutParams);
        }

        showPuzzleInfo();
    }

    public void showPuzzleInfo() {
        final Activity activity = this;
        ((TextView) findViewById(R.id.puzzleName)).setText(selectedPuzzle.getName());
        ((TextView) findViewById(R.id.puzzleStars)).setText("Stars: " + selectedPuzzle.getBestRating());
        ((TextView) findViewById(R.id.puzzleParTime)).setText("Par Time: " + DateHelper.getPuzzleTimeString(selectedPuzzle.getParTime()));
        ((TextView) findViewById(R.id.puzzleParMoves)).setText("Par Moves: " + Integer.toString(selectedPuzzle.getParMoves()));
        ((TextView) findViewById(R.id.puzzleBestTime)).setText("Best Time: " + DateHelper.getPuzzleTimeString(selectedPuzzle.getBestTime()));
        ((TextView) findViewById(R.id.puzzleBestMoves)).setText("Best Moves: " + Integer.toString(selectedPuzzle.getBestMoves()));
        ((TextView) findViewById(R.id.puzzleTilesUnlocked)).setText("Tiles Unlocked: " + selectedPuzzle.getTilesUnlocked().size());
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

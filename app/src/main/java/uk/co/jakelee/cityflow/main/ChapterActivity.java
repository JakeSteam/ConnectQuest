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
import uk.co.jakelee.cityflow.model.Puzzle;

public class ChapterActivity extends Activity {
    private DisplayHelper dh;
    private Puzzle selectedPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        dh = DisplayHelper.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populatePuzzles();
        showPuzzleInfo(selectedPuzzle);
    }

    public void populatePuzzles() {
        LinearLayout puzzleContainer = (LinearLayout) findViewById(R.id.puzzleContainer);
        puzzleContainer.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(7, 7, 7, 7);

        List<Puzzle> puzzles = Puzzle.getPuzzles(Constants.TYPE_STORY);
        int puzzleNumber = 1;
        for (final Puzzle puzzle : puzzles) {
            if (puzzleNumber == 1) {
                selectedPuzzle = puzzle;
            }

            puzzleContainer.addView(dh.createPuzzleSelectImageView(this, puzzleNumber, puzzle), layoutParams);
            puzzleNumber++;
        }
    }

    public void showPuzzleInfo(Puzzle puzzle) {
        selectedPuzzle = puzzle;
        final Activity activity = this;
        ((TextView) findViewById(R.id.puzzleName)).setText(puzzle.getName());
        ((TextView) findViewById(R.id.puzzleStars)).setText("Stars: " + puzzle.getBestRating());
        ((TextView) findViewById(R.id.puzzleParTime)).setText("Par Time: " + DateHelper.getPuzzleTimeString(puzzle.getParTime()));
        ((TextView) findViewById(R.id.puzzleParMoves)).setText("Par Moves: " + Integer.toString(puzzle.getParMoves()));
        ((TextView) findViewById(R.id.puzzleBestTime)).setText("Best Time: " + DateHelper.getPuzzleTimeString(puzzle.getBestTime()));
        ((TextView) findViewById(R.id.puzzleBestMoves)).setText("Best Moves: " + Integer.toString(puzzle.getBestMoves()));
        findViewById(R.id.puzzleButton).setTag(puzzle.getPuzzleId());
        findViewById(R.id.puzzleButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PuzzleActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag());
                startActivity(intent);
            }
        });
    }
}

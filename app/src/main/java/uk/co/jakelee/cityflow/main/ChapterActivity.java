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
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Puzzle;

public class ChapterActivity extends Activity {
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        dh = DisplayHelper.getInstance(this);

        LinearLayout puzzleContainer = (LinearLayout) findViewById(R.id.puzzleContainer);
        populatePuzzles(puzzleContainer);
    }

    public void populatePuzzles(LinearLayout puzzleContainer) {
        final Activity activity = this;
        List<Puzzle> puzzles = Puzzle.getPuzzles(Constants.TYPE_STORY);
        for (Puzzle puzzle : puzzles) {
            TextView chapterText = new TextView(this);
            chapterText.setText("#" + puzzle.getPuzzleId());
            chapterText.setTextSize(24);
            chapterText.setTag(puzzle.getPuzzleId());
            chapterText.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PuzzleActivity.class);
                    intent.putExtra(Constants.INTENT_PUZZLE, (int) v.getTag());
                    startActivity(intent);
                }
            });

            puzzleContainer.addView(chapterText);
        }
    }
}

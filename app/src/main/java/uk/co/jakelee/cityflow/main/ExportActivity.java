package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.StorageHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;

public class ExportActivity extends Activity {
    private Puzzle puzzle;
    private PuzzleCustom puzzleCustom;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        int puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);
        puzzle = Puzzle.getPuzzle(puzzleId);
        puzzleCustom = puzzle.getCustomData();

        populateCard();
    }

    private void populateCard() {
        ((TextView)findViewById(R.id.puzzleName)).setText(puzzle.getName());
        ((TextView)findViewById(R.id.puzzleAuthor)).setText(puzzleCustom.getAuthor());

        ((ImageView)findViewById(R.id.puzzleImage)).setImageResource(dh.getPuzzleDrawableID(10));
        ((TextView)findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());

        StorageHelper.testQR((ImageView)findViewById(R.id.puzzleQrCode), PuzzleShareHelper.getPuzzleString(puzzle));
    }
}

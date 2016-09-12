package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.StorageHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Text;

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
        ((TextView)findViewById(R.id.saveCard)).setText(Text.get("DIALOG_BUTTON_SAVE"));
        ((TextView)findViewById(R.id.shareCard)).setText(Text.get("DIALOG_BUTTON_SHARE"));

        ((TextView)findViewById(R.id.puzzleName)).setText(puzzle.getName());
        ((TextView)findViewById(R.id.puzzleAuthor)).setText(puzzleCustom.getAuthor());

        ((ImageView)findViewById(R.id.puzzleImage)).setImageDrawable(dh.getCustomPuzzleDrawable(puzzle.getPuzzleId()));
        ((TextView)findViewById(R.id.puzzleDesc)).setText(puzzleCustom.getDescription());

        StorageHelper.fillWithQrDrawable((ImageView)findViewById(R.id.puzzleQrCode), PuzzleShareHelper.getPuzzleString(puzzle));
    }

    public void save(View view) {
        // Save to local filesystem
    }

    public void share(View view) {
        // Share to magical intent
        String backup = PuzzleShareHelper.getPuzzleString(puzzle);
        Intent intent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, backup);
        startActivity(Intent.createChooser(intent, Text.get("UI_EXPORT_PUZZLE_HINT")));
    }
}

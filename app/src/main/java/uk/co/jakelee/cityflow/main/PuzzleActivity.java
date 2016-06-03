package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private Puzzle puzzle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        List<Puzzle> puzzles = Puzzle.listAll(Puzzle.class);
        this.puzzle = Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(1)).first();

        if (this.puzzle != null) {
            displayTiles();
        }
    }

    private void displayTiles() {
        LinearLayout tileContainer = (LinearLayout) findViewById(R.id.tileContainer);
        List<Tile> tiles = puzzle.getTiles();

        for (Tile tile : tiles) {
            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = new ImageView(this);
            Picasso.with(this).load(drawableId).into(image);
            tileContainer.addView(image);
        }
    }
}

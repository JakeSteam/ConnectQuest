package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

        this.puzzle = Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(2)).first();

        if (this.puzzle != null) {
            displayTiles();
        }
    }

    private void displayTiles() {
        RelativeLayout tileContainer = (RelativeLayout) findViewById(R.id.tileContainer);
        List<Tile> tiles = puzzle.getTiles();

        int i = 0;
        for (Tile tile : tiles) {
            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = createTileImageView(tile, drawableId);
            tileContainer.addView(image);
        }
    }

    private ImageView createTileImageView(Tile tile, int drawableId) {
        int width = 350;
        int height = 175;
        int maxY = 1;
        ImageView image = new ImageView(this);
        int leftPadding = (tile.getY() + tile.getX()) * (width/2);
        int topPadding = (tile.getX() + maxY - tile.getY()) * (height/2);
        image.setPadding(leftPadding, topPadding, 0, 0);
        Picasso.with(this).load(drawableId).into(image);
        return image;
    }
}

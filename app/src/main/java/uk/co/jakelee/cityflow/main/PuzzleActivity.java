package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private DisplayHelper dh;
    private int puzzleId = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        RelativeLayout tileContainer = (RelativeLayout) findViewById(R.id.tileContainer);
        populateTiles(tileContainer, puzzleId);
    }

    public void populateTiles(RelativeLayout tileContainer, int puzzleId) {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        int maxY = TileHelper.getMaxY(tiles);

        List<ImageView> images = new ArrayList<>();
        for (final Tile tile : tiles) {
            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = dh.createTileImageView(this, tile, maxY, drawableId);
            images.add(image);
        }

        tileContainer.removeAllViews();
        for (ImageView image : images) {
            tileContainer.addView(image);
        }
    }

    public void handleTileClick(ImageView image, Tile tile) {
        tile.rotate();
        int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
        Picasso.with(this).load(drawableId).into(image);
    }

    public void flowCheck(View v) {
        boolean flowsCorrectly = TileHelper.checkPuzzleFlow(this.puzzleId);
        Toast.makeText(this, flowsCorrectly ? "Flows!" : "No flows!", Toast.LENGTH_SHORT).show();
    }
}

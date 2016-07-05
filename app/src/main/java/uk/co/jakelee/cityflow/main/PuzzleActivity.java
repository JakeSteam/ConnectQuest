package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        populateTiles(tiles);
        fetchImages(tiles);

        final Runnable everySecond = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        flowCheck();
                    }
                }).start();
                handler.postDelayed(this, 3 * DateHelper.MILLISECONDS_IN_SECOND);
            }
        };
        handler.post(everySecond);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);

        Tile.executeQuery("UPDATE tile SET rotation = default_rotation WHERE puzzle_id = " + puzzleId);
    }

    public void fetchImages(List<Tile> tiles) {
        for (Tile tile : tiles) {
            List<Integer> ids = ImageHelper.getAllTileDrawableIds(this, tile.getTileTypeId());
            for (Integer id : ids) {
                Picasso.with(this)
                        .load(id)
                        .fetch();
            }
        }
    }

    public void populateTiles(List<Tile> tiles) {
        if (puzzleId == 0) { return; }

        RelativeLayout tileContainer = (RelativeLayout) findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();
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

    public void flowCheck() {
        final Activity activity = this;
        final boolean flowsCorrectly = TileHelper.checkPuzzleFlow(this.puzzleId);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, flowsCorrectly ? "Flows!" : "No flows!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

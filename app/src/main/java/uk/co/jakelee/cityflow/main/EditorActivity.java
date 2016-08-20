package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class EditorActivity extends Activity {
    private DisplayHelper dh;
    private int puzzleId;
    private ImageView selectedTileImage;
    private Tile selectedTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        populateTiles(tiles);
        fetchImages(tiles);

        populateText();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.selectedTileText)).setText(Text.get("TILE_0_NAME"));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (selectedTile != null && selectedTileImage != null) {
            redrawSelectedTile();
        }
    }

    private void redrawSelectedTile() {
        selectedTile = Tile.get(selectedTile.getId());
        int drawableId = ImageHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
        Picasso.with(this).load(drawableId).into(selectedTileImage);
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

        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();
        Pair<Integer, Integer> maxXY = TileHelper.getMaxXY(tiles);
        int leftOffset = (dh.getSizes(this).widthPixels / 3) - (maxXY.first * (dh.getTileWidth() / 2));
        int topOffset = (dh.getSizes(this).heightPixels / 3) - (maxXY.second * (dh.getTileHeight() / 2));

        tileContainer.removeAllViews();
        for (final Tile tile : tiles) {
            ZoomableViewGroup.LayoutParams layoutParams = new ZoomableViewGroup.LayoutParams(ZoomableViewGroup.LayoutParams.WRAP_CONTENT, ZoomableViewGroup.LayoutParams.WRAP_CONTENT);
            int leftPadding = leftOffset + (tile.getY() + tile.getX()) * (dh.getTileWidth()/2);
            int topPadding = topOffset + (tile.getX() + maxXY.second - tile.getY()) * (dh.getTileHeight()/2);
            layoutParams.setMargins(leftPadding, topPadding, 0, 0);

            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = dh.createTileImageView(this, tile, drawableId);

            tileContainer.addView(image, layoutParams);
        }
    }

    public void zoomIn(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() + 0.5f);
    }

    public void zoomOut(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() - 0.5f);
    }

    public void handleTileClick(ImageView image, Tile tile) {
        if (selectedTileImage != null) {
            selectedTileImage.setAlpha(1f);
            selectedTileImage.clearColorFilter();

        }

        selectedTileImage = image;
        selectedTileImage.setAlpha(0.75f);
        selectedTileImage.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        selectedTile = tile;

        TileType tileType = TileType.get(tile.getTileTypeId());
        ((TextView)findViewById(R.id.selectedTileText)).setText(tileType.getName());
    }

    public void rotateTile(View v) {
        if (selectedTile != null && selectedTileImage != null) {
            selectedTile.rotate(false);
            int drawableId = ImageHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
            Picasso.with(this).load(drawableId).into(selectedTileImage);
        }
    }

    public void pickTile(View v) {
        if (selectedTile != null) {
            Intent intent = new Intent(getApplicationContext(), TilePickerActivity.class);
            intent.putExtra(Constants.INTENT_TILE, selectedTile.getId());
            startActivity(intent);
        }
    }

    public void closePuzzle(View v) {
        this.finish();
    }

    public void playPuzzle(View v) {

    }
}

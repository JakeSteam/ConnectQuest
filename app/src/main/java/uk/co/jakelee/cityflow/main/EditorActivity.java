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
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
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
        puzzle.resetTileRotations();

        drawPuzzle(puzzle.getTiles());
    }

    private void drawPuzzle(List<Tile> tiles) {
        populateTiles(tiles);
        fetchImages(tiles);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (selectedTile != null && selectedTileImage != null) {
            redrawSelectedTile();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Puzzle.getPuzzle(puzzleId).resetTileRotations();
    }

    private void redrawSelectedTile() {
        selectedTile = Tile.get(selectedTile.getId());
        int drawableId = ImageHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
        Picasso.with(this).load(drawableId).into(selectedTileImage);
        ((TextView)findViewById(R.id.selectedTileText)).setText(TileType.get(selectedTile.getTileTypeId()).getName());
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
        tileContainer.setBackgroundColor(Background.getActiveBackgroundColour());
        tileContainer.removeAllViews();

        Pair<Integer, Integer> maxXY = TileHelper.getMaxXY(tiles);
        Pair<Float, Integer> displayValues = dh.getScaleFactor(this, maxXY.first + 1, maxXY.second + 1);

        tileContainer.setScaleFactor(displayValues.first, true);
        int topOffset = displayValues.second;

        tileContainer.removeAllViews();
        for (final Tile tile : tiles) {
            ZoomableViewGroup.LayoutParams layoutParams = new ZoomableViewGroup.LayoutParams(ZoomableViewGroup.LayoutParams.WRAP_CONTENT, ZoomableViewGroup.LayoutParams.WRAP_CONTENT);
            int leftPadding = (tile.getY() + tile.getX()) * (dh.getTileWidth()/2);
            int topPadding = topOffset + (tile.getX() + maxXY.second - tile.getY()) * (dh.getTileHeight()/2);
            layoutParams.setMargins(leftPadding, topPadding, 0, 0);

            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = dh.createTileImageView(this, tile, drawableId);

            // Make sure we always have a tile selected
            if (selectedTile == null || selectedTileImage == null) {
                image.setAlpha(0.75f);
                image.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                selectedTileImage = image;
                selectedTile = tile;

                ((TextView)findViewById(R.id.selectedTileText)).setText(tile.getName());
            }

            tileContainer.addView(image, layoutParams);
        }
    }

    public void zoomIn(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() + 0.5f, false);
    }

    public void zoomOut(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() - 0.5f, false);
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
        ((TextView)findViewById(R.id.selectedTileText)).setText(TileType.get(selectedTile.getTileTypeId()).getName());
    }

    public void rotateTile(View v) {
        if (selectedTile != null && selectedTileImage != null) {
            selectedTile = Tile.get(selectedTile.getId());
            selectedTile.rotate(false);
            int drawableId = ImageHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
            Picasso.with(this).load(drawableId).into(selectedTileImage);
        }
    }

    public void pickTile(View v) {
        if (selectedTile != null) {
            Intent intent = new Intent(getApplicationContext(), TilePickerActivity.class);
            intent.putExtra(Constants.INTENT_TILE, selectedTile.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    public void openMenu(View v) {
        Intent intent = new Intent(this, EditorMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case EditorMenuActivity.PLAY:
                playPuzzle();
                break;
            case EditorMenuActivity.SHUFFLE:
                AlertDialogHelper.confirmPuzzleShuffle(this, Puzzle.getPuzzle(puzzleId));
                break;
            case EditorMenuActivity.CHANGE_NAME:
                AlertDialogHelper.changePuzzleInfo(this, PuzzleCustom.get(puzzleId), false);
                break;
            case EditorMenuActivity.CHANGE_DESC:
                AlertDialogHelper.changePuzzleInfo(this, PuzzleCustom.get(puzzleId), true);
                break;
            case EditorMenuActivity.CHANGE_SIZE:
                AlertDialogHelper.resizePuzzle(this, puzzleId);
                break;
            case EditorMenuActivity.SAVE:
                savePuzzle();
                break;
        }
    }

    public void shuffleTiles() {
        List<Tile> tiles = Puzzle.getPuzzle(puzzleId).getTiles();
        Puzzle.shuffle(tiles);
        drawPuzzle(tiles);
    }

    public void savePuzzle() {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        puzzle.saveTileRotations();
        puzzle.resetMetrics();

        this.finish();
    }

    public void playPuzzle() {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        savePuzzle();

        Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
        intent.putExtra(Constants.INTENT_PUZZLE, puzzle.getPuzzleId());
        intent.putExtra(Constants.INTENT_IS_CUSTOM, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}

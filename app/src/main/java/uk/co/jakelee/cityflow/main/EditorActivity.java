package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.TileDisplaySetup;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.interfaces.PuzzleDisplayer;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;
import uk.co.jakelee.cityflow.objects.TileFilter;

public class EditorActivity extends Activity implements PuzzleDisplayer {
    private DisplayHelper dh;
    private int puzzleId;
    private ImageView selectedTileImage;
    private Tile selectedTile;
    private float optimumScale = 1.0f;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);

        if (intent.getIntExtra(Constants.INTENT_ENVIRONMENT, 0) > 0) {
            TileFilter tileFilter = TileFilter.quickCreateFilter(intent.getIntExtra(Constants.INTENT_ENVIRONMENT, 0));
            getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE).edit().putString("tilePickerEnvironments", tileFilter.selected.toString()).apply();
        }

        if (intent.getIntExtra(Constants.INTENT_FAILED_TILES, 0) > 0) {
            AlertHelper.error(this, String.format(Locale.ENGLISH,
                    AlertHelper.getError(AlertHelper.Error.GENERATION_INCOMPLETE),
                    intent.getIntExtra(Constants.INTENT_FAILED_TILES, 0)));
        }
        picasso = Picasso.with(this);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        puzzle.resetTileRotations();

        drawPuzzle(puzzle.getTiles());

        findViewById(R.id.tileContainer).setBackgroundColor(Background.getActiveBackgroundColour());
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
    protected void onStop() {
        super.onStop();

        Puzzle.getPuzzle(puzzleId).resetTileRotations();
        SoundHelper.stopIfExiting(this);
    }

    private void redrawSelectedTile() {
        selectedTile = Tile.get(selectedTile.getId());
        int drawableId = DisplayHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
        picasso.load(drawableId)
                .placeholder(R.drawable.tile_0_1)
                .into(selectedTileImage);
        ((TextView) findViewById(R.id.selectedTileText)).setText(TileType.get(selectedTile.getTileTypeId()).getName());
    }

    public void fetchImages(List<Tile> tiles) {
        for (Tile tile : tiles) {
            List<Integer> ids = DisplayHelper.getAllTileDrawableIds(this, tile.getTileTypeId());
            for (Integer id : ids) {
                picasso.load(id)
                        .fetch();
            }
        }
    }


    public void populateTiles(List<Tile> tiles) {
        TileDisplaySetup tileDisplaySetup = dh.setupTileDisplay(this, tiles, (ZoomableViewGroup) findViewById(R.id.tileContainer), selectedTile, selectedTileImage, true);
        selectedTileImage = tileDisplaySetup.getSelectedImageView();
        optimumScale = tileDisplaySetup.getOptimumScale();
        selectedTile = tiles.get(0);
    }

    public void zoomIn(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() + 0.5f, false);
    }

    public void zoomOut(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() - 0.5f, false);
    }

    public void reset(View v) {
        ((ZoomableViewGroup) findViewById(R.id.tileContainer)).reset(optimumScale);
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
        ((TextView) findViewById(R.id.selectedTileText)).setText(TileType.get(selectedTile.getTileTypeId()).getName());
    }

    public void rotateTile(View v) {
        if (selectedTile != null && selectedTileImage != null) {
            selectedTile = Tile.get(selectedTile.getId());
            selectedTile.rotate(false);
            int drawableId = DisplayHelper.getTileDrawableId(this, selectedTile.getTileTypeId(), selectedTile.getRotation());
            picasso.load(drawableId)
                    .placeholder(R.drawable.tile_0_1)
                    .into(selectedTileImage);
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
            case EditorMenuActivity.ROTATE:
                AlertDialogHelper.rotatePuzzle(this, puzzleId);
                break;
        }
    }

    public void shuffleTiles() {
        List<Tile> tiles = Puzzle.getPuzzle(puzzleId).getTiles();
        Puzzle.shuffle(tiles);
        Tile.executeQuery("UPDATE tile SET default_rotation = rotation WHERE puzzle_id = " + puzzleId);
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

    public Activity getActivity() {
        return this;
    }

    public boolean displayEmptyTile() {
        return true;
    }
}

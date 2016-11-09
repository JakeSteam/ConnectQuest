package uk.co.jakelee.cityflow.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.RandomHelper;
import uk.co.jakelee.cityflow.main.EditorActivity;
import uk.co.jakelee.cityflow.main.PuzzleActivity;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

import static uk.co.jakelee.cityflow.helper.PuzzleHelper.createBasicPuzzleCustomObject;
import static uk.co.jakelee.cityflow.helper.PuzzleHelper.createBasicPuzzleObject;
import static uk.co.jakelee.cityflow.helper.PuzzleHelper.getDefaultTileId;
import static uk.co.jakelee.cityflow.helper.PuzzleHelper.getNextCustomPuzzleId;

public class PuzzleGenerator extends AsyncTask<String, Integer, Integer> {
    private Activity activity;
    private TextView progressText;
    private TextView progressPercentage;
    private Dialog dialog;
    private int xValue;
    private int yValue;
    private int environmentId;
    private boolean autogenerate;
    private boolean shuffleAndPlay;
    private int totalTiles;

    public PuzzleGenerator(Activity activity, Dialog dialog, int xValue, int yValue, int environmentId, boolean autogenerate, boolean shuffleAndPlay) {
        this.activity = activity;
        this.dialog = dialog;
        this.xValue = xValue;
        this.yValue = yValue;
        this.environmentId = environmentId;
        this.autogenerate = autogenerate;
        this.shuffleAndPlay = shuffleAndPlay;

        this.progressText = (TextView)dialog.findViewById(R.id.progressText);
        this.progressPercentage = (TextView)dialog.findViewById(R.id.progressPercentage);
    }

    @Override
    protected void onPostExecute(Integer result) {
        dialog.dismiss();

        if (autogenerate && shuffleAndPlay) {
            activity.startActivity(new Intent(activity, PuzzleActivity.class)
                    .putExtra(Constants.INTENT_PUZZLE, result)
                    .putExtra(Constants.INTENT_IS_CUSTOM, true)
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        } else {
            activity.startActivity(new Intent(activity, EditorActivity.class)
                    .putExtra(Constants.INTENT_PUZZLE, result)
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Integer... values) {
        int percent = (int) (((double) values[0]) / ((double) totalTiles) * 100);
        progressPercentage.setText(String.format(Locale.getDefault(), "%1$d%%", percent));
        progressText.setText(String.format(Locale.getDefault(), "(%1$d/%2$d)", values[0], totalTiles));
    }

    @Override
    protected Integer doInBackground(String... params) {
        return createNewPuzzle(xValue, yValue, environmentId, autogenerate);
    }

    private int createNewPuzzle(int maxX, int maxY, int environmentId, boolean autogenerate) {
        totalTiles = maxX * maxY;
        if (autogenerate) {
            return createFilledPuzzle(maxX, maxY, environmentId);
        } else {
            return createEmptyPuzzle(maxX, maxY, environmentId);
        }
    }

    private int createEmptyPuzzle(int maxX, int maxY, final int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        int defaultTileId = getDefaultTileId(environmentId);
        createBasicPuzzleObject(newPuzzleId).save();
        createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY).save();

        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                tiles.add(new Tile(newPuzzleId, defaultTileId, x, y, Constants.ROTATION_NORTH));
                publishProgress(tiles.size());
            }
        }
        Tile.saveInTx(tiles);

        return newPuzzleId;
    }

    private int createFilledPuzzle(int maxX, int maxY, int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        createBasicPuzzleObject(newPuzzleId).save();
        createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY).save();

        List<Tile> tiles = new ArrayList<>();
        int prevX = 0;
        int prevY = 0;
        int failedAttempts = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                List<Tile> potentialTiles = getPossibleTiles(newPuzzleId, tiles, x, y, maxX - 1, maxY - 1, environmentId);
                if (potentialTiles.size() > 0) {
                    Tile selectedTile = potentialTiles.get(RandomHelper.getNumber(0, potentialTiles.size() - 1));
                    tiles.add(selectedTile);
                    prevX = x;
                    prevY = y;
                    failedAttempts = 0;
                } else {
                    failedAttempts++;
                    if (failedAttempts > 10) {
                        tiles.add(new Tile(newPuzzleId, 0, x, y, Constants.ROTATION_NORTH));
                        prevX = x;
                        prevY = y;
                    } else {
                        tiles.remove(tiles.size() - 1);
                        if (y == 0) {
                            Log.d("Tiles", "X: " + x + ", Y: " + y + " (FAILED Y)");
                            x--;
                            y = maxY - 1;
                        } else {
                            Log.d("Tiles", "X: " + x + ", Y: " + y + " (FAILED)");
                            y = prevY - 1;
                        }
                        //
                        Log.d("Tiles", "X: " + x + ", Y: " + y + " (MODIFIED)");
                    }
                }
                publishProgress(tiles.size());
            }
        }
        if (shuffleAndPlay) {
            Puzzle.shuffle(tiles);
            Tile.executeQuery("UPDATE tile SET default_rotation = rotation WHERE puzzle_id = " + newPuzzleId);
        } else {
            Tile.saveInTx(tiles);
        }
        return newPuzzleId;
    }

    private static List<Tile> getPossibleTiles(int puzzleId, List<Tile> existingTiles, int tileX, int tileY, int maxX, int maxY, int environmentId) {
        Tile southTile = tileY == 0 ? new Tile() : existingTiles.get(existingTiles.size() - 1); // Get the south tile, or an empty one if we're starting a new column
        Tile westTile = tileX == 0 ? new Tile() : existingTiles.get(existingTiles.size() - (maxY + 1)); // Get the west tile (#Y tiles previous), or empty if new row

        int nFlow = tileY == maxY ? 0 : -1;
        int eFlow = tileX == maxX ? 0 : -1;
        int sFlow = southTile.getFlow(Constants.SIDE_NORTH);
        int wFlow = westTile.getFlow(Constants.SIDE_EAST);

        int nHeight = -1;
        int eHeight = -1;
        int sHeight = sFlow > -1 && tileY > 0 ? southTile.getHeight(Constants.SIDE_NORTH) : -1;
        int wHeight = wFlow > -1 && tileX > 0 ? westTile.getHeight(Constants.SIDE_EAST) : -1;

        // Make list
        List<Tile> tiles = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_NORTH, nFlow, eFlow, sFlow, wFlow, nHeight, eHeight, sHeight, wHeight);
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_WEST, wFlow, nFlow, eFlow, sFlow, wHeight, nHeight, eHeight, sHeight));
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_SOUTH, sFlow, wFlow, nFlow, eFlow, sHeight, wHeight, nHeight, eHeight));
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_EAST, eFlow, sFlow, wFlow, nFlow, eHeight, sHeight, wHeight, nHeight));

        return tiles;
    }

    private static List<Tile> getPossibleTilesByRotation(int puzzleId, int x, int y, int environmentId, int rotation, int nFlow, int eFlow, int sFlow, int wFlow, int nHeight, int eHeight, int sHeight, int wHeight) {
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM tile_type WHERE environment_id = %1$d AND flow_north %2$s %3$d AND flow_east %4$s %5$d AND flow_south %6$s %7$d AND flow_west %8$s %9$d AND height_north %10$s %11$d AND height_east %12$s %13$d AND height_south %14$s %15$d AND height_west %16$s %17$d",
                environmentId,
                nFlow >= 0 ? "=" : ">=", nFlow,
                eFlow >= 0 ? "=" : ">=", eFlow,
                sFlow >= 0 ? "=" : ">=", sFlow,
                wFlow >= 0 ? "=" : ">=", wFlow,
                nHeight >= 0 ? "=" : ">=", nHeight,
                eHeight >= 0 ? "=" : ">=", eHeight,
                sHeight >= 0 ? "=" : ">=", sHeight,
                wHeight >= 0 ? "=" : ">=", wHeight);
        List<TileType> tileTypes = TileType.findWithQuery(TileType.class, sql);

        List<Tile> tiles = new ArrayList<>();
        for (TileType tile : tileTypes) {
            tiles.add(new Tile(puzzleId, tile.getTypeId(), x, y, rotation));
        }
        return tiles;
    }
}
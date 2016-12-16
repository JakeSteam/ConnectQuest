package uk.co.jakelee.cityflow.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import uk.co.jakelee.cityflow.model.PuzzleCustom;
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
    private boolean blankPuzzle;
    private boolean shuffleAndPlay;
    private int totalTiles;
    private boolean cancelReceived = false;

    public PuzzleGenerator(Activity activity, Dialog dialog, int xValue, int yValue, int environmentId, boolean blankPuzzle, boolean shuffleAndPlay) {
        this.activity = activity;
        this.dialog = dialog;
        this.xValue = xValue;
        this.yValue = yValue;
        this.environmentId = environmentId;
        this.blankPuzzle = blankPuzzle;
        this.shuffleAndPlay = shuffleAndPlay;

        this.progressText = (TextView)dialog.findViewById(R.id.progressText);
        this.progressPercentage = (TextView)dialog.findViewById(R.id.progressPercentage);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (dialog.isShowing()) {
            dialog.dismiss();

            if (!blankPuzzle && shuffleAndPlay) {
                activity.startActivity(new Intent(activity, PuzzleActivity.class)
                        .putExtra(Constants.INTENT_PUZZLE, result)
                        .putExtra(Constants.INTENT_IS_CUSTOM, true)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            } else {
                activity.startActivity(new Intent(activity, EditorActivity.class)
                        .putExtra(Constants.INTENT_PUZZLE, result)
                        .putExtra(Constants.INTENT_ENVIRONMENT, environmentId)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        }
    }

    @Override
    protected void onPreExecute() {
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                cancel();
            }
        });
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int percent = (int) (((double) values[0]) / ((double) totalTiles) * 100);
        progressPercentage.setText(String.format(Locale.ENGLISH, "%1$d%%", percent));
        progressText.setText(String.format(Locale.ENGLISH, "(%1$d/%2$d)", values[0], totalTiles));
    }

    @Override
    protected Integer doInBackground(String... params) {
        return createNewPuzzle(xValue, yValue, environmentId, blankPuzzle);
    }

    public void cancel() {
        cancelReceived = true;
    }

    private int createNewPuzzle(int maxX, int maxY, int environmentId, boolean blankPuzzle) {
        totalTiles = maxX * maxY;
        if (blankPuzzle) {
            return createEmptyPuzzle(maxX, maxY, environmentId);
        } else {
            return createFilledPuzzle(maxX, maxY, environmentId);
        }
    }

    private int createEmptyPuzzle(int maxX, int maxY, final int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        int defaultTileId = getDefaultTileId(environmentId);
        Puzzle newPuzzle = createBasicPuzzleObject(newPuzzleId);
        PuzzleCustom puzzleCustom = createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY);

        newPuzzle.save();
        puzzleCustom.save();

        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                if (cancelReceived) {
                    newPuzzle.safelyDelete();
                    return 0;
                }

                tiles.add(new Tile(newPuzzleId, defaultTileId, x, y, Constants.ROTATION_NORTH));
                publishProgress(tiles.size());
            }
        }
        Tile.saveInTx(tiles);
        return newPuzzleId;
    }

    private int createFilledPuzzle(int maxX, int maxY, int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        Puzzle newPuzzle = createBasicPuzzleObject(newPuzzleId);
        PuzzleCustom puzzleCustom = createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY);

        newPuzzle.save();
        puzzleCustom.save();

        List<Tile> tiles = new ArrayList<>();
        int prevY = 0;
        int failedAttempts = 0;
        int totalAttempts = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                if (cancelReceived) {
                    newPuzzle.safelyDelete();
                    return 0;
                }

                List<Tile> potentialTiles = getPossibleTiles(newPuzzleId, tiles, x, y, maxX - 1, maxY - 1, environmentId);
                if (potentialTiles.size() > 0) {
                    Tile selectedTile = potentialTiles.get(RandomHelper.getNumber(0, potentialTiles.size() - 1));
                    tiles.add(selectedTile);
                    prevY = y;
                    failedAttempts = 0;
                } else {
                    failedAttempts++;
                    totalAttempts++;
                    if (failedAttempts > 3 || totalAttempts > 10) {
                        tiles.add(new Tile(newPuzzleId, 0, x, y, Constants.ROTATION_NORTH));
                        prevY = y;
                        failedAttempts = 0;
                        totalAttempts = 0;
                    } else {
                        tiles.remove(tiles.size() - 1);
                        if (y == 0) {
                            x--;
                            y = maxY - 1;
                        } else {
                            y = prevY - 1;
                        }
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

        // If there's no flow, then do any height we want
        int sHeight = sFlow > 0 && tileY > 0 ? southTile.getHeight(Constants.SIDE_NORTH) : -1;
        int wHeight = wFlow > 0 && tileX > 0 ? westTile.getHeight(Constants.SIDE_EAST) : -1;

        // Make list
        List<Tile> tiles = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_NORTH, nFlow, eFlow, sFlow, wFlow, nHeight, eHeight, sHeight, wHeight);
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_WEST, wFlow, nFlow, eFlow, sFlow, wHeight, nHeight, eHeight, sHeight));
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_SOUTH, sFlow, wFlow, nFlow, eFlow, sHeight, wHeight, nHeight, eHeight));
        tiles.addAll(getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_EAST, eFlow, sFlow, wFlow, nFlow, eHeight, sHeight, wHeight, nHeight));

        return tiles;
    }

    private static List<Tile> getPossibleTilesByRotation(int puzzleId, int x, int y, int environmentId, int rotation, int nFlow, int eFlow, int sFlow, int wFlow, int nHeight, int eHeight, int sHeight, int wHeight) {
        String sql = String.format(Locale.ENGLISH,
                "SELECT * FROM tile_type WHERE environment_id %1$s %2$d AND flow_north %3$s %4$d AND flow_east %5$s %6$d AND flow_south %7$s %8$d AND flow_west %9$s %10$d AND height_north %11$s %12$d AND height_east %13$s %14$d AND height_south %15$s %16$d AND height_west %17$s %18$d AND status = %19$d " +
                        (x == 0 && y == 0 ? "AND (flow_north > 0 OR flow_east > 0 OR flow_south > 0 OR flow_west > 0)" : ""),
                environmentId > 0 ? "=" : ">=", environmentId,
                nFlow >= 0 ? "=" : ">=", nFlow,
                eFlow >= 0 ? "=" : ">=", eFlow,
                sFlow >= 0 ? "=" : ">=", sFlow,
                wFlow >= 0 ? "=" : ">=", wFlow,
                nHeight >= 0 ? "=" : ">=", nHeight,
                eHeight >= 0 ? "=" : ">=", eHeight,
                sHeight >= 0 ? "=" : ">=", sHeight,
                wHeight >= 0 ? "=" : ">=", wHeight,
                Constants.TILE_STATUS_UNLOCKED);
        List<TileType> tileTypes = TileType.findWithQuery(TileType.class, sql);

        List<Tile> tiles = new ArrayList<>();
        for (TileType tile : tileTypes) {
            tiles.add(new Tile(puzzleId, tile.getTypeId(), x, y, rotation));
        }
        return tiles;
    }
}
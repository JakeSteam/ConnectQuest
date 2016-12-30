package uk.co.jakelee.cityflow.helper;

import android.util.Pair;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class TileHelper {

    public static Pair<Integer, Integer> getMaxXY(List<Tile> tiles) {
        int maxX = 0;
        int maxY = 0;
        for (Tile tile : tiles) {
            if (tile.getX() > maxX) {
                maxX = tile.getX();
            }
            if (tile.getY() > maxY) {
                maxY = tile.getY();
            }
        }
        return new Pair<>(maxX, maxY);
    }

    public static Pair<List<Integer>, List<Integer>> checkPuzzleFlow(int puzzleId, Pair<List<Integer>, List<Integer>> badTiles) {
        List<Long> checkedIds = new ArrayList<>();
        List<Integer> newTilesX = new ArrayList<>();
        List<Integer> newTilesY = new ArrayList<>();

        final int tileCount = badTiles.first.size();
        for (int i = 0; i < tileCount; i++) {
            Tile tile = Tile.get(puzzleId, badTiles.first.get(i), badTiles.second.get(i));
            if (tile != null && !checkedIds.contains(tile.getId()) && !checkTileFlow(tile)) {
                checkedIds.add(tile.getId());
                newTilesX.add(tile.getX());
                newTilesY.add(tile.getY());
            }
        }

        return new Pair<>(newTilesX, newTilesY);
    }

    public static Pair<List<Integer>, List<Integer>> checkFirstPuzzleFlow(final List<Tile> tiles) {
        final List<Integer> newTilesX = new ArrayList<>();
        final List<Integer> newTilesY = new ArrayList<>();

        final int tilesCount = tiles.size();
        ExecutorService taskExecutor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < tilesCount; i++) {
            final int j = i;

            taskExecutor.execute(new Runnable() {
                public void run() {
                    Tile tile = tiles.get(j);
                    if (!checkTileFlow(tile)) {
                        newTilesX.add(tile.getX());
                        newTilesY.add(tile.getY());
                    }
                }
            });
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }

        return new Pair<>(newTilesX, newTilesY);
    }

    private static boolean checkTileFlow(Tile tile) {
        return checkTileFlow(tile, Constants.SIDE_NORTH) &&
                checkTileFlow(tile, Constants.SIDE_EAST) &&
                checkTileFlow(tile, Constants.SIDE_SOUTH) &&
                checkTileFlow(tile, Constants.SIDE_WEST);
    }

    private static boolean checkTileFlow(Tile currentTile, int currentSide) {
        Tile otherTile = getNeighbouringTile(currentTile, currentSide);

        /*// Skip invisible tiles, and only check west & south flows if we're against the edge
        if (currentSide == Constants.SIDE_WEST && currentTile.getX() != 0 || currentSide == Constants.SIDE_SOUTH && currentTile.getY() != 0) {
            return true;
        }*/

        int otherSide = (currentSide == Constants.SIDE_NORTH || currentSide == Constants.SIDE_EAST) ?
                currentSide + 2 :
                currentSide - 2;

        int currentFlow = currentTile.getFlow(currentSide);
        int currentHeight = currentTile.getHeight(currentSide);
        int otherFlow = otherTile.getFlow(otherSide);
        int otherHeight = otherTile.getHeight(otherSide);

        boolean flow = currentFlow == otherFlow; // || tileIsInvisible(otherTile.getTileTypeId()) && flowIsDecorative(currentFlow);
        boolean height = currentHeight == otherHeight || tileIsInvisible(otherTile.getTileTypeId()) || currentFlow == 0;

        return flow && height;
    }

    public static boolean tileIsInvisible(int tileTypeId) {
        return tileTypeId == 0;
    }

    public static TileType getTileType(Tile tile) {
        TileType defaultType = new TileType();

        List<TileType> typeResults = Select.from(TileType.class).where(
                Condition.prop("type_id").eq(tile.getTileTypeId())).list();

        if (typeResults.size() == 0) {
            return defaultType;
        } else {
            return typeResults.get(0);
        }
    }

    private static Tile getNeighbouringTile(Tile tile, int side) {
        int x = tile.getX();
        int y = tile.getY();
        switch (side) {
            case (Constants.SIDE_NORTH):
                y++;
                break;
            case (Constants.SIDE_EAST):
                x++;
                break;
            case (Constants.SIDE_SOUTH):
                y--;
                break;
            case (Constants.SIDE_WEST):
                x--;
                break;
        }

        List<Tile> tileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(x),
                Condition.prop("y").eq(y),
                Condition.prop("tile_type_id").gt(0)).list();

        if (tileResults.size() == 0) {
            return new Tile();
        } else {
            return tileResults.get(0);
        }
    }
}

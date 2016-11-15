package uk.co.jakelee.cityflow.helper;

import android.util.Pair;
import android.widget.TextView;

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

    public static Pair<List<Integer>, List<Integer>> checkPuzzleFlow(int puzzleId, Pair<List<Integer>, List<Integer>> badTiles, final TextView loadingView) {
        List<Long> checkedIds = new ArrayList<>();
        List<Integer> newTilesX = new ArrayList<>();
        List<Integer> newTilesY = new ArrayList<>();

        final int tileCount = badTiles.first.size();
        for (int i = 0; i < tileCount; i++) {
            Tile tile = Tile.get(puzzleId, badTiles.first.get(i), badTiles.second.get(i));
            if (!checkedIds.contains(tile.getId()) && !checkTileFlow(tile)) {
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

    public static boolean checkTileFlow(Tile tile) {
        // If the neighbouring tile is empty, the height is fine.
        // If the tile has no flow on that side, it's fine.
        // Otherwise, check the heights match up.

        Tile northTile = getTile(tile, Constants.SIDE_NORTH);
        boolean flowN = tile.getFlow(Constants.SIDE_NORTH) == northTile.getFlow(Constants.SIDE_SOUTH);
        boolean heightN = (northTile.getTileTypeId() == 0 || tile.getFlow(Constants.SIDE_NORTH) == 0 || tile.getHeight(Constants.SIDE_NORTH) == northTile.getHeight(Constants.SIDE_SOUTH));
        if (!flowN || !heightN) {
            return false;
        }

        Tile eastTile = getTile(tile, Constants.SIDE_EAST);
        boolean flowE = tile.getFlow(Constants.SIDE_EAST) == eastTile.getFlow(Constants.SIDE_WEST);
        boolean heightE = (eastTile.getTileTypeId() == 0 || tile.getFlow(Constants.SIDE_EAST) == 0 || tile.getHeight(Constants.SIDE_EAST) == eastTile.getHeight(Constants.SIDE_WEST));
        if (!flowE || !heightE) {
            return false;
        }

        // Only check south flows if we're on the south side
        if (tile.getY() == 0) {
            Tile southTile = getTile(tile, Constants.SIDE_SOUTH);
            boolean flowS = tile.getFlow(Constants.SIDE_SOUTH) == southTile.getFlow(Constants.SIDE_NORTH);
            boolean heightS = (southTile.getTileTypeId() == 0 || tile.getFlow(Constants.SIDE_SOUTH) == 0 || tile.getHeight(Constants.SIDE_SOUTH) == southTile.getHeight(Constants.SIDE_NORTH));
            if (!flowS || !heightS) {
                return false;
            }
        }

        // Only check west flows if we're on the west side
        if (tile.getX() == 0) {
            Tile westTile = getTile(tile, Constants.SIDE_WEST);
            boolean flowW = tile.getFlow(Constants.SIDE_WEST) == westTile.getFlow(Constants.SIDE_EAST);
            boolean heightW = (westTile.getTileTypeId() == 0 || tile.getFlow(Constants.SIDE_WEST) == 0 || tile.getHeight(Constants.SIDE_WEST) == westTile.getHeight(Constants.SIDE_EAST));
            if (!flowW || !heightW) {
                return false;
            }
        }

        return true;
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

    private static Tile getTile(Tile tile, int side) {
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

        Tile tileDefault = new Tile();
        tileDefault.setX(-99);
        tileDefault.setY(-99);

        List<Tile> tileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(x),
                Condition.prop("y").eq(y),
                Condition.prop("tile_type_id").gt(0)).list();

        if (tileResults.size() == 0) {
            return tileDefault;
        } else {
            return tileResults.get(0);
        }
    }
}

package uk.co.jakelee.cityflow.helper;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.Tile_Type;

public class TileHelper {

    public static int getMaxY(List<Tile> tiles) {
        int maxY = 0;
        for (Tile tile : tiles) {
            if (tile.getY() > maxY) {
                maxY = tile.getY();
            }
        }
        return maxY;
    }

    public static boolean checkPuzzleFlow(int puzzleId) {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        boolean doesItFlow = true;
        for (Tile tile : tiles) {
            if (!checkTileFlow(tile)) {
                doesItFlow = false;
                break;
            }
        }
        return doesItFlow;
    }

    public static boolean checkTileFlow(Tile tile) {
        Tile northTile = getTile(tile, Constants.SIDE_NORTH);
        Tile eastTile = getTile(tile, Constants.SIDE_EAST);
        Tile southTile = getTile(tile, Constants.SIDE_SOUTH);
        Tile westTile = getTile(tile, Constants.SIDE_WEST);

        boolean flowN = tile.getFlow(Constants.SIDE_NORTH) == northTile.getFlow(Constants.SIDE_SOUTH);
        boolean flowE = tile.getFlow(Constants.SIDE_EAST) == eastTile.getFlow(Constants.SIDE_WEST);
        boolean flowS = tile.getFlow(Constants.SIDE_SOUTH) == southTile.getFlow(Constants.SIDE_NORTH);
        boolean flowW = tile.getFlow(Constants.SIDE_WEST) == westTile.getFlow(Constants.SIDE_EAST);
        if (!flowN || !flowE || !flowS || !flowW) {
            return false;
        }

        // If the neighbouring tile is empty, the height is fine.
        // If the tile has no flow on that side, it's fine.
        // Otherwise, check the heights match up.
        boolean heightN = (northTile.getPuzzleId() == 0 || tile.getFlow(Constants.SIDE_NORTH) == 0 || tile.getHeight(Constants.SIDE_NORTH) == northTile.getHeight(Constants.SIDE_SOUTH));
        boolean heightE = (eastTile.getPuzzleId() == 0 || tile.getFlow(Constants.SIDE_EAST) == 0 || tile.getHeight(Constants.SIDE_EAST) == eastTile.getHeight(Constants.SIDE_WEST));
        boolean heightS = (southTile.getPuzzleId() == 0 || tile.getFlow(Constants.SIDE_SOUTH) == 0 || tile.getHeight(Constants.SIDE_SOUTH) == southTile.getHeight(Constants.SIDE_NORTH));
        boolean heightW = (westTile.getPuzzleId() == 0 || tile.getFlow(Constants.SIDE_WEST) == 0 || tile.getHeight(Constants.SIDE_WEST) == westTile.getHeight(Constants.SIDE_EAST));
        return heightN && heightE && heightS && heightW;
    }

    public static Tile_Type getTileType(Tile tile) {
        Tile_Type defaultType = new Tile_Type();

        List<Tile_Type> typeResults = Select.from(Tile_Type.class).where(
                Condition.prop("type_id").eq(tile.getTileTypeId())).list();

        if (typeResults.size() == 0) {
            return defaultType;
        } else {
            return typeResults.get(0);
        }
    }

    public static Tile getTile(Tile tile, int side) {
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
                Condition.prop("y").eq(y)).list();

        if (tileResults.size() == 0) {
            return tileDefault;
        } else {
            return tileResults.get(0);
        }
    }
}

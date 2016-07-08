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
            } else if (!checkTileHeight(tile)) {
                doesItFlow = false;
                break;
            }
        }
        return doesItFlow;
    }

    public static boolean checkTileFlow(Tile tile) {
        return tile.getFlow(Constants.SIDE_NORTH) == getTile(tile, Constants.SIDE_NORTH).getFlow(Constants.SIDE_SOUTH) &&
                tile.getFlow(Constants.SIDE_EAST) == getTile(tile, Constants.SIDE_EAST).getFlow(Constants.SIDE_WEST) &&
                tile.getFlow(Constants.SIDE_SOUTH) == getTile(tile, Constants.SIDE_SOUTH).getFlow(Constants.SIDE_NORTH) &&
                tile.getFlow(Constants.SIDE_WEST) == getTile(tile, Constants.SIDE_WEST).getFlow(Constants.SIDE_EAST);
    }

    public static boolean checkTileHeight(Tile tile) {
        return  tile.getHeight(Constants.SIDE_NORTH) == getTile(tile, Constants.SIDE_NORTH).getHeight(Constants.SIDE_SOUTH) &&
                tile.getHeight(Constants.SIDE_EAST) == getTile(tile, Constants.SIDE_EAST).getHeight(Constants.SIDE_WEST) &&
                tile.getHeight(Constants.SIDE_SOUTH) == getTile(tile, Constants.SIDE_SOUTH).getHeight(Constants.SIDE_NORTH) &&
                tile.getHeight(Constants.SIDE_WEST) == getTile(tile, Constants.SIDE_WEST).getHeight(Constants.SIDE_EAST);
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

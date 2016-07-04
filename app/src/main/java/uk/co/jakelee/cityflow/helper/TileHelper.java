package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.widget.ImageView;

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

    public static ImageView createTileImageView(Context context, Tile tile, int maxY, int width, int height) {
        ImageView image = new ImageView(context);
        int leftPadding = (tile.getY() + tile.getX()) * (width/2);
        int topPadding = (tile.getX() + maxY - tile.getY()) * (height/2);
        image.setPadding(leftPadding, topPadding, 0, 0);

        return image;
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
        boolean flowCorrect = true;
        Tile northTile = getNorthTile(tile);
        int tileNorthFlow = tile.getNorthFlow();
        int northTileSouthFlow = northTile.getSouthFlow();
        if ((northTile.getPuzzleId() == 0 && tileNorthFlow != Constants.FLOW_NONE) || tileNorthFlow != northTileSouthFlow) {
            flowCorrect = false;
        }
        return flowCorrect;
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

    public static Tile getNorthTile(Tile tile) {
        Tile northTileDefault = new Tile();

        List<Tile> northTileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(tile.getX()),
                Condition.prop("y").eq(tile.getY() + 1)).list();

        if (northTileResults.size() == 0) {
            return northTileDefault;
        } else {
            return northTileResults.get(0);
        }
    }
}

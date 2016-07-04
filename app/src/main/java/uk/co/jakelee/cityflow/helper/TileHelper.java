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
        if (tile.getNorthFlow() != getNorthTile(tile).getSouthFlow()) {
            int tileNorthFlow = tile.getNorthFlow();
            Tile northTile = getNorthTile(tile);
            int northTileSouthFlow = northTile.getSouthFlow();
            flowCorrect = false;
        } else if (tile.getEastFlow() != getEastTile(tile).getWestFlow()) {
            int tileEastFlow = tile.getEastFlow();
            Tile eastTile = getEastTile(tile);
            int eastTileWest = eastTile.getWestFlow();
            flowCorrect = false;
        } else if (tile.getSouthFlow() != getSouthTile(tile).getNorthFlow()) {
            int tileSouthFlow = tile.getSouthFlow();
            Tile southTile = getSouthTile(tile);
            int southTileNorthFlow = southTile.getNorthFlow();
            flowCorrect = false;
        } else if (tile.getWestFlow() != getWestTile(tile).getEastFlow()) {
            int tileWestFlow = tile.getWestFlow();
            Tile westTile = getWestTile(tile);
            int westTileEast = westTile.getEastFlow();
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
        northTileDefault.setX(-99);
        northTileDefault.setY(-99);

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

    public static Tile getEastTile(Tile tile) {
        Tile eastTileDefault = new Tile();
        eastTileDefault.setX(-99);
        eastTileDefault.setY(-99);

        List<Tile> eastTileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(tile.getX() + 1),
                Condition.prop("y").eq(tile.getY())).list();

        if (eastTileResults.size() == 0) {
            return eastTileDefault;
        } else {
            return eastTileResults.get(0);
        }
    }

    public static Tile getSouthTile(Tile tile) {
        Tile southTileDefault = new Tile();
        southTileDefault.setX(-99);
        southTileDefault.setY(-99);

        List<Tile> southTileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(tile.getX()),
                Condition.prop("y").eq(tile.getY() - 1)).list();

        if (southTileResults.size() == 0) {
            return southTileDefault;
        } else {
            return southTileResults.get(0);
        }
    }

    public static Tile getWestTile(Tile tile) {
        Tile westTileDefault = new Tile();
        westTileDefault.setX(-99);
        westTileDefault.setY(-99);

        List<Tile> westTileResults = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(tile.getPuzzleId()),
                Condition.prop("x").eq(tile.getX() - 1),
                Condition.prop("y").eq(tile.getY())).list();

        if (westTileResults.size() == 0) {
            return westTileDefault;
        } else {
            return westTileResults.get(0);
        }
    }
}

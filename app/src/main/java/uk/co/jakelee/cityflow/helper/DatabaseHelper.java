package uk.co.jakelee.cityflow.helper;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.Tile_Type;

public class DatabaseHelper {
    public final static int NO_DATABASE = 0;
    public final static int V1_0_0 = 1;

    public static void handlePatches() {
        if (MainActivity.prefs.getInt("databaseVersion", DatabaseHelper.NO_DATABASE) <= DatabaseHelper.NO_DATABASE) {
            DatabaseHelper.initialSetup();
            MainActivity.prefs.edit().putInt("databaseVersion", DatabaseHelper.V1_0_0).apply();
        }
    }

    private static void initialSetup() {
        createPuzzle();
        createSetting();
        createStatistic();
        createTile();
        createTileType();
    }

    private static void createPuzzle() {
        List<Puzzle> puzzles = new ArrayList<>();
            puzzles.add(new Puzzle(1, 0, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(2, 0, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(3, 0, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(4, 0, Constants.TYPE_STORY, 1, 0L, 0, 0));
        Puzzle.saveInTx(puzzles);
    }

    private static void createSetting() {
        List<Setting> settings = new ArrayList<>();
            settings.add(new Setting(1, "Music", true));
            settings.add(new Setting(2, "Sounds", true));
        Setting.saveInTx(settings);
    }

    private static void createStatistic() {
        List<Statistic> statistics = new ArrayList<>();
            statistics.add(new Statistic(1, "PuzzlesCompleted", 0));
            statistics.add(new Statistic(2, "TilesRotated", 0));
        Statistic.saveInTx(statistics);
    }

    private static void createTile() {
        List<Tile> tiles = new ArrayList<>();
            // Flow test
            tiles.add(new Tile(1, 3, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 3, 0, 1, Constants.ROTATION_EAST));

            // Line
            tiles.add(new Tile(2, 3, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(2, 3, 0, 1, Constants.ROTATION_EAST));
            tiles.add(new Tile(2, 3, 1, 0, Constants.ROTATION_WEST));
            tiles.add(new Tile(2, 3, 1, 1, Constants.ROTATION_SOUTH));

            // Solveable square
            tiles.add(new Tile(3, 1, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(3, 2, 0, 1, Constants.ROTATION_EAST));
            tiles.add(new Tile(3, 2, 0, 2, Constants.ROTATION_EAST));
            tiles.add(new Tile(3, 1, 0, 3, Constants.ROTATION_EAST));
            tiles.add(new Tile(3, 2, 1, 0, Constants.ROTATION_WEST));
            tiles.add(new Tile(3, 1, 1, 1, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 1, 1, 2, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 2, 1, 3, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 2, 2, 0, Constants.ROTATION_WEST));
            tiles.add(new Tile(3, 1, 2, 1, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 1, 2, 2, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 2, 2, 3, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 1, 3, 0, Constants.ROTATION_WEST));
            tiles.add(new Tile(3, 2, 3, 1, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 2, 3, 2, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(3, 1, 3, 3, Constants.ROTATION_SOUTH));

            tiles.add(new Tile(4, 3, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 11, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 11, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 3, 0, 3, Constants.ROTATION_NORTH));
        Tile.saveInTx(tiles);
    }

    private static void createTileType() {
        List<Tile_Type> tileTypes = new ArrayList<>();
            tileTypes.add(new Tile_Type(1, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(2, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(3, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(4, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(5, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(6, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(7, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(8, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(9, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(10, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(11, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(12, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL));
        Tile_Type.saveInTx(tileTypes);
    }
}

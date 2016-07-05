package uk.co.jakelee.cityflow.helper;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Chapter;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
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
        createChapter();
        createPuzzle();
        createSetting();
        createStatistic();
        createText();
        createTile();
        createTileType();
    }

    private static void createText() {
        List<Text> texts = new ArrayList<>();
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PACK_1_NAME, "Pack 1 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PACK_1_DESC, "Desc 1 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PACK_2_NAME, "Pack 2 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PACK_2_DESC, "Desc 2 English"));

            texts.add(new Text(Constants.LANGUAGE_OTHER, TextHelper.PACK_1_NAME, "Pack 1 Other"));
            texts.add(new Text(Constants.LANGUAGE_OTHER, TextHelper.PACK_1_DESC, "Desc 1 Other"));
            texts.add(new Text(Constants.LANGUAGE_OTHER, TextHelper.PACK_2_NAME, "Pack 2 Other"));
            texts.add(new Text(Constants.LANGUAGE_OTHER, TextHelper.PACK_2_DESC, "Desc 2 Other"));
        Text.saveInTx(texts);
    }

    private static void createChapter() {
        List<Chapter> chapters = new ArrayList<>();
            chapters.add(new Chapter(1, TextHelper.PACK_1_NAME, TextHelper.PACK_1_DESC, "test", true));
            chapters.add(new Chapter(2, TextHelper.PACK_2_NAME, TextHelper.PACK_2_DESC, "test", true));
        Chapter.saveInTx(chapters);
    }

    private static void createPuzzle() {
        List<Puzzle> puzzles = new ArrayList<>();
            puzzles.add(new Puzzle(1, 1, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(2, 1, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(3, 1, Constants.TYPE_STORY, 1, 0L, 0, 0));
            puzzles.add(new Puzzle(4, 1, Constants.TYPE_STORY, 1, 0L, 0, 0));
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

            // Testing all tiles
            tiles.add(new Tile(4, 1, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 2, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 3, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 4, 0, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 5, 0, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 6, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 7, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 8, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 9, 1, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 10, 1, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 11, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 12, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 13, 2, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 14, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 15, 2, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 16, 3, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 17, 3, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 18, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 19, 3, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 20, 3, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 21, 4, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 22, 4, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 23, 4, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 24, 4, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 25, 4, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 26, 5, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 27, 5, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 28, 5, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 29, 5, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 30, 5, 4, Constants.ROTATION_NORTH));
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
            tileTypes.add(new Tile_Type(13, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(14, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(15, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(16, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(17, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(18, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(19, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(20, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(21, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(22, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(23, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(24, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(25, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(26, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(27, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(28, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(29, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(30, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));

        Tile_Type.saveInTx(tileTypes);
    }
}

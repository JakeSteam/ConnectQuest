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
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.CHAPTER_1_NAME, "Pack 1 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.CHAPTER_1_DESC, "Desc 1 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.CHAPTER_2_NAME, "Pack 2 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.CHAPTER_2_DESC, "Desc 2 English"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.SETTING_MUSIC, "Music"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.SETTING_SOUND, "Sounds"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_1_NAME, "Testing large maps"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_2_NAME, "Simple square"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_3_NAME, "More complex square"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_4_NAME, "All possible tiles"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_5_NAME, "City Flow logo"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, TextHelper.PUZZLE_END_TEXT, "It flows!\n\n%1$d stars\nTime: %2$s\nMoves: %3$d\n\nTarget Time: %4$s\nTarget Moves: %5$d\n\nBest Time: %6$s%7$s\nBest Moves: %8$d%9$s"));
        Text.saveInTx(texts);
    }

    private static void createChapter() {
        List<Chapter> chapters = new ArrayList<>();
            chapters.add(new Chapter(1, TextHelper.CHAPTER_1_NAME, TextHelper.CHAPTER_1_DESC, "test", true));
            chapters.add(new Chapter(2, TextHelper.CHAPTER_2_NAME, TextHelper.CHAPTER_2_DESC, "test", false));
        Chapter.saveInTx(chapters);
    }

    private static void createPuzzle() {
        List<Puzzle> puzzles = new ArrayList<>();
            puzzles.add(new Puzzle(1, TextHelper.PUZZLE_1_NAME, 1, Constants.TYPE_STORY, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(2, TextHelper.PUZZLE_2_NAME, 1, Constants.TYPE_STORY, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(3, TextHelper.PUZZLE_3_NAME, 1, Constants.TYPE_STORY, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(4, TextHelper.PUZZLE_4_NAME, 1, Constants.TYPE_STORY, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(5, TextHelper.PUZZLE_5_NAME, 1, Constants.TYPE_STORY, 1, 10000L, 20, 0L, 0, 0));
        Puzzle.saveInTx(puzzles);
    }

    private static void createSetting() {
        List<Setting> settings = new ArrayList<>();
            settings.add(new Setting(1, TextHelper.SETTING_MUSIC, true));
            settings.add(new Setting(2, TextHelper.SETTING_SOUND, true));
        Setting.saveInTx(settings);
    }

    private static void createStatistic() {
        List<Statistic> statistics = new ArrayList<>();
            statistics.add(new Statistic(1, TextHelper.STATISTIC_PUZZLES_COMPLETED, 0));
            statistics.add(new Statistic(2, TextHelper.STATISTIC_TILES_ROTATED, 0));
        Statistic.saveInTx(statistics);
    }

    private static void createTile() {
        List<Tile> tiles = new ArrayList<>();
            // Testing large maps
            tiles.add(new Tile(1, 10, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 0, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 0, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 1, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 1, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 2, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 2, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 3, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 3, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 3, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 3, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 4, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 4, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 4, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 4, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 4, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 5, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 6, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 10, 7, 0, Constants.ROTATION_NORTH));

            // Simple line
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

            // All possible tiles
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
            tiles.add(new Tile(4, 31, 5, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 32, 6, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 33, 6, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 34, 6, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 35, 6, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 36, 6, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 37, 6, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 38, 7, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 39, 7, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 40, 7, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 41, 7, 3, Constants.ROTATION_NORTH));

            // City flow logo
            tiles.add(new Tile(5, 21, 0, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 1, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 31, 1, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 1, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 2, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 2, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 30, 2, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 21, 2, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 19, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 29, 3, 3, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 14, 3, 4, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 17, 3, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 19, 3, 6, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 18, 3, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 3, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 4, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 29, 4, 2, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 21, 4, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 26, 4, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 14, 4, 5, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 19, 4, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 17, 4, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 13, 4, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 4, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 5, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 5, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 19, 5, 2, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 18, 5, 3, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 26, 5, 4, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 18, 5, 5, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 19, 5, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 19, 5, 7, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 21, 5, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 5, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 5, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 6, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 6, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 6, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 1, 6, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 3, 6, 5, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 2, 6, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 1, 6, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 1, 6, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 3, 6, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 3, 6, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 3, 6, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 7, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 7, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 5, 7, 4, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 33, 7, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 7, 7, 6, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 7, 7, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 7, 7, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 7, 7, 9, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 7, 7, 10, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 7, 7, 11, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 8, 7, 12, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 6, 8, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 33, 8, 4, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 6, 8, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 3, 8, 6, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 1, 8, 7, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 1, 8, 8, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 1, 8, 9, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 5, 8, 10, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 1, 8, 11, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 9, 8, 12, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 6, 8, 13, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 8, 9, 4, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 9, 9, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 9, 9, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 8, 9, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 12, 9, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 12, 9, 9, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 8, 9, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 9, 9, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 8, 9, 12, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 6, 10, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 10, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 8, 10, 7, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 8, 10, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(5, 8, 10, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 8, 10, 10, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 6, 10, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 11, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 11, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 8, 11, 8, Constants.ROTATION_WEST));
            tiles.add(new Tile(5, 8, 11, 9, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(5, 6, 11, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 12, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 12, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 12, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 6, 13, 8, Constants.ROTATION_NORTH));
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
            tileTypes.add(new Tile_Type(7, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.HEIGHT_NORMAL));
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
            tileTypes.add(new Tile_Type(31, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(32, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(33, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(34, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_HIGH));
            tileTypes.add(new Tile_Type(35, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(36, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH));
            tileTypes.add(new Tile_Type(37, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH));
            tileTypes.add(new Tile_Type(38, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(39, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH));
            tileTypes.add(new Tile_Type(40, Constants.ENVIRONMENT_CITY, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(41, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(42, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(43, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(44, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
            tileTypes.add(new Tile_Type(45, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL));
        Tile_Type.saveInTx(tileTypes);
    }
}

package uk.co.jakelee.cityflow.helper;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Pack;
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
        createPack();
        createPuzzle();
        createSetting();
        createStatistic();
        createText();
        createTile();
        createTileType();
    }

    private static void createText() {
        List<Text> texts = new ArrayList<>();
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_1_NAME", "The Big City"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_1_DESC", "Get flowing in the big city."));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_2_NAME", "Escape To The Country"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_2_DESC", "Flow all the way out to the country."));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_MUSIC", "Music"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_SOUND", "Sounds"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_1_NAME", "One Shop Town"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_2_NAME", "No Crossing"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_3_NAME", "Don't Cross The Flows"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_4_NAME", "Transition The Flows"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_5_NAME", "Height Matters!"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_6_NAME", "Make Your Own Solution"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_7_NAME", "Gridlock"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_8_NAME", "Tree Lined Swimway"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_9_NAME", "Competitive Business"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_10_NAME", "The Long Way Round"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_11_NAME", "Highs And Lows"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_END_TEXT", "It flows!\n\n%1$d stars\nTime: %2$s\nMoves: %3$d\n\nTarget Time: %4$s\nTarget Moves: %5$d\n\nBest Time: %6$s%7$s\nBest Moves: %8$d%9$s\nNew Tiles: %10$d"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_1_TEXT", "Grass Road Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_2_TEXT", "Grass Road Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_3_TEXT", "Grass Road End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_4_TEXT", "Grass Road Interchange"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_5_TEXT", "Grass Road T Junction"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_6_TEXT", "Grass"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_7_TEXT", "Grass Road Bridge"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_8_TEXT", "Grass Water Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_9_TEXT", "Grass Water Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_10_TEXT", "Grass Road Straight (Median)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_11_TEXT", "Grass Road Slope"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_12_TEXT", "Grass Slope"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_13_TEXT", "City Red Shop"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_14_TEXT", "City Road Straight (Bus Stop)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_15_TEXT", "City Road Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_16_TEXT", "City Road Interchange"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_17_TEXT", "City Road T Junction"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_18_TEXT", "City Road End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_19_TEXT", "City Road Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_20_TEXT", "City Road Slope"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_21_TEXT", "City"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_22_TEXT", "City Path Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_23_TEXT", "City Path Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_24_TEXT", "City Path End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_25_TEXT", "City Road/Path T Junction"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_26_TEXT", "City Grass End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_27_TEXT", "City Grass Straight (Tree)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_28_TEXT", "City Grass Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_29_TEXT", "City Road Straight (Crossing)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_30_TEXT", "City Green Shop"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_31_TEXT", "City (Fountain)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_32_TEXT", "City (Grass)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_33_TEXT", "Grass Road/Water Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_34_TEXT", "Grass High"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_35_TEXT", "Grass Water End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_36_TEXT", "Grass High Road End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_37_TEXT", "Grass High Road Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_38_TEXT", "City Road End (Underground)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_39_TEXT", "Grass High Road Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_40_TEXT", "City Water Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_41_TEXT", "City Water End"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_42_TEXT", "Grass Road Corner (Sharp)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_43_TEXT", "Grass (Tree 1)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_44_TEXT", "Grass (Tree 2)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_45_TEXT", "Grass (Tree 3)"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_46_TEXT", "City Road/Path Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_47_TEXT", "City High Road Straight"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_48_TEXT", "City High Road Corner"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "STATISTIC_PUZZLES_COMPLETED", "Puzzles Completed"));
            texts.add(new Text(Constants.LANGUAGE_EN_GB, "STATISTIC_TILES_ROTATED", "Tiles Rotated"));
        Text.saveInTx(texts);
    }

    private static void createPack() {
        List<Pack> packs = new ArrayList<>();
            packs.add(new Pack(1, "IAP code", true));
            packs.add(new Pack(2, "IAP code", false));
        Pack.saveInTx(packs);
    }

    private static void createPuzzle() {
        List<Puzzle> puzzles = new ArrayList<>();
            puzzles.add(new Puzzle(1, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(2, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(3, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(4, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(5, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(6, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(7, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(8, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(9, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(10, 1, 10000L, 20, 0L, 0, 0));
            puzzles.add(new Puzzle(11, 1, 10000L, 20, 0L, 0, 0));
        Puzzle.saveInTx(puzzles);
    }

    private static void createSetting() {
        List<Setting> settings = new ArrayList<>();
            settings.add(new Setting(1, true));
            settings.add(new Setting(2, true));
            settings.add(new Setting(3, 0.75f));
            settings.add(new Setting(4, 1.75f));
        Setting.saveInTx(settings);
    }

    private static void createStatistic() {
        List<Statistic> statistics = new ArrayList<>();
            statistics.add(new Statistic(1, 0));
            statistics.add(new Statistic(2, 0));
        Statistic.saveInTx(statistics);
    }

    private static void createTile() {
        List<Tile> tiles = new ArrayList<>();
            tiles.add(new Tile(1, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 19, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 21, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 14, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 29, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 13, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 19, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 19, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(1, 21, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(2, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(2, 18, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(2, 18, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(2, 13, 1, 1, Constants.ROTATION_NORTH));

            tiles.add(new Tile(3, 41, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(3, 41, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(3, 26, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(3, 26, 1, 1, Constants.ROTATION_NORTH));

            tiles.add(new Tile(4, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 14, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 19, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 29, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 46, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 46, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 18, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 18, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(4, 30, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(5, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 20, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 48, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 20, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 48, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 48, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 48, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 48, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(5, 21, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(6, 41, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 31, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(6, 41, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(7, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 17, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 19, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 17, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 16, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 17, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 19, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 17, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(7, 19, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(8, 26, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 41, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 26, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 27, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 40, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 27, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 26, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 41, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(8, 26, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(9, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 18, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 13, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 17, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 14, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 18, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 19, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 18, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(9, 13, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(10, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 20, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 48, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 25, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 24, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 47, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 19, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 20, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(10, 48, 2, 2, Constants.ROTATION_NORTH));

            tiles.add(new Tile(11, 19, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 25, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 20, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 48, 0, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 38, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 22, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 13, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 47, 1, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 38, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 22, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 30, 2, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 20, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 19, 3, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 25, 3, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 29, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(11, 19, 3, 3, Constants.ROTATION_NORTH));

            // All possible tiles
            tiles.add(new Tile(98, 1, 0, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 2, 0, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 3, 0, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 4, 0, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 5, 0, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 6, 1, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 7, 1, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 8, 1, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 9, 1, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 10, 1, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 11, 2, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 12, 2, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 13, 2, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 14, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 15, 2, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 16, 3, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 17, 3, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 18, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 19, 3, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 20, 3, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 21, 4, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 22, 4, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 23, 4, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 24, 4, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 25, 4, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 26, 5, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 27, 5, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 28, 5, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 29, 5, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 30, 5, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 31, 5, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 32, 6, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 33, 6, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 34, 6, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 35, 6, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 36, 6, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 37, 6, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 38, 7, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 39, 7, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 40, 7, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 41, 7, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 42, 7, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 43, 7, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 44, 8, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 45, 8, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 46, 8, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 47, 8, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(98, 48, 8, 4, Constants.ROTATION_NORTH));

            // City flow logo
            tiles.add(new Tile(99, 21, 0, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 1, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 31, 1, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 1, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 2, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 2, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 2, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 30, 2, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 21, 2, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 19, 3, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 29, 3, 3, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 14, 3, 4, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 17, 3, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 19, 3, 6, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 18, 3, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 3, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 4, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 29, 4, 2, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 21, 4, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 26, 4, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 14, 4, 5, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 19, 4, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 17, 4, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 13, 4, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 4, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 5, 0, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 5, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 19, 5, 2, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 18, 5, 3, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 26, 5, 4, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 18, 5, 5, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 19, 5, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 19, 5, 7, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 21, 5, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 5, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 21, 5, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 6, 1, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 6, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 6, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 1, 6, 4, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 3, 6, 5, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 2, 6, 6, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 1, 6, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 1, 6, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 3, 6, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 3, 6, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 3, 6, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 7, 2, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 7, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 5, 7, 4, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 33, 7, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 7, 7, 6, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 7, 7, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 7, 7, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 7, 7, 9, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 7, 7, 10, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 7, 7, 11, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 8, 7, 12, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 6, 8, 3, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 33, 8, 4, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 6, 8, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 3, 8, 6, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 1, 8, 7, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 1, 8, 8, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 1, 8, 9, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 5, 8, 10, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 1, 8, 11, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 9, 8, 12, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 6, 8, 13, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 8, 9, 4, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 9, 9, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 9, 9, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 8, 9, 7, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 12, 9, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 12, 9, 9, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 8, 9, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 9, 9, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 8, 9, 12, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 6, 10, 5, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 10, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 8, 10, 7, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 8, 10, 8, Constants.ROTATION_EAST));
            tiles.add(new Tile(99, 8, 10, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 8, 10, 10, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 6, 10, 11, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 11, 6, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 11, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 8, 11, 8, Constants.ROTATION_WEST));
            tiles.add(new Tile(99, 8, 11, 9, Constants.ROTATION_SOUTH));
            tiles.add(new Tile(99, 6, 11, 10, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 12, 7, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 12, 8, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 12, 9, Constants.ROTATION_NORTH));
            tiles.add(new Tile(99, 6, 13, 8, Constants.ROTATION_NORTH));
        Tile.saveInTx(tiles);
    }

    private static void createTileType() {
        List<Tile_Type> tileTypes = new ArrayList<>();
            tileTypes.add(new Tile_Type(1, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 2));
            tileTypes.add(new Tile_Type(2, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 2));
            tileTypes.add(new Tile_Type(3, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(4, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(5, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(6, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(7, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(8, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(9, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(10, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(11, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(12, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(13, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(14, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(15, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(16, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(17, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(18, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(19, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(20, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(21, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(22, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(23, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(24, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(25, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(26, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(27, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(28, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(29, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(30, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(31, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(32, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(33, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(34, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
            tileTypes.add(new Tile_Type(35, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(36, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
            tileTypes.add(new Tile_Type(37, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
            tileTypes.add(new Tile_Type(38, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(39, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
            tileTypes.add(new Tile_Type(40, Constants.ENVIRONMENT_CITY, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(41, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(42, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(43, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(44, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(45, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(46, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
            tileTypes.add(new Tile_Type(47, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
            tileTypes.add(new Tile_Type(48, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        Tile_Type.saveInTx(tileTypes);
    }
}

package uk.co.jakelee.cityflow.helper;


public class Constants {
    // Intents
    public static final String INTENT_PACK = "uk.co.jakelee.cityflow.pack";
    public static final String INTENT_PUZZLE = "uk.co.jakelee.cityflow.puzzle";
    public static final String INTENT_PUZZLE_TYPE = "uk.co.jakelee.cityflow.puzzletype";
    public static final String INTENT_TILE = "uk.co.jakelee.cityflow.tile";
    public static final String INTENT_IAP = "uk.co.jakelee.cityflow.iap";
    public static final String INTENT_ITEM = "uk.co.jakelee.cityflow.item";

    // Game constants
    public static final int TILE_WIDTH = 130;
    public static final int TILE_HEIGHT = 65;
    public static final int PUZZLE_WIDTH = 129;
    public static final int PUZZLE_X_MIN = 1;
    public static final int PUZZLE_X_MAX = 10;
    public static final int PUZZLE_Y_MIN = 1;
    public static final int PUZZLE_Y_MAX = 10;
    public static final int PUZZLE_HEIGHT = 76;
    public static final int PUZZLE_CUSTOM_ID_OFFSET = 10000;

    // Currency incomes
    public static final int CURRENCY_RECOMPLETE = 1;
    public static final int CURRENCY_FIRST_COMPLETE = 10;
    public static final int CURRENCY_FIRST_COMPLETE_FULL = 15;
    public static final int CURRENCY_CUSTOM_FIRST_COMPLETE = 1;
    public static final int CURRENCY_CUSTOM_FIRST_COMPLETE_FULL = 1;
    public static final int CURRENCY_ADVERT = 45;
    public static final int CURRENCY_ACHIEVEMENT = 100;

    // Google play constants
    public static final String EVENT_COMPLETE_PUZZLE = "CgkIgrzuo64REAIQBA";
    public static final String EVENT_FULLY_COMPLETE_PUZZLE = "CgkIgrzuo64REAIQBQ";
    public static final String EVENT_USE_BOOST = "CgkIgrzuo64REAIQBg";
    public static final String EVENT_TILE_ROTATE = "CgkIgrzuo64REAIQBw";
    public static final String EVENT_IMPORT_PUZZLE = "CgkIgrzuo64REAIQCA";

    public static final String LEADERBOARD_PUZZLES_COMPLETED = "CgkIgrzuo64REAIQDg";
    public static final String LEADERBOARD_PUZZLES_FULLY_COMPLETED = "CgkIgrzuo64REAIQDw";
    public static final String LEADERBOARD_BOOSTS_USED = "CgkIgrzuo64REAIQEg";

    // Lookup constants
    public static final int BOOST_MIN = 1;
    public static final int BOOST_MAX = 4;
    public static final int BOOST_UNDO = 1;
    public static final int BOOST_TIME = 2;
    public static final int BOOST_MOVE = 3;
    public static final int BOOST_SHUFFLE = 4;
    public static final int ENVIRONMENT_MIN = 0;
    public static final int ENVIRONMENT_MAX = 2;
    public static final int ENVIRONMENT_NONE = 0;
    public static final int ENVIRONMENT_GRASS = 1;
    public static final int ENVIRONMENT_CITY = 2;
    public static final int FLOW_MIN = 0;
    public static final int FLOW_MAX = 5;
    public static final int FLOW_NONE = 0;
    public static final int FLOW_WATER = 1;
    public static final int FLOW_ROAD = 2;
    public static final int FLOW_PATH = 3;
    public static final int FLOW_GRASS = 4;
    public static final int FLOW_CANAL = 5;
    public static final int ITEM_BOOST_UNDO = 1;
    public static final int ITEM_BOOST_TIME = 2;
    public static final int ITEM_BOOST_MOVES = 3;
    public static final int ITEM_BOOST_SHUFFLE = 4;
    public static final int ITEM_BOOST_TIME_UPGRADE = 5;
    public static final int ITEM_BOOST_MOVES_UPGRADE = 6;
    public static final int ITEM_BOOST_SHUFFLE_UPGRADE = 7;
    public static final int ITEM_TILE_1 = 8;
    public static final int ITEM_TILE_2 = 9;
    public static final int ITEM_TILE_3 = 10;
    public static final int ITEM_UNLOCK_PACK = 11;
    public static final int ITEM_BOOST_UNDO_10 = 12;
    public static final int ITEM_BOOST_TIME_10 = 13;
    public static final int ITEM_BOOST_MOVES_10 = 14;
    public static final int ITEM_BOOST_SHUFFLE_10 = 15;
    public static final int ITEM_BOOST_UNDO_100 = 16;
    public static final int ITEM_BOOST_TIME_100 = 17;
    public static final int ITEM_BOOST_MOVES_100 = 18;
    public static final int ITEM_BOOST_SHUFFLE_100 = 19;
    public static final int HEIGHT_NO_CONNECT = 0;
    public static final int HEIGHT_LOW = 1;
    public static final int HEIGHT_NORMAL = 2;
    public static final int HEIGHT_HIGH = 3;
    public static final int LANGUAGE_EN_GB = 1;
    public static final int LANGUAGE_OTHER = 2;
    public static final int ROTATION_MIN = 1;
    public static final int ROTATION_MAX = 4;
    public static final int ROTATION_NORTH = 1;
    public static final int ROTATION_EAST = 2;
    public static final int ROTATION_SOUTH = 3;
    public static final int ROTATION_WEST = 4;
    public static final int SETTING_MUSIC = 1;
    public static final int SETTING_SOUNDS = 2;
    public static final int SETTING_MIN_ZOOM = 3;
    public static final int SETTING_MAX_ZOOM = 4;
    public static final int SETTING_ZEN_MODE = 5;
    public static final int SETTING_HIDE_UNSTOCKED_BOOSTS = 6;
    public static final int SETTING_PLAYER_NAME = 7;
    public static final int SETTING_SIGN_IN = 8;
    public static final int SKYSCRAPER_COMPLETE = 1;
    public static final int SKYSCRAPER_TIME = 2;
    public static final int SKYSCRAPER_MOVES = 3;
    public static final int STATISTIC_UNTRACKED = -1;
    public static final int STATISTIC_PUZZLES_COMPLETED = 1;
    public static final int STATISTIC_TILES_ROTATED = 2;
    public static final int STATISTIC_QUESTS_COMPLETED = 3;
    public static final int STATISTIC_PUZZLES_COMPLETED_FULLY = 4;
    public static final int STATISTIC_BOOSTS_USED = 5;
    public static final int STATISTIC_COMPLETE_PACK_1 = 6;
    public static final int STATISTIC_COMPLETE_PACK_2 = 7;
    public static final int STATISTIC_COMPLETE_PACK_3 = 8;
    public static final int STATISTIC_CURRENCY = 9;
    public static final int STORE_CATEGORY_BOOSTS = 1;
    public static final int STORE_CATEGORY_UPGRADES = 2;
    public static final int STORE_CATEGORY_TILES = 3;
    public static final int STORE_CATEGORY_MISC = 4;
    public static final int SIDE_NORTH = 1;
    public static final int SIDE_EAST = 2;
    public static final int SIDE_SOUTH = 3;
    public static final int SIDE_WEST = 4;
    public static final int TILE_STATUS_UNLOCKED = 0;
    public static final int TILE_STATUS_LOCKED = 1;
    public static final int TILE_STATUS_UNPURCHASED = 2;
}

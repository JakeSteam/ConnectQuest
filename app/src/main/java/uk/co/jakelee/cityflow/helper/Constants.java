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
    public static final int PUZZLE_X_MAX = 15;
    public static final int PUZZLE_X_DEFAULT = 3;
    public static final int PUZZLE_Y_MIN = 1;
    public static final int PUZZLE_Y_MAX = 15;
    public static final int PUZZLE_Y_DEFAULT = 3;
    public static final int NUMBER_CARS = 9;
    public static final int CARS_MIN_TIME = 6000;
    public static final int CARS_MAX_TIME = 40000;
    public static final int PUZZLE_HEIGHT = 76;
    public static final int PUZZLE_CUSTOM_ID_OFFSET = 10000;
    public static final int PUZZLE_NAME_MAX_LENGTH = 80;
    public static final int PUZZLE_DESC_MAX_LENGTH = 450;
    public static final int PLAYER_NAME_MAX_LENGTH = 30;

    public static final long PUZZLE_DEFAULT_TIME = 3600000;
    public static final int PUZZLE_DEFAULT_MOVES = 100000;
    public static final int AUTOSAVE_NEVER = 60;

    // Currency incomes
    public static final int CURRENCY_RECOMPLETE = 1;
    public static final int CURRENCY_FIRST_COMPLETE = 5;
    public static final int CURRENCY_FIRST_COMPLETE_FULL = 10;
    public static final int CURRENCY_CUSTOM_FIRST_COMPLETE = 1;
    public static final int CURRENCY_CUSTOM_FIRST_COMPLETE_FULL = 1;
    public static final int CURRENCY_ADVERT = 30;
    public static final int CURRENCY_ACHIEVEMENT = 100;
    public static final int CURRENCY_QUEST_EASY = 50;
    public static final int CURRENCY_QUEST_MEDIUM = 80;
    public static final int CURRENCY_QUEST_HARD = 110;
    public static final int CURRENCY_QUEST_ELITE = 150;

    // Google play constants
    public static final String EVENT_COMPLETE_PUZZLE = "CgkIgrzuo64REAIQBA";
    public static final String EVENT_FULLY_COMPLETE_PUZZLE = "CgkIgrzuo64REAIQBQ";
    public static final String EVENT_USE_BOOST = "CgkIgrzuo64REAIQBg";
    public static final String EVENT_TILE_ROTATE = "CgkIgrzuo64REAIQBw";
    public static final String EVENT_IMPORT_PUZZLE = "CgkIgrzuo64REAIQCA";
    public static final String EVENT_EARN_COINS = "CgkIgrzuo64REAIQLQ";
    public static final String EVENT_BUY_BOOST = "CgkIgrzuo64REAIQLg";
    public static final String EVENT_WATCH_ADVERT = "CgkIgrzuo64REAIQLw";

    public static final String LEADERBOARD_PUZZLES_COMPLETED = "CgkIgrzuo64REAIQDg";
    public static final String LEADERBOARD_PUZZLES_FULLY_COMPLETED = "CgkIgrzuo64REAIQDw";
    public static final String LEADERBOARD_BOOSTS_USED = "CgkIgrzuo64REAIQEg";

    // Lookup constants
    public static final int BACKGROUND_PLAIN = 1;
    public static final int BACKGROUND_NIGHT = 2;
    public static final int BACKGROUND_SUNRISE = 3;
    public static final int BACKGROUND_GRASS = 4;
    public static final int BACKGROUND_SALMON = 5;
    public static final int BACKGROUND_BLUISH = 6;
    public static final int BACKGROUND_PINK = 7;
    public static final int BACKGROUND_BARK = 8;
    public static final int BACKGROUND_EARTH = 9;
    public static final int BACKGROUND_STORMY_SKY = 10;
    public static final int BACKGROUND_DEEP_SEA = 11;
    public static final int BACKGROUND_LIMESTONE = 12;
    public static final int BACKGROUND_SAND = 13;
    public static final int BACKGROUND_FERN = 14;
    public static final int BACKGROUND_OLIVE = 15;
    public static final int BACKGROUND_FAWN = 16;
    public static final int BACKGROUND_SEDONA = 17;
    public static final int BACKGROUND_EGGSHELL = 18;
    public static final int BACKGROUND_CLAY = 19;
    public static final int BACKGROUND_CLAY_GREY = 20;
    public static final int BACKGROUND_MUDDY = 21;
    public static final int BACKGROUND_MUDDY_PINK = 22;
    public static final int BACKGROUND_LIMEY = 23;
    public static final int BACKGROUND_RICH_LIMEY = 24;
    public static final int BACKGROUND_CAMOUFLAGE = 25;
    public static final int BACKGROUND_OMINOUS = 26;
    public static final int BACKGROUND_CLOUDY = 27;
    public static final int BACKGROUND_BLUE_SKIES = 28;
    public static final int BACKGROUND_PETALS = 29;
    public static final int BACKGROUND_SUMMER = 30;
    public static final int BACKGROUND_PEACH = 31;
    public static final int BACKGROUND_PASSIONFRUIT = 32;
    public static final int BACKGROUND_THE_END = 33;
    public static final int BACKGROUND_DESERT = 34;
    public static final int BACKGROUND_DIRTY = 35;
    public static final int BACKGROUND_OVERCAST = 36;
    public static final int BACKGROUND_PRETTY_IN_PINK = 37;
    public static final int BACKGROUND_RAINFOREST = 38;
    public static final int BACKGROUND_MUSHROOM = 39;
    public static final int BOOST_MIN = 1;
    public static final int BOOST_MAX = 4;
    public static final int BOOST_UNDO = 1;
    public static final int BOOST_TIME = 2;
    public static final int BOOST_MOVE = 3;
    public static final int BOOST_SHUFFLE = 4;
    public static final int ENVIRONMENT_MIN = 0;
    public static final int ENVIRONMENT_MAX = 5;
    public static final int ENVIRONMENT_NONE = 0;
    public static final int ENVIRONMENT_GRASS = 1;
    public static final int ENVIRONMENT_CITY = 2;
    public static final int ENVIRONMENT_FOREST = 3;
    public static final int ENVIRONMENT_MOUNTAIN = 4;
    public static final int ENVIRONMENT_DESERT = 5;
    public static final int FLOW_MIN = 0;
    public static final int FLOW_MAX = 7;
    public static final int FLOW_NONE = 0;
    public static final int FLOW_WATER = 1;
    public static final int FLOW_ROAD = 2;
    public static final int FLOW_PATH = 3;
    public static final int FLOW_GRASS = 4;
    public static final int FLOW_CANAL = 5;
    public static final int FLOW_RIVER = 6;
    public static final int FLOW_DIRT = 7;
    public static final int FLOW_RAIL = 8;
    public static final int HEIGHT_MIN = 0;
    public static final int HEIGHT_MAX = 4;
    public static final int HEIGHT_NO_CONNECT = 0;
    public static final int HEIGHT_ULTRA_LOW = 1;
    public static final int HEIGHT_LOW = 2;
    public static final int HEIGHT_NORMAL = 3;
    public static final int HEIGHT_HIGH = 4;
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
    public static final int ITEM_BOOST_UNDO_10 = 12;
    public static final int ITEM_BOOST_TIME_10 = 13;
    public static final int ITEM_BOOST_MOVES_10 = 14;
    public static final int ITEM_BOOST_SHUFFLE_10 = 15;
    public static final int ITEM_BOOST_UNDO_100 = 16;
    public static final int ITEM_BOOST_TIME_100 = 17;
    public static final int ITEM_BOOST_MOVES_100 = 18;
    public static final int ITEM_BOOST_SHUFFLE_100 = 19;
    public static final int ITEM_MAX_CARS = 20;
    public static final int ITEM_PACK_2 = 21;
    public static final int ITEM_PACK_3 = 22;
    public static final int ITEM_PACK_4 = 23;
    public static final int ITEM_PACK_5 = 24;
    public static final int ITEM_PACK_6 = 25;
    public static final int ITEM_PACK_7 = 26;
    public static final int ITEM_PACK_8 = 27;
    public static final int LANGUAGE_MIN = 1;
    public static final int LANGUAGE_MAX = 2;
    public static final int LANGUAGE_EN = 1;
    public static final int LANGUAGE_RU = 2;
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
    public static final int SETTING_MAX_CARS = 9;
    public static final int SETTING_BACKGROUND = 10;
    public static final int SETTING_AUTOSAVE_FREQUENCY = 11;
    public static final int SETTING_LANGUAGE = 12;
    public static final int SETTING_VIBRATION = 13;
    public static final int SETTING_SOUND_PURCHASING = 14;
    public static final int SETTING_SOUND_ROTATING = 15;
    public static final int SKYSCRAPER_COMPLETE = 1;
    public static final int SKYSCRAPER_TIME = 2;
    public static final int SKYSCRAPER_MOVES = 3;
    public static final int STATISTIC_UNTRACKED = -1;
    public static final int STATISTIC_PUZZLES_COMPLETED = 1;
    public static final int STATISTIC_TILES_ROTATED = 2;
    public static final int STATISTIC_QUESTS_COMPLETED = 3;
    public static final int STATISTIC_PUZZLES_COMPLETED_FULLY = 4;
    public static final int STATISTIC_BOOSTS_USED = 5;
    public static final int STATISTIC_CURRENCY = 6;
    public static final int STATISTIC_TAPJOY_COINS = 7;
    public static final int STATISTIC_LAST_AUTOSAVE = 8;
    public static final int STATISTIC_COMPLETE_PACK_1 = 9;
    public static final int STATISTIC_COMPLETE_PACK_2 = 10;
    public static final int STATISTIC_COMPLETE_PACK_3 = 11;
    public static final int STATISTIC_COMPLETE_PACK_4 = 12;
    public static final int STATISTIC_COMPLETE_PACK_5 = 13;
    public static final int STATISTIC_COMPLETE_PACK_6 = 14;
    public static final int STATISTIC_COMPLETE_PACK_7 = 15;
    public static final int STATISTIC_COMPLETE_PACK_8 = 16;
    public static final int STATISTIC_COMPLETE_PACK_9 = 17;
    public static final int STORE_CATEGORY_BOOSTS = 1;
    public static final int STORE_CATEGORY_UPGRADES = 2;
    public static final int STORE_CATEGORY_TILES = 3;
    public static final int STORE_CATEGORY_MISC = 4;
    public static final int STORE_SUBCATEGORY_PACK = 1;
    public static final int SIDE_NORTH = 1;
    public static final int SIDE_EAST = 2;
    public static final int SIDE_SOUTH = 3;
    public static final int SIDE_WEST = 4;
    public static final int TILE_STATUS_UNLOCKED = 0;
    public static final int TILE_STATUS_LOCKED = 1;
    public static final int TILE_STATUS_UNPURCHASED = 2;
}

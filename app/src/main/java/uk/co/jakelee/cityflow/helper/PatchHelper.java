package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Achievement;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.ShopCategory;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class PatchHelper extends AsyncTask<String, String, String> {
    public final static int NO_DATABASE = 0;
    public final static int V1_0_0 = 1;
    public Activity callingActivity;
    public TextView progressText;
    public ProgressBar progressBar;

    public PatchHelper(Activity activity) {
        this.callingActivity = activity;
        this.progressText = (TextView)activity.findViewById(R.id.progressText);
        this.progressBar = (ProgressBar)activity.findViewById(R.id.progressBar);
    }

    public PatchHelper(Activity activity, boolean runningCloudImport) {
        this.callingActivity = activity;
    }

    private void initialSetup() {
        setProgress("Achievements", 0);
        createAchievement();
        setProgress("Boosts", 5);
        createBoost();
        setProgress("Packs", 10);
        createPack();
        setProgress("Settings", 15);
        createSetting();
        setProgress("Statistics", 20);
        createStatistic();
        setProgress("Store Items", 25);
        createStoreItem();
        setProgress("Store Categories", 30);
        createStoreCategory();
        setProgress("Tile Types", 35);
        createTileType();

        // Add 40 - 70 checkpoints inside createText
        setProgress("Text", 40);
        createText();

        // Add 70 - 100 checkpoints inside createPuzzle
        setProgress("Puzzles", 70);
        createPuzzle();
    }

    private void setProgress(String currentTask, int percentage) {
        if (progressText != null && progressBar != null) {
            publishProgress(currentTask);
            progressBar.setProgress(percentage);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        // Should inform the user that they've just updated if a patch has been installed
        String action = "";
        if (MainActivity.prefs.getInt("databaseVersion", PatchHelper.NO_DATABASE) <= PatchHelper.NO_DATABASE) {
            initialSetup();
            MainActivity.prefs.edit().putInt("databaseVersion", PatchHelper.V1_0_0).apply();
            action = "Finished installing database!";
        }
        return action;
    }

    @Override
    protected void onPostExecute(String result) {
        AlertHelper.success(callingActivity, result);

        RelativeLayout mainMenuWrapper = (RelativeLayout)callingActivity.findViewById(R.id.mainMenuWrapper);
        RelativeLayout progressWrapper = (RelativeLayout)callingActivity.findViewById(R.id.progressWrapper);
        if (mainMenuWrapper != null && progressWrapper != null) {
            progressWrapper.setVisibility(View.GONE);
            mainMenuWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(String... values) {
            progressText.setText("Currently installing " + values[0] + "...");
    }

    private static void createAchievement() {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement("Turn It Up 1", 100, Constants.STATISTIC_TILES_ROTATED, "CgkIgrzuo64REAIQAw"));
        achievements.add(new Achievement("Turn It Up 2", 1000, Constants.STATISTIC_TILES_ROTATED, "CgkIgrzuo64REAIQFw"));
        achievements.add(new Achievement("Turn It Up 3", 5000, Constants.STATISTIC_TILES_ROTATED, "CgkIgrzuo64REAIQGA"));
        achievements.add(new Achievement("Turn It Up 4", 10000, Constants.STATISTIC_TILES_ROTATED, "CgkIgrzuo64REAIQGQ"));
        achievements.add(new Achievement("The Best Quest 1", 5, Constants.STATISTIC_QUESTS_COMPLETED, "CgkIgrzuo64REAIQGg"));
        achievements.add(new Achievement("The Best Quest 2", 25, Constants.STATISTIC_QUESTS_COMPLETED, "CgkIgrzuo64REAIQGw"));
        achievements.add(new Achievement("The Best Quest 3", 75, Constants.STATISTIC_QUESTS_COMPLETED, "CgkIgrzuo64REAIQHA"));
        achievements.add(new Achievement("The Best Quest 4", 200, Constants.STATISTIC_QUESTS_COMPLETED, "CgkIgrzuo64REAIQHQ"));
        achievements.add(new Achievement("Working Your Way Through 1", 20, Constants.STATISTIC_PUZZLES_COMPLETED, "CgkIgrzuo64REAIQHg"));
        achievements.add(new Achievement("Working Your Way Through 2", 100, Constants.STATISTIC_PUZZLES_COMPLETED, "CgkIgrzuo64REAIQHw"));
        achievements.add(new Achievement("Working Your Way Through 3", 250, Constants.STATISTIC_PUZZLES_COMPLETED, "CgkIgrzuo64REAIQIA"));
        achievements.add(new Achievement("Working Your Way Through 4", 1500, Constants.STATISTIC_PUZZLES_COMPLETED, "CgkIgrzuo64REAIQIQ"));
        achievements.add(new Achievement("Completionist 1", 20, Constants.STATISTIC_PUZZLES_COMPLETED_FULLY, "CgkIgrzuo64REAIQIg"));
        achievements.add(new Achievement("Completionist 2", 100, Constants.STATISTIC_PUZZLES_COMPLETED_FULLY, "CgkIgrzuo64REAIQIw"));
        achievements.add(new Achievement("Completionist 3", 250, Constants.STATISTIC_PUZZLES_COMPLETED_FULLY, "CgkIgrzuo64REAIQJA"));
        achievements.add(new Achievement("Completionist 4", 1500, Constants.STATISTIC_PUZZLES_COMPLETED_FULLY, "CgkIgrzuo64REAIQJQ"));
        achievements.add(new Achievement("Activate Boost 1", 15, Constants.STATISTIC_BOOSTS_USED, "CgkIgrzuo64REAIQJg"));
        achievements.add(new Achievement("Activate Boost 2", 80, Constants.STATISTIC_BOOSTS_USED, "CgkIgrzuo64REAIQJw"));
        achievements.add(new Achievement("Activate Boost 3", 260, Constants.STATISTIC_BOOSTS_USED, "CgkIgrzuo64REAIQKA"));
        achievements.add(new Achievement("Activate Boost 4", 1000, Constants.STATISTIC_BOOSTS_USED, "CgkIgrzuo64REAIQKQ"));
        achievements.add(new Achievement("Complete Pack 1", 1, Constants.STATISTIC_COMPLETE_PACK_1, "CgkIgrzuo64REAIQKg"));
        achievements.add(new Achievement("Complete Pack 2", 1, Constants.STATISTIC_COMPLETE_PACK_2, "CgkIgrzuo64REAIQKw"));
        achievements.add(new Achievement("Complete Pack 3", 1, Constants.STATISTIC_COMPLETE_PACK_3, "CgkIgrzuo64REAIQLA"));
        Achievement.saveInTx(achievements);
    }

    private static void createBoost() {
        List<Boost> boosts = new ArrayList<>();
        boosts.add(new Boost(Constants.BOOST_UNDO, 1, 12, 0));
        boosts.add(new Boost(Constants.BOOST_TIME, 1, 16, 0));
        boosts.add(new Boost(Constants.BOOST_MOVE, 1, 18, 0));
        boosts.add(new Boost(Constants.BOOST_SHUFFLE, 1, 72, 0));
        Boost.saveInTx(boosts);
    }

    private static void createText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_CLOUD_BEGINNING", "Comparing local and cloud saves..."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_CLOUD_SAVING", "Saving to cloud..."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_CLOUD_SAVED", "Successfully saved game to cloud!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_CLOUD_LOADING", "Loading cloud save..."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_CLOUD_LOADED", "Successfully loaded game from cloud!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_COINS_EARNED", "Earned %1$d coin(s)!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_COINS_EARNED_FREE", "Earned %1$d free coin(s), enjoy!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_DELETE_PUZZLE", "Delete puzzle \"%1$s\"?\n\nNote: This can't be undone!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_SAVE_CONFLICT", "Save conflict detected! Currently resolving, this might take a few seconds, please be patient..."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_SETTING_TOGGLE_ON", "Toggled %1$s on!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ALERT_SETTING_TOGGLE_OFF", "Toggled %1$s off!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CLOUD_SAVE_DESC", "%1$d Stars | %2$d Coins | V%3$s"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREATOR_CREATED", "Created"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREATOR_IMPORTED", "Imported"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREATOR_IMPORT_PUZZLE", "Import Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREATOR_NEW_PUZZLE", "New Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_GRAPHICS_TITLE", "Graphics"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_GRAPHICS_TEXT", "<a href='http://www.dafont.com/claudep.d857'>Claude P</a>: Main font (Yagiuhf No 2).<br>" +
                "<a href='http://fontawesome.io/'>FontAwesome</a>: Button icons.<br>" +
                "<a href='http://kenney.nl/'>Kenney</a>: Base tilesets, majority of UI elements.<br>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_TECHNOLOGIES_TITLE", "Technologies"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_TECHNOLOGIES_TEXT", "<a href='https://github.com/ACRA/acra'>ACRA</a>: Crash management.<br>" +
                "<a href='https://github.com/scottyab/AESCrypt-Android'>AESCrypt</a>: Database / game save encrypting.<br>" +
                "<a href='https://github.com/hotchemi/Android-Rate'>Android-Rate</a>: Play Store rate prompting.<br>" +
                "<a href='https://www.applovin.com/'>AppLovin</a>: Advertising.<br>" +
                "<a href='https://github.com/grantland/android-autofittextview'>AutofitTextView</a>: Intelligently resizing textboxes.<br>" +
                "<a href='https://github.com/keyboardsurfer/Crouton'>Crouton</a>: On-screen alerts.<br>" +
                "<a href='https://github.com/bperin/FontAwesomeAndroid'>FontAwesomeAndroid</a>: FontAwesome support.<br>" +
                "<a href='https://github.com/google/gson'>GSON</a>: Cloud saves + puzzle sharing.<br>" +
                "<a href='https://github.com/anjlab/android-inapp-billing-v3'>IAB</a>: In-app billing.<br>" +
                "<a href='https://github.com/thomashaertel/MultiSpinner'>Multispinner</a>: Multi-selectable options list.<br>" +
                "<a href='https://github.com/square/picasso'>Picasso</a>: Image caching.<br>" +
                "<a href='https://github.com/satyan/sugar'>Sugar</a>: Database ORM.<br>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_OTHER_TITLE", "Other"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "CREDITS_OTHER_TEXT", "Of course, developing a game isn't just about technical resources. As such, there's a few people and communities who definitely need thanking.<br><br>" +
                "<b>Britt</b>: Responsible for a few of the more... interesting ideas in the game, and providing motivation throughout. If she had her way, the game would be a lot more absurd..!<br><br>" +
                "<b>Tash</b>: An absurd amount of design work. Basically, anywhere the game looks rather nice, she's the one to thank!<br><br>" +
                "<b>/r/AndroidGaming</b>: Always providing tons of feedback, beta testing, and just generally being an excellent place for gamers + developers to meet. <br><br>" +
                "<b>/r/CityFlow</b>: There's a lot of crossover between the mentioned subreddits, but I really appreciate everyone who contributes an opinion on the game.<br><br>" +
                "<b>/r/PixelBlacksmith</b>: You guys have been super-patient whilst I work on City Flow, and I hope you all enjoy it as much as Pixel Blacksmith.<br><br>" +
                "<b>You</b>: Thank you for playing the game! A game is nothing without players (especially super dedicated ones who read the credits!). If you ever have any feedback / bugs / suggestions / good jokes / bad puns, <a href='mailto:city.flow@jakelee.co.uk'>email me</a>.<br><br>" +
                "<i>- Jake</i>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_CHANGE_TEXT", "Change %1$s text\n(max %2$d chars)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_CHANGE_SLIDER", "Change %1$s value"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_CLOUD_LOAD_CONFIRM", "Are you sure sure you want to load this cloud save?\n\nLocal: %1$s stars, %2$s coins\nCloud: %3$s stars, %4$s coins"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_CLOUD_SAVE_CONFIRM", "Are you sure you wish to overwrite your cloud save:\n%1$s\n\n(Created on %2$s on your %3$s) with your local save:\n\n%4$d Stars | %5$d Coins | V%6$s?"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_CHANGE", "Change"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_CONFIRM", "Confirm"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_CLOSE", "Close"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_SAVE", "Save"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_CREATE", "Create"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_LOAD", "Load"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_CANCEL", "Cancel"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_BUTTON_DELETE", "Delete"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_SUPPORT_CODE", "Support Code"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "DIALOG_CREDITS", "Credits"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_1_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_2_NAME", "City"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_3_NAME", "Forest"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_4_NAME", "Mountain"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ENVIRONMENT_5_NAME", "Desert"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_ADVERT_NOT_LOADED", "Failed to load ad! This might be due to a poor connection, or there might not be any ads available."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_ADVERT_NOT_VERIFIED", "Something went wrong, and the ad view couldn't be verified!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_FAILED_TO_CONNECT", "Couldn't log in. Please try again later."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_MAX_PURCHASES", "You've purchased the maximum of this item!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_NO_ERROR", ""));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_NOT_ENOUGH_CURRENCY", "You can't afford this item!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_SUPPORT_CODE_INVALID", "Failed to apply support code! Please contact support."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_TECHNICAL", "An unknown technical error occurred! Try again?"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_CLOUD_ERROR", "An error occurred whilst handling the cloud save: %1$s"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ERROR_PUZZLE_NOT_TESTED", "The puzzle must be successfully completed before it can be exported!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_1_NAME", "Water"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_2_NAME", "Road"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_3_NAME", "Path"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_4_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_5_NAME", "Canal"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_6_NAME", "River"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_7_NAME", "Dirt"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "FLOW_8_NAME", "Rail"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "GOOGLE_SIGN_IN", "Sign In"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "GOOGLE_SIGN_OUT", "Sign Out"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "HEIGHT_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "HEIGHT_1_NAME", "Ultra Low"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "HEIGHT_2_NAME", "Low"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "HEIGHT_3_NAME", "Normal"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "HEIGHT_4_NAME", "High"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_1_NAME", "Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_1_DESC", "Undo your 1 most recent move, also decreasing the move count."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_2_NAME", "Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_2_DESC", "Reduce the recorded time for the level by 10%."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_3_NAME", "Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_3_DESC", "Reduce the recorded moves per level by 1."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_4_NAME", "Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_4_DESC", "Randomly all rotate all tiles on the board 1 time."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_5_NAME", "Upgrade Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_5_DESC", "Each upgrade reduces the puzzle time taken by 10% per level."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_6_NAME", "Upgrade Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_6_DESC", "Each upgrade reduces the puzzle moves taken by 1 per level."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_7_NAME", "Upgrade Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_7_DESC", "Each upgrade increases the number of times the board can be shuffled by 1."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_8_NAME", "Unlock Tile 1"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_8_DESC", "Unlock a presumably special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_9_NAME", "Unlock Tile 2"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_9_DESC", "Unlock a presumably super special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_10_NAME", "Unlock Tile 3"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_10_DESC", "Unlock a presumably super super special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_11_NAME", "Unlock Pack"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_11_DESC", "Instantly unlock the next pack, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_12_NAME", "10x Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_12_DESC", "Purchase undo boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_13_NAME", "10x Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_13_DESC", "Purchase time reduction boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_14_NAME", "10x Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_14_DESC", "Purchase move reduction boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_15_NAME", "10x Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_15_DESC", "Purchase shuffle board boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_16_NAME", "100x Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_16_DESC", "Purchase undo boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_17_NAME", "100x Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_17_DESC", "Purchase time reduction boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_18_NAME", "100x Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_18_DESC", "Purchase move reduction boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_19_NAME", "100x Shuffle Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_19_DESC", "Purchase shuffle board boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_20_NAME", "Main Menu Cars"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "ITEM_20_DESC", "Choose how many cars appear on the main menu, between 0 and 75!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "METRIC_TILES_EARNED", "Tiles Earned"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "METRIC_BEST_TIME", "Best Time"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "METRIC_BEST_MOVES", "Best Moves"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_1_NAME", "Tutorial"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_1_DESC", "Let's go with the flow."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_2_NAME", "The Big City"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_2_DESC", "Get flowing in the big city."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_3_NAME", "Escape To The Country"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PACK_3_DESC", "Flow all the way out to the country."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_DEFAULT_NAME", "New Puzzle (%1$dx%2$d, %3$s)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_DEFAULT_DESC", "No description."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_EXPORT_START", "Beginning puzzle export process..."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_CURRENT", "In Progress"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_AVAILABLE", "Available"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_UPCOMING", "Upcoming"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_COMPLETED", "Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_FAILED", "Failed"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "QUEST_COMPLETED_TEXT", "Completed the %1$s quest \"%1$s\" and earned %3$d coins!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_SECTION_AUDIO", "Audio Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_SECTION_GAMEPLAY", "Gameplay Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_SECTION_GOOGLE", "Google Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_SECTION_OTHER", "Other"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_1_NAME", "Music"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_2_NAME", "Sounds"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_3_NAME", "Minimum Zoom"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_4_NAME", "Maximum Zoom"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_5_NAME", "Zen Mode"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_6_NAME", "Hide Unstocked Boosts"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_7_NAME", "Player Name"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_8_NAME", "Google Play Sign In"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SETTING_9_NAME", "Main Menu Cars"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_CATEGORY_1_NAME", "Boosts"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_CATEGORY_2_NAME", "Upgrades"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_CATEGORY_3_NAME", "Tiles"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_CATEGORY_4_NAME", "Misc"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_ADVERT", "Advert"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_OFFERS", "Offers"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_PURCHASE_TEXT", "Buy for %1$s coins"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_NUMBER_PURCHASES", "%1$s%2$s purchases"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_NUMBER_OWNED", "%1$s owned"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_ITEM_PURCHASED", "Successfully purchased %1$s for %2$d coins!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SHOP_MAX_PURCHASED", "Maximum Purchased"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "STATISTIC_1_NAME", "Puzzles Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "STATISTIC_2_NAME", "Tiles Rotated"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "SUCCESS_SUPPORT_CODE", "Successfully applied support code!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_0_NAME", "Invisible Tile"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_1_NAME", "Grass Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_2_NAME", "Grass Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_3_NAME", "Grass Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_4_NAME", "Grass Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_5_NAME", "Grass Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_6_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_7_NAME", "Grass Road Bridge"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_8_NAME", "Grass Water Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_9_NAME", "Grass Water Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_10_NAME", "Grass Road Straight (Median)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_11_NAME", "Grass Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_12_NAME", "Grass Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_13_NAME", "City Red Shop"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_14_NAME", "City Road Straight (Bus Stop)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_15_NAME", "City Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_16_NAME", "City Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_17_NAME", "City Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_18_NAME", "City Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_19_NAME", "City Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_20_NAME", "City Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_21_NAME", "City"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_22_NAME", "City Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_23_NAME", "City Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_24_NAME", "City Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_25_NAME", "City Road/Path T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_26_NAME", "City Grass End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_27_NAME", "City Grass Straight (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_28_NAME", "City Grass Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_29_NAME", "City Road Straight (Crossing)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_30_NAME", "City Green Shop"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_31_NAME", "City (Fountain)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_32_NAME", "City (Grass)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_33_NAME", "Grass Road/Water Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_34_NAME", "Grass High"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_35_NAME", "Grass Water End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_36_NAME", "Grass High Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_37_NAME", "Grass High Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_38_NAME", "City Road End (Underground)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_39_NAME", "Grass High Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_40_NAME", "City Canal Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_41_NAME", "City Canal End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_42_NAME", "Grass Road Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_43_NAME", "Grass (Tree 1)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_44_NAME", "Grass (Tree 2)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_45_NAME", "Grass (Tree 3)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_46_NAME", "City Road/Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_47_NAME", "City High Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_48_NAME", "City High Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_49_NAME", "City Grass Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_50_NAME", "City Canal Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_51_NAME", "City Road/Path Corner (Inverse)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_52_NAME", "City Water Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_53_NAME", "City Water T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_54_NAME", "City Grass Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_55_NAME", "City Grass T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_56_NAME", "City Road Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_57_NAME", "City Road Corner (Lights)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_58_NAME", "City Road Corner (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_59_NAME", "City Road/Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_60_NAME", "Forest River Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_61_NAME", "Forest River Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_62_NAME", "Forest Dirt Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_63_NAME", "Forest High Dirt Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_64_NAME", "Forest Dirt Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_65_NAME", "Forest Dirt Straight (Humped)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_66_NAME", "Forest Dirt T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_67_NAME", "Forest Dirt Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_68_NAME", "Forest Dirt Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_69_NAME", "Forest"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_70_NAME", "Forest Dirt Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_71_NAME", "Forest Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_72_NAME", "Forest High"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_73_NAME", "Forest (Rocks)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_74_NAME", "Forest (Trees)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_75_NAME", "Forest Dirt End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_76_NAME", "Forest River End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_77_NAME", "City Road Straight (Median)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_78_NAME", "City Road Straight (Trees)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_79_NAME", "Grass Road Slope (Narrow)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_80_NAME", "City Road End (Barrier)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_81_NAME", "Forest River T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_82_NAME", "Forest River Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_83_NAME", "Forest High Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_84_NAME", "Forest High Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_85_NAME", "City High Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_86_NAME", "Mountain Hill"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_87_NAME", "Mountain"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_88_NAME", "Mountain River End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_89_NAME", "Mountain River Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_90_NAME", "Mountain River Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_91_NAME", "Mountain River T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_92_NAME", "Mountain River Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_93_NAME", "Desert"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_94_NAME", "Desert Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_95_NAME", "Desert High"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_96_NAME", "Desert Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_97_NAME", "Desert Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_98_NAME", "Desert Road (Crossing)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_99_NAME", "Desert Road End (Round)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_100_NAME", "Desert Road/Path T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_101_NAME", "Desert Path Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_102_NAME", "Desert Rail Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_103_NAME", "Desert Rail Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_104_NAME", "Desert Rail Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_105_NAME", "Desert Rail Double Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_106_NAME", "Desert Rail 3 Split"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_107_NAME", "Desert Rail Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_108_NAME", "Desert Rail End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_109_NAME", "Desert Rail T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_110_NAME", "Desert Rail V Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_111_NAME", "Desert Rail Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_112_NAME", "Desert Rail/Road Crossover"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_113_NAME", "Desert High Road"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_114_NAME", "Desert High Road (Rounded Bridge)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_115_NAME", "Desert High Road (Squared Bridge)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_116_NAME", "Desert Road Bridge"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_117_NAME", "Desert Road Slope (No Bottom)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_118_NAME", "Desert Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_119_NAME", "Desert Road Slope (No Top)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_120_NAME", "Desert Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_121_NAME", "Desert Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_122_NAME", "Desert Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_123_NAME", "Desert Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_124_NAME", "Desert Path V Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_125_NAME", "Desert Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_126_NAME", "Desert Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_127_NAME", "Desert Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_128_NAME", "Desert Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "TILE_128_NAME", "Mountain (Stones)"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_EXPORT_PUZZLE_HINT", "Export puzzle data to:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_COMPLETE_TITLE", "Complete\n100%"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_COMPLETE_TEXT", "Completed!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_TIME_TITLE", "Time\n%1$d%%"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_TIME_TEXT", "%1$s/%2$s"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_MOVES_TITLE", "Moves\n%1$d%%"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_SKYSCRAPER_MOVES_TEXT", "%1$d/%2$d\nmoves"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PACK_OPEN", "Open Pack"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PACK_PURCHASE", "Purchase Pack"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_BY", "By:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_NAME", "Name:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_DESC", "Desc:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_DATE_ADDED", "Added:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_BEST_MOVES", "Moves:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_BEST_TIME", "Time:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_STARS", "Stars:"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_WIDTH", "Width: %1$d"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_HEIGHT", "Height: %1$d"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_PUZZLE_OPTIONS", "Puzzle Options"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_TILE_UNLOCK", "Unlocked %1$s tile(s)!"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "UI_TILE_NO_UNLOCK", "No tiles unlocked."));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_ALL", "All"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_AREA", "Area"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_DESCRIPTION", "Description"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_FLOW", "Flow"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_HEIGHT", "Height"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_NA", "N/A"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_NAME", "Name"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_PAUSED", "Paused"));
        texts.add(new Text(Constants.LANGUAGE_EN_GB, "WORD_START", "Start"));
        Text.saveInTx(texts);
    }

    private static void createPack() {
        List<Pack> packs = new ArrayList<>();
        packs.add(new Pack(1, "", "CgkIgrzuo64REAIQEA", "CgkIgrzuo64REAIQEQ", 6));
        packs.add(new Pack(2, "unlock_pack2", "CgkIgrzuo64REAIQEw", "CgkIgrzuo64REAIQFA", 30));
        packs.add(new Pack(3, "unlock_pack3", "CgkIgrzuo64REAIQFQ", "CgkIgrzuo64REAIQFg", 30));
        Pack.saveInTx(packs);
    }

    private static void createPuzzle() {
        List<Puzzle> puzzles = new ArrayList<>();
        List<Text> texts = new ArrayList<>();
        List<Tile> tiles = new ArrayList<>();

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_1_NAME", "No Crossing"));
        puzzles.add(new Puzzle(1, 1, 10000L, 20, 0L, 0));
        tiles.add(new Tile(1, 87, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 86, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 88, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 86, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 88, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 90, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 92, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 89, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 87, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 88, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 91, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(1, 89, 2, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_2_NAME", "Don't Cross The Flows"));
        puzzles.add(new Puzzle(2, 1, 10000L, 20, 0L, 0));
        tiles.add(new Tile(2, 41, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(2, 41, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(2, 26, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(2, 26, 1, 1, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_3_NAME", "Forest Test"));
        //puzzles.add(new Puzzle(3, 1, 10000L, 20, 0L, 0));
        tiles.add(new Tile(3, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(3, 18, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(3, 18, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(3, 13, 1, 1, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_11_NAME", "One Shop Town"));
        puzzles.add(new Puzzle(11, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(11, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 19, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 21, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 14, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 29, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 13, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 19, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 19, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(11, 21, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_12_NAME", "Beware The Moat"));
        puzzles.add(new Puzzle(12, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(12, 50, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 40, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 50, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 40, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 31, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 40, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 50, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 40, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(12, 50, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_13_NAME", "Pop To The Shops"));
        puzzles.add(new Puzzle(13, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(13, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 29, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 19, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 15, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 30, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 15, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 38, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 13, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(13, 38, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_14_NAME", "Transition The Flows"));
        puzzles.add(new Puzzle(14, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(14, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 14, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 19, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 29, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 46, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 46, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 18, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 18, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(14, 30, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_15_NAME", "Height Matters!"));
        puzzles.add(new Puzzle(15, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(15, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 20, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 48, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 20, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 48, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 48, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 48, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 48, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(15, 21, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_16_NAME", "Make Your Own Solution"));
        puzzles.add(new Puzzle(16, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(16, 41, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 31, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(16, 41, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_17_NAME", "Gridlock"));
        puzzles.add(new Puzzle(17, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(17, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 17, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 19, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 17, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 16, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 17, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 19, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 17, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(17, 19, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_18_NAME", "Tree Lined Swimway"));
        puzzles.add(new Puzzle(18, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(18, 26, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 41, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 26, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 27, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 40, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 27, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 26, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 41, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(18, 26, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_19_NAME", "Competitive Business"));
        puzzles.add(new Puzzle(19, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(19, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 18, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 13, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 17, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 14, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 18, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 19, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 18, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(19, 13, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_20_NAME", "The Long Way Round"));
        puzzles.add(new Puzzle(20, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(20, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 20, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 48, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 25, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 24, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 47, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 19, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 20, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(20, 48, 2, 2, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_21_NAME", "It Never Ends"));
        puzzles.add(new Puzzle(21, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(21, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 24, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 23, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 16, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 24, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 38, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 16, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 38, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(21, 19, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_22_NAME", "Round And Round"));
        puzzles.add(new Puzzle(22, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(22, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 19, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 19, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 19, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 15, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 25, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 25, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 15, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 29, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 25, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 25, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 29, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 19, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 17, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 17, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(22, 19, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_23_NAME", "The High Road"));
        puzzles.add(new Puzzle(23, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(23, 48, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 48, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 31, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 31, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 31, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 31, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 48, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 47, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(23, 48, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_24_NAME", "Better Go Slow!"));
        puzzles.add(new Puzzle(24, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(24, 26, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 27, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 28, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 26, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 18, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 25, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 20, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 48, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 18, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 25, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 20, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 48, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 24, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 22, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 24, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(24, 30, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_25_NAME", "Coming Up For Air"));
        puzzles.add(new Puzzle(25, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(25, 38, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 29, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 15, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 15, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 15, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 15, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 17, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 16, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 17, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(25, 38, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_26_NAME", "Gone Fishing"));
        puzzles.add(new Puzzle(26, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(26, 18, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 25, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 25, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 18, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 23, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 23, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 22, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 24, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 23, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 24, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 22, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 22, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 41, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 41, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 23, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(26, 23, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_27_NAME", "Keep Going Left"));
        puzzles.add(new Puzzle(27, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(27, 49, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 27, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 27, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 49, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 28, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 49, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 49, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 28, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 28, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 26, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 28, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 27, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 49, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 27, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 49, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(27, 26, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_28_NAME", "Don't Miss The Turning"));
        puzzles.add(new Puzzle(28, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(28, 32, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 38, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 20, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 48, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 41, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 40, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 41, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 47, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 38, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 20, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 47, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 48, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 26, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 27, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 26, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(28, 31, 3, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_29_NAME", "End Of The Line"));
        puzzles.add(new Puzzle(29, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(29, 18, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 14, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 17, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 29, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 19, 0, 4, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 13, 0, 5, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 23, 0, 6, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 23, 0, 7, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 30, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 18, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 19, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 31, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 19, 1, 4, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 29, 1, 5, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 25, 1, 6, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 25, 1, 7, Constants.ROTATION_NORTH));
        tiles.add(new Tile(29, 38, 1, 8, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_30_NAME", "A Summer Walk"));
        puzzles.add(new Puzzle(30, 2, 10000L, 20, 0L, 0));
        tiles.add(new Tile(30, 18, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 25, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 29, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 46, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 46, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 22, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 30, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 25, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 38, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 22, 3, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 21, 4, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 4, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 22, 4, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(30, 23, 4, 3, Constants.ROTATION_NORTH));

        texts.add(new Text(Constants.LANGUAGE_EN_GB, "PUZZLE_41_NAME", "Highs And Lows"));
        puzzles.add(new Puzzle(41, 3, 10000L, 20, 0L, 0));
        tiles.add(new Tile(41, 19, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 25, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 20, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 48, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 38, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 22, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 13, 1, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 47, 1, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 38, 2, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 22, 2, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 30, 2, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 20, 2, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 19, 3, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 25, 3, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 29, 3, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(41, 19, 3, 3, Constants.ROTATION_NORTH));


        // Here be custom puzzles
        List<PuzzleCustom> puzzleCustoms = new ArrayList<>();
        puzzles.add(new Puzzle(98, 0, 10000L, 20, 0L, 0));
        puzzles.add(new Puzzle(99, 0, 10000L, 20, 0L, 0));
        puzzles.add(new Puzzle(100, 0, 10000L, 20, 0L, 0));
        puzzles.add(new Puzzle(101, 0, 10000L, 20, 0L, 0));
        puzzles.add(new Puzzle(102, 0, 10000L, 20, 0L, 0));

        puzzleCustoms.add(new PuzzleCustom(98, System.currentTimeMillis(), "Block test", "Test puzzle desc", "Someone", true, true));
        puzzleCustoms.add(new PuzzleCustom(99, System.currentTimeMillis(), "Logo", "Test puzzle desc 2", "Someone 2", true, false));
        puzzleCustoms.add(new PuzzleCustom(100, System.currentTimeMillis(), "A square, but this is a test of long names", "Test puzzle desc 3", "Someone 3", false, true));
        puzzleCustoms.add(new PuzzleCustom(101, System.currentTimeMillis(), "A line", "Test puzzle desc 4", "Someone 4", false, false));
        puzzleCustoms.add(new PuzzleCustom(102, System.currentTimeMillis(), "Empty test", "Test puzzle desc 4", "Someone 4", true, false));

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
        tiles.add(new Tile(100, 1, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(100, 1, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(100, 1, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(100, 1, 1, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(101, 18, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(101, 15, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(101, 15, 0, 2, Constants.ROTATION_NORTH));
        tiles.add(new Tile(101, 18, 0, 3, Constants.ROTATION_NORTH));
        tiles.add(new Tile(102, 0, 0, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(102, 3, 0, 1, Constants.ROTATION_NORTH));
        tiles.add(new Tile(102, 3, 1, 0, Constants.ROTATION_NORTH));
        tiles.add(new Tile(102, 0, 1, 1, Constants.ROTATION_NORTH));
        PuzzleCustom.saveInTx(puzzleCustoms);
        Puzzle.saveInTx(puzzles);
        Text.saveInTx(texts);
        Tile.saveInTx(tiles);
    }

    private static void createSetting() {
        List<Setting> settings = new ArrayList<>();
        settings.add(new Setting(Constants.SETTING_MUSIC, true));
        settings.add(new Setting(Constants.SETTING_SOUNDS, true));
        settings.add(new Setting(Constants.SETTING_MIN_ZOOM, 0.50f, 0.1f, 1.0f));
        settings.add(new Setting(Constants.SETTING_MAX_ZOOM, 1.75f, 1.0f, 3.0f));
        settings.add(new Setting(Constants.SETTING_ZEN_MODE, false));
        settings.add(new Setting(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS, false));
        settings.add(new Setting(Constants.SETTING_PLAYER_NAME, "New Player"));
        settings.add(new Setting(Constants.SETTING_SIGN_IN, true));
        settings.add(new Setting(Constants.SETTING_MAX_CARS, 5, 0, 75));
        Setting.saveInTx(settings);
    }

    private static void createStatistic() {
        List<Statistic> statistics = new ArrayList<>();
        statistics.add(new Statistic(Constants.STATISTIC_PUZZLES_COMPLETED, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_TILES_ROTATED, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_QUESTS_COMPLETED, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_PUZZLES_COMPLETED_FULLY, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_BOOSTS_USED, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_COMPLETE_PACK_1, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_COMPLETE_PACK_2, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_COMPLETE_PACK_3, 0, 0));
        statistics.add(new Statistic(Constants.STATISTIC_CURRENCY, 100000));
        statistics.add(new Statistic(Constants.STATISTIC_TAPJOY_COINS, 0));
        Statistic.saveInTx(statistics);
    }

    private static void createStoreItem() {
        List<ShopItem> shopItems = new ArrayList<>();
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_UNDO, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_UNDO, 1, 4, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_UNDO_10, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_UNDO, 10, 36, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_UNDO_100, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_UNDO, 100, 320, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_TIME, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_TIME, 1, 6, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_TIME_10, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_TIME, 10, 54, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_TIME_100, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_TIME, 100, 480, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_MOVES, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_MOVE, 1, 6, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_MOVES_10, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_MOVE, 10, 54, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_MOVES_100, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_MOVE, 100, 480, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_SHUFFLE, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_SHUFFLE, 1, 8, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_SHUFFLE_10, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_SHUFFLE, 10, 72, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_SHUFFLE_100, Constants.STORE_CATEGORY_BOOSTS, Constants.BOOST_SHUFFLE, 100, 640, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_TIME_UPGRADE, Constants.STORE_CATEGORY_UPGRADES, Constants.BOOST_UNDO, 0, 150, 5, true));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_MOVES_UPGRADE, Constants.STORE_CATEGORY_UPGRADES, Constants.BOOST_MOVE, 0, 150, 5, true));
        shopItems.add(new ShopItem(Constants.ITEM_BOOST_SHUFFLE_UPGRADE, Constants.STORE_CATEGORY_UPGRADES, Constants.BOOST_SHUFFLE, 0, 100, 1, true));
        shopItems.add(new ShopItem(Constants.ITEM_TILE_1, Constants.STORE_CATEGORY_TILES, 1000, 1, false));
        shopItems.add(new ShopItem(Constants.ITEM_TILE_2, Constants.STORE_CATEGORY_TILES, 1500, 1, false));
        shopItems.add(new ShopItem(Constants.ITEM_TILE_3, Constants.STORE_CATEGORY_TILES, 2000, 1, false));
        shopItems.add(new ShopItem(Constants.ITEM_UNLOCK_PACK, Constants.STORE_CATEGORY_MISC, 5000, 0, false));
        shopItems.add(new ShopItem(Constants.ITEM_MAX_CARS, Constants.STORE_CATEGORY_MISC, 2000, 1, false));
        ShopItem.saveInTx(shopItems);
    }

    private static void createStoreCategory() {
        List<ShopCategory> categories = new ArrayList<>();
        categories.add(new ShopCategory(Constants.STORE_CATEGORY_BOOSTS));
        categories.add(new ShopCategory(Constants.STORE_CATEGORY_UPGRADES));
        categories.add(new ShopCategory(Constants.STORE_CATEGORY_TILES));
        categories.add(new ShopCategory(Constants.STORE_CATEGORY_MISC));
        ShopCategory.saveInTx(categories);
    }

    private static void createTileType() {
        List<TileType> tileTypes = new ArrayList<>();
        tileTypes.add(new TileType(0, Constants.ENVIRONMENT_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 0));
        tileTypes.add(new TileType(1, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 1));
        tileTypes.add(new TileType(2, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 2));
        tileTypes.add(new TileType(3, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(4, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.HEIGHT_NORMAL, 4));
        tileTypes.add(new TileType(5, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 5));
        tileTypes.add(new TileType(6, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 6));
        tileTypes.add(new TileType(7, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.FLOW_ROAD, Constants.FLOW_WATER, Constants.HEIGHT_NORMAL, 7));
        tileTypes.add(new TileType(8, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 8));
        tileTypes.add(new TileType(9, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 9));
        tileTypes.add(new TileType(10, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 10));
        tileTypes.add(new TileType(11, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 11));
        tileTypes.add(new TileType(12, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 12));
        tileTypes.add(new TileType(13, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 13));
        tileTypes.add(new TileType(14, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 14));
        tileTypes.add(new TileType(15, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 15));
        tileTypes.add(new TileType(16, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.HEIGHT_NORMAL, 16));
        tileTypes.add(new TileType(17, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 17));
        tileTypes.add(new TileType(18, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 18));
        tileTypes.add(new TileType(19, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 19));
        tileTypes.add(new TileType(20, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 20));
        tileTypes.add(new TileType(21, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(22, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(23, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(24, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(25, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(26, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(27, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(28, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(29, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(30, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(31, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(32, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(33, Constants.ENVIRONMENT_GRASS, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(34, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(35, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(36, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(37, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(38, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(39, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(40, Constants.ENVIRONMENT_CITY, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(41, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(42, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(43, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(44, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(45, Constants.ENVIRONMENT_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(46, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(47, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(48, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(49, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(50, Constants.ENVIRONMENT_CITY, Constants.FLOW_CANAL, Constants.FLOW_CANAL, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(51, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(52, Constants.ENVIRONMENT_CITY, Constants.FLOW_WATER, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(53, Constants.ENVIRONMENT_CITY, Constants.FLOW_WATER, Constants.FLOW_WATER, Constants.FLOW_WATER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(54, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(55, Constants.ENVIRONMENT_CITY, Constants.FLOW_GRASS, Constants.FLOW_GRASS, Constants.FLOW_GRASS, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(56, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(57, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(58, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(59, Constants.ENVIRONMENT_CITY, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(60, Constants.ENVIRONMENT_FOREST, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(61, Constants.ENVIRONMENT_FOREST, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(62, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(63, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(64, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(65, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(66, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_DIRT, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(67, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(68, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(69, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(70, Constants.ENVIRONMENT_FOREST, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(71, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(72, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(73, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(74, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(75, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.FLOW_DIRT, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(76, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(77, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(78, Constants.ENVIRONMENT_CITY, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(79, Constants.ENVIRONMENT_GRASS, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(80, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(81, Constants.ENVIRONMENT_FOREST, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(82, Constants.ENVIRONMENT_FOREST, Constants.FLOW_RIVER, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(83, Constants.ENVIRONMENT_FOREST, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(84, Constants.ENVIRONMENT_FOREST, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(85, Constants.ENVIRONMENT_CITY, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_HIGH, 3));
        tileTypes.add(new TileType(86, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(87, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(88, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_NONE, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(89, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(90, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(91, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_RIVER, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(92, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_RIVER, Constants.HEIGHT_ULTRA_LOW, 3));
        tileTypes.add(new TileType(93, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(94, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(95, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(96, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(97, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(98, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(99, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(100, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_PATH, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(101, Constants.ENVIRONMENT_DESERT, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(102, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(103, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(104, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(105, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(106, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(107, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(108, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(109, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(110, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(111, Constants.ENVIRONMENT_DESERT, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.FLOW_RAIL, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(112, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_RAIL, Constants.FLOW_ROAD, Constants.FLOW_RAIL, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(113, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(114, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(115, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, 3));
        tileTypes.add(new TileType(116, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(117, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(118, Constants.ENVIRONMENT_DESERT, Constants.FLOW_PATH, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(119, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(120, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(121, Constants.ENVIRONMENT_DESERT, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(122, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(123, Constants.ENVIRONMENT_DESERT, Constants.FLOW_NONE, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(124, Constants.ENVIRONMENT_DESERT, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(125, Constants.ENVIRONMENT_DESERT, Constants.FLOW_PATH, Constants.FLOW_PATH, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(126, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_NORMAL, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(127, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(128, Constants.ENVIRONMENT_DESERT, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_ROAD, Constants.FLOW_NONE, Constants.HEIGHT_LOW, 3));
        tileTypes.add(new TileType(128, Constants.ENVIRONMENT_MOUNTAIN, Constants.FLOW_NONE, Constants.HEIGHT_ULTRA_LOW, 3));
        TileType.saveInTx(tileTypes);
    }
}

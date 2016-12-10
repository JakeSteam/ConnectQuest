package uk.co.jakelee.cityflow.helper;

import android.util.Log;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.model.Text;

import static com.orm.query.Select.from;

public class TextHelper {
    public static int getDefaultLanguage() {
        switch(Locale.getDefault().getLanguage()) {
            case "de": return Constants.LANGUAGE_DE;
            case "fr": return Constants.LANGUAGE_FR;
            case "pl": return Constants.LANGUAGE_PL;
            case "ru": return Constants.LANGUAGE_RU;
            case "es": return Constants.LANGUAGE_ES;
            case "nl": return Constants.LANGUAGE_NL;
            case "zh": return Constants.LANGUAGE_ZH;
            default: return Constants.LANGUAGE_EN;
        }
    }

    public static String getLanguageFlag(int languageId) {
        switch(languageId) {
            case Constants.LANGUAGE_EN: return new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7));
            case Constants.LANGUAGE_DE: return new String(Character.toChars(0x1F1E9)) + new String(Character.toChars(0x1F1EA));
            case Constants.LANGUAGE_FR: return new String(Character.toChars(0x1F1EB)) + new String(Character.toChars(0x1F1F7));
            case Constants.LANGUAGE_PL: return new String(Character.toChars(0x1F1F5)) + new String(Character.toChars(0x1F1F1));
            case Constants.LANGUAGE_RU: return new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA));
            case Constants.LANGUAGE_ES: return new String(Character.toChars(0x1F1EA)) + new String(Character.toChars(0x1F1F8));
            case Constants.LANGUAGE_NL: return new String(Character.toChars(0x1F1F3)) + new String(Character.toChars(0x1F1F1));
            case Constants.LANGUAGE_ZH: return new String(Character.toChars(0x1F1E8)) + new String(Character.toChars(0x1F1F3));
            case Constants.LANGUAGE_SV: return new String(Character.toChars(0x1F1F8)) + new String(Character.toChars(0x1F1EA));
            default: return "";
        }
    }

    public static void installLanguagePack(int languageId) {
        switch(languageId) {
            case Constants.LANGUAGE_EN:
                installEnglishText();
                break;
            case Constants.LANGUAGE_DE:
                installGermanText();
                break;
            case Constants.LANGUAGE_FR:
                installFrenchText();
                break;
            case Constants.LANGUAGE_PL:
                installPolishText();
                break;
            case Constants.LANGUAGE_RU:
                installRussianText();
                break;
            case Constants.LANGUAGE_ES:
                installSpanishText();
                break;
            case Constants.LANGUAGE_NL:
                installDutchText();
                break;
            case Constants.LANGUAGE_ZH:
                installChineseText();
                break;
            case Constants.LANGUAGE_SV:
                installSwedishText();
                break;
        }
    }

    public static boolean isPackInstalled(int languageId) {
        Text text = from(Text.class).where(
                Condition.prop("language").eq(languageId),
                Condition.prop("text_id").eq("INSTALL_CHECK")).first();
        return text != null;
    }

    public static int getNonEnglishTextCount() {
        List<Text> texts = Select.from(Text.class).where(
                Condition.prop("language").notEq(Constants.LANGUAGE_EN)).list();
        return texts.size();
    }

    private static void installEnglishText() {
        Log.d("Languages", "Installing English text pack");
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_EN, "INSTALL_CHECK", ""));

        // Added post-translation
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_LANGUAGE_INSTALL", "Installing language pack: %1$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_RESET_LANGUAGE", "Game language set to English!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_RESET_LANGUAGE", "Reset Language"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_RESET_LANGUAGE_CONFIRM", "Reset language back to English, uninstalling the %1$d translations currently installed?\n\nReinstall packs by changing language!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_185_NAME", "Desert (Plant)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_186_NAME", "Desert (Flower)"));

        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_BACKGROUND_UNLOCK", "Unlocked '%1$s' background!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CARD_SAVED", "Card image saved to gallery!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_COINS_PURCHASED", "Successfully purchased %1$d coins!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_COINS_PURCHASED_PACK", "Successfully purchased %1$d coins, and unlocked an exclusive puzzle pack & a new background!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CLOUD_BEGINNING", "Comparing local and cloud saves..."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CLOUD_SAVING", "Saving to cloud..."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CLOUD_SAVED", "Successfully saved game to cloud!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CLOUD_LOADING", "Loading cloud save..."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_CLOUD_LOADED", "Successfully loaded game from cloud!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_COINS_EARNED", "Earned %1$d coin(s)!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_COINS_EARNED_FREE", "Earned %1$d free coin(s), enjoy!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_PUZZLE_COPIED", "Successfully created \"%1$s (Copy)\"!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_PUZZLE_IMPORTED", "Successfully imported the puzzle, enjoy!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_SHUFFLE_PUZZLE", "Randomly rotate all tiles (keeping their position)?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_DELETE_PUZZLE", "Delete puzzle \"%1$s\"?\n\nNote: This can't be undone!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_SAVE_CONFLICT", "Save conflict detected! Currently resolving, this might take a few seconds, please be patient..."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_SETTING_TOGGLE_ON", "Toggled %1$s on!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ALERT_SETTING_TOGGLE_OFF", "Toggled %1$s off!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_1_NAME", "Plain"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_1_HINT", "This is already unlocked!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_2_NAME", "Night"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_2_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_3_NAME", "Sunrise"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_3_HINT", "Unlocked by completing achievement \"Turn It Up 1\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_4_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_4_HINT", "Unlocked by completing achievement \"Turn It Up 2\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_5_NAME", "Salmon"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_5_HINT", "Unlocked by completing achievement \"Turn It Up 3\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_6_NAME", "Bluish"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_6_HINT", "Unlocked by completing achievement \"Turn It Up 4\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_7_NAME", "Pink"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_7_HINT", "Unlocked by completing achievement \"The Best Quest 1\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_8_NAME", "Bark"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_8_HINT", "Unlocked by completing achievement \"The Best Quest 2\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_9_NAME", "Earth"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_9_HINT", "Unlocked by completing achievement \"The Best Quest 3\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_10_NAME", "Stormy Sky"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_10_HINT", "Unlocked by completing achievement \"The Best Quest 4\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_11_NAME", "Deep Sea"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_11_HINT", "Unlocked by completing achievement \"Working Your Way Through 1\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_12_NAME", "Limestone"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_12_HINT", "Unlocked by completing achievement \"Working Your Way Through 2\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_13_NAME", "Sand"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_13_HINT", "Unlocked by completing achievement \"Working Your Way Through 3\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_14_NAME", "Fern"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_14_HINT", "Unlocked by completing achievement \"Working Your Way Through 4\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_15_NAME", "Olive"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_15_HINT", "Unlocked by completing achievement \"Completionist 1\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_16_NAME", "Fawn"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_16_HINT", "Unlocked by completing achievement \"Completionist 2\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_17_NAME", "Sedona"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_17_HINT", "Unlocked by completing achievement \"Completionist 3\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_18_NAME", "Eggshell"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_18_HINT", "Unlocked by completing achievement \"Completionist 4\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_19_NAME", "Clay"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_19_HINT", "Unlocked by completing Pack 4."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_20_NAME", "Clay Grey"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_20_HINT", "Unlocked by completing Pack 5."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_21_NAME", "Muddy"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_21_HINT", "Unlocked by completing Pack 6."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_22_NAME", "Muddy Pink"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_22_HINT", "Unlocked by completing Pack 7."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_23_NAME", "Limey"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_23_HINT", "Unlocked by completing achievement \"Activate Boost 1\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_24_NAME", "Rich Limey"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_24_HINT", "Unlocked by completing achievement \"Activate Boost 2\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_25_NAME", "Camouflage"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_25_HINT", "Unlocked by completing achievement \"Activate Boost 3\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_26_NAME", "Ominous"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_26_HINT", "Unlocked by completing achievement \"Activate Boost 4\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_27_NAME", "Cloudy"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_27_HINT", "Unlocked by completing Pack 1."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_28_NAME", "Blue Skies"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_28_HINT", "Unlocked by completing Pack 2."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_29_NAME", "Petals"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_29_HINT", "Unlocked by completing Pack 3."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_30_NAME", "Summer"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_30_HINT", "Unlocked by viewing the credits."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_31_NAME", "Peach"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_31_HINT", "Unlocked by completing Pack 8."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_32_NAME", "Passionfruit"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_32_HINT", "Unlocked by completing Pack 9."));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_33_NAME", "The End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_33_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_34_NAME", "Desert"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_34_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_35_NAME", "Dirty"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_35_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_36_NAME", "Overcast"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_36_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_37_NAME", "Pretty In Pink"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_37_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_38_NAME", "Rainforest"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_38_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_39_NAME", "Mushroom"));
        texts.add(new Text(Constants.LANGUAGE_EN, "BACKGROUND_39_HINT", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "CLOUD_AUTOSAVE_DESC", "(Auto) %1$d Stars | %2$d Coins | V%3$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CLOUD_SAVE_DESC", "%1$d Stars | %2$d Coins | V%3$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREATOR_CREATED", "Created"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREATOR_IMPORTED", "Imported"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREATOR_NEW_PUZZLE", "New Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_GRAPHICS_TITLE", "Graphics"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_GRAPHICS_TEXT", "<a href='http://www.dafont.com/claudep.d857'>Claude P</a>: Main font (Yagiuhf No 2).<br>" +
                "<a href='http://fontawesome.io/'>FontAwesome</a>: Button icons.<br>" +
                "<a href='http://kenney.nl/'>Kenney</a>: Base tilesets, majority of UI elements.<br>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_TECHNOLOGIES_TITLE", "Technologies"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_TECHNOLOGIES_TEXT", "<a href='https://github.com/ACRA/acra'>ACRA</a>: Crash management.<br>" +
                "<a href='https://github.com/scottyab/AESCrypt-Android'>AESCrypt</a>: Database / game save encrypting.<br>" +
                "<a href='https://github.com/aitorvs/allowme'>AllowMe</a>: Permissions handling.<br>" +
                "<a href='https://github.com/hotchemi/Android-Rate'>Android-Rate</a>: Play Store rate prompting.<br>" +
                "<a href='http://www.antp.be/software/renamer'>Ant Renamer</a>: Bulk file operations.<br>" +
                "<a href='https://www.applovin.com/'>AppLovin</a>: Advertising.<br>" +
                "<a href='https://github.com/grantland/android-autofittextview'>AutofitTextView</a>: Intelligently resizing textboxes.<br>" +
                "<a href='https://github.com/keyboardsurfer/Crouton'>Crouton</a>: On-screen alerts.<br>" +
                "<a href='https://github.com/bperin/FontAwesomeAndroid'>FontAwesomeAndroid</a>: FontAwesome support.<br>" +
                "<a href='https://github.com/google/gson'>GSON</a>: Cloud saves + puzzle sharing.<br>" +
                "<a href='https://github.com/anjlab/android-inapp-billing-v3'>IAB</a>: In-app billing.<br>" +
                "<a href='https://github.com/thomashaertel/MultiSpinner'>Multispinner</a>: Multi-selectable options list.<br>" +
                "<a href='https://github.com/square/picasso'>Picasso</a>: Image caching.<br>" +
                "<a href='https://github.com/satyan/sugar'>Sugar</a>: Database ORM.<br>" +
                "<a href='https://github.com/zxing/zxing'>ZXing</a>: QR Code reading / writing.<br>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_SOUNDS_TITLE", "Audio"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_SOUNDS_TEXT", "<a href='https://www.freesound.org/'>FreeSound</a>: Various sound effects<br>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_OTHER_TITLE", "Other"));
        texts.add(new Text(Constants.LANGUAGE_EN, "CREDITS_OTHER_TEXT", "Of course, developing a game isn't just about technical resources. As such, there's a few people and communities who definitely need thanking.<br><br>" +
                "<b>Britt</b>: Responsible for a few of the more... interesting ideas in the game, and providing motivation throughout. If she had her way, the game would be a lot more absurd..!<br><br>" +
                "<b>Tash</b>: An absurd amount of design work. Basically, anywhere the game looks rather nice, she's the one to thank!<br><br>" +
                "<b>/r/AndroidGaming</b>: Always providing tons of feedback, beta testing, and just generally being an excellent place for gamers + developers to meet. <br><br>" +
                "<b>/r/CityFlow</b>: There's a lot of crossover between the mentioned subreddits, but I really appreciate everyone who contributes an opinion on the game.<br><br>" +
                "<b>/r/PixelBlacksmith</b>: You guys have been super-patient whilst I work on City Flow, and I hope you all enjoy it as much as Pixel Blacksmith.<br><br>" +
                "<b>You</b>: Thank you for playing the game! A game is nothing without players (especially super dedicated ones who read the credits!). If you ever have any feedback / bugs / suggestions / good jokes / bad puns, <a href='mailto:city.flow@jakelee.co.uk'>email me</a>.<br><br>" +
                "<i>- Jake</i>" +
                ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_TEXT", "Change %1$s text\n(max %2$d chars)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_SLIDER", "Change %1$s value"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CLOUD_LOAD_CONFIRM", "Are you sure sure you want to load this cloud save?\n\nLocal: %1$s stars, %2$s coins\nCloud: %3$s stars, %4$s coins"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CLOUD_SAVE_CONFIRM", "Are you sure you wish to overwrite your cloud save:\n%1$s\n\n(Created on %2$s on your %3$s) with your local save:\n\n%4$d Stars | %5$d Coins | V%6$s?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_CHANGE", "Change"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_CONFIRM", "Confirm"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_SHUFFLE", "Shuffle"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_CLOSE", "Close"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_SAVE", "Save"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_SHARE", "Share"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_CREATE", "Create"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_LOAD", "Load"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_CANCEL", "Cancel"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_DELETE", "Delete"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_RESIZE", "Resize"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_TEXT", "Text"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_LEFT", "Left"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_BUTTON_RIGHT", "Right"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_RESIZE_CONFIRM", "Are you sure you want to resize the puzzle from %1$dx%2$d to %3$dx%4$d?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_STATISTICS", "Statistics"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_SUPPORT_CODE", "Support Code"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CREDITS", "Credits"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_PLAY", "Play Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_SHUFFLE_TILES", "Rotate All Tiles"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_ROTATE_PUZZLE", "Rotate Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_ROTATE_CONFIRM", "Which way would you like to rotate the puzzle?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_NAME", "Change Name"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_DESC", "Change Desc"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_WIDTH", "Change Width"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_CHANGE_HEIGHT", "Change Height"));
        texts.add(new Text(Constants.LANGUAGE_EN, "DIALOG_SAVE_EXIT", "Save & Exit"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_1_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_2_NAME", "City"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_3_NAME", "Forest"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_4_NAME", "Mountain"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_5_NAME", "Desert"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ENVIRONMENT_6_NAME", "Golf"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_ADVERT_NOT_LOADED", "Failed to load ad! This might be due to a poor connection, or there might not be any ads available."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_ADVERT_NOT_VERIFIED", "Something went wrong, and the ad view couldn't be verified!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_FAILED_TO_CONNECT", "Couldn't log in. Please try again later."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_MAX_PURCHASES", "You've purchased the maximum of this item!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_NO_ERROR", ""));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_NOT_ENOUGH_CURRENCY", "You can't afford this item!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_SUPPORT_CODE_INVALID", "Failed to apply support code! Please contact support."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_SUPPORT_CODE_USED", "Support code already used!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_TECHNICAL", "An unknown technical error occurred! Try again?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_CLOUD_ERROR", "An error occurred whilst handling the cloud save: %1$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_PUZZLE_NOT_TESTED", "The puzzle must be successfully completed before it can be exported!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_PUZZLE_TOO_SMALL", "The puzzle needs more than just one tile!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_CARD_NOT_SAVED", "The card could not be saved, check storage permissions."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_CAMERA_IMPORT_FAIL", "The puzzle card could not be read, please try retaking the image, or improving lighting conditions."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_FILE_IMPORT_FAIL", "No puzzle could be imported from the image, please check that a puzzle card image has been selected."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_TEXT_IMPORT_FAIL", "No puzzle could be imported from the text, please check that it was copied correctly."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_BACKGROUND_LOCKED", "You need to unlock this background first!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_NO_IAB", "In app billing is not available on this device!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_IAB_FAILED", "Purchase failed! Please ensure you're logged in, and have a payment method configured."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ERROR_PUZZLE_SOLVED", "Puzzle already solved! Try shuffling the tiles first!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_1_NAME", "Water"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_2_NAME", "Road"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_3_NAME", "Path"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_4_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_5_NAME", "Canal"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_6_NAME", "River"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_7_NAME", "Dirt"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_8_NAME", "Rail"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_9_NAME", "Golf"));
        texts.add(new Text(Constants.LANGUAGE_EN, "FLOW_9_NAME", "Toxic"));
        texts.add(new Text(Constants.LANGUAGE_EN, "GOOGLE_SIGN_IN", "Sign In"));
        texts.add(new Text(Constants.LANGUAGE_EN, "GOOGLE_SIGN_OUT", "Sign Out"));
        texts.add(new Text(Constants.LANGUAGE_EN, "HEIGHT_0_NAME", "None"));
        texts.add(new Text(Constants.LANGUAGE_EN, "HEIGHT_1_NAME", "Ultra Low"));
        texts.add(new Text(Constants.LANGUAGE_EN, "HEIGHT_2_NAME", "Low"));
        texts.add(new Text(Constants.LANGUAGE_EN, "HEIGHT_3_NAME", "Normal"));
        texts.add(new Text(Constants.LANGUAGE_EN, "HEIGHT_4_NAME", "High"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_1_NAME", "Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_1_DESC", "Undo your 1 most recent move, also decreasing the move count."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_2_NAME", "Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_2_DESC", "Reduce the recorded time for the level by 10%."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_3_NAME", "Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_3_DESC", "Reduce the recorded moves per level by 1."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_4_NAME", "Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_4_DESC", "Randomly all rotate all tiles on the board 1 time."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_5_NAME", "Upgrade Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_5_DESC", "Each upgrade reduces the puzzle time taken by 10% per level."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_6_NAME", "Upgrade Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_6_DESC", "Each upgrade reduces the puzzle moves taken by 1 per level."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_7_NAME", "Upgrade Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_7_DESC", "Each upgrade increases the number of times the board can be shuffled by 1."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_8_NAME", "Unlock Tile 1"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_8_DESC", "Unlock a presumably special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_9_NAME", "Unlock Tile 2"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_9_DESC", "Unlock a presumably super special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_10_NAME", "Unlock Tile 3"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_10_DESC", "Unlock a presumably super super special tile?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_12_NAME", "10x Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_12_DESC", "Purchase undo boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_13_NAME", "10x Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_13_DESC", "Purchase time reduction boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_14_NAME", "10x Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_14_DESC", "Purchase move reduction boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_15_NAME", "10x Shuffle Board"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_15_DESC", "Purchase shuffle board boosts in bulk, saving 10%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_16_NAME", "100x Undo"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_16_DESC", "Purchase undo boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_17_NAME", "100x Time Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_17_DESC", "Purchase time reduction boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_18_NAME", "100x Move Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_18_DESC", "Purchase move reduction boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_19_NAME", "100x Shuffle Reduction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_19_DESC", "Purchase shuffle board boosts in massive bulk, saving 20%!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_20_NAME", "Main Menu Cars"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_20_DESC", "Choose how many cars appear on the main menu, between 0 and 75!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_21_NAME", "Unlock Pack 2"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_21_DESC", "Instantly unlock pack 2, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_22_NAME", "Unlock Pack 3"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_22_DESC", "Instantly unlock pack 3, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_23_NAME", "Unlock Pack 4"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_23_DESC", "Instantly unlock pack 4, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_24_NAME", "Unlock Pack 5"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_24_DESC", "Instantly unlock pack 5, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_25_NAME", "Unlock Pack 6"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_25_DESC", "Instantly unlock pack 6, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_26_NAME", "Unlock Pack 7"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_26_DESC", "Instantly unlock pack 7, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_27_NAME", "Unlock Pack 8"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_27_DESC", "Instantly unlock pack 8, without collecting all stars."));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_28_NAME", "Zen Mode"));
        texts.add(new Text(Constants.LANGUAGE_EN, "ITEM_28_DESC", "Hide puzzle timer and move counter, for a more relaxing experience!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_0_NAME", "English"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_1_NAME", "Zhōngwén"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_2_NAME", "Deutsch"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_3_NAME", "Français"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_4_NAME", "Polski"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_5_NAME", "Pусский"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_6_NAME", "Español"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_7_NAME", "Nederlands"));
        texts.add(new Text(Constants.LANGUAGE_EN, "LANGUAGE_8_NAME", "Svenska"));
        texts.add(new Text(Constants.LANGUAGE_EN, "METRIC_TILES_EARNED", "Tiles Earned"));
        texts.add(new Text(Constants.LANGUAGE_EN, "METRIC_BEST_TIME", "Best Time"));
        texts.add(new Text(Constants.LANGUAGE_EN, "METRIC_BEST_MOVES", "Best Moves"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_1_NAME", "Tutorial"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_2_NAME", "The Big City"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_3_NAME", "Escape To The Country"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_4_NAME", "Forest Fun"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_5_NAME", "Mountain Climbing"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_6_NAME", "Desert Oasis"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_7_NAME", "Flowing Frenzy"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_8_NAME", "Heady Heights"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_9_NAME", "Contributor's Challenge"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PACK_9_CHALLENGE", "Purchase any number of coins to unlock this pack."));
        texts.add(new Text(Constants.LANGUAGE_EN, "PUZZLE_DEFAULT_NAME", "New Puzzle (%1$dx%2$d, %3$s)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "PUZZLE_DEFAULT_DESC", "No description."));
        texts.add(new Text(Constants.LANGUAGE_EN, "PUZZLE_EXPORT_START", "Beginning puzzle export process..."));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_CURRENT", "In Progress"));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_AVAILABLE", "Available"));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_UPCOMING", "Upcoming"));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_COMPLETED", "Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_FAILED", "Failed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "QUEST_COMPLETED_TEXT", "Completed the %1$s quest \"%2$s\" and earned %3$d coins!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_SECTION_AUDIO", "Audio Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_SECTION_GAMEPLAY", "Game Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_SECTION_GOOGLE", "Google Settings"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_SECTION_OTHER", "Other"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_1_NAME", "Music"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_2_NAME", "Sounds"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_3_NAME", "Minimum Zoom"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_4_NAME", "Maximum Zoom"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_5_NAME", "Zen Mode"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_6_NAME", "Hide Unstocked Boosts"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_7_NAME", "Player Name"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_8_NAME", "Google Play Sign In"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_9_NAME", "Main Menu Cars"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_10_NAME", "Puzzle Background"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_11_NAME", "Autosave Freq (Mins)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_12_NAME", "Language"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_13_NAME", "Vibration"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_14_NAME", "Purchase Sound"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_15_NAME", "Tile Rotate Sound"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_16_NAME", "Setting Change Sound"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_17_NAME", "Main Music"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SETTING_18_NAME", "Puzzle Music"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_CATEGORY_1_NAME", "Boosts"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_CATEGORY_2_NAME", "Upgrades"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_CATEGORY_3_NAME", "Tiles"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_CATEGORY_4_NAME", "Misc"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_ADVERT", "Advert"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_OFFERS", "Offers"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_PURCHASE_TEXT", "Buy for %1$s coins"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_NUMBER_PURCHASES", "%1$s%2$s purchases"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_NUMBER_OWNED", "%1$s owned"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_ITEM_PURCHASED", "Successfully purchased %1$s for %2$d coins!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_ITEM_PURCHASED_BACKGROUND", "Successfully purchased %1$s for %2$d coins, and unlocked '%3$s'!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SHOP_MAX_PURCHASED", "Maximum Purchased"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_1_NAME", "Puzzles Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_2_NAME", "Tiles Rotated"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_3_NAME", "Quests Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_4_NAME", "Puzzles Fully Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_5_NAME", "Boosts Used"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_6_NAME", "Coins"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_7_NAME", "TapJoy Coins"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_8_NAME", "Last Autosave"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_9_NAME", "Pack 1 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_10_NAME", "Pack 2 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_11_NAME", "Pack 3 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_12_NAME", "Pack 4 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_13_NAME", "Pack 5 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_14_NAME", "Pack 6 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_15_NAME", "Pack 7 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_16_NAME", "Pack 8 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "STATISTIC_17_NAME", "Pack 9 Completed"));
        texts.add(new Text(Constants.LANGUAGE_EN, "SUCCESS_SUPPORT_CODE", "Successfully applied support code!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_0_NAME", "Invisible Tile"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_1_NAME", "Grass Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_2_NAME", "Grass Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_3_NAME", "Grass Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_4_NAME", "Grass Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_5_NAME", "Grass Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_6_NAME", "Grass"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_7_NAME", "Grass Road Bridge"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_8_NAME", "Grass Water Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_9_NAME", "Grass Water Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_10_NAME", "Grass Road Straight (Median)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_11_NAME", "Grass Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_12_NAME", "Grass Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_13_NAME", "City (Red Shop)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_14_NAME", "City Road Straight (Bus Stop)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_15_NAME", "City Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_16_NAME", "City Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_17_NAME", "City Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_18_NAME", "City Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_19_NAME", "City Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_20_NAME", "City Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_21_NAME", "City"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_22_NAME", "City Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_23_NAME", "City Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_24_NAME", "City Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_25_NAME", "City Road/Path T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_26_NAME", "City Grass End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_27_NAME", "City Grass Straight (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_28_NAME", "City Grass Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_29_NAME", "City Road Straight (Crossing)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_30_NAME", "City (Green Shop)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_31_NAME", "City (Fountain)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_32_NAME", "City (Grass)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_33_NAME", "Grass Road/Water Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_34_NAME", "Grass High"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_35_NAME", "Grass Water End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_36_NAME", "Grass High Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_37_NAME", "Grass High Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_38_NAME", "City Road End (Underground)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_39_NAME", "Grass High Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_40_NAME", "City Canal Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_41_NAME", "City Canal End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_42_NAME", "Grass Road Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_43_NAME", "Grass (Tree 1)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_44_NAME", "Grass (Tree 2)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_45_NAME", "Grass (Tree 3)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_46_NAME", "City Road/Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_47_NAME", "City High Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_48_NAME", "City High Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_49_NAME", "City Grass Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_50_NAME", "City Canal Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_51_NAME", "City Road/Path Corner (Inverse)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_52_NAME", "City Water Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_53_NAME", "City Water T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_54_NAME", "City Grass Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_55_NAME", "City Grass T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_56_NAME", "City Road Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_57_NAME", "City Road Corner (Lights)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_58_NAME", "City Road Corner (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_59_NAME", "City Road/Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_60_NAME", "Forest River Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_61_NAME", "Forest River Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_62_NAME", "Forest Dirt Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_63_NAME", "Forest High Dirt Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_64_NAME", "Forest Dirt Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_65_NAME", "Forest Dirt Straight (Humped)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_66_NAME", "Forest Dirt T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_67_NAME", "Forest Dirt Corner (Sharp)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_68_NAME", "Forest Dirt Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_69_NAME", "Forest"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_70_NAME", "Forest Dirt Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_71_NAME", "Forest Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_72_NAME", "Forest High"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_73_NAME", "Forest (Rocks)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_74_NAME", "Forest (Trees)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_75_NAME", "Forest Dirt End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_76_NAME", "Forest River End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_77_NAME", "City Road Straight (Median)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_78_NAME", "City Road Straight (Trees)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_79_NAME", "Grass Road Slope (Narrow)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_80_NAME", "City Road End (Barrier)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_81_NAME", "Forest River T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_82_NAME", "Forest River Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_83_NAME", "Forest High Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_84_NAME", "Forest High Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_85_NAME", "City High Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_86_NAME", "Mountain Hill"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_87_NAME", "Mountain"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_88_NAME", "Mountain River End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_89_NAME", "Mountain River Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_90_NAME", "Mountain River Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_91_NAME", "Mountain River T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_92_NAME", "Mountain River Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_93_NAME", "Desert"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_94_NAME", "Desert Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_95_NAME", "Desert High"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_96_NAME", "Desert Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_97_NAME", "Desert Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_98_NAME", "Desert Road (Crossing)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_99_NAME", "Desert Road End (Round)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_100_NAME", "Desert Road/Path T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_101_NAME", "Desert Path Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_102_NAME", "Desert Rail Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_103_NAME", "Desert Rail Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_104_NAME", "Desert Rail Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_105_NAME", "Desert Rail Double Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_106_NAME", "Desert Rail 3 Split"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_107_NAME", "Desert Rail Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_108_NAME", "Desert Rail End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_109_NAME", "Desert Rail T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_110_NAME", "Desert Rail V Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_111_NAME", "Desert Rail Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_112_NAME", "Desert Rail/Road Crossover"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_113_NAME", "Desert High Road"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_114_NAME", "Desert High Road (Rounded Bridge)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_115_NAME", "Desert High Road (Squared Bridge)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_116_NAME", "Desert Road Bridge"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_117_NAME", "Desert Road Slope (No Bottom)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_118_NAME", "Desert Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_119_NAME", "Desert Road Slope (No Top)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_120_NAME", "Desert Road Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_121_NAME", "Desert Path Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_122_NAME", "Desert Road Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_123_NAME", "Desert Path End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_124_NAME", "Desert Path V Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_125_NAME", "Desert Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_126_NAME", "Desert Road Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_127_NAME", "Desert Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_128_NAME", "Desert Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_129_NAME", "Mountain (Stones)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_130_NAME", "City Path T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_131_NAME", "City High"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_132_NAME", "City Path/Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_133_NAME", "City Road Straight (Lights 1)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_134_NAME", "Grass Road Corner (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_135_NAME", "City Road Straight (Lights 2)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_136_NAME", "City (Grass Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_137_NAME", "City Slope"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_138_NAME", "Golf"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_139_NAME", "Golf Straight (Dip)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_140_NAME", "Golf Straight (Bump)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_141_NAME", "Golf Straight (Castle)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_142_NAME", "Golf Straight (Ditch)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_143_NAME", "Golf End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_144_NAME", "Golf Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_145_NAME", "Golf Straight (Hill)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_146_NAME", "Golf End (Hole)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_147_NAME", "Golf Straight (Squared Blocks)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_148_NAME", "Golf Straight (Curved Blocks)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_149_NAME", "Golf Straight (Blocks)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_150_NAME", "Golf Straight (Strip)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_151_NAME", "Golf Corner (Curved)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_152_NAME", "Golf Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_153_NAME", "Golf Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_154_NAME", "Golf T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_155_NAME", "Golf Straight (Tunnel)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_156_NAME", "Golf Straight (Shed)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_157_NAME", "Desert Rail/Road T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_158_NAME", "Desert Road/Rail T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_159_NAME", "Desert Road/Rail Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_160_NAME", "Desert Rail/Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_161_NAME", "Desert Path/Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_162_NAME", "Desert Rail/Path Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_163_NAME", "Desert High Road End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_164_NAME", "Desert High Road Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_165_NAME", "Desert High Rail Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_166_NAME", "Desert High Rail End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_167_NAME", "City (Red Shop 2)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_168_NAME", "City (Red Glass Shop)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_169_NAME", "City (Brown House)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_170_NAME", "City (Cream House)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_171_NAME", "City (Red House)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_172_NAME", "Grass Straight (Trees)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_173_NAME", "Mountain Toxic End"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_174_NAME", "Mountain Toxic Corner"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_175_NAME", "Mountain Toxic Straight"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_176_NAME", "Mountain Toxic T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_177_NAME", "Mountain Toxic Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_178_NAME", "Mountain (Lilypad)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_179_NAME", "Mountain (Cacti)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_180_NAME", "Mountain (Rock)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_181_NAME", "Mountain (Mushroom)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_182_NAME", "Grass Water T Junction"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_183_NAME", "Grass Water Interchange"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TILE_184_NAME", "Grass Water Corner (Tree)"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_1", "Rotate tiles to form a consistent flow, with no loose ends. Tap to rotate!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_2", "Completing a puzzle will usually unlock a new tile in the level creator, there's hundreds!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_3", "Completing all the puzzles in a pack in fewer moves & less time than the par will unlock the next pack for free."));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_4", "Flows can transition and cross over, it all still counts as flowing!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_5", "Puzzles also have different heights, and can be set in different environments. What's your favourite?"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_6", "Bored of packs? Over 186^225 (over 1 novensexagintacentillion!) different puzzles can be generated in the \"Build\" area!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_7", "Not a fan of the music / sounds? Customise them in the settings menu!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_8", "You're probably reading these okay, but the language can be changed in the settings too. Almost all translations are fan-submitted, feel free to help out!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_9", "Want free coins? Completing quests & levels or watching adverts & offers are all good earners!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_10", "Decorative tiles can be rotated in any direction, since they have no flow."));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_11", "Share your puzzles at reddit.com/r/CityFlow, or any of the social media sites (links in settings)!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_12", "New packs, tiles, game options, and boosts can be purchased in the shop."));
        texts.add(new Text(Constants.LANGUAGE_EN, "TUTORIAL_13", "Can't fully complete a pack to unlock the next one? Use your hard earned coins to buy it instead!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_BACKGROUND_SELECT_TITLE", "%1$d/%2$d Unlocked"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_IAP_TITLE", "Purchase Coins"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_IAP_TEASER", "First purchase of any pack unlocks a bonus puzzle pack!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_IAP_TIP", "Larger packs are better value, and keep an eye out for seasonal price reductions!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_COMPLETE_TITLE", "Complete\n100%"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_COMPLETE_TEXT", "Completed!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_TIME_TITLE", "Time\n%1$d%%"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_TIME_TEXT", "%1$s/%2$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_MOVES_TITLE", "Moves\n%1$d%%"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_SKYSCRAPER_MOVES_TEXT", "%1$d/%2$d\nmoves"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_OPEN", "Open Pack"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_PURCHASE", "Purchase Pack"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_UNLOCKABLE_HEADER", "Pack locked!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_UNLOCKABLE_INSTRUCTION", "Fully complete pack \"%1$s\" (currently %2$d / %3$d stars) to unlock, or purchase for coins in the shop."));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_UNLOCKED_STARS", "%1$d / %2$d Stars"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_UNLOCKED_TIME", "Best Time: %1$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PACK_UNLOCKED_MOVES", "Best Moves: %1$s"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_BY", "By:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_NAME", "Name:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_DESC", "Desc:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_DATE_ADDED", "Added:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_BEST_MOVES", "Moves:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_BEST_TIME", "Time:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_STARS", "Stars:"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_WIDTH", "Width: %1$d"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_HEIGHT", "Height: %1$d"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_OPTIONS", "Puzzle Options"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_AUTOGENERATE", "Blank Puzzle"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_SHUFFLE_PLAY", "Shuffle & Play"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_RESIZE", "Resize \"%1$s\""));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_PUZZLE_RESIZE_HINT", "Width = top left -> bottom right\nHeight = bottom left -> top right."));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_TILE_UNLOCK_PUZZLE", "To unlock \"%1$s\", complete puzzle \"%2$s\" in pack \"%3$s\"."));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_TILE_UNLOCK_SHOP", "Unlock \"%1$s\" by purchasing it in the shop."));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_TILE_UNLOCK", "Unlocked %1$s tile(s)!"));
        texts.add(new Text(Constants.LANGUAGE_EN, "UI_TILE_NO_UNLOCK", "No tiles unlocked."));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_ALL", "All"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_AREA", "Area"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_DESCRIPTION", "Description"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_FLOW", "Flow"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_HEIGHT", "Height"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_IMPORT", "Import"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_NEVER", "Never"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_NA", "N/A"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_NAME", "Name"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_OPEN", "Open"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_PAUSED", "Paused"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_START", "Start"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_UNLOCK", "Unlock"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_LOCKED", "Locked"));
        texts.add(new Text(Constants.LANGUAGE_EN, "WORD_LOADING", "Loading"));
        Text.saveInTx(texts);
    }

    private static void installGermanText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_DE, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_DE, "SETTING_SECTION_GAMEPLAY", "GERMAN TEXT"));
        Text.saveInTx(texts);
    }

    private static void installDutchText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_NL, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_NL, "SETTING_SECTION_GAMEPLAY", "DUTCH TEXT"));
        Text.saveInTx(texts);
    }

    private static void installFrenchText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_FR, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_FR, "SETTING_SECTION_GAMEPLAY", "FRENCH TEXT"));
        Text.saveInTx(texts);
    }

    private static void installPolishText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_PL, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_PL, "SETTING_SECTION_GAMEPLAY", "POLISH TEXT"));
        Text.saveInTx(texts);
    }

    private static void installRussianText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_RU, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_RU, "SETTING_SECTION_GAMEPLAY", "RUSSIAN TEXT"));
        Text.saveInTx(texts);
    }

    private static void installSpanishText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_ES, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_ES, "SETTING_SECTION_GAMEPLAY", "SPANISH TEXT"));
        Text.saveInTx(texts);
    }

    private static void installChineseText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_ZH, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_ZH, "SETTING_SECTION_GAMEPLAY", "CHINESE TEXT"));
        Text.saveInTx(texts);
    }

    private static void installSwedishText() {
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(Constants.LANGUAGE_SV, "INSTALL_CHECK", ""));

        texts.add(new Text(Constants.LANGUAGE_SV, "SETTING_SECTION_GAMEPLAY", "SWEDISH TEXT"));
        Text.saveInTx(texts);
    }
}

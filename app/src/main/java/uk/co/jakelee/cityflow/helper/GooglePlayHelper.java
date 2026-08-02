package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.gson.Gson;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.BuildConfig;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Achievement;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Iap;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.SupportCode;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

import static uk.co.jakelee.cityflow.model.Statistic.find;

/**
 * Play Games Services v2: sign-in is automatic and there is no sign-out. Every v2 client needs an
 * Activity, but the fire-and-forget writes are called from places holding only a Context, so
 * MainActivity registers itself here on creation and those calls borrow it.
 */
public class GooglePlayHelper {
    public static final int RC_ACHIEVEMENTS = 9002;
    public static final int RC_LEADERBOARDS = 9003;
    public static final int RC_SAVED_GAMES = 9004;
    private static final int MAX_COVER_BYTES = 800 * 1024;
    private static final String SAVE_DELIMITER = "UNIQUEDELIMITINGSTRING";
    private static final String mCurrentSaveName = "cityflowCloudSave";
    private static final String mAutosaveName = "autoSave";
    private static Activity gamesActivity;
    private static boolean authenticated = false;
    private static byte[] cloudSaveData;
    private static Context callingContext;
    private static Activity callingActivity;
    private static Snapshot loadedSnapshot;

    public static void initialise(final Activity activity) {
        gamesActivity = activity;
        PlayGamesSdk.initialize(activity.getApplicationContext());
        refreshAuthentication(activity);
    }

    public static void refreshAuthentication(final Activity activity) {
        if (!isUsable(activity)) {
            return;
        }

        PlayGames.getGamesSignInClient(activity).isAuthenticated().addOnCompleteListener(task ->
                authenticated = task.isSuccessful() && task.getResult().isAuthenticated());
    }

    /** Only needed when automatic sign-in didn't take; v2 has no sign-out. */
    public static void signIn(final Activity activity) {
        if (!isUsable(activity)) {
            return;
        }

        PlayGames.getGamesSignInClient(activity).signIn().addOnCompleteListener(task ->
                authenticated = task.isSuccessful() && task.getResult().isAuthenticated());
    }

    private static boolean isUsable(Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean AreGooglePlayServicesInstalled(Activity activity) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(activity);
        return code == ConnectionResult.SUCCESS;
    }

    public static boolean IsConnected() {
        return authenticated && isUsable(gamesActivity);
    }

    public static void UpdateEvent(String eventId, int quantity) {
        if (!IsConnected() || quantity <= 0) {
            return;
        }

        PlayGames.getEventsClient(gamesActivity).increment(eventId, quantity);
    }

    public static void UpdateLeaderboards(String leaderboardID, int value) {
        if (!IsConnected()) {
            return;
        }

        PlayGames.getLeaderboardsClient(gamesActivity).submitScore(leaderboardID, value);
    }

    public static void UpdateAchievements() {
        if (!IsConnected()) {
            return;
        }

        List<Statistic> statistics = Select.from(Statistic.class).where(
                Condition.prop("last_sent_value").notEq(Constants.STATISTIC_UNTRACKED)).list();

        for (Statistic statistic : statistics) {
            int currentValue = statistic.getIntValue();
            int lastSentValue = statistic.getLastSentValue();
            List<Achievement> achievements = Select.from(Achievement.class).where(
                    Condition.prop("statistic_id").eq(statistic.getId())).orderBy("maximum_value ASC").list();

            for (Achievement achievement : achievements) {
                UpdateAchievement(achievement, currentValue, lastSentValue);
                if (achievement.getMaximumValue() <= currentValue) {
                    Statistic.addCurrency((Iap.hasCoinDoubler() ? 2 : 1) * Constants.CURRENCY_ACHIEVEMENT);
                }
            }

            UpdateStatistic(statistic, currentValue, lastSentValue);
        }
    }

    private static void UpdateAchievement(Achievement achievement, int currentValue, int lastSentValue) {
        boolean hasChanged = (currentValue > lastSentValue);
        boolean isAchieving = achievement.getMaximumValue() <= currentValue && achievement.getMaximumValue() >= lastSentValue;
        boolean isAchieved = (achievement.getMaximumValue() <= lastSentValue);
        if (hasChanged && !isAchieved) {
            int difference = currentValue - lastSentValue;
            if (achievement.getMaximumValue() == 1) {
                PlayGames.getAchievementsClient(gamesActivity).unlock(achievement.getRemoteID());
            } else {
                PlayGames.getAchievementsClient(gamesActivity).increment(achievement.getRemoteID(), difference);
            }

            if (isAchieving && achievement.getColourID() > 0) {
                Background.get(achievement.getColourID()).unlock();
            }
        }
    }

    private static void UpdateStatistic(Statistic statistic, int currentValue, int lastSentValue) {
        if (currentValue > lastSentValue) {
            statistic.setLastSentValue(currentValue);
            statistic.save();
        }
    }

    // v2's intent getters are asynchronous, hence launching from the callback.
    public static void ShowAchievements(final Activity activity) {
        if (!IsConnected()) {
            return;
        }

        PlayGames.getAchievementsClient(activity).getAchievementsIntent().addOnSuccessListener(intent ->
                activity.startActivityForResult(intent, RC_ACHIEVEMENTS));
    }

    public static void ShowAllLeaderboards(final Activity activity) {
        if (!IsConnected()) {
            return;
        }

        PlayGames.getLeaderboardsClient(activity).getAllLeaderboardsIntent().addOnSuccessListener(intent ->
                activity.startActivityForResult(intent, RC_LEADERBOARDS));
    }

    public static void ShowLeaderboard(final Activity activity, String leaderboardID) {
        if (!IsConnected()) {
            return;
        }

        PlayGames.getLeaderboardsClient(activity).getLeaderboardIntent(leaderboardID).addOnSuccessListener(intent ->
                activity.startActivityForResult(intent, RC_LEADERBOARDS));
    }

    public static void ShowSavedGames(final Activity activity) {
        if (!IsConnected()) {
            return;
        }

        PlayGames.getSnapshotsClient(activity)
                .getSelectSnapshotIntent("Cloud Saves", true, true, 1)
                .addOnSuccessListener(intent -> activity.startActivityForResult(intent, RC_SAVED_GAMES));
    }

    public static void SavedGamesIntent(final Context context, final Activity activity, final Intent intent) {
        if (intent == null || !IsConnected()) {
            return;
        }
        callingContext = context;
        callingActivity = activity;

        final boolean loading = intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA);
        if (!loading && !intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_NEW)) {
            return;
        }

        openSnapshot(activity, loading);
    }

    private static void openSnapshot(final Activity activity, final boolean loading) {
        PlayGames.getSnapshotsClient(activity).open(mCurrentSaveName, true)
                .addOnSuccessListener(result -> {
                    if (result.isConflict()) {
                        resolveConflict(activity, result.getConflict(), loading);
                    } else {
                        useSnapshot(result.getData(), loading);
                    }
                })
                .addOnFailureListener(e -> reportCloudFailure(e));
    }

    private static void resolveConflict(final Activity activity, SnapshotsClient.SnapshotConflict conflict, final boolean loading) {
        Snapshot mine = conflict.getSnapshot();
        Snapshot theirs = conflict.getConflictingSnapshot();
        Snapshot newest = mine.getMetadata().getLastModifiedTimestamp() >= theirs.getMetadata().getLastModifiedTimestamp() ? mine : theirs;

        if (callingActivity != null) {
            callingActivity.runOnUiThread(() -> AlertHelper.error(callingActivity, Text.get("ALERT_SAVE_CONFLICT")));
        }

        PlayGames.getSnapshotsClient(activity).resolveConflict(conflict.getConflictId(), newest)
                .addOnSuccessListener(result -> {
                    if (result.isConflict()) {
                        resolveConflict(activity, result.getConflict(), loading);
                    } else {
                        useSnapshot(result.getData(), loading);
                    }
                })
                .addOnFailureListener(e -> reportCloudFailure(e));
    }

    // v2's Task callbacks arrive on the main thread, unlike the AsyncTask this replaced, so the
    // read and applyBackup - which rewrites twelve tables a row at a time - are moved off it.
    private static void useSnapshot(final Snapshot snapshot, final boolean loading) {
        if (snapshot == null) {
            return;
        }

        if (loading) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        cloudSaveData = snapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                        reportCloudFailure(e);
                        return;
                    }
                    loadFromCloud(true);
                }
            }).start();
        } else {
            loadedSnapshot = snapshot;
            saveToCloud();
        }
    }

    private static void reportCloudFailure(final Exception e) {
        if (callingActivity == null) {
            return;
        }

        callingActivity.runOnUiThread(() -> AlertHelper.error(callingActivity,
                String.format(Locale.ENGLISH, AlertHelper.getError(AlertHelper.Error.CLOUD_ERROR), e.getMessage())));
    }

    private static void loadFromCloud(final boolean checkIsImprovement) {
        if (!IsConnected() || callingContext == null || callingActivity == null || cloudSaveData == null) {
            return;
        }

        if (!checkIsImprovement) {
            callingActivity.runOnUiThread(() -> AlertHelper.info(callingActivity, Text.get("ALERT_CLOUD_LOADING"), true));
        }

        Pair<Integer, Integer> cloudData = getStarsAndCoinsFromSave(cloudSaveData);

        if (!checkIsImprovement || newSaveIsBetter(cloudData)) {
            applyBackup(new String(cloudSaveData));
        } else {
            final Pair<Integer, Integer> values = cloudData;
            callingActivity.runOnUiThread(() -> AlertDialogHelper.confirmCloudLoad(callingActivity,
                    PuzzleHelper.getTotalStars(),
                    Statistic.getCurrency(),
                    values.first,
                    values.second));
        }
    }

    public static void forceLoadFromCloud() {
        new Thread(new Runnable() {
            public void run() {
                loadFromCloud(false);
            }
        }).start();
    }

    public static void forceSaveToCloud() {
        if (callingActivity == null || callingContext == null || loadedSnapshot == null) {
            return;
        }

        callingActivity.runOnUiThread(() -> AlertHelper.info(callingActivity, Text.get("ALERT_CLOUD_SAVING")));

        // Serialising reads twelve tables, so it stays off the main thread; the commit goes back
        // onto it, where the Play Games client expects to be used.
        new Thread(new Runnable() {
            public void run() {
                byte[] data = createBackup();
                String desc = String.format(Locale.ENGLISH, Text.get("CLOUD_SAVE_DESC"),
                        PuzzleHelper.getTotalStars(),
                        Statistic.getCurrency(),
                        BuildConfig.VERSION_NAME);
                Bitmap cover = decodeCoverImage(callingContext);

                loadedSnapshot.getSnapshotContents().writeBytes(data);

                final SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                        .setDescription(desc)
                        .setCoverImage(cover)
                        .build();

                callingActivity.runOnUiThread(() -> commitSnapshot(metadataChange));
            }
        }).start();
    }

    private static void commitSnapshot(SnapshotMetadataChange metadataChange) {
        PlayGames.getSnapshotsClient(callingActivity).commitAndClose(loadedSnapshot, metadataChange)
                .addOnSuccessListener(metadata -> AlertHelper.success(callingActivity, Text.get("ALERT_CLOUD_SAVED")))
                .addOnFailureListener(e -> reportCloudFailure(e));
    }

    // Decoded at full size the logo is a lot of heap to ask for mid-save.
    private static Bitmap decodeCoverImage(Context context) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.main_logo, bounds);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        while ((bounds.outWidth / options.inSampleSize) * (bounds.outHeight / options.inSampleSize) * 4 > MAX_COVER_BYTES) {
            options.inSampleSize *= 2;
        }

        return BitmapFactory.decodeResource(context.getResources(), R.drawable.main_logo, options);
    }

    // The autosave is fire-and-forget: it opens its own snapshot, so it neither reads nor disturbs
    // the manual save slot above.
    public static void autosave(final Context context) {
        if (!IsConnected()) {
            return;
        }

        final Activity activity = gamesActivity;
        PlayGames.getSnapshotsClient(activity).open(mAutosaveName, true)
                .addOnSuccessListener(result -> {
                    if (result.isConflict()) {
                        return;
                    }
                    writeAutosave(activity, context, result.getData());
                });
    }

    private static void writeAutosave(final Activity activity, final Context context, final Snapshot snapshot) {
        if (snapshot == null) {
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                byte[] data = createBackup();
                String desc = String.format(Locale.ENGLISH, Text.get("CLOUD_AUTOSAVE_DESC"),
                        PuzzleHelper.getTotalStars(),
                        Statistic.getCurrency(),
                        BuildConfig.VERSION_NAME);
                Bitmap cover = decodeCoverImage(context);

                snapshot.getSnapshotContents().writeBytes(data);

                final SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                        .setCoverImage(cover)
                        .setDescription(desc)
                        .build();

                activity.runOnUiThread(() -> PlayGames.getSnapshotsClient(activity)
                        .commitAndClose(snapshot, metadataChange)
                        .addOnSuccessListener(metadata -> {
                            Statistic lastAutosave = Statistic.find(Constants.STATISTIC_LAST_AUTOSAVE);
                            lastAutosave.setLongValue(System.currentTimeMillis());
                            lastAutosave.save();
                        }));
            }
        }).start();
    }

    public static boolean shouldAutosave() {
        Statistic lastAutosave = find(Constants.STATISTIC_LAST_AUTOSAVE);
        int minutesBetweenSaves = Setting.getInt(Constants.SETTING_AUTOSAVE_FREQUENCY);

        if (minutesBetweenSaves == Constants.AUTOSAVE_NEVER) {
            return false;
        }

        long nextAutosave = lastAutosave.getLongValue() + DateHelper.minutesToMilliseconds(minutesBetweenSaves);
        return nextAutosave <= System.currentTimeMillis();
    }

    private static void saveToCloud() {
        if (!IsConnected() || callingContext == null || callingActivity == null || loadedSnapshot == null) {
            return;
        }

        SnapshotMetadata metadata = loadedSnapshot.getMetadata();
        if (metadata.getDeviceName() == null) {
            forceSaveToCloud();
        } else {
            AlertDialogHelper.confirmCloudSave(callingActivity,
                    PuzzleHelper.getTotalStars(),
                    Statistic.getCurrency(),
                    metadata.getDescription(),
                    metadata.getLastModifiedTimestamp(),
                    metadata.getDeviceName());
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends SugarRecord>[] backupClasses = new Class[] {
            Background.class,
            Boost.class,
            Iap.class,
            Pack.class,
            Puzzle.class,
            PuzzleCustom.class,
            ShopItem.class,
            Setting.class,
            Statistic.class,
            SupportCode.class,
            Tile.class,
            TileType.class};

    public static byte[] createBackup() {
        Gson gson = new Gson();

        String backupString = PatchHelper.LATEST_PATCH + GooglePlayHelper.SAVE_DELIMITER;
        backupString += PuzzleHelper.getTotalStars() + GooglePlayHelper.SAVE_DELIMITER;
        backupString += Statistic.getCurrency() + GooglePlayHelper.SAVE_DELIMITER;

        for (Class<? extends SugarRecord> backupClass : backupClasses) {
            backupString += gson.toJson(SugarRecord.listAll(backupClass)) + GooglePlayHelper.SAVE_DELIMITER;
        }

        return backupString.getBytes();
    }

    // applyBackup deletes each table before repopulating it, so a truncated snapshot would destroy
    // the player's game partway through. This guard is the only thing preventing that.
    public static boolean isPlausibleSave(String backupData) {
        if (backupData == null || backupData.isEmpty()) {
            return false;
        }

        String[] splitData = splitBackupData(backupData);
        if (splitData.length <= 3) {
            return false;
        }

        try {
            Integer.parseInt(splitData[0].trim());
            Integer.parseInt(splitData[1].trim());
            Integer.parseInt(splitData[2].trim());
            // Index 3 is Background, the first backed-up table: a save with none is not a save.
            Background[] backgrounds = new Gson().fromJson(splitData[3], Background[].class);
            return backgrounds != null && backgrounds.length > 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void applyBackup(String backupData) {
        if (!isPlausibleSave(backupData)) {
            reportCloudFailure(new IOException("Save data was incomplete, so nothing was changed"));
            return;
        }

        Gson gson = new Gson();
        String[] splitData = splitBackupData(backupData);

        if (MainActivity.prefs != null) {
            MainActivity.prefs.edit().putInt("databaseVersion", Integer.parseInt(splitData[0].trim())).apply();
        }

        // 0 is db version, 1 & 2 are stars & coins
        int backupPosition = 3;
        for (Class<? extends SugarRecord> backupClass : backupClasses) {
            if (splitData.length > backupPosition) {
                SugarRecord.deleteAll(backupClass);
                SugarRecord.saveInTx(fromJsonList(gson, splitData[backupPosition++], backupClass));
            }
        }

        new PatchHelper(callingActivity, false).execute();

        if (callingActivity != null) {
            callingActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertHelper.success(callingActivity, Text.get("ALERT_CLOUD_LOADED"));
                }
            });
        }
    }

    private static <T extends SugarRecord> List<T> fromJsonList(Gson gson, String json, Class<T> className) {
        Object[] array = (Object[])java.lang.reflect.Array.newInstance(className, 1);
        array = gson.fromJson(json, array.getClass());
        List<T> list = new ArrayList<>();
        for (Object item : array) {
            list.add((T)item);
        }
        return list;
    }

    public static Pair<Integer, Integer> getStarsAndCoinsFromSave(byte[] saveBytes) {
        int stars = 0;
        int coins = 0;

        // Runs before isPlausibleSave, so it has to tolerate a garbage snapshot rather than throw
        // out of a background thread. Zeroes fall through to the "cloud save is worse" prompt.
        String[] splitData = splitBackupData(new String(saveBytes));
        if (splitData.length > 2) {
            try {
                stars = Integer.parseInt(splitData[1].trim());
                coins = Integer.parseInt(splitData[2].trim());
            } catch (NumberFormatException e) {
                return new Pair<>(0, 0);
            }
        }

        return new Pair<>(stars, coins);
    }

    public static boolean newSaveIsBetter(Pair<Integer, Integer> newValues) {
        return !(newValues.first <= PuzzleHelper.getTotalStars() && newValues.second <= Statistic.getCurrency());
    }

    private static String[] splitBackupData(String backupData) {
        String[] splitData = backupData.split(GooglePlayHelper.SAVE_DELIMITER);
        for (int i = 0; i < splitData.length; i++) {
            splitData[i] = splitData[i].replace(GooglePlayHelper.SAVE_DELIMITER, "");
        }

        return splitData;
    }
}

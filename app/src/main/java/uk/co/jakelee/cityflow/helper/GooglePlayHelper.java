package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.QuestBuffer;
import com.google.android.gms.games.quest.Quests;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.gson.Gson;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.nio.charset.Charset;
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

public class GooglePlayHelper implements com.google.android.gms.common.api.ResultCallback {
    public static final int RC_ACHIEVEMENTS = 9002;
    public static final int RC_LEADERBOARDS = 9003;
    public static final int RC_SAVED_GAMES = 9004;
    public static final int RC_QUESTS = 9005;
    private static final int RESULT_OK = -1;
    private static final int RC_SIGN_IN = 9001;
    private static final String SAVE_DELIMITER = "UNIQUEDELIMITINGSTRING";
    private static final String mCurrentSaveName = "cityflowCloudSave";
    public static GoogleApiClient mGoogleApiClient;
    private static boolean mResolvingConnectionFailure = false;
    private static byte[] cloudSaveData;
    private static Context callingContext;
    private static Activity callingActivity;
    private static Snapshot loadedSnapshot;

    public static void ConnectionFailed(Activity activity, ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(activity,
                mGoogleApiClient, connectionResult,
                RC_SIGN_IN, AlertHelper.getError(AlertHelper.Error.FAILED_TO_CONNECT));
    }

    public static void ActivityResult(Activity activity, int requestCode, int resultCode) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                Setting signIn = Setting.findById(Setting.class, Constants.SETTING_SIGN_IN);
                signIn.setBooleanValue(false);
                signIn.save();
            }
        }
    }

    public static String CompleteQuest(Quest quest) {
        Games.Quests.claim(mGoogleApiClient, quest.getQuestId(),
                quest.getCurrentMilestone().getMilestoneId());

        String questName = quest.getName();
        String questDifficulty = new String(quest.getCurrentMilestone().getCompletionRewardData(), Charset.forName("UTF-8"));
        int questCoins = getQuestReward(questDifficulty);

        Statistic.increaseByX(Constants.STATISTIC_CURRENCY, questCoins);
        Statistic.increaseByOne(Constants.STATISTIC_QUESTS_COMPLETED);
        GooglePlayHelper.UpdateEvent(Constants.EVENT_COMPLETE_QUEST, 1);
        return String.format(Locale.ENGLISH, Text.get("QUEST_COMPLETED_TEXT"),
                questDifficulty,
                questName,
                questCoins);
    }

    private static int getQuestReward(String questDifficulty) {
        switch (questDifficulty) {
            case "Easy":
                return Constants.CURRENCY_QUEST_EASY;
            case "Medium":
                return Constants.CURRENCY_QUEST_MEDIUM;
            case "Hard":
                return Constants.CURRENCY_QUEST_HARD;
            case "Elite":
                return Constants.CURRENCY_QUEST_ELITE;
        }
        return 0;
    }

    public static void UpdateEvent(String eventId, int quantity) {
        if (!IsConnected() || quantity <= 0) {
            return;
        }

        Games.Events.increment(mGoogleApiClient, eventId, quantity);
    }

    public static void UpdateLeaderboards(String leaderboardID, int value) {
        if (!IsConnected()) {
            return;
        }

        Games.Leaderboards.submitScore(mGoogleApiClient, leaderboardID, value);
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
        if (hasChanged && !isAchieved && mGoogleApiClient.isConnected()) {
            int difference = currentValue - lastSentValue;
            if (achievement.getMaximumValue() == 1) {
                Games.Achievements.unlock(mGoogleApiClient, achievement.getRemoteID());
            } else {
                Games.Achievements.increment(mGoogleApiClient, achievement.getRemoteID(), difference);
            }

            if (isAchieving && achievement.getColourID() > 0) {
                Background.get(achievement.getColourID()).unlock();
            }
        }
    }

    private static void UpdateStatistic(Statistic statistic, int currentValue, int lastSentValue) {
        if (currentValue > lastSentValue && mGoogleApiClient.isConnected()) {
            statistic.setLastSentValue(currentValue);
            statistic.save();
        }
    }

    public static void SavedGamesIntent(final Context context, final Activity activity, final Intent intent) {
        if (intent == null || !mGoogleApiClient.isConnected()) {
            return;
        }
        callingContext = context;
        callingActivity = activity;

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult result = Games.Snapshots.open(mGoogleApiClient, mCurrentSaveName, true).await();

                // Conflict! Let's fix it
                while (!result.getStatus().isSuccess()) {
                    callingActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertHelper.error(callingActivity, Text.get("ALERT_SAVE_CONFLICT"));
                        }
                    });

                    if (result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT) {
                        Snapshot snapshot = result.getSnapshot();
                        Snapshot conflictSnapshot = result.getConflictingSnapshot();
                        Snapshot mResolvedSnapshot = snapshot;

                        if (snapshot.getMetadata().getLastModifiedTimestamp() < conflictSnapshot.getMetadata().getLastModifiedTimestamp()) {
                            mResolvedSnapshot = conflictSnapshot;
                        }

                        result = Games.Snapshots.resolveConflict(mGoogleApiClient, result.getConflictId(), mResolvedSnapshot).await();
                    }
                }

                Snapshot snapshot = result.getSnapshot();
                try {
                    if (intent.hasExtra(Snapshots.EXTRA_SNAPSHOT_METADATA)) {
                        cloudSaveData = snapshot.getSnapshotContents().readFully();
                        loadFromCloud(true);
                    } else if (intent.hasExtra(Snapshots.EXTRA_SNAPSHOT_NEW)) {
                        loadedSnapshot = snapshot;
                        saveToCloud();
                    }
                } catch (final IOException e) {
                    callingActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertHelper.error(callingActivity, String.format(Locale.ENGLISH, AlertHelper.getError(AlertHelper.Error.CLOUD_ERROR), e.getMessage()));
                        }
                    });
                }
                return result.getStatus().getStatusCode();
            }
        };

        task.execute();
    }

    private static void loadFromCloud(final boolean checkIsImprovement) {
        if (!IsConnected() || callingContext == null || callingActivity == null || cloudSaveData == null) {
            return;
        }

        callingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!checkIsImprovement) {
                    AlertHelper.info(callingActivity, Text.get("ALERT_CLOUD_LOADING"), true);
                }
            }
        });

        Pair<Integer, Integer> cloudData = getStarsAndCoinsFromSave(cloudSaveData);

        if (!checkIsImprovement || newSaveIsBetter(cloudData)) {
            applyBackup(new String(cloudSaveData));
        } else {
            AlertDialogHelper.confirmCloudLoad(callingActivity,
                    PuzzleHelper.getTotalStars(),
                    Statistic.getCurrency(),
                    cloudData.first,
                    cloudData.second);
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
        callingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertHelper.info(callingActivity, Text.get("ALERT_CLOUD_SAVING"));
            }
        });

        new Thread(new Runnable() {
            public void run() {
                byte[] data = createBackup();
                String desc = String.format(Locale.ENGLISH, Text.get("CLOUD_SAVE_DESC"),
                        PuzzleHelper.getTotalStars(),
                        Statistic.getCurrency(),
                        BuildConfig.VERSION_NAME);
                Bitmap cover = BitmapFactory.decodeResource(callingContext.getResources(), R.drawable.main_logo);

                loadedSnapshot.getSnapshotContents().writeBytes(data);

                SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                        .setDescription(desc)
                        .setCoverImage(cover)
                        .build();

                // Commit the operation
                Games.Snapshots.commitAndClose(mGoogleApiClient, loadedSnapshot, metadataChange);

                callingActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertHelper.success(callingActivity, Text.get("ALERT_CLOUD_SAVED"));
                    }
                });
            }
        }).start();
    }

    public static void autosave(Context context) {
        if (!IsConnected()) {
            Log.d("GPH", "Not connected..!");
            return;
        }

        Snapshots.OpenSnapshotResult result = Games.Snapshots.open(mGoogleApiClient, "autoSave", true).await();
        byte[] data = createBackup();
        String desc = String.format(Locale.ENGLISH, Text.get("CLOUD_AUTOSAVE_DESC"),
                PuzzleHelper.getTotalStars(),
                Statistic.getCurrency(),
                BuildConfig.VERSION_NAME);
        Bitmap cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_logo);

        // Check the result of the open operation
        if (result.getStatus().isSuccess()) {
            Snapshot snapshot = result.getSnapshot();
            snapshot.getSnapshotContents().writeBytes(data);

            // Create the change operation
            SnapshotMetadataChange metadataChange = new
                    SnapshotMetadataChange.Builder()
                    .setCoverImage(cover)
                    .setDescription(desc)
                    .build();

            Games.Snapshots.commitAndClose(mGoogleApiClient, snapshot, metadataChange);

            Statistic lastAutosave = Statistic.find(Constants.STATISTIC_LAST_AUTOSAVE);
            lastAutosave.setLongValue(System.currentTimeMillis());
            lastAutosave.save();
        }
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

        if (loadedSnapshot.getMetadata().getDeviceName() == null) {
            forceSaveToCloud();
        } else {
            AlertDialogHelper.confirmCloudSave(callingActivity,
                    PuzzleHelper.getTotalStars(),
                    Statistic.getCurrency(),
                    loadedSnapshot.getMetadata().getDescription(),
                    loadedSnapshot.getMetadata().getLastModifiedTimestamp(),
                    loadedSnapshot.getMetadata().getDeviceName());
        }

    }

    public static boolean AreGooglePlayServicesInstalled(Activity activity) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(activity);
        return code == ConnectionResult.SUCCESS;
    }

    public static boolean IsConnected() {
        return GooglePlayHelper.mGoogleApiClient != null && GooglePlayHelper.mGoogleApiClient.isConnected();
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

    public static void applyBackup(String backupData) {
        Gson gson = new Gson();
        String[] splitData = splitBackupData(backupData);

        if (backupData.length() == 0 || splitData.length <= 3) {
            return;
        }

        if (MainActivity.prefs != null) {
            MainActivity.prefs.edit().putInt("databaseVersion", Integer.parseInt(splitData[0])).apply();
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

        String[] splitData = splitBackupData(new String(saveBytes));
        if (splitData.length > 2) {
            stars = Integer.parseInt(splitData[1]);
            coins = Integer.parseInt(splitData[2]);
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

    public void onResult(com.google.android.gms.common.api.Result result) {
        Quests.LoadQuestsResult r = (Quests.LoadQuestsResult) result;
        QuestBuffer qb = r.getQuests();
        qb.close();
    }
}

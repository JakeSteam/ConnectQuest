package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import uk.co.jakelee.cityflow.BuildConfig;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.MainActivity;
import uk.co.jakelee.cityflow.model.Achievement;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

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

    public enum IAPs { unlock_pack2, unlock_pack3 }

    public static void ConnectionFailed(Activity activity, ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(activity,
                mGoogleApiClient, connectionResult,
                RC_SIGN_IN, ErrorHelper.get(ErrorHelper.Error.FAILED_TO_CONNECT));
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
        return String.format(Text.get("QUEST_COMPLETED_TEXT"),
                questDifficulty,
                questName,
                questCoins);
    }

    private static int getQuestReward(String questDifficulty) {
        switch (questDifficulty) {
            case "Easy": return Constants.CURRENCY_QUEST_EASY;
            case "Medium": return Constants.CURRENCY_QUEST_MEDIUM;
            case "Hard": return Constants.CURRENCY_QUEST_HARD;
            case "Elite": return Constants.CURRENCY_QUEST_ELITE;
        }
        return 0;
    }

    public void onResult(com.google.android.gms.common.api.Result result) {
        Quests.LoadQuestsResult r = (Quests.LoadQuestsResult)result;
        QuestBuffer qb = r.getQuests();
        qb.close();
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
                    Statistic.addCurrency(Constants.CURRENCY_ACHIEVEMENT);
                }
            }

            UpdateStatistic(statistic, currentValue, lastSentValue);
        }
    }

    private static void UpdateAchievement(Achievement achievement, int currentValue, int lastSentValue) {
        boolean hasChanged = (currentValue > lastSentValue);
        boolean isAchieved = (achievement.getMaximumValue() <= lastSentValue);
        if (hasChanged && !isAchieved && mGoogleApiClient.isConnected()) {
            int difference = currentValue - lastSentValue;
            if (achievement.getMaximumValue() == 1) {
                Games.Achievements.unlock(mGoogleApiClient, achievement.getRemoteID());
            } else {
                Games.Achievements.increment(mGoogleApiClient, achievement.getRemoteID(), difference);
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
                            AlertHelper.error(callingActivity, String.format(ErrorHelper.get(ErrorHelper.Error.CLOUD_ERROR), e.getMessage()));
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
                    AlertHelper.info(callingActivity, Text.get("ALERT_CLOUD_LOADING"));
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
                String desc = String.format(Text.get("CLOUD_SAVE_DESC"),
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

    public static byte[] createBackup() {
        Gson gson = new Gson();
        String backupString;

        backupString = MainActivity.prefs.getInt("databaseVersion", PatchHelper.V1_0_0) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += PuzzleHelper.getTotalStars() + GooglePlayHelper.SAVE_DELIMITER;
        backupString += Statistic.getCurrency() + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Boost.listAll(Boost.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Pack.listAll(Pack.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Puzzle.listAll(Puzzle.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(PuzzleCustom.listAll(PuzzleCustom.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Setting.listAll(Setting.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Statistic.listAll(Statistic.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(Tile.listAll(Tile.class)) + GooglePlayHelper.SAVE_DELIMITER;
        backupString += gson.toJson(TileType.listAll(TileType.class)) + GooglePlayHelper.SAVE_DELIMITER;

        return backupString.getBytes();
    }

    public static void applyBackup(String backupData) {
        Gson gson = new Gson();

        String[] splitData = splitBackupData(backupData);
        MainActivity.prefs.edit().putInt("databaseVersion", Integer.parseInt(splitData[0])).apply();

        // Skipping [1] and [2], as they're used for comparing saves

        if (splitData.length > 3) {
            Boost[] boosts = gson.fromJson(splitData[3], Boost[].class);
            Boost.deleteAll(Boost.class);
            Boost.saveInTx(boosts);
        }

        if (splitData.length > 4) {
            Pack[] packs = gson.fromJson(splitData[4], Pack[].class);
            Pack.deleteAll(Pack.class);
            Pack.saveInTx(packs);
        }
        
        if (splitData.length > 5) {
            Puzzle[] puzzles = gson.fromJson(splitData[5], Puzzle[].class);
            Puzzle.deleteAll(Puzzle.class);
            Puzzle.saveInTx(puzzles);
        }
        
        if (splitData.length > 6) {
            PuzzleCustom[] puzzleCustoms = gson.fromJson(splitData[6], PuzzleCustom[].class);
            PuzzleCustom.deleteAll(PuzzleCustom.class);
            PuzzleCustom.saveInTx(puzzleCustoms);
        }
        
        if (splitData.length > 7) {
            Setting[] settings = gson.fromJson(splitData[7], Setting[].class);
            Setting.deleteAll(Setting.class);
            Setting.saveInTx(settings);
        }
        
        if (splitData.length > 8) {
            Statistic[] statistics = gson.fromJson(splitData[8], Statistic[].class);
            Statistic.deleteAll(Statistic.class);
            Statistic.saveInTx(statistics);
        }

        if (splitData.length > 9) {
            Tile[] tiles = gson.fromJson(splitData[9], Tile[].class);
            Tile.deleteAll(Tile.class);
            Tile.saveInTx(tiles);
        }

        if (splitData.length > 10) {
            TileType[] tileTypes = gson.fromJson(splitData[10], TileType[].class);
            TileType.deleteAll(TileType.class);
            TileType.saveInTx(tileTypes);
        }

        new PatchHelper(callingActivity, true).execute();

        if (callingActivity != null) {
            callingActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertHelper.success(callingActivity, Text.get("ALERT_CLOUD_LOADED"));
                }
            });
        }
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
}

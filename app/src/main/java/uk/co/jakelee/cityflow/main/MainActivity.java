package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.QuestUpdateListener;
import com.tapjoy.Tapjoy;

import hotchemi.android.rate.AppRate;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.PatchHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.helper.TextHelper;
import uk.co.jakelee.cityflow.model.Setting;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        QuestUpdateListener {
    public static SharedPreferences prefs;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);
        dh = DisplayHelper.getInstance(this);

        new PatchHelper(this, true).execute();

        ratingPrompt();

        GooglePlayHelper.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();
        tryGoogleLogin();

        Tapjoy.onActivityStart(this);

        if (Setting.getSafeBoolean(Constants.SETTING_MUSIC)) {
            SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
        }
    }

    public void tryGoogleLogin() {
        // If we've got all we need, and we need to sign in, or it is first run.
        if (GooglePlayHelper.AreGooglePlayServicesInstalled(this) &&
                !GooglePlayHelper.IsConnected() &&
                !GooglePlayHelper.mGoogleApiClient.isConnecting() &&
                (Setting.getSafeBoolean(Constants.SETTING_SIGN_IN) || prefs.getInt("databaseVersion", PatchHelper.NO_DATABASE) <= PatchHelper.NO_DATABASE)) {
            GooglePlayHelper.mGoogleApiClient.connect();
        }
    }

    private void ratingPrompt() {
        AppRate.with(this)
                .setInstallDays(3)
                .setLaunchTimes(3)
                .setRemindInterval(3)
                .setShowLaterButton(true)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        RelativeLayout container = (RelativeLayout) findViewById(R.id.carContainer);
        int numCars = container.getChildCount();
        for (int i = 0; i < numCars; i++) {
            container.getChildAt(i).clearAnimation();
        }
        container.removeAllViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        createAnimations();
        ((TextView) findViewById(R.id.languageFlag)).setText(TextHelper.getLanguageFlag(prefs.getInt("language", Constants.LANGUAGE_EN)));

        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
    }

    private void createAnimations() {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.carContainer);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int numCars = Setting.getInt(Constants.SETTING_MAX_CARS);
        for (int i = 0; i < numCars; i++) {
            dh.createCarAnimation(container, metrics);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Tapjoy.onActivityStop(this);
        SoundHelper.stopIfExiting(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (GooglePlayHelper.IsConnected()) {
            Games.Quests.registerQuestUpdateListener(GooglePlayHelper.mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        GooglePlayHelper.ConnectionFailed(this, connectionResult);
    }

    @Override
    public void onConnectionSuspended(int i) {
        GooglePlayHelper.mGoogleApiClient.connect();
    }

    public void onQuestCompleted(Quest quest) {
        AlertHelper.success(this, GooglePlayHelper.CompleteQuest(quest));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        GooglePlayHelper.ActivityResult(this, requestCode, resultCode);
    }

    public void openStory(View view) {
        startActivity(new Intent(this,
                StoryActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public void openCreator(View view) {
        startActivity(new Intent(this,
                CreatorActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public void openSettings(View view) {
        startActivity(new Intent(this,
                SettingsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public void openShop(View view) {
        startActivity(new Intent(this,
                ShopActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }

    public void openQuestMenu(View view) {
        startActivity(new Intent(this,
                QuestActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }
}

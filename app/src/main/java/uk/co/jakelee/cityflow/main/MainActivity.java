package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.QuestUpdateListener;
import com.tapjoy.Tapjoy;

import hotchemi.android.rate.AppRate;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DatabaseHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.model.Setting;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        QuestUpdateListener {
    public static SharedPreferences prefs;
    private GooglePlayHelper gph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);
        gph = new GooglePlayHelper();

        MainActivity.prefs.edit().putInt("language", Constants.LANGUAGE_EN_GB).apply();

        DatabaseHelper.handlePatches();

        ratingPrompt();

        GooglePlayHelper.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();
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
    protected void onStart() {
        super.onStart();

        if (!GooglePlayHelper.IsConnected() && !GooglePlayHelper.mGoogleApiClient.isConnecting() &&
                Setting.getSafeBoolean(Constants.SETTING_SIGN_IN) && GooglePlayHelper.AreGooglePlayServicesInstalled(this)) {
             GooglePlayHelper.mGoogleApiClient.connect();
        }

        Tapjoy.onActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Tapjoy.onActivityStop(this);
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
        // Show quest complete message
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        GooglePlayHelper.ActivityResult(this, requestCode, resultCode);
    }

    public void openChapters(View view) {
        Intent intent = new Intent(this, StoryActivity.class);
        startActivity(intent);
    }

    public void openCreator(View view) {
        Intent intent = new Intent(this, CreatorActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }
}

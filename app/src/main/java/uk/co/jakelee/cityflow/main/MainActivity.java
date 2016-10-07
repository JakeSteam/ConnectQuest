package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

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
import uk.co.jakelee.cityflow.model.Setting;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        QuestUpdateListener {
    public static SharedPreferences prefs;
    private GooglePlayHelper gph;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);
        gph = new GooglePlayHelper();
        dh = DisplayHelper.getInstance(this);

        new PatchHelper(this).execute();

        ratingPrompt();

        GooglePlayHelper.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();

        Tapjoy.onActivityStart(this);
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

        if (!GooglePlayHelper.IsConnected() && !GooglePlayHelper.mGoogleApiClient.isConnecting() &&
                Setting.getSafeBoolean(Constants.SETTING_SIGN_IN) && GooglePlayHelper.AreGooglePlayServicesInstalled(this)) {
            GooglePlayHelper.mGoogleApiClient.connect();
        }

        createAnimations();
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
        Intent intent = new Intent(this, StoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openCreator(View view) {
        Intent intent = new Intent(this, CreatorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openQuestMenu(View view) {
        Intent intent = new Intent(this, QuestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}

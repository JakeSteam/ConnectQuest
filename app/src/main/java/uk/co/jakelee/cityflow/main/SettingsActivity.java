package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.quest.Quests;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Text;

public class SettingsActivity extends Activity {
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dh = DisplayHelper.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateVisibilities();
        populateText();
        populateSettings();
    }

    public void updateVisibilities() {
        boolean isConnected = GooglePlayHelper.IsConnected();
        findViewById(R.id.signInButton).setVisibility(isConnected ? View.GONE : View.VISIBLE);
        findViewById(R.id.signOutButton).setVisibility(isConnected ? View.VISIBLE : View.GONE);
        findViewById(R.id.googlePlayFeatureButtons).setVisibility(isConnected ? View.VISIBLE : View.GONE);

        findViewById(R.id.maxCarsContainer).setVisibility(ShopItem.isPurchased(Constants.ITEM_MAX_CARS) ? View.VISIBLE : View.GONE);
    }

    public void populateText() {
        ((TextView) findViewById(R.id.settingSectionAudio)).setText(Text.get("SETTING_SECTION_AUDIO"));
        ((TextView) findViewById(R.id.soundToggleText)).setText(Text.get("SETTING_2_NAME"));
        ((TextView) findViewById(R.id.musicToggleText)).setText(Text.get("SETTING_1_NAME"));

        ((TextView) findViewById(R.id.settingSectionGameplay)).setText(Text.get("SETTING_SECTION_GAMEPLAY"));
        ((TextView) findViewById(R.id.zenToggleText)).setText(Text.get("SETTING_5_NAME"));
        ((TextView) findViewById(R.id.hideBoostText)).setText(Text.get("SETTING_6_NAME"));
        ((TextView) findViewById(R.id.playerNameText)).setText(Text.get("SETTING_7_NAME"));
        ((TextView) findViewById(R.id.minZoomText)).setText(Text.get("SETTING_3_NAME"));
        ((TextView) findViewById(R.id.maxZoomText)).setText(Text.get("SETTING_4_NAME"));
        ((TextView) findViewById(R.id.maxCarsText)).setText(Text.get("SETTING_9_NAME"));

        ((TextView) findViewById(R.id.settingSectionGoogle)).setText(Text.get("SETTING_SECTION_GOOGLE"));
        ((TextView) findViewById(R.id.signInButton)).setText(Text.get("GOOGLE_SIGN_IN"));
        ((TextView) findViewById(R.id.signOutButton)).setText(Text.get("GOOGLE_SIGN_OUT"));

        ((TextView) findViewById(R.id.settingSectionOther)).setText(Text.get("SETTING_SECTION_OTHER"));
        ((TextView) findViewById(R.id.supportCodeButton)).setText(Text.get("DIALOG_SUPPORT_CODE"));
        ((TextView) findViewById(R.id.creditsButton)).setText(Text.get("DIALOG_CREDITS"));
    }

    public void populateSettings() {
        Drawable tick = dh.createDrawable(R.drawable.ui_level_unselected_completed, 50, 50);
        Drawable cross = dh.createDrawable(R.drawable.ui_level_unselected, 50, 50);

        // Sound settings
        ImageView musicToggle = (ImageView) findViewById(R.id.musicToggleButton);
        boolean musicToggleValue = Setting.getSafeBoolean(Constants.SETTING_MUSIC);
        musicToggle.setImageDrawable(musicToggleValue ? tick : cross);

        ImageView soundToggle = (ImageView) findViewById(R.id.soundToggleButton);
        boolean soundToggleValue = Setting.getSafeBoolean(Constants.SETTING_SOUNDS);
        soundToggle.setImageDrawable(soundToggleValue ? tick : cross);

        // Gameplay settings
        ImageView zenToggle = (ImageView) findViewById(R.id.zenToggleButton);
        boolean zenToggleValue = Setting.getSafeBoolean(Constants.SETTING_ZEN_MODE);
        zenToggle.setImageDrawable(zenToggleValue ? tick : cross);

        ImageView hideBoostsToggle = (ImageView) findViewById(R.id.hideBoostButton);
        boolean hideBoostsToggleValue = Setting.getSafeBoolean(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS);
        hideBoostsToggle.setImageDrawable(hideBoostsToggleValue ? tick : cross);

        TextView playerName = (TextView) findViewById(R.id.playerNameDisplay);
        playerName.setText(Setting.getString(Constants.SETTING_PLAYER_NAME));

        TextView minZoom = (TextView) findViewById(R.id.minZoomButton);
        minZoom.setText(String.format("%.2f", Setting.getFloat(Constants.SETTING_MIN_ZOOM)));

        TextView maxZoom = (TextView) findViewById(R.id.maxZoomButton);
        maxZoom.setText(String.format("%.2f", Setting.getFloat(Constants.SETTING_MAX_ZOOM)));

        TextView maxCars = (TextView) findViewById(R.id.maxCarsButton);
        maxCars.setText(Integer.toString(Setting.getInt(Constants.SETTING_MAX_CARS)));
    }

    public void toggleSetting(View v) {
        int settingID = 0;
        switch (v.getId()) {
            case R.id.soundToggleButton:
                settingID = Constants.SETTING_SOUNDS;
                break;
            case R.id.musicToggleButton:
                settingID = Constants.SETTING_MUSIC;
                break;
            case R.id.zenToggleButton:
                settingID = Constants.SETTING_ZEN_MODE;
                break;
            case R.id.hideBoostButton:
                settingID = Constants.SETTING_HIDE_UNSTOCKED_BOOSTS;
                break;
        }

        if (settingID > 0) {
            Setting settingToToggle = Setting.findById(Setting.class, settingID);
            settingToToggle.setBooleanValue(!settingToToggle.getBooleanValue());
            settingToToggle.save();

            AlertHelper.success(this, String.format(Text.get(settingToToggle.getBooleanValue() ? "ALERT_SETTING_TOGGLE_ON" : "ALERT_SETTING_TOGGLE_OFF"),
                    settingToToggle.getName()));

            populateSettings();
        }
    }

    public void changeText(View v) {
        switch (v.getId()) {
            case R.id.playerNameDisplay:
                AlertDialogHelper.changeSettingText(this, Constants.SETTING_PLAYER_NAME);
                break;
        }
    }

    public void changeFloat(View v) {
        AlertDialogHelper.changeSettingFloat(this, Integer.parseInt((String)v.getTag()));
    }

    public void changeInt(View v) {
        AlertDialogHelper.changeSettingInt(this, Integer.parseInt((String)v.getTag()));
    }

    public void openSupportCode(View v) {
        AlertDialogHelper.enterSupportCode(getApplicationContext(), this);
    }

    public void openCredits(View v) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        AlertHelper.info(this, Text.get("ALERT_CLOUD_BEGINNING"));
        GooglePlayHelper.SavedGamesIntent(getApplicationContext(), this, intent);
    }

    public void openAchievements(View v) {
        if (GooglePlayHelper.IsConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(GooglePlayHelper.mGoogleApiClient), GooglePlayHelper.RC_ACHIEVEMENTS);
        }
    }

    public void openCloudSaves(View v) {
        if (GooglePlayHelper.IsConnected()) {
            Intent savedGamesIntent = Games.Snapshots.getSelectSnapshotIntent(GooglePlayHelper.mGoogleApiClient,
                    "Cloud Saves", true, true, 1);
            startActivityForResult(savedGamesIntent, GooglePlayHelper.RC_SAVED_GAMES);
        }
    }

    public void openQuests(View v) {
        if (GooglePlayHelper.IsConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, Quests.SELECT_ALL_QUESTS), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void openLeaderboards(View v) {
        if (GooglePlayHelper.IsConnected()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(GooglePlayHelper.mGoogleApiClient), GooglePlayHelper.RC_LEADERBOARDS);
        }
    }

    public void signIn(View v) {
        if (GooglePlayHelper.mGoogleApiClient.isConnecting()) {
            return;
        }
        GooglePlayHelper.mGoogleApiClient.connect();

        Setting signIn = Setting.findById(Setting.class, Constants.SETTING_SIGN_IN);
        signIn.setBooleanValue(true);
        signIn.save();
    }

    public void signOut(View v) {
        if (!GooglePlayHelper.mGoogleApiClient.isConnected()) {
            return;
        }

        Games.signOut(GooglePlayHelper.mGoogleApiClient);
        GooglePlayHelper.mGoogleApiClient.disconnect();

        Setting signIn = Setting.findById(Setting.class, Constants.SETTING_SIGN_IN);
        signIn.setBooleanValue(false);
        signIn.save();
    }
}

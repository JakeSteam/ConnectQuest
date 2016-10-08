package uk.co.jakelee.cityflow.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aitorvs.android.allowme.AllowMeActivity;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.quest.Quests;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.PermissionHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;

import static uk.co.jakelee.cityflow.main.MainActivity.prefs;

public class SettingsActivity extends AllowMeActivity {
    private boolean initialisedSpinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        findViewById(R.id.autosaveRow).setVisibility(isConnected ? View.VISIBLE : View.GONE);

        findViewById(R.id.maxCarsContainer).setVisibility(ShopItem.isPurchased(Constants.ITEM_MAX_CARS) ? View.VISIBLE : View.GONE);
    }

    public void populateText() {
        ((TextView) findViewById(R.id.settingSectionAudio)).setText(Text.get("SETTING_SECTION_AUDIO"));
        ((TextView) findViewById(R.id.soundToggleText)).setText(Text.get("SETTING_2_NAME"));
        ((TextView) findViewById(R.id.musicToggleText)).setText(Text.get("SETTING_1_NAME"));

        ((TextView) findViewById(R.id.settingSectionGameplay)).setText(Text.get("SETTING_SECTION_GAMEPLAY"));
        ((TextView) findViewById(R.id.languagePickerText)).setText(Text.get("SETTING_12_NAME"));
        ((TextView) findViewById(R.id.backgroundPickerText)).setText(Text.get("SETTING_10_NAME"));
        ((TextView) findViewById(R.id.zenToggleText)).setText(Text.get("SETTING_5_NAME"));
        ((TextView) findViewById(R.id.hideBoostText)).setText(Text.get("SETTING_6_NAME"));
        ((TextView) findViewById(R.id.playerNameText)).setText(Text.get("SETTING_7_NAME"));
        ((TextView) findViewById(R.id.minZoomText)).setText(Text.get("SETTING_3_NAME"));
        ((TextView) findViewById(R.id.maxZoomText)).setText(Text.get("SETTING_4_NAME"));
        ((TextView) findViewById(R.id.vibrationToggleText)).setText(Text.get("SETTING_13_NAME"));
        ((TextView) findViewById(R.id.maxCarsText)).setText(Text.get("SETTING_9_NAME"));

        ((TextView) findViewById(R.id.settingSectionGoogle)).setText(Text.get("SETTING_SECTION_GOOGLE"));
        ((TextView) findViewById(R.id.signInButton)).setText(Text.get("GOOGLE_SIGN_IN"));
        ((TextView) findViewById(R.id.signOutButton)).setText(Text.get("GOOGLE_SIGN_OUT"));
        ((TextView) findViewById(R.id.autosaveText)).setText(Text.get("SETTING_11_NAME"));
        ((TextView) findViewById(R.id.lastAutosaveText)).setText(Text.get("STATISTIC_11_NAME"));

        ((TextView) findViewById(R.id.settingSectionOther)).setText(Text.get("SETTING_SECTION_OTHER"));
        ((TextView) findViewById(R.id.creditsButton)).setText(Text.get("DIALOG_CREDITS"));
        ((TextView) findViewById(R.id.statisticsButton)).setText(Text.get("DIALOG_STATISTICS"));
        ((TextView) findViewById(R.id.supportCodeButton)).setText(Text.get("DIALOG_SUPPORT_CODE"));
    }

    public void populateSettings() {
        // Sound settings
        ((TextView) findViewById(R.id.musicToggleButton)).setText(Setting.getSafeBoolean(Constants.SETTING_MUSIC) ? R.string.icon_tick : R.string.icon_cross);
        ((TextView) findViewById(R.id.musicToggleButton)).setTextColor(ContextCompat.getColor(this, Setting.getSafeBoolean(Constants.SETTING_MUSIC) ? R.color.green : R.color.red));

        ((TextView) findViewById(R.id.soundToggleButton)).setText(Setting.getSafeBoolean(Constants.SETTING_SOUNDS) ? R.string.icon_tick : R.string.icon_cross);
        ((TextView) findViewById(R.id.soundToggleButton)).setTextColor(ContextCompat.getColor(this, Setting.getSafeBoolean(Constants.SETTING_SOUNDS) ? R.color.green : R.color.red));

        // Gameplay settings
        Background background = Background.getActiveBackground();
        ((TextView)findViewById(R.id.backgroundPickerButton)).setText(background.getName());
        findViewById(R.id.backgroundPickerButton).setBackgroundColor(background.getBackgroundColour());

        ((TextView) findViewById(R.id.zenToggleButton)).setText(Setting.getSafeBoolean(Constants.SETTING_ZEN_MODE) ? R.string.icon_tick : R.string.icon_cross);
        ((TextView) findViewById(R.id.zenToggleButton)).setTextColor(ContextCompat.getColor(this, Setting.getSafeBoolean(Constants.SETTING_ZEN_MODE) ? R.color.green : R.color.red));

        ((TextView) findViewById(R.id.hideBoostButton)).setText(Setting.getSafeBoolean(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS) ? R.string.icon_tick : R.string.icon_cross);
        ((TextView) findViewById(R.id.hideBoostButton)).setTextColor(ContextCompat.getColor(this, Setting.getSafeBoolean(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS) ? R.color.green : R.color.red));

        ((TextView) findViewById(R.id.vibrationToggleButton)).setText(Setting.getSafeBoolean(Constants.SETTING_VIBRATION) ? R.string.icon_tick : R.string.icon_cross);
        ((TextView) findViewById(R.id.vibrationToggleButton)).setTextColor(ContextCompat.getColor(this, Setting.getSafeBoolean(Constants.SETTING_VIBRATION) ? R.color.green : R.color.red));

        ((TextView) findViewById(R.id.playerNameDisplay)).setText(Setting.getString(Constants.SETTING_PLAYER_NAME));
        ((TextView) findViewById(R.id.minZoomButton)).setText(String.format("%.2f", Setting.getFloat(Constants.SETTING_MIN_ZOOM)));
        ((TextView) findViewById(R.id.maxZoomButton)).setText(String.format("%.2f", Setting.getFloat(Constants.SETTING_MAX_ZOOM)));
        ((TextView) findViewById(R.id.maxCarsButton)).setText(Integer.toString(Setting.getInt(Constants.SETTING_MAX_CARS)));

        // Google Play settings
        ((TextView) findViewById(R.id.autosaveDisplay)).setText(Integer.toString(Setting.getInt(Constants.SETTING_AUTOSAVE_FREQUENCY)));
        ((TextView) findViewById(R.id.lastAutosaveDisplay)).setText(DateHelper.displayTime(Statistic.find(Constants.STATISTIC_LAST_AUTOSAVE).getLongValue(), DateHelper.datetime));

        createDropdowns();
    }

    private void createDropdowns() {
        int numLanguages = (Constants.LANGUAGE_MAX - Constants.LANGUAGE_MIN) + 1;
        ArrayAdapter<String> envAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner);
        envAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        for (int i = 1; i <= numLanguages; i++) {
            envAdapter.add(Text.get("LANGUAGE_" + i + "_NAME"));
        }

        final Spinner spinner = (Spinner)findViewById(R.id.languagePicker);
        int setting = Setting.get(Constants.SETTING_LANGUAGE).getIntValue();
        spinner.setAdapter(envAdapter);
        spinner.setSelection(setting - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!initialisedSpinner) {
                    initialisedSpinner = true;
                } else {
                    Setting setting = Setting.get(Constants.SETTING_LANGUAGE);
                    setting.setIntValue(position + 1);
                    setting.save();
                    prefs.edit().putInt("language", Setting.get(Constants.SETTING_LANGUAGE).getIntValue()).apply();

                    populateText();
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    public void toggleSetting(View v) {
        int settingID = 0;
        switch (v.getId()) {
            case R.id.soundToggleButton:
                toggleSetting(Constants.SETTING_SOUNDS);
                break;
            case R.id.musicToggleButton:
                toggleSetting(Constants.SETTING_MUSIC);
                break;
            case R.id.zenToggleButton:
                toggleSetting(Constants.SETTING_ZEN_MODE);
                break;
            case R.id.hideBoostButton:
                toggleSetting(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS);
                break;
            case R.id.vibrationToggleButton:
                PermissionHelper.runIfPossible(Manifest.permission.VIBRATE, new Runnable() {
                    @Override
                    public void run() {
                        toggleSetting(Constants.SETTING_VIBRATION);
                    }
                });
                break;
        }
    }

    private void toggleSetting(int settingID) {
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
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openStatistics(View v) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void openBackgroundPicker(View v) {
        Intent intent = new Intent(getApplicationContext(), BackgroundPickerActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent != null) {
            AlertHelper.info(this, Text.get("ALERT_CLOUD_BEGINNING"));
            GooglePlayHelper.SavedGamesIntent(getApplicationContext(), this, intent);
        }
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
        if (GooglePlayHelper.mGoogleApiClient.isConnecting() || GooglePlayHelper.mGoogleApiClient.isConnected()) {
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

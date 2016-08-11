package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertDialogHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Setting;

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

        populateSettings();
    }

    public void populateSettings() {
        Drawable tick = dh.createDrawable(R.drawable.ui_level_unselected_completed, 50, 50);
        Drawable cross = dh.createDrawable(R.drawable.ui_level_locked, 50, 50);

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

            populateSettings();
        }
    }

    public void changeText(View v) {
        int settingID = 0;
        switch (v.getId()) {
            case R.id.playerNameDisplay:
                settingID = Constants.SETTING_PLAYER_NAME;
                break;
        }

        if (settingID > 0) {
            AlertDialogHelper.changePlayerName(this, "Change Player Name:", settingID);

            populateSettings();
        }
    }
}

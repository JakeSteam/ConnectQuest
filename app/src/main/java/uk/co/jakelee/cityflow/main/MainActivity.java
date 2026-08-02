package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hotchemi.android.rate.AppRate;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.PatchHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.helper.TextHelper;
import uk.co.jakelee.cityflow.model.Setting;

public class MainActivity extends Activity {
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

        GooglePlayHelper.initialise(this);

        if (Setting.getSafeBoolean(Constants.SETTING_MUSIC)) {
            SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
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

        // Sign-in can complete while the game is backgrounded, so re-read it rather than trusting
        // whatever the launch-time check saw.
        GooglePlayHelper.refreshAuthentication(this);

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

        SoundHelper.stopIfExiting(this);
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

}

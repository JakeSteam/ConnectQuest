package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Setting;

public class SoundHelper {
    private static SoundHelper soundHelper = null;
    private final Context context;

    private MediaPlayer mediaPlayer;
    public enum SOUNDS {purchasing, rotating, settings};
    public static final int[] purchasingSounds = {R.raw.purchase1, R.raw.purchase2};
    public static final int[] rotatingSounds = {R.raw.click1, R.raw.click2, R.raw.click3, R.raw.click4, R.raw.click5};
    public static final int[] settingSounds = {R.raw.setting1, R.raw.setting2};

    private SoundHelper(Context context) {
        this.context = context;
    }

    public static SoundHelper getInstance(Context ctx) {
        if (soundHelper == null) {
            soundHelper = new SoundHelper(ctx.getApplicationContext());
        }
        return soundHelper;
    }

    public void playSound(SOUNDS soundType) {
        int settingId = 0;
        int[] sounds = new int[]{};
        switch (soundType) {
            case purchasing:
                sounds = purchasingSounds;
                settingId = Constants.SETTING_SOUND_PURCHASING;
                break;
            case rotating:
                sounds = rotatingSounds;
                settingId = Constants.SETTING_SOUND_ROTATING;
                break;
            case settings:
                sounds = settingSounds;
                settingId = Constants.SETTING_SOUND_SETTINGS;
                break;
        }

        Setting selectedSound = Setting.get(settingId);
        if (selectedSound.getIntValue() > 0) {
            playSound(sounds[selectedSound.getIntValue() - 1]);
        } else {
            playSound(sounds[new Random().nextInt(sounds.length)]);
        }
    }

    private void playSound(int soundID) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, soundID);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Setting;

public class SoundHelper {
    public enum SOUNDS {purchasing, rotating};
    public static final int[] purchasingSounds = {R.raw.purchase1, R.raw.purchase2};
    public static final int[] rotatingSounds = {R.raw.click1, R.raw.click2, R.raw.click3, R.raw.click4};

    public static void playSound(Context context, SOUNDS soundType) {
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
        }

        Setting selectedSound = Setting.get(settingId);
        if (selectedSound.getIntValue() > 0) {
            playSound(context, sounds[selectedSound.getIntValue() - 1]);
        } else {
            playSound(context, sounds[new Random().nextInt(sounds.length)]);
        }
    }

    private static void playSound(Context context, int soundID) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, soundID);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.d("CityFlow", e.toString());
        }
    }
}
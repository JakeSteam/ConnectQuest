package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Setting;

public class SoundHelper {
    public static final int[] mainSongs = {R.raw.main_carefree, R.raw.main_carpe_diem, R.raw.main_rainbows};
    public static final int[] puzzleSongs = {R.raw.puzzle_faceoff, R.raw.puzzle_ghost_dance, R.raw.puzzle_dreamy_flashback, R.raw.puzzle_bright_wish};
    public static final int[] purchasingSounds = {R.raw.purchase1, R.raw.purchase2};
    public static final int[] rotatingSounds = {R.raw.click1, R.raw.click2, R.raw.click3, R.raw.click4, R.raw.click5, R.raw.click6, R.raw.click7, R.raw.click8, R.raw.click9, R.raw.click10};
    public static final int[] settingSounds = {R.raw.setting1, R.raw.setting2};
    private static SoundHelper soundHelper = null;
    private static boolean keepPlayingMusic = false;
    private final Context context;
    private AUDIO currentTrack;
    private MediaPlayer soundPlayer;
    private MediaPlayer songPlayer;

    private SoundHelper(Context context) {
        this.context = context;
    }

    public static SoundHelper getInstance(Context ctx) {
        if (soundHelper == null) {
            soundHelper = new SoundHelper(ctx.getApplicationContext());
        }
        return soundHelper;
    }

    public static void stopIfExiting(Activity activity) {
        if (!SoundHelper.keepPlayingMusic) {
            SoundHelper.getInstance(activity).stopAudio(true);
        }
        SoundHelper.keepPlayingMusic = false;
    }

    public void playSound(AUDIO audioType) {
        boolean isMusic = false;
        int settingId = 0;
        int[] sounds = new int[]{};
        switch (audioType) {
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
            case main:
                sounds = mainSongs;
                settingId = Constants.SETTING_SONG_MAIN;
                isMusic = true;
                break;
            case puzzle:
                sounds = puzzleSongs;
                settingId = Constants.SETTING_SONG_PUZZLE;
                isMusic = true;
                break;
        }

        Setting selectedAudio = Setting.get(settingId);
        if (selectedAudio != null && selectedAudio.getIntValue() > 0) {
            playSound(sounds[selectedAudio.getIntValue() - 1], isMusic);
        } else {
            playSound(sounds[new Random().nextInt(sounds.length)], isMusic);
        }
    }

    private void playSound(int soundID, boolean isMusic) {
        if (isMusic && Setting.getSafeBoolean(Constants.SETTING_MUSIC)) {
            stopAudio(true);
            songPlayer = MediaPlayer.create(context, soundID);
            if (songPlayer != null) {
                songPlayer.start();
                songPlayer.setLooping(true);
            }
        } else if (!isMusic && Setting.getSafeBoolean(Constants.SETTING_SOUNDS)) {
            stopAudio(false);
            soundPlayer = MediaPlayer.create(context, soundID);
            if (soundPlayer != null) {
                soundPlayer.start();
            }
        }
    }

    public void stopAudio(boolean stopMusic) {
        if (stopMusic && songPlayer != null) {
            songPlayer.pause();
        }
        if (!stopMusic && soundPlayer != null) {
            soundPlayer.release();
        }
    }

    public void playOrResumeMusic(AUDIO musicRequest) {
        if (Setting.getSafeBoolean(Constants.SETTING_MUSIC)) {
            if (songPlayer != null && currentTrack != null && musicRequest == currentTrack) {
                // If we're already playing that track
                songPlayer.start();
            } else {
                // Change track / start a song
                playSound(musicRequest);
            }
            currentTrack = musicRequest;
            SoundHelper.keepPlayingMusic = true;
        } else {
            stopAudio(true);
        }
    }

    public enum AUDIO {purchasing, rotating, settings, main, puzzle}
}
package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Setting;

public class MusicHelper {
    private static MusicHelper mediaHelper = null;
    private final Context context;

    private MediaPlayer mediaPlayer;
    public enum SONG {main, puzzle};
    public static final int[] mainSongs = {R.raw.Main_Carefree, R.raw.Main_Carpe_Diem, R.raw.Main_Rainbows};
    public static final int[] puzzleSongs = {R.raw.Puzzle_Faceoff, R.raw.Puzzle_Ghost_Dance, R.raw.Puzzle_Crypto};

    private MusicHelper(Context context) {
        this.context = context;
    }

    public static MusicHelper getInstance(Context ctx) {
        if (mediaHelper == null) {
            mediaHelper = new MusicHelper(ctx.getApplicationContext());
        }
        return mediaHelper;
    }

    public void playMusic(SONG songType) {
        int settingId = 0;
        int[] songs = new int[]{};
        switch (songType) {
            case main:
                songs = mainSongs;
                settingId = Constants.SETTING_SONG_MAIN;
                break;
            case puzzle:
                songs = puzzleSongs;
                settingId = Constants.SETTING_SONG_PUZZLE;
                break;
        }

        Setting selectedSong = Setting.get(settingId);
        if (selectedSong.getIntValue() > 0) {
            playMusic(songs[selectedSong.getIntValue() - 1]);
        } else {
            playMusic(songs[new Random().nextInt(songs.length)]);
        }
    }

    private void playMusic(int songID) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, songID);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
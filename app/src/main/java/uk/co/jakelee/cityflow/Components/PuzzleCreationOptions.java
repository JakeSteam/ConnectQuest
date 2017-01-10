package uk.co.jakelee.cityflow.components;


import android.app.Activity;
import android.content.SharedPreferences;

import uk.co.jakelee.cityflow.helper.Constants;

import static android.content.Context.MODE_PRIVATE;

public class PuzzleCreationOptions {
    private int x;
    private int y;
    private int environmentId;
    private boolean emptyPuzzle;
    private boolean shuffleAndPlay;
    private boolean deleteAfterPlay;
    private SharedPreferences prefs;

    public PuzzleCreationOptions(Activity activity) {
        prefs = activity.getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);
        x = prefs.getInt("puzzleOptions-x", Constants.PUZZLE_X_DEFAULT);
        y = prefs.getInt("puzzleOptions-y", Constants.PUZZLE_Y_DEFAULT);
        environmentId = prefs.getInt("puzzleOptions-environment", Constants.ENVIRONMENT_GRASS);
        emptyPuzzle = prefs.getBoolean("puzzleOptions-empty", false);
        shuffleAndPlay = prefs.getBoolean("puzzleOptions-shuffle", false);
        deleteAfterPlay = prefs.getBoolean("puzzleOptions-delete", false);
    }

    public void save() {
        prefs.edit()
                .putInt("puzzleOptions-x", x)
                .putInt("puzzleOptions-y", y)
                .putInt("puzzleOptions-environment", environmentId)
                .putBoolean("puzzleOptions-empty", emptyPuzzle)
                .putBoolean("puzzleOptions-shuffle", shuffleAndPlay)
                .putBoolean("puzzleOptions-delete", deleteAfterPlay)
                .apply();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    public boolean isEmptyPuzzle() {
        return emptyPuzzle;
    }

    public void setEmptyPuzzle(boolean emptyPuzzle) {
        this.emptyPuzzle = emptyPuzzle;
    }

    public boolean isShuffleAndPlay() {
        return shuffleAndPlay;
    }

    public void setShuffleAndPlay(boolean shuffleAndPlay) {
        this.shuffleAndPlay = shuffleAndPlay;
    }

    public boolean isDeleteAfterPlay() {
        return deleteAfterPlay;
    }

    public void setDeleteAfterPlay(boolean deleteAfterPlay) {
        this.deleteAfterPlay = deleteAfterPlay;
    }
}

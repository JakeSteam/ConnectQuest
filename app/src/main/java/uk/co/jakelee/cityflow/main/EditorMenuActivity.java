package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Text;

public class EditorMenuActivity extends Activity {
    public final static int PLAY = 1;
    public final static int SHUFFLE = 2;
    public final static int CHANGE_NAME = 3;
    public final static int CHANGE_DESC = 4;
    public final static int CHANGE_SIZE = 5;
    public final static int SAVE = 6;
    public final static int ROTATE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_menu);
        SoundHelper.keepPlayingMusic = true;
        populateText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateText() {
        ((TextView)findViewById(R.id.playPuzzle)).setText(Text.get("DIALOG_PLAY"));
        ((TextView)findViewById(R.id.changePuzzleName)).setText(Text.get("DIALOG_CHANGE_NAME"));
        ((TextView)findViewById(R.id.changePuzzleDesc)).setText(Text.get("DIALOG_CHANGE_DESC"));
        ((TextView)findViewById(R.id.shufflePuzzle)).setText(Text.get("DIALOG_SHUFFLE_TILES"));
        ((TextView)findViewById(R.id.rotatePuzzle)).setText(Text.get("DIALOG_ROTATE_PUZZLE"));
        ((TextView)findViewById(R.id.changePuzzleSize)).setText(Text.get("DIALOG_BUTTON_RESIZE"));
        ((TextView)findViewById(R.id.savePuzzle)).setText(Text.get("DIALOG_SAVE_EXIT"));
    }

    public void playPuzzle(View v) {
        returnResult(PLAY);
    }

    public void shufflePuzzle(View v) {
        returnResult(SHUFFLE);
    }

    public void rotatePuzzle(View v) {
        returnResult(ROTATE);
    }

    public void changePuzzleName(View v) {
        returnResult(CHANGE_NAME);
    }

    public void changePuzzleDesc(View v) {
        returnResult(CHANGE_DESC);
    }

    public void changePuzzleSize(View v) {
        returnResult(CHANGE_SIZE);
    }

    public void savePuzzle(View v) {
        returnResult(SAVE);
    }

    private void returnResult(int action) {
        Intent returnIntent = new Intent();
        setResult(action, returnIntent);
        finish();
    }

    public void closePopup (View v) {
        this.finish();
    }
}

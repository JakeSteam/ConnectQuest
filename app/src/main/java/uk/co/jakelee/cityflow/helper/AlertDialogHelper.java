package uk.co.jakelee.cityflow.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.CustomInfoActivity;
import uk.co.jakelee.cityflow.main.SettingsActivity;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;

public class AlertDialogHelper {
    public static void changePlayerName(final SettingsActivity activity, final String questionText, final int settingId) {
        final EditText playerNameBox = new EditText(activity.getApplicationContext());
        playerNameBox.setText(Setting.getString(Constants.SETTING_PLAYER_NAME));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(questionText);
        alertDialog.setView(playerNameBox);

        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Setting settingToToggle = Setting.findById(Setting.class, settingId);
                settingToToggle.setStringValue(playerNameBox.getText().toString().trim());
                settingToToggle.save();
                activity.populateSettings();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void changePuzzleInfo(final CustomInfoActivity activity, final PuzzleCustom puzzleCustom, final boolean changeDesc) {
        final EditText puzzleInfoInput = new EditText(activity.getApplicationContext());
        puzzleInfoInput.setText(changeDesc ? puzzleCustom.getDescription() : puzzleCustom.getName());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(changeDesc ? "Enter Puzzle Description:" : "Enter Puzzle Name:");
        alertDialog.setView(puzzleInfoInput);

        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String newString = puzzleInfoInput.getText().toString().trim();
                if (changeDesc) {
                    puzzleCustom.setDescription(newString);
                } else {
                    puzzleCustom.setName(newString);
                }
                puzzleCustom.save();
                activity.redisplayInfo();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}

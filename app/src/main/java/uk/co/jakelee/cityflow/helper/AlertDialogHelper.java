package uk.co.jakelee.cityflow.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import uk.co.jakelee.cityflow.main.SettingsActivity;
import uk.co.jakelee.cityflow.model.Setting;

public class AlertDialogHelper {
    public static void changePlayerName(final Context context, final SettingsActivity activity, final String questionText, final int settingId) {
        final EditText playerNameBox = new EditText(context);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
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
}

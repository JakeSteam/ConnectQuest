package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

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

    public static void displaySlider(Activity activity, int settingId) {
        final Setting setting = Setting.get(settingId);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_slider);
        dialog.setTitle("Set size!");
        dialog.setCancelable(true);
        dialog.show();

        TextView settingName = (TextView)dialog.findViewById(R.id.settingName);
        settingName.setText(setting.getName());

        final TextView currentValue = (TextView)dialog.findViewById(R.id.currentValue);
        currentValue.setText(setting.getFloatValue() + "");

        final TextView minValue = (TextView)dialog.findViewById(R.id.minValue);
        minValue.setText(setting.getFloatMin() + "");

        final TextView maxValue = (TextView)dialog.findViewById(R.id.maxValue);
        maxValue.setText(setting.getFloatMax() + "");

        final SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seekbar);
        seekbar.setProgress(getProgressFromFloat(setting.getFloatValue(), setting.getFloatMin(), setting.getFloatMax()));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                currentValue.setText(String.format("%.2f",
                        getFloatFromProgress(seekbar.getProgress(), setting.getFloatMin(), setting.getFloatMax())));
            }
        });

        final TextView saveValue = (TextView) dialog.findViewById(R.id.saveValue);
        saveValue.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                float adjustedValue = getFloatFromProgress(seekbar.getProgress(), setting.getFloatMin(), setting.getFloatMax());
                setting.setFloatValue(adjustedValue);
                setting.save();
                dialog.dismiss();
            }
        });
    }

    public static float getFloatFromProgress(int value, float min, float max) {
        return min + (((max - min) * value) / 100);
    }

    public static int getProgressFromFloat(float value, float min, float max) {
        return (int) Math.ceil(((value - min) / (max - min)) * 100);

    }
}

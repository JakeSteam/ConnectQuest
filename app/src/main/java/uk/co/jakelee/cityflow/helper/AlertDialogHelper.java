package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.CreatorActivity;
import uk.co.jakelee.cityflow.main.CustomInfoActivity;
import uk.co.jakelee.cityflow.main.SettingsActivity;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Text;

public class AlertDialogHelper {
    public static void confirmCloudLoad(final Activity activity, int localStars, int localPuzzles, int cloudStars, int cloudPuzzles) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(String.format(Text.get("DIALOG_CLOUD_LOAD_CONFIRM"),
                localStars,
                localPuzzles,
                cloudStars,
                cloudPuzzles));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_LOAD"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GooglePlayHelper.forceLoadFromCloud();
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });
    }

    public static void confirmCloudSave(final Activity activity, String desc, long saveTime, String deviceName) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(String.format(Text.get("DIALOG_CLOUD_SAVE_CONFIRM"),
                desc,
                DateHelper.displayTime(saveTime, DateHelper.datetime),
                deviceName));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_SAVE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GooglePlayHelper.forceSaveToCloud();
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });
    }

    public static void enterSupportCode(final Context context, final Activity activity) {
        final EditText supportCodeBox = new EditText(context);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(Text.get("DIALOG_SUPPORT_CODE"));
        alertDialog.setView(supportCodeBox);

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_CONFIRM"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //String supportCode = EncryptHelper.encode("1571111687000|UPDATE setting SET boolean_value = 1");
                String supportCode = supportCodeBox.getText().toString().trim();
                if (EncryptHelper.applyCode(supportCode)) {
                    Crouton.showText(activity, Text.get("SUCCESS_SUPPORT_CODE"), StyleHelper.SUCCESS);
                } else {
                    Crouton.showText(activity, Text.get("ERROR_SUPPORT_CODE_INVALID"), StyleHelper.ERROR);
                }
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final Dialog dialog = alertDialog.create();
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        dialog.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static void changeSettingText(final SettingsActivity activity, final int settingId) {
        final Setting settingToToggle = Setting.findById(Setting.class, settingId);
        final EditText editText = new EditText(activity.getApplicationContext());
        editText.setText(Setting.getString(Constants.SETTING_PLAYER_NAME));
        editText.setFilters(new InputFilter[]{ FilterHelper.playerName });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(String.format(Text.get("DIALOG_CHANGE_TEXT"), settingToToggle.getName(), Constants.PLAYER_NAME_MAX_LENGTH));
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_CHANGE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                settingToToggle.setStringValue(editText.getText().toString().trim());
                settingToToggle.save();
                activity.populateSettings();
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void changePuzzleInfo(final CustomInfoActivity activity, final PuzzleCustom puzzleCustom, final boolean changeDesc) {
        final EditText puzzleInfoInput = new EditText(activity.getApplicationContext());
        puzzleInfoInput.setText(changeDesc ? puzzleCustom.getDescription() : puzzleCustom.getName());
        puzzleInfoInput.setFilters(new InputFilter[]{ changeDesc ? FilterHelper.puzzleDesc : FilterHelper.puzzleName });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog);
        alertDialog.setMessage(String.format(Text.get("DIALOG_CHANGE_TEXT"),
                changeDesc ? Text.get("WORD_DESCRIPTION") : Text.get("WORD_NAME"),
                changeDesc ? Constants.PUZZLE_DESC_MAX_LENGTH : Constants.PUZZLE_NAME_MAX_LENGTH));
        alertDialog.setView(puzzleInfoInput);

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_CHANGE"), new DialogInterface.OnClickListener() {
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

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void puzzleCreationOptions(final CreatorActivity activity) {
        // Creating dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_puzzle_options);
        dialog.setTitle(Text.get("UI_PUZZLE_OPTIONS"));
        dialog.setCancelable(true);

        // Filling in all the text fields
        ((TextView)dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView)dialog.findViewById(R.id.createButton)).setText(Text.get("DIALOG_BUTTON_CREATE"));
        ((TextView)dialog.findViewById(R.id.title)).setText(Text.get("UI_PUZZLE_OPTIONS"));
        ((TextView)dialog.findViewById(R.id.minWidth)).setText(Integer.toString(Constants.PUZZLE_X_MIN));
        ((TextView)dialog.findViewById(R.id.maxWidth)).setText(Integer.toString(Constants.PUZZLE_X_MAX));
        ((TextView)dialog.findViewById(R.id.minHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MIN));
        ((TextView)dialog.findViewById(R.id.maxHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MAX));
        ((TextView)dialog.findViewById(R.id.currentWidth)).setText(String.format(Text.get("UI_PUZZLE_WIDTH"), Constants.PUZZLE_X_MIN));
        ((TextView)dialog.findViewById(R.id.currentHeight)).setText(String.format(Text.get("UI_PUZZLE_HEIGHT"), Constants.PUZZLE_Y_MIN));

        // Creating X slider
        final SeekBar sliderWidth = (SeekBar) dialog.findViewById(R.id.sliderWidth);
        sliderWidth.setProgress(0);
        sliderWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView)dialog.findViewById(R.id.currentWidth)).setText(String.format(Text.get("UI_PUZZLE_WIDTH"), getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX)));
            }
        });

        // Creating Y slider
        final SeekBar sliderHeight = (SeekBar) dialog.findViewById(R.id.sliderHeight);
        sliderHeight.setProgress(0);
        sliderHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView)dialog.findViewById(R.id.currentHeight)).setText(String.format(Text.get("UI_PUZZLE_HEIGHT"), getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX)));
            }
        });

        dialog.findViewById(R.id.createButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int xValue = getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX);
                int yValue = getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX);
                PuzzleHelper.createNewPuzzle(xValue, yValue);

                dialog.dismiss();
                activity.populatePuzzles();
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void changeSettingSlider(final SettingsActivity activity, int settingId) {
        final Setting setting = Setting.get(settingId);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_slider);
        dialog.setTitle(String.format(Text.get("DIALOG_CHANGE_SLIDER"), setting.getName()));
        dialog.setCancelable(true);

        ((TextView)dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView)dialog.findViewById(R.id.saveValue)).setText(Text.get("DIALOG_BUTTON_SAVE"));
        ((TextView)dialog.findViewById(R.id.settingName)).setText(setting.getName());

        TextView minValue = (TextView)dialog.findViewById(R.id.minValue);
        TextView maxValue = (TextView)dialog.findViewById(R.id.maxValue);
        final TextView currentValue = (TextView)dialog.findViewById(R.id.currentValue);

        minValue.setText(String.format("%.2f", setting.getFloatMin()));
        maxValue.setText(String.format("%.2f", setting.getFloatMax()));
        currentValue.setText(String.format("%.2f", setting.getFloatValue()));

        final SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seekbar);
        seekbar.setProgress(getProgressFromFloat(setting.getFloatValue(), setting.getFloatMin(), setting.getFloatMax()));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                currentValue.setText(String.format("%.2f",
                        getFloatFromProgress(seekbar.getProgress(), setting.getFloatMin(), setting.getFloatMax())));
            }
        });

        dialog.findViewById(R.id.saveValue).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                float adjustedValue = getFloatFromProgress(seekbar.getProgress(), setting.getFloatMin(), setting.getFloatMax());
                setting.setFloatValue(adjustedValue);
                setting.save();
                dialog.dismiss();
                activity.populateSettings();
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static float getFloatFromProgress(int value, float min, float max) {
        return min + (((max - min) * value) / 100);
    }

    public static int getProgressFromFloat(float value, float min, float max) {
        return (int) Math.ceil(((value - min) / (max - min)) * 100);
    }

    public static int getIntFromProgress(int value, int min, int max) {
        return min + (((max - min) * value) / 100);
    }
}

package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import java.util.Locale;

import uk.co.jakelee.cityflow.BuildConfig;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.PuzzleCreationOptions;
import uk.co.jakelee.cityflow.components.PuzzleGenerator;
import uk.co.jakelee.cityflow.main.CreatorActivity;
import uk.co.jakelee.cityflow.main.CustomInfoActivity;
import uk.co.jakelee.cityflow.main.EditorActivity;
import uk.co.jakelee.cityflow.main.SettingsActivity;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.SupportCode;
import uk.co.jakelee.cityflow.model.Text;

public class AlertDialogHelper {
    public static void resetGameLanguage(final SettingsActivity activity) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_RESET_LANGUAGE_CONFIRM"), TextHelper.getNonEnglishTextCount()));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_DELETE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.resetGameLanguage();
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void confirmPuzzleDeletion(final CreatorActivity activity, final Puzzle puzzle) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("ALERT_DELETE_PUZZLE"), puzzle.getName()));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_DELETE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                puzzle.safelyDelete();
                activity.populatePuzzles();
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

    public static void confirmPuzzleShuffle(final EditorActivity activity, final Puzzle puzzle) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("ALERT_SHUFFLE_PUZZLE"), puzzle.getName()));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_SHUFFLE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.shuffleTiles();
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

    public static void confirmCloudLoad(final Activity activity, int localStars, int localCurrency, int cloudStars, int cloudCurrency) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_CLOUD_LOAD_CONFIRM"),
                localStars,
                localCurrency,
                cloudStars,
                cloudCurrency));

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

    public static void confirmCloudSave(final Activity activity, int localStars, int localCurrency, String desc, long saveTime, String deviceName) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_CLOUD_SAVE_CONFIRM"),
                desc,
                DateHelper.displayTime(saveTime, DateHelper.datetime),
                deviceName,
                localStars,
                localCurrency,
                BuildConfig.VERSION_NAME));

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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(Text.get("DIALOG_SUPPORT_CODE"));
        alertDialog.setView(supportCodeBox);

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_CONFIRM"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //String supportCode = EncryptHelper.encode((System.currentTimeMillis() + 172800000) + "|UPDATE statistic SET int_value = \"" + EncryptHelper.encode(1200, 6) + "\" WHERE statistic_id = 6");
                String supportCode = supportCodeBox.getText().toString().trim();
                if (SupportCode.alreadyApplied(supportCode)) {
                    AlertHelper.error(activity, AlertHelper.getError(AlertHelper.Error.SUPPORT_CODE_USED));
                } else if (EncryptHelper.applyCode(supportCode)) {
                    AlertHelper.success(activity, Text.get("SUCCESS_SUPPORT_CODE"));
                    Log.d("Support code", supportCode);
                } else {
                    AlertHelper.error(activity, AlertHelper.getError(AlertHelper.Error.SUPPORT_CODE_INVALID));
                }
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void importPuzzleText(final Context context, final CreatorActivity activity) {
        final EditText puzzleBox = new EditText(context);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(Text.get("WORD_IMPORT"));
        alertDialog.setView(puzzleBox);

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_LOAD"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String puzzleString = puzzleBox.getText().toString().trim();
                Intent intent = new Intent().putExtra("PUZZLE_TEXT", puzzleString);
                activity.onActivityResult(CreatorActivity.INTENT_TEXT, Activity.RESULT_OK, intent);
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public static void changeSettingText(final SettingsActivity activity, final int settingId) {
        final Setting settingToToggle = Setting.findById(Setting.class, settingId);
        final EditText editText = new EditText(activity.getApplicationContext());
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setText(Setting.getString(Constants.SETTING_PLAYER_NAME));
        editText.setSelectAllOnFocus(true);
        editText.setFilters(new InputFilter[]{FilterHelper.getFilter(Constants.PLAYER_NAME_MAX_LENGTH)});

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_CHANGE_TEXT"), settingToToggle.getName(), Constants.PLAYER_NAME_MAX_LENGTH));
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

        AlertDialog alertToShow = alertDialog.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertToShow.show();
    }

    public static void changePuzzleInfo(final Activity activity, final PuzzleCustom puzzleCustom, final boolean changeDesc) {
        final EditText puzzleInfoInput = new EditText(activity.getApplicationContext());
        puzzleInfoInput.setText(changeDesc ? puzzleCustom.getDescription() : puzzleCustom.getName());
        puzzleInfoInput.setSelectAllOnFocus(true);
        puzzleInfoInput.setFilters(new InputFilter[]{FilterHelper.getFilter(changeDesc ? Constants.PUZZLE_DESC_MAX_LENGTH : Constants.PUZZLE_NAME_MAX_LENGTH)});

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_CHANGE_TEXT"),
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

                if (activity instanceof CustomInfoActivity) {
                    ((CustomInfoActivity) activity).redisplayInfo();
                }
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertToShow = alertDialog.create();
        alertToShow.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertToShow.show();
    }

    public static void puzzleCreationOptions(final CreatorActivity activity) {
        // Creating dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_puzzle_options);
        dialog.setTitle(Text.get("UI_PUZZLE_OPTIONS"));
        dialog.setCancelable(true);

        // Get saved options
        final PuzzleCreationOptions options = new PuzzleCreationOptions(activity);

        // Filling in all the text fields
        ((TextView) dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView) dialog.findViewById(R.id.createButton)).setText(Text.get("DIALOG_BUTTON_CREATE"));
        ((TextView) dialog.findViewById(R.id.title)).setText(Text.get("UI_PUZZLE_OPTIONS"));
        ((TextView) dialog.findViewById(R.id.minWidth)).setText(Integer.toString(Constants.PUZZLE_X_MIN));
        ((TextView) dialog.findViewById(R.id.maxWidth)).setText(Integer.toString(Constants.PUZZLE_X_MAX));
        ((TextView) dialog.findViewById(R.id.minHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MIN));
        ((TextView) dialog.findViewById(R.id.maxHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MAX));
        ((TextView) dialog.findViewById(R.id.currentWidth)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_WIDTH"), options.getX()));
        ((TextView) dialog.findViewById(R.id.currentHeight)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_HEIGHT"), options.getY()));
        ((TextView) dialog.findViewById(R.id.environmentText)).setText(Text.get("WORD_AREA"));
        ((TextView) dialog.findViewById(R.id.emptyText)).setText(Text.get("UI_PUZZLE_AUTOGENERATE"));
        ((TextView) dialog.findViewById(R.id.shuffleText)).setText(Text.get("UI_PUZZLE_SHUFFLE_PLAY"));

        // Creating X slider
        final SeekBar sliderWidth = (SeekBar) dialog.findViewById(R.id.sliderWidth);
        sliderWidth.setProgress(getProgressFromFloat(options.getX(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX));
        sliderWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView) dialog.findViewById(R.id.currentWidth)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_WIDTH"), getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX)));
            }
        });

        // Creating Y slider
        final SeekBar sliderHeight = (SeekBar) dialog.findViewById(R.id.sliderHeight);
        sliderHeight.setProgress(getProgressFromFloat(options.getY(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX));
        sliderHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView) dialog.findViewById(R.id.currentHeight)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_HEIGHT"), getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX)));
            }
        });

        // Creating Environment picker
        int numEnvironments = (Constants.ENVIRONMENT_MAX - Constants.ENVIRONMENT_MIN) + 1;
        ArrayAdapter<String> envAdapter = new ArrayAdapter<>(activity, R.layout.custom_spinner_item);
        envAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        for (int i = 0; i < numEnvironments; i++) {
            envAdapter.add(i == 0 ? Text.get("WORD_ALL") : Text.get("ENVIRONMENT_" + i + "_NAME"));
        }

        // Disable "Blank puzzle" option if we're shuffle + playing
        final CheckBox shuffleCheckbox = (CheckBox) dialog.findViewById(R.id.shuffleCheckbox);
        final CheckBox emptyCheckbox = (CheckBox) dialog.findViewById(R.id.emptyCheckbox);
        emptyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                shuffleCheckbox.setEnabled(!b);
            }
        });

        // Disable area spinner if we're using all tiles
        final Spinner environmentPicker = (Spinner) dialog.findViewById(R.id.environmentPicker);
        environmentPicker.setAdapter(envAdapter);
        environmentPicker.setSelection(options.getEnvironmentId());

        shuffleCheckbox.setChecked(options.isShuffleAndPlay());
        emptyCheckbox.setChecked(options.isEmptyPuzzle());

        // Create button
        dialog.findViewById(R.id.createButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                options.setX(getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX));
                options.setY(getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX));
                options.setEnvironmentId(environmentPicker.getSelectedItemPosition());
                options.setEmptyPuzzle(emptyCheckbox.isChecked());
                options.setShuffleAndPlay(shuffleCheckbox.isChecked());

                if (options.getX() <= 1 && options.getY() <= 1) {
                    AlertHelper.error(activity, AlertHelper.getError(AlertHelper.Error.PUZZLE_TOO_SMALL));
                } else {
                    puzzleLoadingProgress(activity, options);
                    options.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void puzzleLoadingProgress(final Activity activity, PuzzleCreationOptions options) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_puzzle_loading);
        dialog.setCancelable(true);
        ((TextView) dialog.findViewById(R.id.title)).setText(Text.get("WORD_LOADING"));
        dialog.show();

        new PuzzleGenerator(activity, dialog, options).execute("");
    }

    public static void resizePuzzle(final EditorActivity activity, final int puzzleId) {
        final Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        final Pair<Integer, Integer> oldXY = TileHelper.getMaxXY(puzzle.getTiles());

        // Creating dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_puzzle_size);
        dialog.setCancelable(true);

        // Filling in all the text fields
        ((TextView) dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView) dialog.findViewById(R.id.resizeButton)).setText(Text.get("DIALOG_BUTTON_RESIZE"));
        ((TextView) dialog.findViewById(R.id.title)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_RESIZE"), puzzle.getName()));
        ((TextView) dialog.findViewById(R.id.resizeHint)).setText(Text.get("UI_PUZZLE_RESIZE_HINT"));
        ((TextView) dialog.findViewById(R.id.minWidth)).setText(Integer.toString(Constants.PUZZLE_X_MIN));
        ((TextView) dialog.findViewById(R.id.maxWidth)).setText(Integer.toString(Constants.PUZZLE_X_MAX));
        ((TextView) dialog.findViewById(R.id.minHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MIN));
        ((TextView) dialog.findViewById(R.id.maxHeight)).setText(Integer.toString(Constants.PUZZLE_Y_MAX));
        ((TextView) dialog.findViewById(R.id.currentWidth)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_WIDTH"), oldXY.first + 1));
        ((TextView) dialog.findViewById(R.id.currentHeight)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_HEIGHT"), oldXY.second + 1));

        // Creating X slider
        final SeekBar sliderWidth = (SeekBar) dialog.findViewById(R.id.sliderWidth);
        sliderWidth.setProgress(getProgressFromFloat(oldXY.first + 1, Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX));
        sliderWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView) dialog.findViewById(R.id.currentWidth)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_WIDTH"), getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX)));
            }
        });

        // Creating Y slider
        final SeekBar sliderHeight = (SeekBar) dialog.findViewById(R.id.sliderHeight);
        sliderHeight.setProgress(getProgressFromFloat(oldXY.second + 1, Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX));
        sliderHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                ((TextView) dialog.findViewById(R.id.currentHeight)).setText(String.format(Locale.ENGLISH, Text.get("UI_PUZZLE_HEIGHT"), getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX)));
            }
        });

        // Create button
        dialog.findViewById(R.id.resizeButton).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int newX = getIntFromProgress(sliderWidth.getProgress(), Constants.PUZZLE_X_MIN, Constants.PUZZLE_X_MAX);
                int newY = getIntFromProgress(sliderHeight.getProgress(), Constants.PUZZLE_Y_MIN, Constants.PUZZLE_Y_MAX);

                if (newX <= 1 && newY <= 1) {
                    AlertHelper.error(activity, AlertHelper.getError(AlertHelper.Error.PUZZLE_TOO_SMALL));
                } else if ((oldXY.first + 1) != newX || (oldXY.second + 1) != newY) {
                    puzzle.saveTileRotations();
                    confirmResize(activity, puzzleId, oldXY.first + 1, oldXY.second + 1, newX, newY);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void confirmResize(final Activity activity, final int puzzleId, final int oldX, final int oldY, final int newX, final int newY) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(String.format(Locale.ENGLISH, Text.get("DIALOG_RESIZE_CONFIRM"), oldX, oldY, newX, newY));

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_RESIZE"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PuzzleHelper.resizePuzzle(puzzleId, oldX, oldY, newX, newY);
                Intent intent = new Intent(activity, EditorActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, puzzleId);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                activity.finish();
                activity.startActivity(intent);
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

    public static void rotatePuzzle(final Activity activity, final int puzzleId) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(Text.get("DIALOG_ROTATE_CONFIRM"));

        alertDialog.setNeutralButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton(Text.get("DIALOG_BUTTON_LEFT"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PuzzleHelper.rotatePuzzle(puzzleId, false);

                activity.finish();
                activity.startActivity(new Intent(activity, EditorActivity.class)
                        .putExtra(Constants.INTENT_PUZZLE, puzzleId)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        });

        alertDialog.setPositiveButton(Text.get("DIALOG_BUTTON_RIGHT"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PuzzleHelper.rotatePuzzle(puzzleId, true);

                activity.finish();
                activity.startActivity(new Intent(activity, EditorActivity.class)
                        .putExtra(Constants.INTENT_PUZZLE, puzzleId)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });
    }

    public static void openPackLeaderboard(final Activity activity, final String timeLeaderboard, final String movesLeaderboard) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.Theme_AlertDialog);
        alertDialog.setMessage(Text.get("DIALOG_LEADERBOARD_CONFIRM"));

        alertDialog.setNeutralButton(Text.get("DIALOG_BUTTON_CANCEL"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setNegativeButton(Text.get("METRIC_BEST_TIME"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, timeLeaderboard), GooglePlayHelper.RC_LEADERBOARDS);
            }
        });

        alertDialog.setPositiveButton(Text.get("METRIC_BEST_MOVES"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, movesLeaderboard), GooglePlayHelper.RC_LEADERBOARDS);
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });
    }

    public static void changeSettingFloat(final SettingsActivity activity, int settingId) {
        final Setting setting = Setting.get(settingId);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_slider);
        dialog.setTitle(String.format(Locale.ENGLISH, Text.get("DIALOG_CHANGE_SLIDER"), setting.getName()));
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView) dialog.findViewById(R.id.saveValue)).setText(Text.get("DIALOG_BUTTON_SAVE"));
        ((TextView) dialog.findViewById(R.id.settingName)).setText(setting.getName());

        TextView minValue = (TextView) dialog.findViewById(R.id.minValue);
        TextView maxValue = (TextView) dialog.findViewById(R.id.maxValue);
        final TextView currentValue = (TextView) dialog.findViewById(R.id.currentValue);

        minValue.setText(String.format(Locale.ENGLISH, "%.2f", setting.getFloatMin()));
        maxValue.setText(String.format(Locale.ENGLISH, "%.2f", setting.getFloatMax()));
        currentValue.setText(String.format(Locale.ENGLISH, "%.2f", setting.getFloatValue()));

        final SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seekbar);
        seekbar.setProgress(getProgressFromFloat(setting.getFloatValue(), setting.getFloatMin(), setting.getFloatMax()));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                currentValue.setText(String.format(Locale.ENGLISH, "%.2f",
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

    public static void changeSettingInt(final SettingsActivity activity, final int settingId) {
        final Setting setting = Setting.get(settingId);

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_slider);
        dialog.setTitle(String.format(Locale.ENGLISH, Text.get("DIALOG_CHANGE_SLIDER"), setting.getName()));
        dialog.setCancelable(true);

        ((TextView) dialog.findViewById(R.id.close)).setText(Text.get("DIALOG_BUTTON_CLOSE"));
        ((TextView) dialog.findViewById(R.id.saveValue)).setText(Text.get("DIALOG_BUTTON_SAVE"));
        ((TextView) dialog.findViewById(R.id.settingName)).setText(setting.getName());

        TextView minValue = (TextView) dialog.findViewById(R.id.minValue);
        TextView maxValue = (TextView) dialog.findViewById(R.id.maxValue);
        final TextView currentValue = (TextView) dialog.findViewById(R.id.currentValue);

        minValue.setText(Integer.toString(setting.getIntMin()));
        maxValue.setText(settingId == Constants.SETTING_AUTOSAVE_FREQUENCY ?
                Text.get("WORD_NEVER") :
                Integer.toString(setting.getIntMax()));
        currentValue.setText(settingId == Constants.SETTING_AUTOSAVE_FREQUENCY && setting.getIntValue() == Constants.AUTOSAVE_NEVER ?
                Text.get("WORD_NEVER") :
                Integer.toString(setting.getIntValue()));

        final SeekBar seekbar = (SeekBar) dialog.findViewById(R.id.seekbar);
        seekbar.setProgress(getProgressFromFloat((float) setting.getIntValue(), (float) setting.getIntMin(), (float) setting.getIntMax()));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                int progressInt = getIntFromProgress(seekbar.getProgress(), setting.getIntMin(), setting.getIntMax());
                currentValue.setText((settingId == Constants.SETTING_AUTOSAVE_FREQUENCY && progressInt == Constants.AUTOSAVE_NEVER) ?
                        Text.get("WORD_NEVER") :
                        Integer.toString(progressInt));
            }
        });

        dialog.findViewById(R.id.saveValue).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int adjustedValue = getIntFromProgress(seekbar.getProgress(), setting.getIntMin(), setting.getIntMax());
                setting.setIntValue(adjustedValue);
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

    public static int getIntFromProgress(int value, int min, int max) {
        return min + (((max - min) * value) / 100);
    }

    public static int getProgressFromFloat(float value, float min, float max) {
        return (int) Math.ceil(((value - min) / (max - min)) * 100);
    }

}

package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Text;

public class BackgroundPickerActivity extends Activity {
    private TextView selectedBackgroundTile;
    private Background selectedBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_picker);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);

        ((TextView)findViewById(R.id.save)).setText(Text.get("DIALOG_BUTTON_SAVE"));


    }

    @Override
    protected void onResume() {
        super.onResume();

        displayBackgrounds();
        updateBackgroundInfo();
        ((TextView)findViewById(R.id.backgroundSelectTitle)).setText(String.format(Text.get("UI_BACKGROUND_SELECT_TITLE"),
                Background.getUnlockedBackgroundCount(),
                Background.listAll(Background.class).size()));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void displayBackgrounds() {
        LayoutInflater inflater = LayoutInflater.from(this);
        TableLayout backgroundContainer = (TableLayout) findViewById(R.id.backgroundContainer);
        backgroundContainer.removeAllViews();

        TableRow row = new TableRow(this);

        List<Background> backgrounds = Background.listAll(Background.class);
        int numBackgrounds = backgrounds.size();
        for (int index = 1; index <= numBackgrounds; index++) {
            Background background = backgrounds.get(index - 1);

            TextView backgroundView = (TextView) inflater.inflate(R.layout.custom_background_tile, null);
            backgroundView.setBackgroundColor(background.getBackgroundColour());
            backgroundView.setText(background.isActive() ? R.string.icon_tick : background.isUnlocked() ? R.string.icon_unlock : R.string.icon_lock);
            backgroundView.setTag(background.getBackgroundId());
            backgroundView.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    selectBackground(v);
                }
            });

            if (background.isActive()) {
                backgroundView.setTextColor(ContextCompat.getColor(this, R.color.green));
                selectedBackgroundTile = backgroundView;
                selectedBackground = background;
            }

            row.addView(backgroundView);
            if (index % 5 == 0 || index == numBackgrounds) {
                backgroundContainer.addView(row);
                row = new TableRow(this);
            }
        }
    }

    public void selectBackground(View v) {
        selectedBackgroundTile = (TextView)v;
        selectedBackground = Background.get((int)v.getTag());

        updateBackgroundInfo();
    }

    private void updateBackgroundInfo() {
        ((TextView)findViewById(R.id.backgroundStatus)).setText(selectedBackground.isActive() ? R.string.icon_tick : selectedBackground.isUnlocked() ? R.string.icon_unlock : R.string.icon_lock);
        ((TextView)findViewById(R.id.backgroundStatus)).setTextColor(selectedBackground.isUnlocked() ? ContextCompat.getColor(this, R.color.green) : selectedBackground.getBackgroundColour());

        ((TextView)findViewById(R.id.backgroundName)).setText(selectedBackground.isUnlocked() ? selectedBackground.getName() : "???");
        ((TextView)findViewById(R.id.backgroundHint)).setText(selectedBackground.getHint());

        findViewById(R.id.save).setVisibility(selectedBackground.isUnlocked() ? View.VISIBLE : View.INVISIBLE);
    }

    public void saveBackground(View v) {
        if (selectedBackground.isUnlocked()) {
            Background.setActiveBackground((int) selectedBackgroundTile.getTag());
            displayBackgrounds();
        }
    }

    public void closePopup (View v) {
        this.finish();
    }
}
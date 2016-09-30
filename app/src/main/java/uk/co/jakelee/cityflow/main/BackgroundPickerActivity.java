package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Text;

public class BackgroundPickerActivity extends Activity {
    private ImageView selectedBackgroundTile;
    private Background selectedBackground;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_picker);
        dh = DisplayHelper.getInstance(this);

        ((TextView)findViewById(R.id.save)).setText(Text.get("DIALOG_BUTTON_SAVE"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayBackgrounds();
        updateBackgroundInfo();
        ((TextView)findViewById(R.id.backgroundSelectTitle)).setText(Text.get("UI_BACKGROUND_SELECT_TITLE"));
    }

    private void displayBackgrounds() {
        TableLayout backgroundContainer = (TableLayout) findViewById(R.id.backgroundContainer);
        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dh.dpToPixel(40));
        int margin = dh.dpToPixel(5);
        layoutParams.setMargins(margin, margin, margin, margin);

        List<Background> backgrounds = Background.listAll(Background.class);
        int numBackgrounds = backgrounds.size();
        for (int index = 1; index <= numBackgrounds; index++) {
            Background background = backgrounds.get(index - 1);

            ImageView backgroundView = new ImageView(this);
            backgroundView.setImageResource(R.drawable.transparent);
            backgroundView.setBackgroundColor(background.getBackgroundColour());
            backgroundView.setTag(background.getBackgroundId());
            backgroundView.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    selectBackground(v);
                }
            });

            if (background.isActive()) {
                // backgroundView add colour filter
                selectedBackgroundTile = backgroundView;
                selectedBackground = background;
            }

            row.addView(backgroundView, layoutParams);
            if (index % 5 == 0 || index == numBackgrounds) {
                backgroundContainer.addView(row);
                row = new TableRow(this);
            }
        }
    }

    public void selectBackground(View v) {
        // remove colour filter from selectedBackgroundTile (if != null)
        // set colour filter on v
        selectedBackgroundTile = (ImageView)v;
        selectedBackground = Background.get((int)v.getTag());
        updateBackgroundInfo();
    }

    private void updateBackgroundInfo() {

        ((TextView)findViewById(R.id.backgroundStatus)).setText(selectedBackground.isUnlocked() ? R.string.icon_tick : R.string.icon_lock);
        ((TextView)findViewById(R.id.backgroundStatus)).setTextColor(ContextCompat.getColor(this, selectedBackground.isUnlocked() ? R.color.green : R.color.red));

        ((TextView)findViewById(R.id.backgroundName)).setText(selectedBackground.isUnlocked() ? selectedBackground.getName() : "???");
        ((TextView)findViewById(R.id.backgroundHint)).setText(selectedBackground.isUnlocked() ? "" : selectedBackground.getHint());

        ((TextView)findViewById(R.id.backgroundHint)).setText(selectedBackground.isUnlocked() ? "" : selectedBackground.getHint());

        findViewById(R.id.save).setVisibility(selectedBackground.isUnlocked() ? View.VISIBLE : View.INVISIBLE);
    }

    public void saveBackground(View v) {
        if (selectedBackground.isUnlocked()) {
            Background.setActiveBackground((int) selectedBackgroundTile.getTag());
            this.finish();
        } 
    }

    public void closePopup (View v) {
        this.finish();
    }
}
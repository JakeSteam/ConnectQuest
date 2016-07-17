package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Pack;

public class StoryActivity extends Activity {
    private DisplayHelper dh;
    public Pack selectedPack = new Pack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        dh = DisplayHelper.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedPack = Pack.getPack(selectedPack.getPackId());
        populatePacks();
    }

    public void populatePacks() {
        LinearLayout packContainer = (LinearLayout) findViewById(R.id.packContainer);
        packContainer.removeAllViews();

        List<Pack> packs = Pack.listAll(Pack.class);
        for (final Pack pack : packs) {
            if (selectedPack == null || selectedPack.getPackId() == 0) {
                selectedPack = pack;
            }
            boolean isSelected = selectedPack.getPackId() == pack.getPackId();

            LayoutInflater inflater = LayoutInflater.from(this);
            View inflatedView = inflater.inflate(R.layout.custom_pack_preview, null);
            RelativeLayout packPreview = (RelativeLayout) inflatedView.findViewById(R.id.packPreview);
            ProgressBar packProgress = (ProgressBar) inflatedView.findViewById(R.id.packProgress);

            if (!isSelected) {
                packProgress.setProgress(pack.getCurrentStars());
                packProgress.setMax(pack.getMaxStars());
            }
            packProgress.setVisibility(isSelected ? View.INVISIBLE : View.VISIBLE);

            inflatedView.findViewById(R.id.packColourIndicator).setBackgroundResource(pack.isUnlocked() ? R.color.green : R.color.red);

            ImageView image = (ImageView)packPreview.findViewById(R.id.packImage);
            image.setImageResource(dh.getPuzzleDrawableID(pack.getFirstPuzzleId()));

            TextView text = (TextView)packPreview.findViewById(R.id.packName);
            text.setText(pack.getName());

            inflatedView.setTag(pack.getPackId());
            inflatedView.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    selectedPack = pack;
                    populatePacks();
                }
            });

            packContainer.addView(inflatedView);
        }

        showPackInfo();
    }

    public void showPackInfo() {
        final Activity activity = this;
        ((TextView) findViewById(R.id.packDesc)).setText(selectedPack.getDescription());

        ((TextView) findViewById(R.id.packStars)).setText(selectedPack.getCurrentStars() + " / " + selectedPack.getMaxStars());
        boolean allStars = selectedPack.getCurrentStars() == selectedPack.getMaxStars();
        ((TextView) findViewById(R.id.packStars)).setTextColor(allStars ? Color.YELLOW : Color.BLACK);

        TextView packButton = (TextView) findViewById(R.id.packButton);
        packButton.setTag(selectedPack.getPackId());
        if (selectedPack.isUnlocked()) {
            packButton.setText("Open Pack");
            packButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PackActivity.class);
                    intent.putExtra(Constants.INTENT_PACK, (int) v.getTag());
                    startActivity(intent);
                }
            });
        } else {
            Pack previousPack = Pack.getPack(selectedPack.getPackId() - 1);
            packButton.setText("Purchase Pack (" + previousPack.getCurrentStars() + " / " + previousPack.getMaxStars() + ")");
            packButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // IAP prompt
                }
            });
        }
    }
}

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

import com.google.android.gms.games.Games;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Text;

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
            packProgress.setVisibility(isSelected || !pack.isUnlocked() ? View.INVISIBLE : View.VISIBLE);

            inflatedView.findViewById(R.id.packColourIndicator).setBackgroundResource(isSelected ? R.color.green : R.color.red);

            ImageView image = (ImageView)packPreview.findViewById(R.id.packImage);
            image.setImageDrawable(dh.getPuzzleDrawable(pack.getFirstPuzzleId(), pack.isUnlocked()));

            TextView text = (TextView)packPreview.findViewById(R.id.packName);
            text.setText(pack.getPackId() + ": " + pack.getName());

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
            packButton.setText(Text.get("UI_PACK_OPEN"));
            packButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PackActivity.class);
                    intent.putExtra(Constants.INTENT_PACK, (int) v.getTag());
                    startActivity(intent);
                }
            });
        } else {
            packButton.setText(Text.get("UI_PACK_PURCHASE"));
            packButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), IAPActivity.class);
                    intent.putExtra(Constants.INTENT_IAP, GooglePlayHelper.IAPs.unlock_pack2.toString());
                    startActivity(intent);
                }
            });
        }
    }

    public void openLeaderboard(View v) {
        if (GooglePlayHelper.IsConnected()) {
            if (v.getId() == R.id.timeLeaderboard && !selectedPack.getTimeLeaderboard().equals("")) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, selectedPack.getTimeLeaderboard()), GooglePlayHelper.RC_LEADERBOARDS);
            } else if (v.getId() == R.id.movesLeaderboard && !selectedPack.getMovesLeaderboard().equals("")) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, selectedPack.getMovesLeaderboard()), GooglePlayHelper.RC_LEADERBOARDS);
            }
        }
    }
}

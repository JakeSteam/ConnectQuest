package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.BillingHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Iap;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Text;

public class IAPActivity extends Activity implements BillingHelper.Listener {
    private BillingHelper billing;
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iap);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);

        dh = DisplayHelper.getInstance(this);

        List<String> products = new ArrayList<>();
        Set<String> consumables = new HashSet<>();
        for (Iap iap : Iap.listAll(Iap.class)) {
            products.add(iap.getIapCode());
            // Coin packs can be re-bought; the doubler and tile unlock are permanent.
            if (iap.getCoins() > 0) {
                consumables.add(iap.getIapCode());
            }
        }
        billing = new BillingHelper(this, this, products, consumables);

        populateText();
        populateIaps();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateText() {
        ((TextView) findViewById(R.id.iapTitle)).setText(Text.get("UI_IAP_TITLE"));
        ((TextView) findViewById(R.id.teaserText)).setText(Text.get(Iap.hasPurchasedAnything() ? "UI_IAP_TIP" : "UI_IAP_TEASER"));
    }

    @Override
    public void onBillingReady(Set<String> ownedProducts) {
        // Restores entitlements after a reinstall, which the old code never did, and relabels the
        // buttons now Play has told us the real prices.
        for (String productId : ownedProducts) {
            Iap iap = Iap.get(productId);
            if (iap != null && iap.getPurchases() == 0) {
                iap.purchase();
            }
        }
        populateIaps();
        populateText();
    }

    @Override
    public void onProductPurchased(String productId) {
        Iap iap = Iap.get(productId);
        if (iap == null) {
            return;
        }
        iap.purchase();

        Pack iapUnlockedPack = Pack.getPack(9);
        if (!iapUnlockedPack.isPurchased()) {
            Background.get(uk.co.jakelee.cityflow.helper.Constants.BACKGROUND_SUMMER).unlock();
            iapUnlockedPack.setPurchased(true);
            iapUnlockedPack.save();

            AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("ALERT_ITEM_PURCHASED_PACK"), iap.getName()));
        } else {
            AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("ALERT_ITEM_PURCHASED"), iap.getName()));
        }

        populateText();
    }

    @Override
    public void onBillingError() {
        AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.IAB_FAILED));
    }

    public void buyIAP(View v) {
        if (billing.isReady()) {
            billing.purchase((String) v.getTag());
        } else {
            AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.IAB_FAILED));
        }
    }

    @Override
    public void onDestroy() {
        if (billing != null)
            billing.release();
        super.onDestroy();
    }

    private void populateIaps() {
        LinearLayout scrollView = (LinearLayout) findViewById(R.id.iapContainer);
        scrollView.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<Iap> iaps = Iap.listAll(Iap.class);

        for (Iap iap : iaps) {
            RelativeLayout iapButton = (RelativeLayout) inflater.inflate(R.layout.custom_iap_button, null);

            ((ImageView) iapButton.findViewById(R.id.itemImage)).setImageResource(dh.getIabDrawableID(iap.getIapCode()));
            ((TextView) iapButton.findViewById(R.id.itemName)).setText(iap.getName());

            String price = billing == null ? null : billing.getFormattedPrice(iap.getIapCode());
            ((TextView) iapButton.findViewById(R.id.itemPrice)).setText(price == null ? "?.??" : price);
            iapButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    buyIAP(v);
                }
            });
            iapButton.setTag(iap.getIapCode());

            scrollView.addView(iapButton, layoutParams);
        }
    }

    public void closePopup(View v) {
        this.finish();
    }

}

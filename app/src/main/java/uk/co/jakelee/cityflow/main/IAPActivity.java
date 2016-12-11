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

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Iap;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Text;

public class IAPActivity extends Activity implements BillingProcessor.IBillingHandler {
    private BillingProcessor bp;
    private DisplayHelper dh;
    boolean canBuyIAPs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iap);
        SoundHelper.keepPlayingMusic = true;

        dh = DisplayHelper.getInstance(this);
        canBuyIAPs = BillingProcessor.isIabServiceAvailable(this);
        if (canBuyIAPs) {
            bp = new BillingProcessor(this, getPublicKey(), this);
        } else {
            AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.NO_IAB));
        }

        populateText();
        populateIaps();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateText() {
        ((TextView)findViewById(R.id.iapTitle)).setText(Text.get("UI_IAP_TITLE"));
        ((TextView)findViewById(R.id.teaserText)).setText(Text.get(Iap.hasPurchasedAnything() ? "UI_IAP_TIP" : "UI_IAP_TEASER"));
    }

    @Override
    public void onBillingInitialized() {
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        bp.consumePurchase(productId);
        Iap.get(productId).purchase();

        Pack iapUnlockedPack = Pack.getPack(9);
        if (iapUnlockedPack.isPurchased()) {
            AlertHelper.success(this, Text.get("ALERT_COINS_PURCHASED_PACK"));
            Background.get(uk.co.jakelee.cityflow.helper.Constants.BACKGROUND_SUMMER).unlock();
            iapUnlockedPack.setPurchased(true);
            iapUnlockedPack.save();
        } else {
            AlertHelper.success(this, Text.get("ALERT_COINS_PURCHASED"));
        }

        populateText();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.IAB_FAILED));
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void buyIAP(View v) {
        if (canBuyIAPs) {
            bp.purchase(this, (String)v.getTag());
        } else {
            AlertHelper.error(this, AlertHelper.getError(AlertHelper.Error.IAB_FAILED));
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    private void populateIaps() {
        LinearLayout scrollView = (LinearLayout)findViewById(R.id.iapContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<Iap> iaps = Iap.listAll(Iap.class);

        for (Iap iap : iaps) {
            SkuDetails iapInfo = bp.getPurchaseListingDetails(iap.getIapCode());
            RelativeLayout iapButton = (RelativeLayout) inflater.inflate(R.layout.custom_iap_button, null);
            ((ImageView) iapButton.findViewById(R.id.itemImage)).setImageResource(dh.getIabDrawableID(iap.getIapCode()));

            if (iapInfo != null) {
                ((TextView) iapButton.findViewById(R.id.itemPrice)).setText(iapInfo.currency + " " + iapInfo.priceText);
            } else {
                ((TextView) iapButton.findViewById(R.id.itemPrice)).setText("£?.??");
            }

            iapButton.setTag(iap.getIapCode());
            iapButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    buyIAP(v);
                }
            });
            scrollView.addView(iapButton, layoutParams);
        }
    }

    public void closePopup (View v) {
        this.finish();
    }

    private String getPublicKey() {
        String[] keyArray = new String[] {
                "MIIBIjANBgkqhki",
                "G9w0BAQEFAAOCAQ",
                "8AMIIBCgKCAQEAg",
                "lb0laN6iAJ3ktHB",
                "uB2UVLkoPSPAs1Y",
                "KmeB/pbm2y2JcgD",
                "VKF9zXHLOC6xG+D",
                "KBsb2v01ESSm7pX",
                "twqOyQB+Ik6u9BM",
                "qJ46gwKm1sKs06d",
                "oMHaG3+73/fb1iL",
                "G1ochlXaj96jxYN",
                "PMppDva3Wt0A9nD2Pnw+0UjWkzo9403d+lXjFvWGRqj4yxG",
                "irGnWlFZSgJzJNfJiYpqdgaw7O9tu0GEJDYZON5RqLmTimb",
                "kT4CjMGGL2Kuubu7LcKWRLaEikEL7bzUWGHJzGzMpzj1F40",
                "nJB4yUJiXLq0SI5zETUisxq3XjeJ0v1xGR/T+VzBttJ5skO",
                "fSpGknAdjSSc77CMOQIDAQAB"
        };

        StringBuilder builder = new StringBuilder();
        for (String keyPart : keyArray) {
            builder.append(keyPart);
        }

        return builder.toString();
    }
}

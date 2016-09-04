package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.model.Pack;

public class IAPActivity extends Activity implements BillingProcessor.IBillingHandler {
    private SkuDetails iapInfo;
    BillingProcessor bp;
    boolean canBuyIAPs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iap);

        Intent intent = getIntent();
        String iapCode = intent.getStringExtra(Constants.INTENT_IAP);

        canBuyIAPs = BillingProcessor.isIabServiceAvailable(this);
        if (canBuyIAPs) {
            bp = new BillingProcessor(this, getPublicKey(), this);
            iapInfo = bp.getPurchaseListingDetails(iapCode);

            if (iapInfo != null) {
                populateIapInfo();
            }
        }
    }

    @Override
    public void onBillingInitialized() {
        if (bp.isPurchased(GooglePlayHelper.IAPs.unlock_pack2.name()) && !Pack.getPack(2).isPurchased()) {
            Pack pack1 = Pack.getPack(1);
            pack1.setPurchased(true);
            pack1.save();

            AlertHelper.success(this, "Maybe restored pack 2?");
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (productId.equals(GooglePlayHelper.IAPs.unlock_pack2.name())) {
            Pack pack1 = Pack.getPack(1);
            pack1.setPurchased(true);
            pack1.save();

            AlertHelper.success(this, "Maybe purchased pack 1?");
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        AlertHelper.error(this, "Uh oh, some kind of error occurred. Try again later?");
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
        if (canBuyIAPs && iapInfo != null) {
            bp.purchase(this, iapInfo.productId);
        } else {
            AlertHelper.error(this, "Uh oh, some kind of error occurred. Try again later?");
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    private void populateIapInfo() {
        ((TextView)findViewById(R.id.iapName)).setText(iapInfo.title);
        ((TextView)findViewById(R.id.iapDesc)).setText(iapInfo.description);

        ((TextView)findViewById(R.id.purchaseButton)).setText(iapInfo.currency + iapInfo.priceValue);
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

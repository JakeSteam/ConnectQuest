package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.StoreItem;

public class ShopItemActivity extends Activity {
    private StoreItem storeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);

        Intent intent = getIntent();
        storeItem = StoreItem.get(intent.getIntExtra(Constants.INTENT_ITEM, 0));

        if (storeItem != null) {
            populateItemInfo();
        }
    }

    private void populateItemInfo() {
        ((TextView)findViewById(R.id.itemName)).setText(storeItem.getName());
        ((TextView)findViewById(R.id.itemDesc)).setText(storeItem.getDescription());

        if (storeItem.getCategoryId() == Constants.STORE_CATEGORY_BOOSTS && storeItem.getRewardId() > 0) {
            Boost boost = Boost.get(storeItem.getRewardId());
            ((TextView) findViewById(R.id.itemPurchases)).setText(boost.getOwned() + " owned");
        } else {
            ((TextView) findViewById(R.id.itemPurchases)).setText(storeItem.getPurchases() + (storeItem.getMaxPurchases() > 0 ? "/" + storeItem.getMaxPurchases() : "") + " purchases");
        }

        if (storeItem.getMaxPurchases() > 0 && storeItem.getPurchases() >= storeItem.getMaxPurchases()) {
            ((TextView)findViewById(R.id.purchaseButton)).setText("Maximum Reached");
        } else {
            ((TextView)findViewById(R.id.purchaseButton)).setText("Buy for " + storeItem.getPrice() + " coins");
        }
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    public void buyItem(View view) {
        ErrorHelper.Error purchaseResult = storeItem.canPurchase();
        if (purchaseResult != ErrorHelper.Error.NO_ERROR) {
            Crouton.makeText(this, ErrorHelper.get(purchaseResult), Style.ALERT).show();
        } else {
            storeItem.purchase();
            Crouton.makeText(this, "Successfully purchased " + storeItem.getName() + " for " + storeItem.getPrice() + " coins!", Style.CONFIRM).show();
        }

        storeItem = StoreItem.get(storeItem.getItemId());
        populateItemInfo();
    }

    public void closePopup (View v) {
        this.finish();
    }
}

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

        ((TextView)findViewById(R.id.purchaseButton)).setText("Buy for " + storeItem.getPrice());
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    public void buyItem(View view) {
        ErrorHelper.Error purchaseResult = storeItem.tryPurchase();
        if (purchaseResult != ErrorHelper.Error.NO_ERROR) {
            Crouton.makeText(this, ErrorHelper.get(purchaseResult), Style.ALERT).show();
        } else {
            Crouton.makeText(this, "Successfully purchased " + storeItem.getName() + " for " + storeItem.getPrice() + " currency!", Style.CONFIRM).show();
        }
        populateItemInfo();
    }

    public void closePopup (View v) {
        this.finish();
    }
}

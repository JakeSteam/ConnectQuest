package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.StyleHelper;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;

public class ShopItemActivity extends Activity {
    private ShopItem shopItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);

        Intent intent = getIntent();
        shopItem = ShopItem.get(intent.getIntExtra(Constants.INTENT_ITEM, 0));

        if (shopItem != null) {
            populateItemInfo();
        }
    }

    private void populateItemInfo() {
        ((TextView)findViewById(R.id.itemName)).setText(shopItem.getName());
        ((TextView)findViewById(R.id.itemDesc)).setText(shopItem.getDescription());

        if (shopItem.getCategoryId() == Constants.STORE_CATEGORY_BOOSTS && shopItem.getRewardId() > 0) {
            Boost boost = Boost.get(shopItem.getRewardId());
            ((TextView) findViewById(R.id.itemPurchases)).setText(boost.getOwned() + " owned");
        } else {
            ((TextView) findViewById(R.id.itemPurchases)).setText(shopItem.getPurchases() + (shopItem.getMaxPurchases() > 0 ? "/" + shopItem.getMaxPurchases() : "") + " purchases");
        }

        if (shopItem.getMaxPurchases() > 0 && shopItem.getPurchases() >= shopItem.getMaxPurchases()) {
            ((TextView)findViewById(R.id.purchaseButton)).setText("Maximum Reached");
        } else {
            ((TextView)findViewById(R.id.purchaseButton)).setText("Buy for " + shopItem.getPrice() + " coins");
        }
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    public void buyItem(View view) {
        ErrorHelper.Error purchaseResult = shopItem.canPurchase();
        if (purchaseResult != ErrorHelper.Error.NO_ERROR) {
            Crouton.showText(this, ErrorHelper.get(purchaseResult), StyleHelper.ERROR, (ViewGroup)findViewById(R.id.croutonview));
        } else {
            shopItem.purchase();
            Crouton.showText(this, "Successfully purchased " + shopItem.getName() + " for " + shopItem.getPrice() + " coins!", StyleHelper.SUCCESS, (ViewGroup)findViewById(R.id.croutonview));
        }

        shopItem = ShopItem.get(shopItem.getItemId());
        populateItemInfo();
    }

    public void closePopup (View v) {
        this.finish();
    }
}

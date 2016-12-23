package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class ShopItemActivity extends Activity {
    private ShopItem shopItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);

        Intent intent = getIntent();
        shopItem = ShopItem.get(intent.getIntExtra(Constants.INTENT_ITEM, 0));

        if (shopItem != null) {
            populateItemInfo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateItemInfo() {
        if (shopItem.getCategoryId() == Constants.STORE_CATEGORY_TILES) {
            ((TextView) findViewById(R.id.itemName)).setText(TileType.get(shopItem.getSubcategoryId()).getName());
            ((TextView) findViewById(R.id.itemDesc)).setText("");
        } else {
            ((TextView) findViewById(R.id.itemName)).setText(shopItem.getName());
            ((TextView) findViewById(R.id.itemDesc)).setText(shopItem.getDescription());
        }

        if (shopItem.getCategoryId() == Constants.STORE_CATEGORY_BOOSTS && shopItem.getSubcategoryId() > 0) {
            Boost boost = Boost.get(shopItem.getSubcategoryId());
            ((TextView) findViewById(R.id.itemPurchases)).setText(String.format(Locale.ENGLISH, Text.get("SHOP_NUMBER_OWNED"), boost.getOwned()));
        } else {
            ((TextView) findViewById(R.id.itemPurchases)).setText(String.format(Locale.ENGLISH, Text.get("SHOP_NUMBER_PURCHASES"),
                    shopItem.getPurchases(),
                    shopItem.getMaxPurchases() > 0 ? "/" + shopItem.getMaxPurchases() : ""));
        }

        if (shopItem.getMaxPurchases() > 0 && shopItem.getPurchases() >= shopItem.getMaxPurchases()) {
            ((TextView) findViewById(R.id.purchaseButton)).setText(Text.get("SHOP_MAX_PURCHASED"));
        } else {
            ((TextView) findViewById(R.id.purchaseButton)).setText(String.format(Locale.ENGLISH, Text.get("SHOP_PURCHASE_TEXT"), shopItem.getPrice()));
        }
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    public void buyItem(View view) {
        AlertHelper.Error purchaseResult = shopItem.canPurchase();
        if (purchaseResult != AlertHelper.Error.NO_ERROR) {
            AlertHelper.error(this, AlertHelper.getError(purchaseResult));
        } else {
            SoundHelper.getInstance(this).playSound(SoundHelper.AUDIO.purchasing);
            shopItem.purchase();

            Background background = Background.get(Constants.BACKGROUND_SUMMER);
            if (!background.isUnlocked()) {
                background.unlock();
                AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("SHOP_ITEM_PURCHASED_BACKGROUND"),
                        shopItem.getCategoryId() == Constants.STORE_CATEGORY_TILES ? TileType.get(shopItem.getSubcategoryId()).getName() : shopItem.getName(),
                        shopItem.getPrice(),
                        background.getName()));
            } else {
                AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("SHOP_ITEM_PURCHASED"),
                        shopItem.getCategoryId() == Constants.STORE_CATEGORY_TILES ? TileType.get(shopItem.getSubcategoryId()).getName() : shopItem.getName(),
                        shopItem.getPrice()));
            }
        }

        shopItem = ShopItem.get(shopItem.getItemId());
        populateItemInfo();
    }

    public void closePopup(View v) {
        this.finish();
    }
}

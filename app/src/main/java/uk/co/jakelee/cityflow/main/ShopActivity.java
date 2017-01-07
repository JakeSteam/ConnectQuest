package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AdvertHelper;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Iap;
import uk.co.jakelee.cityflow.model.ShopCategory;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;

public class ShopActivity extends Activity {
    private DisplayHelper dh;
    private int selectedCategory = 1;
    private int preselectedItem = 0;
    private TJPlacement offerWall;
    private TJPlacement watchAdvert;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        dh = DisplayHelper.getInstance(this);

        TJPlacementListener placementListener = AdvertHelper.getInstance(this);
        offerWall = new TJPlacement(this, "OfferWall", placementListener);
        watchAdvert = new TJPlacement(this, "WatchAdvert", placementListener);
        offerWall.requestContent();
        watchAdvert.requestContent();

        Intent intent = getIntent();
        preselectedItem = intent.getIntExtra(Constants.INTENT_ITEM, 0);

        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tapjoy.onActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Tapjoy.onActivityStop(this);
        handler.removeCallbacksAndMessages(null);

        SoundHelper.stopIfExiting(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateText();
        populateTabs();
        populateItems();

        if (preselectedItem > 0) {
            displayInformation(ShopItem.get(preselectedItem));
            preselectedItem = 0;
        }

        final Activity activity = this;
        final Runnable everyFiveSeconds = new Runnable() {
            @Override
            public void run() {
                Tapjoy.getCurrencyBalance(new TJGetCurrencyBalanceListener() {
                    @Override
                    public void onGetCurrencyBalanceResponse(String currencyName, int balance) {
                        if (AdvertHelper.synchroniseCoins(activity, balance)) {
                            populateText();
                        }
                    }

                    @Override
                    public void onGetCurrencyBalanceResponseFailure(String error) {
                    }
                });
                handler.postDelayed(this, DateHelper.MILLISECONDS_IN_SECOND * 5);
            }
        };
        handler.post(everyFiveSeconds);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacksAndMessages(null);
    }

    private void populateText() {
        ((TextView) findViewById(R.id.freeCoinsBanner)).setText(Text.get("SHOP_BANNER"));
        ((TextView) findViewById(R.id.freeCurrencyAdvert)).setText(Text.get("SHOP_ADVERT"));
        ((TextView) findViewById(R.id.freeCurrencyOffers)).setText(Text.get("SHOP_OFFERS"));
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    private void populateTabs() {
        LinearLayout tabContainer = (LinearLayout) findViewById(R.id.tabContainer);
        tabContainer.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        LayoutInflater inflater = LayoutInflater.from(this);

        List<ShopCategory> categories = ShopCategory.listAll(ShopCategory.class);
        for (ShopCategory category : categories) {
            TextView categoryTab = (TextView) inflater.inflate(R.layout.custom_store_tab, null);
            categoryTab.setBackgroundResource(selectedCategory == category.getCategoryId() ? R.color.city : R.color.ltltgrey);
            categoryTab.setText(category.getName());
            categoryTab.setTag(category.getCategoryId());
            tabContainer.addView(categoryTab, lp);
        }
    }

    public void populateItems() {
        TableLayout itemContainer = (TableLayout) findViewById(R.id.itemContainer);
        itemContainer.removeAllViews();

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        int margins = dh.dpToPixel(5);
        layoutParams.setMargins(margins, margins, margins, margins);

        List<ShopItem> items = ShopCategory.getItems(selectedCategory);
        int numItems = items.size();
        int numItemsProcessed = 0;
        TableRow row = new TableRow(this);

        for (ShopItem item : items) {
            row.addView(dh.createItemSelectButton(this, item), layoutParams);
            numItemsProcessed++;
            if (numItemsProcessed % 3 == 0 || numItemsProcessed == numItems) {
                itemContainer.addView(row);
                row = new TableRow(this);
            }
        }
    }

    public void changeTab(View view) {
        selectedCategory = (int) view.getTag();
        populateTabs();
        populateItems();
    }

    public void buyCoins(View view) {
        Intent intent = new Intent(this, IAPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void displayInformation(ShopItem item) {
        Intent intent = new Intent(this, ShopItemActivity.class);
        intent.putExtra(Constants.INTENT_ITEM, item.getItemId());
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void launchAdvert(View v) {
        AdvertHelper.getInstance(getApplicationContext()).showAdvert(this, watchAdvert);
    }

    public void launchOffers(View v) {
        if (Tapjoy.isConnected()) {
            if (offerWall.isContentReady()) {
                offerWall.showContent();
            } else {
                offerWall.requestContent();
            }
        }
    }

    public void advertWatched() {
        Statistic.addCurrency((Iap.hasCoinDoubler() ? 2 : 1) * Constants.CURRENCY_ADVERT);
        AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("ALERT_COINS_EARNED_FREE"), Constants.CURRENCY_ADVERT));
        GooglePlayHelper.UpdateEvent(Constants.EVENT_WATCH_ADVERT, 1);

        populateText();
    }
}

package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.AdvertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.StyleHelper;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.StoreCategory;
import uk.co.jakelee.cityflow.model.StoreItem;
import uk.co.jakelee.cityflow.model.Text;

public class ShopActivity extends Activity {
    private DisplayHelper dh;
    private int selectedCategory = 1;
    private AdvertHelper ah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        dh = DisplayHelper.getInstance(this);
        ah = AdvertHelper.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateText();
        populateTabs();
        populateItems();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.freeCurrencyText)).setText(Text.get("SHOP_FREE_CURRENCY"));
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    private void populateTabs() {
        LinearLayout tabContainer = (LinearLayout) findViewById(R.id.tabContainer);
        tabContainer.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        LayoutInflater inflater = LayoutInflater.from(this);

        List<StoreCategory> categories = StoreCategory.listAll(StoreCategory.class);
        for (StoreCategory category : categories) {
            TextView categoryTab = (TextView) inflater.inflate(R.layout.custom_store_tab, null);
            categoryTab.setBackgroundResource(selectedCategory == category.getCategoryId() ? R.drawable.ui_panel_city : R.drawable.ui_panel_grey);
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

        List<StoreItem> items = StoreItem.getByCategory(selectedCategory);
        int numItems = items.size();
        TableRow row = new TableRow(this);

        for (int itemIndex = 1; itemIndex <= numItems; itemIndex++) {
            StoreItem item = items.get(itemIndex - 1);
            row.addView(dh.createItemSelectButton(this, item), layoutParams);

            if (itemIndex % 3 == 0 || itemIndex == numItems) {
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

    public void displayInformation(StoreItem item) {
        Intent intent = new Intent(this, ShopItemActivity.class);
        intent.putExtra(Constants.INTENT_ITEM, item.getItemId());
        startActivity(intent);
    }

    public void watchAdvert(View v) {
        AdvertHelper.getInstance(this).showAdvert(this);
    }

    public void advertWatched() {
        Statistic.addCurrency(Constants.CURRENCY_ADVERT);
        Crouton.showText(this, "Earned " + Constants.CURRENCY_ADVERT + " coins!", StyleHelper.SUCCESS);

        populateText();
    }
}

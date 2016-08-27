package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.StoreItem;

public class ShopActivity extends Activity {
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        dh = DisplayHelper.getInstance(this);
    }

    private void populateText() {
        ((TextView) findViewById(R.id.freeCurrencyText)).setText("Free Currency");
        ((TextView) findViewById(R.id.currencyCountText)).setText(Integer.toString(Statistic.getCurrency()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateText();
        populateItems();
    }

    public void populateItems() {
        TableLayout itemContainer = (TableLayout) findViewById(R.id.itemContainer);
        itemContainer.removeAllViews();

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        int margins = dh.dpToPixel(5);
        layoutParams.setMargins(margins, margins, margins, margins);

        List<StoreItem> items = StoreItem.listAll(StoreItem.class);
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

    public void displayInformation(StoreItem item) {
        Intent intent = new Intent(this, ShopItemActivity.class);
        intent.putExtra(Constants.INTENT_ITEM, item.getItemId());
        startActivity(intent);
    }
}

package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.ErrorHelper;

public class StoreItem extends SugarRecord {
    private int itemId;
    private int price;
    private int purchases;
    private int maxPurchases;
    private boolean applyMultiplier;

    public StoreItem() {
    }

    public StoreItem(int itemId, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.price = price;
        this.purchases = 0;
        this.maxPurchases = maxPurchases;
        this.applyMultiplier = applyMultiplier;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    public int getMaxPurchases() {
        return maxPurchases;
    }

    public void setMaxPurchases(int maxPurchases) {
        this.maxPurchases = maxPurchases;
    }

    public boolean isApplyMultiplier() {
        return applyMultiplier;
    }

    public void setApplyMultiplier(boolean applyMultiplier) {
        this.applyMultiplier = applyMultiplier;
    }

    public static StoreItem get(int itemId)  {
        return Select.from(StoreItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
    }

    public String getName() {
        return Text.get("ITEM_", getItemId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("ITEM_", getItemId(), "_DESC");
    }

    public ErrorHelper.Error tryPurchase() {
        StoreItem item = Select.from(StoreItem.class).where(
                Condition.prop("item_id").eq(getItemId())).first();

        Statistic currency = Select.from(Statistic.class).where(
                Condition.prop("enum_name").eq(Statistic.Fields.Currency)).first();

        if (item == null) {
            return ErrorHelper.Error.TECHNICAL;
        } else if (item.getMaxPurchases() != 0 && item.getPurchases() >= item.getMaxPurchases()) {
            return ErrorHelper.Error.MAX_PURCHASES;
        } else if (item.getPrice() > currency.getIntValue()) {
            return ErrorHelper.Error.NOT_ENOUGH_CURRENCY;
        }

        item.setPurchases(item.getPurchases() + 1);
        item.save();

        currency.setIntValue(currency.getIntValue() - item.getPrice());
        currency.save();

        return ErrorHelper.Error.NO_ERROR;
    }
}

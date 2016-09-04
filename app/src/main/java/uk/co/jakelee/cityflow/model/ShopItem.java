package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;

public class ShopItem extends SugarRecord {
    private int itemId;
    private int categoryId;
    private int rewardId;
    private int rewardQuantity;
    private int price;
    private int purchases;
    private int maxPurchases;
    private boolean applyMultiplier;

    public ShopItem() {
    }

    public ShopItem(int itemId, int categoryId, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.rewardId = 0;
        this.rewardQuantity = 0;
        this.price = price;
        this.purchases = 0;
        this.maxPurchases = maxPurchases;
        this.applyMultiplier = applyMultiplier;
    }

    public ShopItem(int itemId, int categoryId, int boostId, int boostQuantity, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.rewardId = boostId;
        this.rewardQuantity = boostQuantity;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public int getRewardQuantity() {
        return rewardQuantity;
    }

    public void setRewardQuantity(int rewardQuantity) {
        this.rewardQuantity = rewardQuantity;
    }

    public int getPrice() {
        if (!applyMultiplier()) {
            return price;
        }
        return (purchases + 1) * price;
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

    public boolean applyMultiplier() {
        return applyMultiplier;
    }

    public void setApplyMultiplier(boolean applyMultiplier) {
        this.applyMultiplier = applyMultiplier;
    }

    public static ShopItem get(int itemId)  {
        return Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
    }

    public static List<ShopItem> getByCategory(int categoryId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("category_id").eq(categoryId)).list();
    }

    public boolean atMaxPurchases() {
        return getMaxPurchases() != 0 && getPurchases() >= getMaxPurchases();
    }

    public String getName() {
        return Text.get("ITEM_", getItemId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("ITEM_", getItemId(), "_DESC");
    }

    public static ShopItem find(int itemId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
    }

    public void purchase() {
        ShopItem item = ShopItem.find(getItemId());
        Statistic currency = Statistic.find(Constants.STATISTIC_CURRENCY);

        item.setPurchases(item.getPurchases() + 1);
        item.save();

        currency.setIntValue(currency.getIntValue() - item.getPrice());
        currency.save();

        if (getCategoryId() == Constants.STORE_CATEGORY_BOOSTS) {
            GooglePlayHelper.UpdateEvent(Constants.EVENT_BUY_BOOST, getRewardQuantity());
            Boost boost = Boost.get(getRewardId());
            boost.setOwned(boost.getOwned() + getRewardQuantity());
            boost.save();
        } else if (getCategoryId() == Constants.STORE_CATEGORY_UPGRADES) {
            Boost boost = Boost.get(getRewardId());
            boost.setLevel(boost.getLevel() + 1);
            boost.save();
        } else if (getCategoryId() == Constants.STORE_CATEGORY_TILES) {
            TileType tileType = TileType.get(getRewardId());
            tileType.setPuzzleRequired(1);
            tileType.save();
        } else if (getItemId() == Constants.ITEM_UNLOCK_PACK) {
            // Unlock the next pack I guess?
        }
    }

    public ErrorHelper.Error canPurchase() {
        ShopItem item = Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(getItemId())).first();

        Statistic currency = Statistic.find(Constants.STATISTIC_CURRENCY);

        if (item == null) {
            return ErrorHelper.Error.TECHNICAL;
        } else if (item.atMaxPurchases()) {
            return ErrorHelper.Error.MAX_PURCHASES;
        } else if (item.getPrice() > currency.getIntValue()) {
            return ErrorHelper.Error.NOT_ENOUGH_CURRENCY;
        }
        return ErrorHelper.Error.NO_ERROR;
    }

    public static boolean isPurchased(int itemId) {
        ShopItem item = Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
        return !(item == null || item.getPurchases() <= 0);
    }
}

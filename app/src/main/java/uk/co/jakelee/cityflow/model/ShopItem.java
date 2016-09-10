package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ErrorHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;

public class ShopItem extends SugarRecord {
    private int itemId;
    private int categoryId;
    private int subcategoryId;
    private int miscData;
    private int price;
    private int purchases;
    private int maxPurchases;
    private boolean applyMultiplier;

    public ShopItem() {
    }

    public ShopItem(int itemId, int categoryId, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.subcategoryId = 0;
        this.miscData = 0;
        this.price = price;
        this.purchases = 0;
        this.maxPurchases = maxPurchases;
        this.applyMultiplier = applyMultiplier;
    }

    public ShopItem(int itemId, int categoryId, int boostId, int boostQuantity, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.subcategoryId = boostId;
        this.miscData = boostQuantity;
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

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public int getMiscData() {
        return miscData;
    }

    public void setMiscData(int miscData) {
        this.miscData = miscData;
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

    public boolean atMaxPurchases() {
        return getMaxPurchases() != 0 && getPurchases() >= getMaxPurchases();
    }

    public String getName() {
        return Text.get("ITEM_", getItemId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("ITEM_", getItemId(), "_DESC");
    }

    public static ShopItem get(int itemId)  {
        return Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
    }

    public static ShopItem getPackItem(int packId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("category_id").eq(Constants.STORE_CATEGORY_MISC),
                Condition.prop("subcategory_id").eq(Constants.STORE_SUBCATEGORY_PACK),
                Condition.prop("misc_data").eq(packId)).first();
    }

    public void purchase() {
        ShopItem item = ShopItem.get(getItemId());
        Statistic currency = Statistic.find(Constants.STATISTIC_CURRENCY);

        item.setPurchases(item.getPurchases() + 1);
        item.save();

        currency.setIntValue(currency.getIntValue() - item.getPrice());
        currency.save();

        // Go through each category first, then the misc subcategories
        if (getCategoryId() == Constants.STORE_CATEGORY_BOOSTS) {
            GooglePlayHelper.UpdateEvent(Constants.EVENT_BUY_BOOST, getMiscData());
            Boost boost = Boost.get(getSubcategoryId());
            boost.setOwned(boost.getOwned() + getMiscData());
            boost.save();
        } else if (getCategoryId() == Constants.STORE_CATEGORY_UPGRADES) {
            Boost boost = Boost.get(getSubcategoryId());
            boost.setLevel(boost.getLevel() + 1);
            boost.save();
        } else if (getCategoryId() == Constants.STORE_CATEGORY_TILES) {
            TileType tileType = TileType.get(getSubcategoryId());
            tileType.setPuzzleRequired(1);
            tileType.save();
        } else if (getSubcategoryId() == Constants.STORE_SUBCATEGORY_PACK) {
            Pack targetPack = Pack.getPack(getMiscData());
            targetPack.setPurchased(true);
            targetPack.save();
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

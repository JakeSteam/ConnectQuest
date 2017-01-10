package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.EncryptHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;

public class ShopItem extends SugarRecord {
    private int itemId;
    private int categoryId;
    private int subcategoryId;
    private int miscData;
    private String price;
    private String purchases;
    private int maxPurchases;
    private boolean applyMultiplier;

    public ShopItem() {
    }

    public ShopItem(int itemId, int categoryId, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.subcategoryId = 0;
        this.miscData = 0;
        this.price = EncryptHelper.encode(price, itemId);
        this.purchases = EncryptHelper.encode(0, itemId);
        this.maxPurchases = maxPurchases;
        this.applyMultiplier = applyMultiplier;
    }

    public ShopItem(int itemId, int categoryId, int subcategoryId, int miscData, int price, int maxPurchases, boolean applyMultiplier) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.miscData = miscData;
        this.price = EncryptHelper.encode(price, itemId);
        this.purchases = EncryptHelper.encode(0, itemId);
        this.maxPurchases = maxPurchases;
        this.applyMultiplier = applyMultiplier;
    }

    public static ShopItem get(int itemId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
    }

    public static ShopItem getPackItem(int packId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("category_id").eq(Constants.STORE_CATEGORY_MISC),
                Condition.prop("subcategory_id").eq(Constants.STORE_SUBCATEGORY_PACK),
                Condition.prop("misc_data").eq(packId)).first();
    }

    public static boolean isPurchased(int itemId) {
        ShopItem item = Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(itemId)).first();
        return !(item == null || item.getPurchases() <= 0);
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
            return EncryptHelper.decodeToInt(price, itemId);
        }
        return (EncryptHelper.decodeToInt(purchases, itemId) + 1) * EncryptHelper.decodeToInt(price, itemId);
    }

    public void setPrice(int price) {
        this.price = EncryptHelper.encode(price, itemId);
    }

    public int getPurchases() {
        return EncryptHelper.decodeToInt(purchases, itemId);
    }

    public void setPurchases(int purchases) {
        this.purchases = EncryptHelper.encode(purchases, itemId);
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

    public void purchase() {
        ShopItem item = ShopItem.get(getItemId());
        Statistic currency = Statistic.find(Constants.STATISTIC_CURRENCY);

        currency.setIntValue(currency.getIntValue() - item.getPrice());
        currency.save();

        item.setPurchases(item.getPurchases() + 1);
        item.save();

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
            tileType.setStatus(Constants.TILE_STATUS_UNLOCKED);
            tileType.save();
        } else if (getSubcategoryId() == Constants.STORE_SUBCATEGORY_PACK) {
            Pack targetPack = Pack.getPack(getMiscData());
            targetPack.setPurchased(true);
            targetPack.save();
        } else if (getItemId() == Constants.ITEM_ZEN_MODE) {
            Setting zenMode = Setting.get(Constants.SETTING_ZEN_MODE);
            zenMode.setBooleanValue(true);
            zenMode.save();
        }
    }

    public AlertHelper.Error canPurchase() {
        ShopItem item = Select.from(ShopItem.class).where(
                Condition.prop("item_id").eq(getItemId())).first();

        Statistic currency = Statistic.find(Constants.STATISTIC_CURRENCY);

        if (item == null) {
            return AlertHelper.Error.TECHNICAL;
        } else if (item.atMaxPurchases() || item.isUnlockedPack()) {
            return AlertHelper.Error.MAX_PURCHASES;
        } else if (item.getPrice() > currency.getIntValue()) {
            return AlertHelper.Error.NOT_ENOUGH_CURRENCY;
        }
        return AlertHelper.Error.NO_ERROR;
    }

    public boolean isUnlockedPack() {
        return (getCategoryId() == Constants.STORE_CATEGORY_MISC
                && getSubcategoryId() == Constants.STORE_SUBCATEGORY_PACK
                && Pack.getPack(getMiscData()).isUnlocked());
    }
}

package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.EncryptHelper;

public class Iap extends SugarRecord {
    private String iapCode;
    private String purchases;
    private String maxPurchases;
    private String coins;

    public Iap() {
    }

    public Iap(String iapCode, int coins, int maxPurchases) {
        this.iapCode = iapCode;
        this.purchases = EncryptHelper.encode(0, 234);
        this.maxPurchases = EncryptHelper.encode(maxPurchases, 333);
        this.coins = EncryptHelper.encode(coins, 456);
    }

    public static Iap get(String code) {
        return Select.from(Iap.class).where(
                Condition.prop("iap_code").eq(code)).first();
    }

    public static boolean hasCoinDoubler() {
        Iap iap = Iap.get("x2_coins");
        return iap != null && iap.getPurchases() > 0;
    }

    public static boolean hasAllTiles() {
        Iap iap = Iap.get("all_tiles");
        return iap != null && iap.getPurchases() > 0;
    }

    public static boolean hasPurchasedAnything() {
        boolean hasPurchased = false;
        List<Iap> iaps = Iap.listAll(Iap.class);
        for (Iap iap : iaps) {
            if (iap.getPurchases() > 0) {
                hasPurchased = true;
                break;
            }
        }
        return hasPurchased;
    }

    public String getIapCode() {
        return iapCode;
    }

    public void setIapCode(String iapCode) {
        this.iapCode = iapCode;
    }

    public int getPurchases() {
        return EncryptHelper.decodeToInt(purchases, 234);
    }

    public void setPurchases(int purchases) {
        this.purchases = EncryptHelper.encode(purchases, 234);
    }

    public int getMaxPurchases() {
        return EncryptHelper.decodeToInt(maxPurchases, 333);
    }

    public void setMaxPurchases(int maxPurchases) {
        this.maxPurchases = EncryptHelper.encode(maxPurchases, 333);
    }

    public int getCoins() {
        return EncryptHelper.decodeToInt(coins, 456);
    }

    public void setCoins(int coins) {
        this.coins = EncryptHelper.encode(coins, 456);
    }

    public String getName() {
        switch (iapCode) {
            case "100_coins":
            case "1000_coins":
                return getCoins() + " " + Text.get("STATISTIC_6_NAME");
            case "x2_coins":
                return Text.get("IAP_COIN_DOUBLER");
            case "all_tiles":
                return Text.get("IAP_UNLOCK_ALL_TILES");
            default:
                return "";
        }
    }

    public void purchase() {
        if (getCoins() > 0) {
            Statistic.addCurrency(getCoins());
        }
        setPurchases(getPurchases() + 1);
        save();
    }
}

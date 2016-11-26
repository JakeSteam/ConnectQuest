package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.EncryptHelper;

public class Iap extends SugarRecord{
    private String iapCode;
    private String purchases;
    private String coins;

    public Iap() {
    }

    public Iap(String iapCode, int coins) {
        this.iapCode = iapCode;
        this.purchases = EncryptHelper.encode(0, 234);
        this.coins = EncryptHelper.encode(coins, 456);
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

    public int getCoins() {
        return EncryptHelper.decodeToInt(coins, 456);
    }

    public void setCoins(int coins) {
        this.coins = EncryptHelper.encode(coins, 456);
    }

    public static Iap get(String code) {
        return Select.from(Iap.class).where(
                Condition.prop("iap_code").eq(code)).first();
    }

    public void purchase() {
        Statistic.addCurrency(getCoins());
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
}

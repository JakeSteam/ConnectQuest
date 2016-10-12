package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Iap extends SugarRecord{
    private String iapCode;
    private int purchases;
    private int coins;

    public Iap() {
    }

    public Iap(String iapCode, int coins) {
        this.iapCode = iapCode;
        this.purchases = 0;
        this.coins = coins;
    }

    public String getIapCode() {
        return iapCode;
    }

    public void setIapCode(String iapCode) {
        this.iapCode = iapCode;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
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

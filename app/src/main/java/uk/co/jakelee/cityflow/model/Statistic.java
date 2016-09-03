package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.EncryptHelper;

public class Statistic extends SugarRecord {
    private int statisticId;
    private String stringValue;
    private String intValue;
    private String boolValue;
    private int lastSentValue;

    public Statistic() {
    }

    public Statistic(int statisticId, String stringValue) {
        this.statisticId = statisticId;
        this.stringValue = EncryptHelper.encode(stringValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, int intValue) {
        this.statisticId = statisticId;
        this.intValue = EncryptHelper.encode(intValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, boolean boolValue) {
        this.statisticId = statisticId;
        this.boolValue = EncryptHelper.encode(boolValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, int intValue, int lastSentValue) {
        this.statisticId = statisticId;
        this.intValue = EncryptHelper.encode(intValue, statisticId);
        this.lastSentValue = lastSentValue;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        this.statisticId = statisticId;
    }

    public String getStringValue() {
        return EncryptHelper.decode(stringValue, statisticId);
    }

    public void setStringValue(String stringValue) {
        this.stringValue = EncryptHelper.encode(stringValue, statisticId);
    }

    public int getIntValue() {
        return EncryptHelper.decodeToInt(intValue, statisticId);
    }

    public void setIntValue(int intValue) {
        this.intValue = EncryptHelper.encode(intValue, statisticId);
    }

    public boolean isBoolValue() {
        return EncryptHelper.decodeToBool(boolValue, statisticId);
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = EncryptHelper.encode(boolValue, statisticId);
    }

    public int getLastSentValue() {
        return lastSentValue;
    }

    public void setLastSentValue(int lastSentValue) {
        this.lastSentValue = lastSentValue;
    }

    public String getName() {
        return Text.get("STATISTIC_", getStatisticId(), "_NAME");
    }

    public static void increaseByOne(int statisticId) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(statisticId)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + 1);
            statToIncrease.save();
        }
    }

    public static void increaseByX(int statisticId, int value) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(statisticId)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + value);
            statToIncrease.save();
        }
    }

    public static int get(int statisticId) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(statisticId)).first();

        if (statToIncrease != null) {
            return statToIncrease.getIntValue();
        }
        return 0;
    }

    public static Statistic find(int statisticId) {
        return Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(statisticId)).first();
    }

    public static int getCurrency() {
        return Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(Constants.STATISTIC_CURRENCY)).first().getIntValue();
    }

    public static void addCurrency(int amount) {
        increaseByX(Constants.STATISTIC_CURRENCY, amount);
    }

    public enum Fields {
        PuzzlesCompleted, TilesRotated, QuestsCompleted, PuzzlesCompletedFully, BoostsUsed, CompletePack1, CompletePack2, CompletePack3, Currency, TapJoyCoinsEarned
    }
}

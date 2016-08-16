package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ModificationHelper;

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
        this.stringValue = ModificationHelper.encode(stringValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, int intValue) {
        this.statisticId = statisticId;
        this.intValue = ModificationHelper.encode(intValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, boolean boolValue) {
        this.statisticId = statisticId;
        this.boolValue = ModificationHelper.encode(boolValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, int intValue, int lastSentValue) {
        this.statisticId = statisticId;
        this.intValue = ModificationHelper.encode(intValue, statisticId);
        this.lastSentValue = lastSentValue;
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        this.statisticId = statisticId;
    }

    public String getStringValue() {
        return ModificationHelper.decode(stringValue, statisticId);
    }

    public void setStringValue(String stringValue) {
        this.stringValue = ModificationHelper.encode(stringValue, statisticId);
    }

    public int getIntValue() {
        return ModificationHelper.decodeToInt(intValue, statisticId);
    }

    public void setIntValue(int intValue) {
        this.intValue = ModificationHelper.encode(intValue, statisticId);
    }

    public boolean isBoolValue() {
        return ModificationHelper.decodeToBool(boolValue, statisticId);
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = ModificationHelper.encode(boolValue, statisticId);
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

    public static void increaseByOne(Fields statistic) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("name").eq(statistic)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + 1);
            statToIncrease.save();
        }
    }

    public static void increaseByX(Fields statistic, int value) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("name").eq(statistic)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + value);
            statToIncrease.save();
        }
    }

    public static int getTotalStars() {
        return 10;
    }

    public enum Fields {
        QuestsCompleted
    }
}

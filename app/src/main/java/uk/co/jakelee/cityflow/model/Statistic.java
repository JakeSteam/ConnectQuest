package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.ModificationHelper;

public class Statistic extends SugarRecord {
    private int statisticId;
    private Fields enumName;
    private String stringValue;
    private String intValue;
    private String boolValue;
    private int lastSentValue;

    public Statistic() {
    }

    public Statistic(int statisticId, Fields enumName, String stringValue) {
        this.statisticId = statisticId;
        this.enumName = enumName;
        this.stringValue = ModificationHelper.encode(stringValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, Fields enumName, int intValue) {
        this.statisticId = statisticId;
        this.enumName = enumName;
        this.intValue = ModificationHelper.encode(intValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, Fields enumName, boolean boolValue) {
        this.statisticId = statisticId;
        this.enumName = enumName;
        this.boolValue = ModificationHelper.encode(boolValue, statisticId);
        this.lastSentValue = Constants.STATISTIC_UNTRACKED;
    }

    public Statistic(int statisticId, Fields enumName, int intValue, int lastSentValue) {
        this.statisticId = statisticId;
        this.enumName = enumName;
        this.intValue = ModificationHelper.encode(intValue, statisticId);
        this.lastSentValue = lastSentValue;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        this.statisticId = statisticId;
    }

    public Fields getEnumName() {
        return enumName;
    }

    public void setEnumName(Fields enumName) {
        this.enumName = enumName;
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
                Condition.prop("enum_name").eq(statistic)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + 1);
            statToIncrease.save();
        }
    }

    public static void increaseByOne(int statisticId) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("statistic_id").eq(statisticId)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + 1);
            statToIncrease.save();
        }
    }

    public static void increaseByX(Fields statistic, int value) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("enum_name").eq(statistic)).first();

        if (statToIncrease != null) {
            statToIncrease.setIntValue(statToIncrease.getIntValue() + value);
            statToIncrease.save();
        }
    }

    public static int get(Fields statistic) {
        Statistic statToIncrease = Select.from(Statistic.class).where(
                Condition.prop("enum_name").eq(statistic)).first();

        if (statToIncrease != null) {
            return statToIncrease.getIntValue();
        }
        return 0;
    }

    public enum Fields {
        PuzzlesCompleted, TilesRotated, QuestsCompleted, PuzzlesCompletedFully, BoostsUsed, CompletePack1, CompletePack2, CompletePack3
    }
}

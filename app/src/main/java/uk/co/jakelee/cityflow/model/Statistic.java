package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

import uk.co.jakelee.cityflow.helper.ModificationHelper;

public class Statistic extends SugarRecord {
    private int statisticId;
    private String stringValue;
    private String intValue;
    private String boolValue;

    public Statistic() {
    }

    public Statistic(int statisticId, String stringValue) {
        this.statisticId = statisticId;
        this.stringValue = ModificationHelper.encode(stringValue, statisticId);
    }

    public Statistic(int statisticId, int intValue) {
        this.statisticId = statisticId;
        this.intValue = ModificationHelper.encode(intValue, statisticId);
    }

    public Statistic(int statisticId, boolean boolValue) {
        this.statisticId = statisticId;
        this.boolValue = ModificationHelper.encode(boolValue, statisticId);
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

    public String getName() {
        return Text.get("STATISTIC_", getStatisticId(), "_NAME");
    }
}

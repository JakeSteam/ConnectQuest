package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Statistic extends SugarRecord {
    private int statisticId;
    private String stringValue;
    private int intValue;
    private boolean boolValue;

    public Statistic() {
    }

    public Statistic(int statisticId, String stringValue) {
        this.statisticId = statisticId;
        this.stringValue = stringValue;
    }

    public Statistic(int statisticId, int intValue) {
        this.statisticId = statisticId;
        this.intValue = intValue;
    }

    public Statistic(int statisticId, boolean boolValue) {
        this.statisticId = statisticId;
        this.boolValue = boolValue;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        this.statisticId = statisticId;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public String getName() {
        return Text.get("STATISTIC_", getStatisticId());
    }
}

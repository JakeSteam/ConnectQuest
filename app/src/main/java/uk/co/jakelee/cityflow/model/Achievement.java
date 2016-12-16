package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Achievement extends SugarRecord {
    private String name;
    private int maximumValue;
    private long statisticId;
    private String remoteID;
    private int colourID;

    public Achievement() {
    }

    public Achievement(String name, int maximumValue, long statisticId, String remoteID) {
        this.name = name;
        this.maximumValue = maximumValue;
        this.statisticId = statisticId;
        this.remoteID = remoteID;
        this.colourID = 0;
    }

    public Achievement(String name, int maximumValue, long statisticId, String remoteID, int colourID) {
        this.name = name;
        this.maximumValue = maximumValue;
        this.statisticId = statisticId;
        this.remoteID = remoteID;
        this.colourID = colourID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
    }

    public long getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(long statisticId) {
        this.statisticId = statisticId;
    }

    public String getRemoteID() {
        return remoteID;
    }

    public void setRemoteID(String remoteID) {
        this.remoteID = remoteID;
    }

    public int getColourID() {
        return colourID;
    }

    public void setColourID(int colourID) {
        this.colourID = colourID;
    }
}

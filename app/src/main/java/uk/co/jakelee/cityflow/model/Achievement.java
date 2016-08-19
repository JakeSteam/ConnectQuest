package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Achievement extends SugarRecord {
    private String name;
    private String description;
    private int maximumValue;
    private long statisticId;
    private String remoteID;

    public Achievement() {
    }

    public Achievement(String name, String description, int maximumValue, long statisticId, String remoteID) {
        this.name = name;
        this.description = description;
        this.maximumValue = maximumValue;
        this.statisticId = statisticId;
        this.remoteID = remoteID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

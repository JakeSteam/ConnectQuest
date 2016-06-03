package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Flow extends SugarRecord {
    private int flowId;
    private String name;
    private String description;

    public Flow() {
    }

    public Flow(int flowId, String name, String description) {
        this.flowId = flowId;
        this.name = name;
        this.description = description;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
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
}

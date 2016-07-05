package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Tile_Type extends SugarRecord {
    private int typeId;
    private int environmentId;
    private int flowNorth;
    private int flowEast;
    private int flowSouth;
    private int flowWest;
    private int heightNorth;
    private int heightEast;
    private int heightSouth;
    private int heightWest;

    public Tile_Type() {

    }

    public Tile_Type(int typeId, int environmentId, int flow, int height) {
        this.typeId = typeId;
        this.environmentId = environmentId;
        this.flowNorth = flow;
        this.flowEast = flow;
        this.flowSouth = flow;
        this.flowWest = flow;
        this.heightNorth = height;
        this.heightEast = height;
        this.heightSouth = height;
        this.heightWest = height;
    }

    public Tile_Type(int typeId, int environmentId, int flowNorth, int flowEast, int flowSouth, int flowWest, int height) {
        this.typeId = typeId;
        this.environmentId = environmentId;
        this.flowNorth = flowNorth;
        this.flowEast = flowEast;
        this.flowSouth = flowSouth;
        this.flowWest = flowWest;
        this.heightNorth = height;
        this.heightEast = height;
        this.heightSouth = height;
        this.heightWest = height;
    }

    public Tile_Type(int typeId, int environmentId, int flowNorth, int flowEast, int flowSouth, int flowWest, int heightNorth, int heightEast, int heightSouth, int heightWest) {
        this.typeId = typeId;
        this.environmentId = environmentId;
        this.flowNorth = flowNorth;
        this.flowEast = flowEast;
        this.flowSouth = flowSouth;
        this.flowWest = flowWest;
        this.heightNorth = heightNorth;
        this.heightEast = heightEast;
        this.heightSouth = heightSouth;
        this.heightWest = heightWest;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    public int getFlowNorth() {
        return flowNorth;
    }

    public void setFlowNorth(int flowNorth) {
        this.flowNorth = flowNorth;
    }

    public int getFlowEast() {
        return flowEast;
    }

    public void setFlowEast(int flowEast) {
        this.flowEast = flowEast;
    }

    public int getFlowSouth() {
        return flowSouth;
    }

    public void setFlowSouth(int flowSouth) {
        this.flowSouth = flowSouth;
    }

    public int getFlowWest() {
        return flowWest;
    }

    public void setFlowWest(int flowWest) {
        this.flowWest = flowWest;
    }

    public int getHeightNorth() {
        return heightNorth;
    }

    public void setHeightNorth(int heightNorth) {
        this.heightNorth = heightNorth;
    }

    public int getHeightEast() {
        return heightEast;
    }

    public void setHeightEast(int heightEast) {
        this.heightEast = heightEast;
    }

    public int getHeightSouth() {
        return heightSouth;
    }

    public void setHeightSouth(int heightSouth) {
        this.heightSouth = heightSouth;
    }

    public int getHeightWest() {
        return heightWest;
    }

    public void setHeightWest(int heightWest) {
        this.heightWest = heightWest;
    }
}



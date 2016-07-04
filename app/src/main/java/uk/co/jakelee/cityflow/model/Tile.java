package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.TileHelper;

public class Tile extends SugarRecord {
    private int puzzleId;
    private int tileTypeId;
    private int x;
    private int y;
    private int rotation;

    public Tile() {
    }

    public Tile(int puzzleId, int tileTypeId, int x, int y, int rotation) {
        this.puzzleId = puzzleId;
        this.tileTypeId = tileTypeId;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public int getTileTypeId() {
        return tileTypeId;
    }

    public void setTileTypeId(int tileTypeId) {
        this.tileTypeId = tileTypeId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void rotate() {
        if (this.rotation == Constants.ROTATION_WEST) {
            this.rotation = Constants.ROTATION_NORTH;
        } else {
            this.rotation++;
        }
        this.save();
    }

    public int getNorthFlow() {
        int flow = Constants.FLOW_NONE;
        Tile_Type type = TileHelper.getTileType(this);

        if (rotation == Constants.ROTATION_NORTH) {
            flow = type.getFlowSouth();
        } else if (rotation == Constants.ROTATION_EAST) {
            flow = type.getFlowWest();
        } else if (rotation == Constants.ROTATION_SOUTH) {
            flow = type.getFlowNorth();
        } else if (rotation == Constants.ROTATION_WEST) {
            flow = type.getFlowEast();
        }
        return flow;
    }

    public int getSouthFlow() {
        int flow = Constants.FLOW_NONE;
        Tile_Type type = TileHelper.getTileType(this);

        if (rotation == Constants.ROTATION_SOUTH) {
            flow = type.getFlowSouth();
        } else if (rotation == Constants.ROTATION_WEST) {
            flow = type.getFlowWest();
        } else if (rotation == Constants.ROTATION_NORTH) {
            flow = type.getFlowNorth();
        } else if (rotation == Constants.ROTATION_EAST) {
            flow = type.getFlowEast();
        }
        return flow;
    }
}

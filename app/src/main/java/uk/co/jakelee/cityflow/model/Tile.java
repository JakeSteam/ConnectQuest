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
    private int defaultRotation;

    public Tile() {
    }

    public Tile(int puzzleId, int tileTypeId, int x, int y, int rotation) {
        this.puzzleId = puzzleId;
        this.tileTypeId = tileTypeId;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.defaultRotation = rotation;
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

    public int getDefaultRotation() {
        return defaultRotation;
    }

    public void setDefaultRotation(int defaultRotation) {
        this.defaultRotation = defaultRotation;
    }

    public void rotate(boolean undoing) {
        if (undoing) {
            if (this.rotation == Constants.ROTATION_NORTH) {
                this.rotation = Constants.ROTATION_WEST;
            } else {
                this.rotation--;
            }
        } else {
            if (this.rotation == Constants.ROTATION_WEST) {
                this.rotation = Constants.ROTATION_NORTH;
            } else {
                this.rotation++;
            }
        }
        this.save();
    }

    public int getFlow(int side) {
        int flow = Constants.FLOW_NONE;
        TileType type = TileHelper.getTileType(this);

        int target = side + (5 - rotation);
        if (target > 4) {
            target -= 4;
        }

        if (target == Constants.ROTATION_NORTH) {
            flow = type.getFlowNorth();
        } else if (target == Constants.ROTATION_EAST) {
            flow = type.getFlowEast();
        } else if (target == Constants.ROTATION_SOUTH) {
            flow = type.getFlowSouth();
        } else if (target == Constants.ROTATION_WEST) {
            flow = type.getFlowWest();
        }
        return flow;
    }

    public int getHeight(int side) {
        int height = Constants.HEIGHT_NORMAL;
        TileType type = TileHelper.getTileType(this);

        int target = side + (5 - rotation);
        if (target > 4) {
            target -= 4;
        }

        if (type.getTypeId() == 0) {
            return height;
        }

        if (target == Constants.ROTATION_NORTH) {
            height = type.getHeightNorth();
        } else if (target == Constants.ROTATION_EAST && type.getHeightEast() != Constants.HEIGHT_NORMAL) {
            height = type.getHeightEast();
        } else if (target == Constants.ROTATION_SOUTH && type.getHeightSouth() != Constants.HEIGHT_NORMAL) {
            height = type.getHeightSouth();
        } else if (target == Constants.ROTATION_WEST && type.getHeightWest() != Constants.HEIGHT_NORMAL) {
            height = type.getHeightWest();
        }
        return height;
    }

    public static Tile get(long tileId) {
        return Tile.findById(Tile.class, tileId);
    }
}

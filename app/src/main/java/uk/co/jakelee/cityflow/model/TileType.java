package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.TileHelper;

public class TileType extends SugarRecord {
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
    private int puzzleRequired;
    private int status;

    public TileType() {

    }

    private int statusFromPuzzleRequired(int puzzleRequired) {
        switch (puzzleRequired) {
            case Constants.TILE_UNPURCHASED:
                return Constants.TILE_STATUS_UNPURCHASED;
            case Constants.TILE_UNLOCKED:
                return Constants.TILE_STATUS_UNLOCKED;
            default:
                return Constants.TILE_STATUS_LOCKED;
        }
    }

    public TileType(int typeId, int environmentId, int flow, int height, int puzzleRequired) {
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
        this.puzzleRequired = puzzleRequired;
        this.status = statusFromPuzzleRequired(puzzleRequired);
    }

    public TileType(int typeId, int environmentId, int flowNorth, int flowEast, int flowSouth, int flowWest, int height, int puzzleRequired) {
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
        this.puzzleRequired = puzzleRequired;
        this.status = statusFromPuzzleRequired(puzzleRequired);
    }

    public TileType(int typeId, int environmentId, int flowNorth, int flowEast, int flowSouth, int flowWest, int heightNorth, int heightEast, int heightSouth, int heightWest, int puzzleRequired) {
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
        this.puzzleRequired = puzzleRequired;
        this.status = statusFromPuzzleRequired(puzzleRequired);
    }

    public static TileType get(int tileTypeId) {
        return Select.from(TileType.class).where(
                Condition.prop("type_id").eq(tileTypeId)).first();
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

    public int getPuzzleRequired() {
        return puzzleRequired;
    }

    public void setPuzzleRequired(int puzzleRequired) {
        this.puzzleRequired = puzzleRequired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return Text.get("TILE_", getTypeId(), "_NAME");
    }

    public Puzzle getRequiredPuzzle() {
        return Select.from(Puzzle.class).where(
                Condition.prop("puzzle_id").eq(puzzleRequired)).first();
    }

    public int getFlow(int side, int rotation) {
        int target = side + (5 - rotation);
        if (target > 4) {
            target -= 4;
        }

        if (target == Constants.ROTATION_NORTH) {
            return getFlowNorth();
        } else if (target == Constants.ROTATION_EAST) {
            return getFlowEast();
        } else if (target == Constants.ROTATION_SOUTH) {
            return getFlowSouth();
        } else if (target == Constants.ROTATION_WEST) {
            return getFlowWest();
        }
        return Constants.FLOW_NONE;
    }

    public int getHeight(int side, int rotation) {
        int height = Constants.HEIGHT_NORMAL;
        if (TileHelper.tileIsInvisible(getTypeId())) {
            return height;
        }

        int target = side + (5 - rotation);
        if (target > 4) {
            target -= 4;
        }

        if (target == Constants.ROTATION_NORTH) {
            return getHeightNorth();
        } else if (target == Constants.ROTATION_EAST && getHeightEast() != Constants.HEIGHT_NORMAL) {
           return getHeightEast();
        } else if (target == Constants.ROTATION_SOUTH && getHeightSouth() != Constants.HEIGHT_NORMAL) {
            return getHeightSouth();
        } else if (target == Constants.ROTATION_WEST && getHeightWest() != Constants.HEIGHT_NORMAL) {
            return getHeightWest();
        }
        return height;
    }
}



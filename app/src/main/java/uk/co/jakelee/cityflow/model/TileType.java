package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.EncryptHelper;

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
    private String puzzleRequired;
    private int status;

    public TileType() {

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
        this.puzzleRequired = EncryptHelper.encode(puzzleRequired, typeId);
        this.status = Constants.TILE_STATUS_UNLOCKED;
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
        this.puzzleRequired = EncryptHelper.encode(puzzleRequired, typeId);
        this.status = Constants.TILE_STATUS_UNLOCKED;
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
        this.puzzleRequired = EncryptHelper.encode(puzzleRequired, typeId);
        this.status = Constants.TILE_STATUS_UNLOCKED;
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
        return EncryptHelper.decodeToInt(puzzleRequired, typeId);
    }

    public void setPuzzleRequired(int puzzleRequired) {
        this.puzzleRequired = EncryptHelper.encode(puzzleRequired, typeId);
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

    public static TileType get(int tileTypeId) {
        return Select.from(TileType.class).where(
                Condition.prop("type_id").eq(tileTypeId)).first();
    }

    public Puzzle getRequiredPuzzle() {
        return Select.from(Puzzle.class).where(
                Condition.prop("puzzle_id").eq(puzzleRequired)).first();
    }
}



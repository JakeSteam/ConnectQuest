package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;

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

    public static Tile get(long tileId) {
        return Tile.findById(Tile.class, tileId);
    }

    public static Tile get(int puzzleId, int x, int y) {
        return Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(puzzleId),
                Condition.prop("x").eq(x),
                Condition.prop("y").eq(y),
                Condition.prop("tile_type_id").gt(0)).first();
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

    public String getName() {
        return Text.get("TILE_", getTileTypeId(), "_NAME");
    }
}

package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Puzzle extends SugarRecord {
    private int puzzleId;
    private int packId;
    private long parTime;
    private int parMoves;
    private long bestTime;
    private int bestMoves;
    private boolean completionStar;
    private boolean timeStar;
    private boolean movesStar;

    public Puzzle() {
    }

    public Puzzle(int puzzleId, int packId, long parTime, int parMoves, long bestTime, int bestMoves) {
        this.puzzleId = puzzleId;
        this.packId = packId;
        this.parTime = parTime;
        this.parMoves = parMoves;
        this.bestTime = bestTime;
        this.bestMoves = bestMoves;
        this.completionStar = false;
        this.timeStar = false;
        this.movesStar = false;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public long getParTime() {
        return parTime;
    }

    public void setParTime(long parTime) {
        this.parTime = parTime;
    }

    public int getParMoves() {
        return parMoves;
    }

    public void setParMoves(int parMoves) {
        this.parMoves = parMoves;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public int getBestMoves() {
        return bestMoves;
    }

    public void setBestMoves(int bestMoves) {
        this.bestMoves = bestMoves;
    }

    public boolean hasCompletionStar() {
        return completionStar;
    }

    public void setCompletionStar(boolean completionStar) {
        this.completionStar = completionStar;
    }

    public boolean hasTimeStar() {
        return timeStar;
    }

    public void setTimeStar(boolean timeStar) {
        this.timeStar = timeStar;
    }

    public boolean hasMovesStar() {
        return movesStar;
    }

    public void setMovesStar(boolean movesStar) {
        this.movesStar = movesStar;
    }

    public String getName() {
        return Text.get("PUZZLE_", getPuzzleId(), "_NAME");
    }

    public List<Tile> getTiles() {
        return Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(this.getPuzzleId()))
                .orderBy("y DESC, x ASC").list();
    }

    public List<Tile_Type> getTilesUnlocked() {
        return Select.from(Tile_Type.class).where(
                Condition.prop("puzzle_required").eq(puzzleId)).list();
    }

    public static Puzzle getPuzzle(int puzzleId) {
        return Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(puzzleId)).first();
    }
}

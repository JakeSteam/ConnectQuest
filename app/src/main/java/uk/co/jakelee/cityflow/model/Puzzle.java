package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Puzzle extends SugarRecord {
    private int puzzleId;
    private int chapter;
    private int type;
    private int complexity;
    private long bestTime;
    private int bestMoves;
    private int bestRating;

    public Puzzle() {
    }

    public Puzzle(int puzzleId, int chapter, int type, int complexity, long bestTime, int bestMoves, int bestRating) {
        this.puzzleId = puzzleId;
        this.chapter = chapter;
        this.type = type;
        this.complexity = complexity;
        this.bestTime = bestTime;
        this.bestMoves = bestMoves;
        this.bestRating = bestRating;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public int getchapter() {
        return chapter;
    }

    public void setchapter(int chapter) {
        this.chapter = chapter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
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

    public int getBestRating() {
        return bestRating;
    }

    public void setBestRating(int bestRating) {
        this.bestRating = bestRating;
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = Tile.listAll(Tile.class);
        return Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(this.getPuzzleId())).list();
    }
}

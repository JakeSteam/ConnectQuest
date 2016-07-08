package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Puzzle extends SugarRecord {
    private int puzzleId;
    private int name;
    private int chapter;
    private int type;
    private long parTime;
    private int parMoves;
    private long bestTime;
    private int bestMoves;
    private int bestRating;

    public Puzzle() {
    }

    public Puzzle(int puzzleId, int name, int chapter, int type, long parTime, int parMoves, long bestTime, int bestMoves, int bestRating) {
        this.puzzleId = puzzleId;
        this.name = name;
        this.chapter = chapter;
        this.type = type;
        this.parTime = parTime;
        this.parMoves = parMoves;
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

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getBestRating() {
        return bestRating;
    }

    public void setBestRating(int bestRating) {
        this.bestRating = bestRating;
    }

    public List<Tile> getTiles() {
        return Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(this.getPuzzleId()))
                .orderBy("y DESC, x ASC").list();
    }

    public static Puzzle getPuzzle(int puzzleId) {
        return Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(puzzleId)).first();
    }

    public static List<Puzzle> getPuzzles(int puzzleType) {
        return Select.from(Puzzle.class).where(
                Condition.prop("type").eq(puzzleType)).list();
    }
}

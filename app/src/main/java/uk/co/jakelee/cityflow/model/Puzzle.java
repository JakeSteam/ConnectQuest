package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.EncryptHelper;
import uk.co.jakelee.cityflow.helper.PuzzleShareHelper;
import uk.co.jakelee.cityflow.helper.RandomHelper;

public class Puzzle extends SugarRecord {
    private int puzzleId;
    private int packId;
    private String parTime;
    private String parMoves;
    private String bestTime;
    private String bestMoves;
    private String completionStar;
    private String timeStar;
    private String movesStar;

    public Puzzle() {
    }

    public Puzzle(int puzzleId, int packId, long parTime, int parMoves, long bestTime, int bestMoves) {
        this.puzzleId = puzzleId;
        this.packId = packId;
        this.parTime = EncryptHelper.encode(parTime, puzzleId);
        this.parMoves = EncryptHelper.encode(parMoves, puzzleId);
        this.bestTime = EncryptHelper.encode(bestTime, puzzleId);
        this.bestMoves = EncryptHelper.encode(bestMoves, puzzleId);
        this.completionStar = EncryptHelper.encode(false, puzzleId + 1000);
        this.timeStar = EncryptHelper.encode(false, puzzleId + 2000);
        this.movesStar = EncryptHelper.encode(false, puzzleId + 3000);
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
        return EncryptHelper.decodeToLong(parTime, puzzleId);
    }

    public void setParTime(long parTime) {
        this.parTime = EncryptHelper.encode(parTime, puzzleId);
    }

    public int getParMoves() {
        return EncryptHelper.decodeToInt(parMoves, puzzleId);
    }

    public void setParMoves(int parMoves) {
        this.parMoves = EncryptHelper.encode(parMoves, puzzleId);
    }

    public long getBestTime() {
        return EncryptHelper.decodeToLong(bestTime, puzzleId);
    }

    public void setBestTime(long bestTime) {
        this.bestTime = EncryptHelper.encode(bestTime, puzzleId);
    }

    public int getBestMoves() {
        return EncryptHelper.decodeToInt(bestMoves, puzzleId);
    }

    public void setBestMoves(int bestMoves) {
        this.bestMoves = EncryptHelper.encode(bestMoves, puzzleId);
    }

    public boolean hasCompletionStar() {
        return EncryptHelper.decodeToBool(completionStar, puzzleId + 1000);
    }

    public void setCompletionStar(boolean completionStar) {
        this.completionStar = EncryptHelper.encode(completionStar, puzzleId + 1000);
    }

    public boolean hasTimeStar() {
        return EncryptHelper.decodeToBool(timeStar, puzzleId + 2000);
    }

    public void setTimeStar(boolean timeStar) {
        this.timeStar = EncryptHelper.encode(timeStar, puzzleId + 2000);
    }

    public boolean hasMovesStar() {
        return EncryptHelper.decodeToBool(movesStar, puzzleId + 3000);
    }

    public void setMovesStar(boolean movesStar) {
        this.movesStar = EncryptHelper.encode(movesStar, puzzleId + 3000);
    }

    public String getName() {
        return Text.get("PUZZLE_", getPuzzleId(), "_NAME");
    }

    public void safelyDelete() {
        PuzzleCustom puzzleCustom = getCustomData();
        if (puzzleCustom != null) {
            puzzleCustom.delete();
        }
        delete();
    }

    public List<Tile> getTiles() {
        return Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(this.getPuzzleId()))
                .orderBy("y DESC, x ASC").list();
    }

    public List<TileType> getUnlockableTiles() {
        return Select.from(TileType.class).where(
                Condition.prop("puzzle_required").eq(puzzleId)).list();
    }

    public static Puzzle getPuzzle(int puzzleId) {
        return Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(puzzleId)).first();
    }

    public static void shuffle(List<Tile> tiles) {
        for (Tile tile : tiles) {
            tile.setRotation(RandomHelper.getNumber(Constants.ROTATION_MIN, Constants.ROTATION_MAX));
        }
        Tile.saveInTx(tiles);
    }

    public int getStarCount() {
        int stars = 0;
        if (hasCompletionStar()) {
            stars++;
        }
        if (hasMovesStar()) {
            stars++;
        }
        if (hasTimeStar()) {
            stars++;
        }
        return stars;
    }

    public static List<Puzzle> getCustomPuzzles(boolean displayOthers) {
        int authorSelection = displayOthers ? 0 : 1;
        return Select.from(Puzzle.class).where("puzzle_id IN (SELECT puzzle_id FROM puzzle_custom WHERE original_author = " + authorSelection + " ORDER BY date_added DESC)").list();
    }

    public PuzzleCustom getCustomData() {
        return Select.from(PuzzleCustom.class).where(Condition.prop("puzzle_id").eq(puzzleId)).first();
    }

    public String getShareText() {
        return PuzzleShareHelper.getPuzzleString(this);
    }
}

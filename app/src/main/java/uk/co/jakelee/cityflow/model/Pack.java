package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.ModificationHelper;

public class Pack extends SugarRecord{
    private int packId;
    private String iapCode;
    private String timeLeaderboard;
    private String movesLeaderboard;
    private String currentMoves;
    private String currentTime;
    private String currentStars;
    private String maxStars;
    private String purchased;

    public Pack() {
    }

    public Pack(int packId, String iapCode, String timeLeaderboard, String movesLeaderboard, int maxStars) {
        this.packId = packId;
        this.iapCode = ModificationHelper.encode(iapCode, packId);
        this.timeLeaderboard = timeLeaderboard;
        this.movesLeaderboard = movesLeaderboard;
        this.purchased = ModificationHelper.encode(false, packId);
        this.currentMoves = ModificationHelper.encode(0, packId);
        this.currentTime = ModificationHelper.encode(0, packId);
        this.currentStars = ModificationHelper.encode(0, packId);
        this.maxStars = ModificationHelper.encode(maxStars, packId);
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public String getIapCode() {
        return ModificationHelper.decode(iapCode, packId);
    }

    public void setIapCode(String iapCode) {
        this.iapCode = ModificationHelper.encode(iapCode, packId);
    }

    public String getTimeLeaderboard() {
        return timeLeaderboard;
    }

    public void setTimeLeaderboard(String timeLeaderboard) {
        this.timeLeaderboard = timeLeaderboard;
    }

    public String getMovesLeaderboard() {
        return movesLeaderboard;
    }

    public void setMovesLeaderboard(String movesLeaderboard) {
        this.movesLeaderboard = movesLeaderboard;
    }

    public boolean isPurchased() {
        return ModificationHelper.decodeToBool(purchased, packId);
    }

    public void setPurchased(boolean purchased) {
        this.purchased = ModificationHelper.encode(purchased, packId);
    }

    public int getCurrentMoves() {
        return ModificationHelper.decodeToInt(currentMoves, packId);
    }

    public void setCurrentMoves(int currentMoves) {
        this.currentMoves = ModificationHelper.encode(currentMoves, packId);
    }

    public int getCurrentTime() {
        return ModificationHelper.decodeToInt(currentTime, packId);
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = ModificationHelper.encode(currentTime, packId);
    }

    public int getCurrentStars() {
        return ModificationHelper.decodeToInt(currentStars, packId);
    }

    public void setCurrentStars(int currentStars) {
        this.currentStars = ModificationHelper.encode(currentStars, packId);
    }

    public int getMaxStars() {
        return ModificationHelper.decodeToInt(maxStars, packId);
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = ModificationHelper.encode(maxStars, packId);
    }

    public static Pack getPack(int packId) {
        return Select.from(Pack.class).where(
                Condition.prop("pack_id").eq(packId)).first();
    }

    public List<Puzzle> getPuzzles() {
        return Select.from(Puzzle.class).where(
                Condition.prop("pack_id").eq(packId)).list();
    }

    public int getFirstPuzzleId() {
        return Select.from(Puzzle.class).where(
                Condition.prop("pack_id").eq(packId)).first().getPuzzleId();
    }

    public boolean isUnlocked() {
        return isPurchased() || isPreviousPackComplete();
    }

    public boolean isPreviousPackComplete() {
        if (getPackId() == 1) {
            return true;
        }

        Pack previousPack = Pack.getPack(getPackId() - 1);
        return previousPack.getCurrentStars() >= previousPack.getMaxStars();
    }

    public void refreshMetrics() {
        int bestMoves = 0;
        int bestTime = 0;
        int bestStars = 0;
        boolean allCompleted = true;

        List<Puzzle> puzzles = getPuzzles();
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getStarCount() > 0) {
                bestMoves += puzzle.getBestMoves();
                bestTime += puzzle.getBestTime();
            } else {
                allCompleted = false;
            }
            bestStars += puzzle.getStarCount();
        }

        if (allCompleted && (getCurrentMoves() == 0 || bestMoves < getCurrentMoves())) {
            setCurrentMoves(bestMoves);
            GooglePlayHelper.UpdateLeaderboards(getMovesLeaderboard(), bestMoves);
        }

        if (allCompleted && (getCurrentTime() == 0 || bestTime < getCurrentTime())) {
            setCurrentTime(bestTime);
            GooglePlayHelper.UpdateLeaderboards(getTimeLeaderboard(), bestTime);
        }
        setCurrentStars(bestStars);
        save();
    }

    public String getName() {
        return Text.get("PACK_", getPackId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("PACK_", getPackId(), "_DESC");
    }
}

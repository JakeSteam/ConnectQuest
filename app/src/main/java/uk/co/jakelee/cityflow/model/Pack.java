package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.EncryptHelper;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;

public class Pack extends SugarRecord{
    private int packId;
    private String timeLeaderboard;
    private String movesLeaderboard;
    private String currentMoves;
    private String currentTime;
    private String currentStars;
    private String maxStars;
    private String purchased;
    private String unlockable;

    public Pack() {
    }

    public Pack(int packId, String timeLeaderboard, String movesLeaderboard, int maxStars, boolean unlockable) {
        this.packId = packId;
        this.timeLeaderboard = timeLeaderboard;
        this.movesLeaderboard = movesLeaderboard;
        this.purchased = EncryptHelper.encode(false, packId);
        this.currentMoves = EncryptHelper.encode(0, packId);
        this.currentTime = EncryptHelper.encode(0, packId);
        this.currentStars = EncryptHelper.encode(0, packId);
        this.maxStars = EncryptHelper.encode(maxStars, packId);
        this.purchased = EncryptHelper.encode(false, packId);
        this.unlockable = EncryptHelper.encode(unlockable, packId);
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
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
        return EncryptHelper.decodeToBool(purchased, packId);
    }

    public void setPurchased(boolean purchased) {
        this.purchased = EncryptHelper.encode(purchased, packId);
    }

    public int getCurrentMoves() {
        return EncryptHelper.decodeToInt(currentMoves, packId);
    }

    public void setCurrentMoves(int currentMoves) {
        this.currentMoves = EncryptHelper.encode(currentMoves, packId);
    }

    public int getCurrentTime() {
        return EncryptHelper.decodeToInt(currentTime, packId);
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = EncryptHelper.encode(currentTime, packId);
    }

    public int getCurrentStars() {
        return EncryptHelper.decodeToInt(currentStars, packId);
    }

    public void setCurrentStars(int currentStars) {
        this.currentStars = EncryptHelper.encode(currentStars, packId);
    }

    public int getMaxStars() {
        return EncryptHelper.decodeToInt(maxStars, packId);
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = EncryptHelper.encode(maxStars, packId);
    }

    public boolean isUnlockable() {
        return EncryptHelper.decodeToBool(unlockable, packId);
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

    public void increaseCompletedCount() {
        int packCompleteStatistic = 0;
        switch(packId) {
            case 1:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_1;
                break;
            case 2:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_2;
                break;
            case 3:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_3;
                break;
            case 4:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_4;
                break;
            case 5:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_5;
                break;
            case 6:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_6;
                break;
            case 7:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_7;
                break;
            case 8:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_8;
                break;
            case 9:
                packCompleteStatistic = Constants.STATISTIC_COMPLETE_PACK_9;
                break;
        }
        if (packCompleteStatistic > 0) {
            Statistic.increaseByOne(packCompleteStatistic);
        }
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
                bestStars += puzzle.getStarCount();
            } else {
                allCompleted = false;
            }
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

    public String getUnlockChallenge() {
        return Text.get("PACK_", getPackId(), "_CHALLENGE");
    }
}

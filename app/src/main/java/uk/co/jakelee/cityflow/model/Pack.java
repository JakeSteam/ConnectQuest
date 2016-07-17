package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Pack extends SugarRecord{
    private int packId;
    private String iapCode;
    private int currentStars;
    private boolean purchased;
    private int maxStars;

    public Pack() {
    }

    public Pack(int packId, String iapCode, int maxStars, boolean unlocked) {
        this.packId = packId;
        this.iapCode = iapCode;
        this.purchased = unlocked;
        this.currentStars = 0;
        this.maxStars = maxStars;
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public String getIapCode() {
        return iapCode;
    }

    public void setIapCode(String iapCode) {
        this.iapCode = iapCode;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public int getCurrentStars() {
        return currentStars;
    }

    public void setCurrentStars(int currentStars) {
        this.currentStars = currentStars;
    }

    public int getMaxStars() {
        return maxStars;
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = maxStars;
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

    public String getName() {
        return Text.get("PACK_", getPackId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("PACK_", getPackId(), "_DESC");
    }
}

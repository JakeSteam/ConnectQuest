package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.ModificationHelper;

public class Pack extends SugarRecord{
    private int packId;
    private String iapCode;
    private String currentStars;
    private String purchased;
    private String maxStars;

    public Pack() {
    }

    public Pack(int packId, String iapCode, int maxStars, boolean purchased) {
        this.packId = packId;
        this.iapCode = ModificationHelper.encode(iapCode, packId);
        this.purchased = ModificationHelper.encode(purchased, packId);
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

    public boolean isPurchased() {
        return ModificationHelper.decodeToBool(purchased, packId);
    }

    public void setPurchased(boolean purchased) {
        this.purchased = ModificationHelper.encode(purchased, packId);
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

    public String getName() {
        return Text.get("PACK_", getPackId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("PACK_", getPackId(), "_DESC");
    }
}

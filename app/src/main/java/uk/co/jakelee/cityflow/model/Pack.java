package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Pack extends SugarRecord{
    private int packId;
    private String iapCode;
    private boolean purchased;
    

    public Pack() {
    }

    public Pack(int packId, String iapCode, boolean unlocked) {
        this.packId = packId;
        this.iapCode = iapCode;
        this.purchased = unlocked;
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

    public String getName() {
        return Text.get("PACK_", getPackId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("PACK_", getPackId(), "_DESC");
    }
}

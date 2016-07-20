package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import uk.co.jakelee.cityflow.helper.ModificationHelper;

public class Boost extends SugarRecord{
    private int boostId;
    private String level;
    private String owned;
    private String used;

    public Boost(int boostId, int level, int owned, int used) {
        this.boostId = boostId;
        this.level = ModificationHelper.encode(level, boostId);
        this.owned = ModificationHelper.encode(owned, boostId);
        this.used = ModificationHelper.encode(used, boostId);
    }

    public int getBoostId() {
        return boostId;
    }

    public void setBoostId(int boostId) {
        this.boostId = boostId;
    }

    public int getLevel() {
        return ModificationHelper.decodeToInt(level, boostId);
    }

    public void setLevel(int level) {
        this.level = ModificationHelper.encode(level, boostId);
    }

    public int getOwned() {
        return ModificationHelper.decodeToInt(owned, boostId);
    }

    public void setOwned(int owned) {
        this.owned = ModificationHelper.encode(owned, boostId);
    }

    public int getUsed() {
        return ModificationHelper.decodeToInt(used, boostId);
    }

    public void setUsed(int used) {
        this.used = ModificationHelper.encode(used, boostId);
    }

    public String getName() {
        return Text.get("BOOST_", getBoostId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("BOOST_", getBoostId(), "_DESC");
    }

    public String getUpgradeText() {
        return Text.get("BOOST_", getBoostId(), "_UPGRADE");
    }

    public static int getOwned(int boostId) {
        List<Boost> boosts = Boost.listAll(Boost.class);

        return Select.from(Boost.class).where(
                Condition.prop("boost_id").eq(boostId)).first().getOwned();
    }
}

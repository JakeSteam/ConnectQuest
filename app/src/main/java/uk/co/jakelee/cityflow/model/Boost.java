package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.EncryptHelper;

public class Boost extends SugarRecord{
    private int boostId;
    private String level;
    private String owned;
    private String used;

    public Boost() {}

    public Boost(int boostId, int level, int owned, int used) {
        this.boostId = boostId;
        this.level = EncryptHelper.encode(level, boostId);
        this.owned = EncryptHelper.encode(owned, boostId);
        this.used = EncryptHelper.encode(used, boostId);
    }

    public int getBoostId() {
        return boostId;
    }

    public void setBoostId(int boostId) {
        this.boostId = boostId;
    }

    public int getLevel() {
        return EncryptHelper.decodeToInt(level, boostId);
    }

    public void setLevel(int level) {
        this.level = EncryptHelper.encode(level, boostId);
    }

    public int getOwned() {
        return EncryptHelper.decodeToInt(owned, boostId);
    }

    public void setOwned(int owned) {
        this.owned = EncryptHelper.encode(owned, boostId);
    }

    public int getUsed() {
        return EncryptHelper.decodeToInt(used, boostId);
    }

    public void setUsed(int used) {
        this.used = EncryptHelper.encode(used, boostId);
    }

    public static int getOwnedCount(int boostId) {
        return Select.from(Boost.class).where(
                Condition.prop("boost_id").eq(boostId)).first().getOwned();
    }

    public static void add(int boostId) {
        Boost boost = Select.from(Boost.class).where(
                Condition.prop("boost_id").eq(boostId)).first();

        if (boost != null) {
            boost.setOwned(boost.getOwned() + 1);
            boost.save();
        }
    }

    public static void use(int boostId) {
        Boost boost = Select.from(Boost.class).where(
                Condition.prop("boost_id").eq(boostId)).first();

        if (boost != null && boost.getOwned() > 0) {
            boost.use();
            boost.save();
        }
    }

    public void use() {
        setOwned(getOwned() - 1);
        setUsed(getUsed() + 1);
        save();
    }

    public static Boost get(int boostId) {
        return Select.from(Boost.class).where(
                Condition.prop("boost_id").eq(boostId)).first();
    }
}

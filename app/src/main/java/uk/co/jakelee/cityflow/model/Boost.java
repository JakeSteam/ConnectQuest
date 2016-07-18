package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Boost extends SugarRecord{
    private int boostId;
    private int level;
    private int owned;
    private int used;

    public Boost(int boostId, int level, int owned, int used) {
        this.boostId = boostId;
        this.level = level;
        this.owned = owned;
        this.used = used;
    }

    public int getBoostId() {
        return boostId;
    }

    public void setBoostId(int boostId) {
        this.boostId = boostId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOwned() {
        return owned;
    }

    public void setOwned(int owned) {
        this.owned = owned;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getName() {
        return Text.get("BOOST_", getBoostId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("BOOST_", getBoostId(), "_DESC");
    }
}

package uk.co.jakelee.cityflow.model;

import android.graphics.Color;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.Constants;

public class Background extends SugarRecord {
    private int backgroundId;
    private String hex;
    private boolean unlocked;
    private boolean active;

    public Background() {
    }

    public Background(int backgroundId, String hex) {
        this.backgroundId = backgroundId;
        this.hex = hex;
        this.unlocked = false;
        this.active = false;
    }

    public Background(int backgroundId, String hex, boolean unlocked, boolean active) {
        this.backgroundId = backgroundId;
        this.hex = hex;
        this.unlocked = unlocked;
        this.active = active;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        this.save();
    }

    public static Background get(int backgroundId) {
        return Select.from(Background.class).where(
                Condition.prop("background_id").eq(backgroundId)).first();
    }

    public static int getActiveBackground() {
        return Color.parseColor("#" + Setting.getString(Constants.SETTING_BACKGROUND));
    }

    public String getName() {
        return Text.get("BACKGROUND_", getBackgroundId(), "_NAME");
    }

    public String getHint() {
        return Text.get("BACKGROUND_", getBackgroundId(), "_HINT");
    }
}

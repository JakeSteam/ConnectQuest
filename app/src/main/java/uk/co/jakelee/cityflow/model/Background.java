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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Background get(int backgroundId) {
        return Select.from(Background.class).where(
                Condition.prop("background_id").eq(backgroundId)).first();
    }

    public static int getActiveBackgroundColour() {
        return Color.parseColor("#" + Setting.getString(Constants.SETTING_BACKGROUND));
    }

    public int getBackgroundColour() {
        return Color.parseColor("#" + getHex());
    }

    public static Background getActiveBackground() {
        return Select.from(Background.class).where(
                Condition.prop("active").eq(1)).first();
    }

    public static int getUnlockedBackgroundCount() {
        return (int) Select.from(Background.class).where(
                Condition.prop("unlocked").eq(1)).count();
    }

    public static void setActiveBackground(int backgroundId) {
        Background.executeQuery("UPDATE background SET active = 0");
        Background.executeQuery("UPDATE background SET active = 1 WHERE background_id = " + backgroundId);
    }

    public String getName() {
        return Text.get("BACKGROUND_", getBackgroundId(), "_NAME");
    }

    public String getHint() {
        return Text.get("BACKGROUND_", getBackgroundId(), "_HINT");
    }
}

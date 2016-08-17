package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

public class Setting extends SugarRecord {
    private int settingId;
    private boolean booleanValue;
    private int intValue;
    private float floatValue;
    private float floatMin;
    private float floatMax;
    private String stringValue;

    public Setting() {
    }

    public Setting(int settingId, boolean booleanValue) {
        this.settingId = settingId;
        this.booleanValue = booleanValue;
    }

    public Setting(int settingId, int intValue) {
        this.settingId = settingId;
        this.intValue = intValue;
    }

    public Setting(int settingId, float floatValue, float floatMin, float floatMax) {
        this.settingId = settingId;
        this.floatValue = floatValue;
        this.floatMin = floatMin;
        this.floatMax = floatMax;
    }

    public Setting(int settingId, String stringValue) {
        this.settingId = settingId;
        this.stringValue = stringValue;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean value) {
        this.booleanValue = value;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public float getFloatMin() {
        return floatMin;
    }

    public void setFloatMin(float floatMin) {
        this.floatMin = floatMin;
    }

    public float getFloatMax() {
        return floatMax;
    }

    public void setFloatMax(float floatMax) {
        this.floatMax = floatMax;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public static boolean isTrue(int settingId) {
        Setting setting = Select.from(Setting.class).where(
                Condition.prop("setting_id").eq(settingId)).first();

        return setting != null && setting.getBooleanValue();
    }

    public static Setting get(int settingId)  {
        return Select.from(Setting.class).where(
                Condition.prop("setting_id").eq(settingId)).first();
    }

    public static float getInt(int settingId) {
        Setting setting = Select.from(Setting.class).where(
                Condition.prop("setting_id").eq(settingId)).first();

        if (setting != null) {
            return setting.getIntValue();
        }
        return 0;
    }

    public static float getFloat(int settingId) {
        Setting setting = Select.from(Setting.class).where(
                Condition.prop("setting_id").eq(settingId)).first();

        if (setting != null) {
            return setting.getFloatValue();
        }
        return 0f;
    }

    public static String getString(int settingId) {
        Setting setting = Select.from(Setting.class).where(
                Condition.prop("setting_id").eq(settingId)).first();

        if (setting != null) {
            return setting.getStringValue();
        }
        return "";
    }

    public static boolean getSafeBoolean(long settingId) {
        Setting setting = Setting.findById(Setting.class, settingId);

        return setting != null && setting.getBooleanValue();
    }

    public String getName() {
        return Text.get("SETTING_", getSettingId(), "_NAME");
    }
}

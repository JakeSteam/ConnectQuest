package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Setting extends SugarRecord {
    private int settingId;
    private boolean value;

    public Setting() {
    }

    public Setting(int settingId, boolean value) {
        this.settingId = settingId;
        this.value = value;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getName() {
        return Text.get("SETTING_", getSettingId());
    }
}

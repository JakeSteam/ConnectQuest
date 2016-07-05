package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Setting extends SugarRecord {
    private int settingId;
    private int name;
    private boolean value;

    public Setting() {
    }

    public Setting(int settingId, int name, boolean value) {
        this.settingId = settingId;
        this.name = name;
        this.value = value;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}

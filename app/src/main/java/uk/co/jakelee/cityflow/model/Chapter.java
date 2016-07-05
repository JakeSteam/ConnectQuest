package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Chapter extends SugarRecord{
    private int chapterId;
    private String name;
    private String description;
    private String iapCode;
    private boolean unlocked;

    public Chapter() {
    }

    public Chapter(int chapterId, String name, String description, String iapCode, boolean unlocked) {
        this.chapterId = chapterId;
        this.name = name;
        this.description = description;
        this.iapCode = iapCode;
        this.unlocked = unlocked;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIapCode() {
        return iapCode;
    }

    public void setIapCode(String iapCode) {
        this.iapCode = iapCode;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}

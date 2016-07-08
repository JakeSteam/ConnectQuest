package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class Chapter extends SugarRecord{
    private int chapterId;
    private String iapCode;
    private boolean unlocked;

    public Chapter() {
    }

    public Chapter(int chapterId, String iapCode, boolean unlocked) {
        this.chapterId = chapterId;
        this.iapCode = iapCode;
        this.unlocked = unlocked;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
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

    public String getName() {
        return Text.get("CHAPTER_", getChapterId(), "_NAME");
    }

    public String getDescription() {
        return Text.get("CHAPTER_", getChapterId(), "_DESC");
    }
}

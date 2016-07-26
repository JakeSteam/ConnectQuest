package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;

public class PuzzleCustom extends SugarRecord {
    private int puzzleId;
    private long dateAdded;
    private String name;
    private String description;
    private String author;
    private boolean originalAuthor;

    public PuzzleCustom() {
    }

    public PuzzleCustom(int puzzleId, long dateAdded, String name, String description, String author, boolean originalAuthor) {
        this.puzzleId = puzzleId;
        this.dateAdded = dateAdded;
        this.name = name;
        this.description = description;
        this.author = author;
        this.originalAuthor = originalAuthor;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isOriginalAuthor() {
        return originalAuthor;
    }

    public void setOriginalAuthor(boolean originalAuthor) {
        this.originalAuthor = originalAuthor;
    }
}

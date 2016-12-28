package uk.co.jakelee.cityflow.components;

public class DisplayValues {
    private float zoomFactor;
    private int offset;
    private boolean isLeftOffset;

    public DisplayValues(float zoomFactor, int offset, boolean isYFactorMoreThanX) {
        this.zoomFactor = zoomFactor;
        this.offset = offset;
        this.isLeftOffset = isYFactorMoreThanX;
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(float zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isLeftOffset() {
        return isLeftOffset;
    }

    public void setLeftOffset(boolean leftOffset) {
        isLeftOffset = leftOffset;
    }
}

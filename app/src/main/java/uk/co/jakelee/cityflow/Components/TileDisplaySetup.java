package uk.co.jakelee.cityflow.components;

import android.widget.ImageView;

public class TileDisplaySetup {
    private ImageView selectedImageView;
    private float optimumScale;

    public TileDisplaySetup(ImageView selectedImageView, float optimumScale) {
        this.selectedImageView = selectedImageView;
        this.optimumScale = optimumScale;
    }

    public ImageView getSelectedImageView() {
        return selectedImageView;
    }

    public void setSelectedImageView(ImageView selectedImageView) {
        this.selectedImageView = selectedImageView;
    }

    public float getOptimumScale() {
        return optimumScale;
    }

    public void setOptimumScale(float optimumScale) {
        this.optimumScale = optimumScale;
    }
}

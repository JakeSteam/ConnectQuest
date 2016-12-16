package uk.co.jakelee.cityflow.interfaces;

import android.app.Activity;
import android.widget.ImageView;

import uk.co.jakelee.cityflow.model.Tile;

public interface PuzzleDisplayer {
    void handleTileClick(ImageView image, Tile tile);
    Activity getActivity();
}

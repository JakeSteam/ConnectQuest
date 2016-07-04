package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import uk.co.jakelee.cityflow.model.Tile;

public class TileHelper {

    public static int getMaxY(List<Tile> tiles) {
        int maxY = 0;
        for (Tile tile : tiles) {
            if (tile.getY() > maxY) {
                maxY = tile.getY();
            }
        }
        return maxY;
    }

    public static ImageView createTileImageView(Context context, Tile tile, int maxY, int width, int height) {
        ImageView image = new ImageView(context);
        int leftPadding = (tile.getY() + tile.getX()) * (width/2);
        int topPadding = (tile.getX() + maxY - tile.getY()) * (height/2);
        image.setPadding(leftPadding, topPadding, 0, 0);

        return image;
    }
}

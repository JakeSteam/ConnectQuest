package uk.co.jakelee.cityflow.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;

public class ImageHelper {
    public static int getTileDrawableId(Context context, int tile, int rotation) {
        String name = String.format(context.getString(R.string.tile_filename), tile, rotation);
        return getDrawableId(context, name);
    }

    public static List<Integer> getAllTileDrawableIds(Context context, int tile) {
        List<Integer> ids = new ArrayList<>();
            ids.add(getDrawableId(context, "tile_" + tile + "_1"));
            ids.add(getDrawableId(context, "tile_" + tile + "_2"));
            ids.add(getDrawableId(context, "tile_" + tile + "_3"));
            ids.add(getDrawableId(context, "tile_" + tile + "_4"));
        return ids;
    }

    public static int getDrawableId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}

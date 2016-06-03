package uk.co.jakelee.cityflow.helper;

import android.content.Context;

import uk.co.jakelee.cityflow.R;

public class ImageHelper {
    public static int getTileDrawableId(Context context, int tile, int rotation) {
        String name = String.format(context.getString(R.string.tile_filename), tile, rotation);
        return getDrawableId(context, name);
    }

    public static int getDrawableId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}

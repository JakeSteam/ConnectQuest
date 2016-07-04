package uk.co.jakelee.cityflow.helper;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;

public class DisplayHelper {
    private final Context context;
    private static DisplayHelper dhInstance = null;

    public DisplayHelper(Context context) {
        this.context = context;
    }

    public static DisplayHelper getInstance(Context ctx) {
        if (dhInstance == null) {
            dhInstance = new DisplayHelper(ctx.getApplicationContext());
        }
        return dhInstance;
    }

    public int getTileWidth() {
        return dpToPixel(Constants.TILE_WIDTH);
    }

    public int getTileHeight() {
        return dpToPixel(Constants.TILE_HEIGHT);
    }

    public void populateTiles(RelativeLayout tileContainer, int puzzleId) {
        Puzzle puzzle = Select.from(Puzzle.class).where(Condition.prop("puzzle_id").eq(puzzleId)).first();
        List<Tile> tiles = puzzle.getTiles();
        int maxY = TileHelper.getMaxY(tiles);

        for (Tile tile : tiles) {
            int drawableId = ImageHelper.getTileDrawableId(context, tile.getTileTypeId(), tile.getRotation());
            ImageView image = createTileImageView(tile, maxY, drawableId);
            tileContainer.addView(image);
        }
    }

    private ImageView createTileImageView(final Tile tile, int maxY, int drawableId) {
        ImageView image = TileHelper.createTileImageView(context, tile, maxY, getTileWidth(), getTileHeight());
        Picasso.with(context).load(drawableId).into(image);

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, String.format(context.getString(R.string.debugTileInfo),
                        tile.getX(),
                        tile.getY(),
                        tile.getTileTypeId(),
                        tile.getRotation()), Toast.LENGTH_SHORT).show();
            }
        });
        return image;
    }

    public int dpToPixel(float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }
}

package uk.co.jakelee.cityflow.helper;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.ChapterActivity;
import uk.co.jakelee.cityflow.main.PuzzleActivity;
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

    public ImageView createTileImageView(final PuzzleActivity activity, final Tile tile, int maxY, int drawableId) {
        final ImageView image = TileHelper.createTileImageView(context, tile, maxY, getTileWidth(), getTileHeight());
        Picasso.with(context).load(drawableId).into(image);

        image.setDrawingCacheEnabled(true);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getDrawingCache() == null) { return false; }
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                if (bmp == null || bmp.getWidth() < event.getX() || bmp.getHeight() < event.getY()) {
                    return false;
                }
                int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                if (color == Color.TRANSPARENT) {
                    return false;
                } else {
                    if(event.getAction()==MotionEvent.ACTION_UP) {
                        activity.handleTileClick(image, tile);
                    }
                    return true;
                }
            }});
        return image;
    }

    public TextView createPuzzleSelectImageView(final ChapterActivity activity, int puzzleNumber, final Puzzle puzzle) {
        TextView puzzleText = new TextView(activity);
        puzzleText.setText("  " + puzzleNumber + " ");
        puzzleText.setTextSize(30);
        puzzleText.setPadding(10, 10, 10, 10);
        puzzleText.setBackgroundResource(R.drawable.white);
        puzzleText.setTag(puzzle.getPuzzleId());
        puzzleText.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.showPuzzleInfo(puzzle);
            }
        });
        return puzzleText;
    }

    public int dpToPixel(float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }
}

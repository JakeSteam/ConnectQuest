package uk.co.jakelee.cityflow.helper;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.PackActivity;
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
        final ImageView image = new ImageView(activity);
        Picasso.with(context).load(drawableId).into(image);

        image.setDrawingCacheEnabled(true);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                    int color = bmp.getPixel((int) event.getX(), (int) event.getY());
                    if (color == Color.TRANSPARENT) {
                        return false;
                    } else {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            activity.handleTileClick(image, tile);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }});
        return image;
    }

    public TextView createPuzzleSelectImageView(final PackActivity activity, int puzzleNumber, final Puzzle puzzle, boolean isSelected) {
        TextView puzzleText = new TextView(activity);
        puzzleText.setText(Integer.toString(puzzleNumber));
        puzzleText.setTextSize(30);
        puzzleText.setGravity(Gravity.CENTER);
        puzzleText.setPadding(10, 10, 10, 10);
        puzzleText.setBackgroundResource(isSelected ? R.color.green : R.color.white);
        puzzleText.setTag(puzzle.getPuzzleId());
        puzzleText.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.selectedPuzzle = puzzle;
                activity.populatePuzzles();
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

    public Drawable getPuzzleDrawable(int puzzle, boolean haveCompleted) {
        Drawable puzzleDrawable = createDrawable(getPuzzleDrawableID(puzzle), Constants.PUZZLE_WIDTH, Constants.PUZZLE_HEIGHT);
        if (!haveCompleted) {
            puzzleDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        }
        return puzzleDrawable;
    }

    public int getPuzzleDrawableID(int puzzle) {
        return context.getResources().getIdentifier("puzzle_" + puzzle, "drawable", context.getPackageName());
    }

    public Drawable createDrawable(int drawableId, int width, int height) {
        Bitmap rawImage = BitmapFactory.decodeResource(context.getResources(), drawableId);
        int adjustedWidth = dpToPixel(width);
        int adjustedHeight = dpToPixel(height);
        Bitmap resizedImage = Bitmap.createScaledBitmap(rawImage, adjustedWidth, adjustedHeight, false);
        return new BitmapDrawable(context.getResources(), resizedImage);
    }
}

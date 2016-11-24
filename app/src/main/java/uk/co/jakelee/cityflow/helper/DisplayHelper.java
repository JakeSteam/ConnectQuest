package uk.co.jakelee.cityflow.helper;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.TextViewFont;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.main.EditorActivity;
import uk.co.jakelee.cityflow.main.PackActivity;
import uk.co.jakelee.cityflow.main.PuzzleActivity;
import uk.co.jakelee.cityflow.main.ShopActivity;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

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

    private DisplayMetrics getSizes(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    public Pair<Float, Integer> getDisplayValues(Activity activity, int xTiles, int yTiles) {
        DisplayMetrics displayMetrics = getSizes(activity);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        double totalTilesAmount = (xTiles + yTiles) / 2.0;
        int puzzleHeight = (int)(totalTilesAmount * getTileHeight());
        int puzzleWidth = (int)(totalTilesAmount * getTileWidth());

        float xZoomFactor = screenWidth / (float)(puzzleWidth);
        float yZoomFactor = screenHeight / (float)(puzzleHeight);
        float zoomFactor = Math.min(xZoomFactor, yZoomFactor);

        int topOffset = puzzleHeight / 3;
        return new Pair<>(zoomFactor, topOffset);
    }

    public ImageView createTileImageView(final Activity activity, final Tile tile, int drawableId) {
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
                        // We don't care about this / subsequent touch events
                        return false;
                    } else {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (activity.getClass().equals(PuzzleActivity.class)) {
                                ((PuzzleActivity)activity).handleTileClick(image, tile);
                            } else if (activity.getClass().equals(EditorActivity.class)) {
                                ((EditorActivity)activity).handleTileClick(image, tile);
                            }
                        }
                        // We care about subsequent events, even if it was a touch down
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }});
        return image;
    }

    public RelativeLayout createPuzzleSelectButton(final PackActivity activity, int puzzleNumber, final Puzzle puzzle, boolean isSelected, boolean lastLevelCompleted) {
        boolean hasAllStars = puzzle.hasCompletionStar() && puzzle.hasMovesStar() && puzzle.hasTimeStar();
        boolean hasCompleted = puzzle.hasCompletionStar();

        RelativeLayout puzzleButton = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.custom_puzzle_select_button, null);

        puzzleButton.setBackgroundColor(ContextCompat.getColor(activity, isSelected ? R.color.green : R.color.ltltgrey));
        ((TextView)puzzleButton.findViewById(R.id.puzzleNumber)).setText(" " + puzzleNumber + " ");
        ((TextView)puzzleButton.findViewById(R.id.puzzleStatus)).setText(
                !lastLevelCompleted ? R.string.icon_lock : hasAllStars ? R.string.icon_tick : hasCompleted ? R.string.icon_tick : R.string.icon_unlock);
        ((TextView)puzzleButton.findViewById(R.id.puzzleStatus)).setTextColor(ContextCompat.getColor(activity,
                !lastLevelCompleted ? R.color.ltred : hasAllStars ? R.color.gold : hasCompleted ? R.color.dkgreen : R.color.ltgrey));

        puzzleButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.selectedPuzzle = puzzle;
                activity.populatePuzzles();
            }
        });
        return puzzleButton;
    }

    public RelativeLayout createItemSelectButton(final ShopActivity activity, final ShopItem item) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        RelativeLayout itemButton = (RelativeLayout) inflater.inflate(R.layout.custom_item_select_button, null);
        ((ImageView)itemButton.findViewById(R.id.itemImage)).setImageResource(getItemDrawableID(item.getItemId()));
        ((TextView)itemButton.findViewById(R.id.itemPrice)).setText(item.atMaxPurchases() ? "N/A" : Integer.toString(item.getPrice()));

        itemButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.displayInformation(item);
            }
        });
        return itemButton;
    }

    public ImageView createTileIcon(TileType tileType, int width, int height) {
        int padding = dpToPixel(3);
        ImageView tileIcon = new ImageView(context);
        if (tileType.getStatus() == Constants.TILE_STATUS_UNPURCHASED) {
            tileIcon.setImageDrawable(createDrawable(R.drawable.tile_unbought, width, height));
        } else if (tileType.getStatus() == Constants.TILE_STATUS_LOCKED){
            tileIcon.setImageDrawable(createDrawable(R.drawable.tile_locked, width, height));
        } else {
            tileIcon.setImageDrawable(createDrawable(getTileDrawableID(tileType.getTypeId()), width, height));
        }
        tileIcon.setPadding(padding, padding, padding, padding);

        return tileIcon;
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

    public Drawable getCustomPuzzleDrawable(int puzzleId) {
        String pathName = context.getFilesDir() + "/puzzle_" + puzzleId + ".png";
        return Drawable.createFromPath(pathName);
    }

    public int getTileDrawableID(int tile) {
        return context.getResources().getIdentifier("tile_" + tile + "_1", "drawable", context.getPackageName());
    }

    public int getIabDrawableID(String iabName) {
        return context.getResources().getIdentifier("iab_" + iabName + "", "drawable", context.getPackageName());
    }

    public int getItemDrawableID(int item) {
        return context.getResources().getIdentifier("item_" + item, "drawable", context.getPackageName());
    }

    public int getCarDrawableID(int carNumber, int rotation) {
        return context.getResources().getIdentifier("car_" + carNumber + "_" + rotation, "drawable", context.getPackageName());
    }

    public int getPackDrawableID(int packId) {
        return context.getResources().getIdentifier("pack_" + packId, "drawable", context.getPackageName());
    }

    public Drawable createDrawable(int drawableId, int width, int height) {
        Bitmap rawImage = BitmapFactory.decodeResource(context.getResources(), drawableId);
        int adjustedWidth = dpToPixel(width);
        int adjustedHeight = dpToPixel(height);
        Bitmap resizedImage = Bitmap.createScaledBitmap(rawImage, adjustedWidth, adjustedHeight, false);
        return new BitmapDrawable(context.getResources(), resizedImage);
    }

    public TextView createTextView(String text, int size, int color) {
        TextView textView = new TextViewFont(context);
        textView.setText(text);
        textView.setTextSize(size);
        textView.setTextColor(color);

        return textView;
    }

    public ImageView createCarImageview(int rotation) {
        int carDrawable = getCarDrawableID(RandomHelper.getNumber(1, Constants.NUMBER_CARS), rotation);
        ImageView imageview = new ImageView(context);
        imageview.setImageDrawable(createDrawable(carDrawable, 20, 20));
        return imageview;
    }

    public void createCarAnimation(RelativeLayout container, DisplayMetrics metrics) {
        int rotation = RandomHelper.getNumber(Constants.ROTATION_MIN, Constants.ROTATION_MAX);
        int duration = RandomHelper.getNumber(Constants.CARS_MIN_TIME, Constants.CARS_MAX_TIME);

        ImageView carView = createCarImageview(rotation);
        container.addView(carView);
        carView.startAnimation(AnimationHelper.move(metrics, rotation, duration));
    }

    public void setupTileDisplay(Activity activity, List<Tile> tiles, ZoomableViewGroup tileContainer, int puzzleId, Tile selectedTile, ImageView selectedTileImage, boolean isEditor) {
        if (puzzleId == 0) {
            return;
        }

        tileContainer.removeAllViews();

        Pair<Integer, Integer> maxXY = TileHelper.getMaxXY(tiles);
        Pair<Float, Integer> displayValues = getDisplayValues(activity, maxXY.first + 1, maxXY.second + 1);

        float optimumScale = displayValues.first;
        int topOffset = displayValues.second;

        tileContainer.setScaleFactor(optimumScale, true);
        tileContainer.removeAllViews();
        for (final Tile tile : tiles) {
            ZoomableViewGroup.LayoutParams layoutParams = new ZoomableViewGroup.LayoutParams(ZoomableViewGroup.LayoutParams.WRAP_CONTENT, ZoomableViewGroup.LayoutParams.WRAP_CONTENT);
            int leftPadding = (tile.getY() + tile.getX()) * (getTileWidth() / 2);
            int topPadding = topOffset + (tile.getX() + maxXY.second - tile.getY()) * (getTileHeight() / 2);
            layoutParams.setMargins(leftPadding, topPadding, 0, 0);

            int drawableId = ImageHelper.getTileDrawableId(activity, tile.getTileTypeId(), tile.getRotation());
            ImageView image = createTileImageView(activity, tile, drawableId);

            //Make sure we always have a tile selected
            if (isEditor && selectedTile == null || selectedTileImage == null) {
                image.setAlpha(0.75f);
                image.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                selectedTileImage = image;
                selectedTile = tile;

                TextView selectedTileText = (TextView)activity.findViewById(R.id.selectedTileText);
                if (selectedTileText != null) {
                    ((TextView) activity.findViewById(R.id.selectedTileText)).setText(tile.getName());
                }
            }

            tileContainer.addView(image, layoutParams);
        }
    }
}

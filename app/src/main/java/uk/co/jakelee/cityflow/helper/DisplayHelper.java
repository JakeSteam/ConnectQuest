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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.TextViewFont;
import uk.co.jakelee.cityflow.main.EditorActivity;
import uk.co.jakelee.cityflow.main.PackActivity;
import uk.co.jakelee.cityflow.main.PuzzleActivity;
import uk.co.jakelee.cityflow.main.ShopActivity;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.ShopItem;
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

    public DisplayMetrics getSizes(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
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
                        return false;
                    } else {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (activity.getClass().equals(PuzzleActivity.class)) {
                                ((PuzzleActivity)activity).handleTileClick(image, tile);
                            } else if (activity.getClass().equals(EditorActivity.class)) {
                                ((EditorActivity)activity).handleTileClick(image, tile);
                            }
                        }
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }});
        return image;
    }

    public TextView createPuzzleSelectButton(final PackActivity activity, int puzzleNumber, final Puzzle puzzle, boolean isSelected, boolean lastLevelCompleted) {
        boolean hasAllStars = puzzle.hasCompletionStar() && puzzle.hasMovesStar() && puzzle.hasTimeStar();
        boolean hasCompleted = puzzle.hasCompletionStar();

        LayoutInflater inflater = LayoutInflater.from(activity);
        TextView puzzleButton = (TextView) inflater.inflate(R.layout.custom_puzzle_select_button, null);

        puzzleButton.setText(Integer.toString(puzzleNumber));
        if (!lastLevelCompleted) {
            puzzleButton.setBackgroundResource(R.drawable.ui_level_locked);
        } else if (isSelected) {
            puzzleButton.setBackgroundResource(hasCompleted ? (hasAllStars ? R.drawable.ui_level_selected_completed_fully : R.drawable.ui_level_selected_completed) : R.drawable.ui_level_selected);
        } else {
            puzzleButton.setBackgroundResource(hasCompleted ? (hasAllStars ? R.drawable.ui_level_unselected_completed_fully : R.drawable.ui_level_unselected_completed) : R.drawable.ui_level_unselected);
        }
        if (lastLevelCompleted) {
            puzzleButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    activity.selectedPuzzle = puzzle;
                    activity.populatePuzzles();
                }
            });
        }
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

    public ImageView createBoostIcon(int boostId, int width, int height) {
        int padding = dpToPixel(3);
        ImageView boostIcon = new ImageView(context);
        boostIcon.setImageDrawable(createDrawable(getBoostDrawableID(boostId), width, height));
        boostIcon.setTag(boostId);
        boostIcon.setPadding(padding, padding, padding, padding);
        boostIcon.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Display message with boost info
            }
        });

        return boostIcon;
    }

    public ImageView createTileIcon(int tileId, int width, int height) {
        int padding = dpToPixel(3);
        ImageView tileIcon = new ImageView(context);
        tileIcon.setImageDrawable(createDrawable(getTileDrawableID(tileId), width, height));
        tileIcon.setTag(tileId);
        tileIcon.setPadding(padding, padding, padding, padding);
        tileIcon.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Display message with tile info
            }
        });

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
        String pathName = "/data/data/uk.co.jakelee.cityflow/files/puzzle_" + puzzleId + ".png";
        return Drawable.createFromPath(pathName);
    }

    public int getBoostDrawableID(int boost) {
        return context.getResources().getIdentifier("boost_" + boost, "drawable", context.getPackageName());
    }

    public int getTileDrawableID(int tile) {
        return context.getResources().getIdentifier("tile_" + tile + "_1", "drawable", context.getPackageName());
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
}

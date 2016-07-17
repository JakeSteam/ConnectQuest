package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.PuzzleHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;
    private int movesMade = 0;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeLastMoved = 0L;
    private ImageView lastChangedImage;
    private Tile lastChangedTile;
    private boolean undoing = false;
    private boolean justUndone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        populateTiles(tiles);
        fetchImages(tiles);
        startCountdownTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    public void fetchImages(List<Tile> tiles) {
        for (Tile tile : tiles) {
            List<Integer> ids = ImageHelper.getAllTileDrawableIds(this, tile.getTileTypeId());
            for (Integer id : ids) {
                Picasso.with(this)
                        .load(id)
                        .fetch();
            }
        }
    }

    public void suppressClick(View v) {
        // Nope
    }

    public void startCountdownTimer() {
        final TextView countdownTimer = (TextView)findViewById(R.id.initialCountdownText);
        new CountDownTimer(4000, 100) {
            public void onTick(long millisUntilFinished) {
                int timeLeft = (int) Math.ceil(millisUntilFinished / 1000);
                if (timeLeft > 0) {
                    countdownTimer.setText(Integer.toString(timeLeft));
                } else {
                    countdownTimer.setTextSize(100);
                    countdownTimer.setText("Flow!");
                }
            }

            public void onFinish() {
                countdownTimer.setVisibility(View.GONE);
                startTimeTakenTimer();

                new Thread(new Runnable() {
                    public void run() {
                        flowCheck();
                    }
                }).start();
            }

        }.start();
    }

    public void startTimeTakenTimer() {
        if (!Setting.isTrue(Constants.SETTING_ZEN_MODE)) {
            findViewById(R.id.moveCounter).setVisibility(View.VISIBLE);
            findViewById(R.id.puzzleTimer).setVisibility(View.VISIBLE);
            handler.post(updateTimerThread);
        }

        findViewById(R.id.zoomIn).setVisibility(View.VISIBLE);
        findViewById(R.id.zoomOut).setVisibility(View.VISIBLE);
        findViewById(R.id.undoButton).setVisibility(View.VISIBLE);

        startTime = SystemClock.uptimeMillis();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            ((TextView)(findViewById(R.id.puzzleTimer))).setText(DateHelper.getPuzzleTimeString(timeInMilliseconds));
            handler.postDelayed(this, 20);
        }
    };

    public void populateTiles(List<Tile> tiles) {
        if (puzzleId == 0) { return; }

        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();
        Pair<Integer, Integer> maxXY = TileHelper.getMaxXY(tiles);
        int leftOffset = (dh.getSizes(this).widthPixels / 3) - (maxXY.first * (dh.getTileWidth() / 2));
        int topOffset = (dh.getSizes(this).heightPixels / 3) - (maxXY.second * (dh.getTileHeight() / 2));

        tileContainer.removeAllViews();
        for (final Tile tile : tiles) {
            ZoomableViewGroup.LayoutParams layoutParams = new ZoomableViewGroup.LayoutParams(ZoomableViewGroup.LayoutParams.WRAP_CONTENT, ZoomableViewGroup.LayoutParams.WRAP_CONTENT);
            int leftPadding = leftOffset + (tile.getY() + tile.getX()) * (dh.getTileWidth()/2);
            int topPadding = topOffset + (tile.getX() + maxXY.second - tile.getY()) * (dh.getTileHeight()/2);
            layoutParams.setMargins(leftPadding, topPadding, 0, 0);

            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = dh.createTileImageView(this, tile, maxXY.second, drawableId);

            tileContainer.addView(image, layoutParams);
        }
    }

    public void zoomIn(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() + 0.5f);
    }

    public void zoomOut(View v) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup) findViewById(R.id.tileContainer);
        tileContainer.setScaleFactor(tileContainer.getScaleFactor() - 0.5f);
    }

    public void handleTileClick(ImageView image, Tile tile) {
        tile.rotate(undoing);
        int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
        Picasso.with(this).load(drawableId).into(image);

        ((TextView) findViewById(R.id.moveCounter)).setText(Integer.toString(++movesMade));
        ((TextView) findViewById(R.id.undoButton)).setTextColor(undoing ? Color.GRAY : Color.BLACK);

        timeLastMoved = SystemClock.uptimeMillis();

        undoing = false;
        justUndone = false;

        this.lastChangedImage = image;
        this.lastChangedTile = tile;
    }

    public void undoLastMove(View v) {
        if (lastChangedImage != null && !justUndone) {
            undoing = true;
            movesMade = movesMade - 2;
            handleTileClick(lastChangedImage, lastChangedTile);
            justUndone = true;
        }
    }

    public void flowCheck() {
        boolean flowsCorrectly = false;
        while (!flowsCorrectly) {
            flowsCorrectly = TileHelper.checkPuzzleFlow(this.puzzleId);
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);

                displayPuzzleEndScreen();
                Tile.executeQuery("UPDATE tile SET rotation = default_rotation WHERE puzzle_id = " + puzzleId);
            }
        });
    }

    public void displayPuzzleEndScreen() {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        findViewById(R.id.puzzleTimer).setVisibility(View.GONE);
        findViewById(R.id.zoomIn).setVisibility(View.GONE);
        findViewById(R.id.zoomOut).setVisibility(View.GONE);
        findViewById(R.id.moveCounter).setVisibility(View.GONE);
        findViewById(R.id.undoButton).setVisibility(View.GONE);

        findViewById(R.id.endGame).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.starCompletion)).setImageResource(puzzle.hasCompletionStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
        ((ImageView) findViewById(R.id.starTime)).setImageResource(puzzle.hasTimeStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);
        ((ImageView) findViewById(R.id.starMoves)).setImageResource(puzzle.hasMovesStar() ? R.drawable.ui_star_achieved : R.drawable.ui_star_unachieved);

        timeInMilliseconds = timeLastMoved - startTime;
        int stars = PuzzleHelper.getStars(puzzle, timeInMilliseconds, movesMade);
        boolean isFirstComplete = puzzle.getBestTime() == 0;

        Pair<Boolean, Boolean> newBests = PuzzleHelper.updateBest(puzzle, timeInMilliseconds, movesMade, stars);


        ((TextView) findViewById(R.id.currentMoves)).setText(Integer.toString(movesMade));
        ((TextView) findViewById(R.id.currentTime)).setText(DateHelper.getPuzzleTimeString(timeInMilliseconds));

        ((TextView) findViewById(R.id.bestMoves)).setText(puzzle.getBestMoves() + (newBests.second ? "*" : ""));
        ((TextView) findViewById(R.id.bestTime)).setText(DateHelper.getPuzzleTimeString(puzzle.getBestTime()) + (newBests.first ? "*" : ""));

        ((TextView) findViewById(R.id.parMoves)).setText(Integer.toString(puzzle.getParMoves()));
        ((TextView) findViewById(R.id.parTime)).setText(DateHelper.getPuzzleTimeString(puzzle.getParTime()));

        ((TextView) findViewById(R.id.tilesUnlocked)).setText(puzzle.getUnlockableTiles().size() + (isFirstComplete ? "*" : ""));

        // New tiles dialog
        // If current / best <= par, display in gold
        // XIf new best, display asterisk next to best text
    }

    public void closePuzzle(View v) {
        this.finish();
    }
}

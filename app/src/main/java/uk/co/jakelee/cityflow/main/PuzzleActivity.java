package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;

    private Handler customHandler = new Handler();

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

        final Runnable everySecond = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        flowCheck();
                    }
                }).start();
                handler.postDelayed(this, DateHelper.MILLISECONDS_IN_SECOND);
            }
        };
        handler.post(everySecond);
        startCountdownTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);

        Tile.executeQuery("UPDATE tile SET rotation = default_rotation WHERE puzzle_id = " + puzzleId);
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
        final TextView countdownTimer = (TextView)findViewById(R.id.clickableCountdown);
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
            }

        }.start();
    }

    public void startTimeTakenTimer() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000);
            ((TextView)(findViewById(R.id.puzzleTimer))).setText(secs + "." + milliseconds);
            customHandler.postDelayed(this, 10);
        }

    };

    public void populateTiles(List<Tile> tiles) {
        if (puzzleId == 0) { return; }

        RelativeLayout tileContainer = (RelativeLayout) findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();
        int maxY = TileHelper.getMaxY(tiles);

        List<ImageView> images = new ArrayList<>();
        for (final Tile tile : tiles) {
            int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
            ImageView image = dh.createTileImageView(this, tile, maxY, drawableId);
            images.add(image);
        }

        tileContainer.removeAllViews();
        for (ImageView image : images) {
            tileContainer.addView(image);
        }
    }

    public void handleTileClick(ImageView image, Tile tile) {
        tile.rotate();
        int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
        Picasso.with(this).load(drawableId).into(image);
    }

    public void flowCheck() {
        final boolean flowsCorrectly = TileHelper.checkPuzzleFlow(this.puzzleId);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.successMessage).setVisibility(flowsCorrectly ? View.VISIBLE : View.GONE);
                if (flowsCorrectly) {
                    customHandler.removeCallbacksAndMessages(null);
                }
            }
        });
    }
}

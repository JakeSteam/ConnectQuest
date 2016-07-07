package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.PuzzleHelper;
import uk.co.jakelee.cityflow.helper.TextHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;
    private int movesMade = 0;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;

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
                handler.postDelayed(this, DateHelper.MILLISECONDS_IN_SECOND / 2);
            }
        };
        handler.post(everySecond);
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
        final TextView countdownTimer = (TextView)findViewById(R.id.blockingMessage);
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
        handler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            ((TextView)(findViewById(R.id.puzzleTimer))).setText(DateHelper.getPuzzleTimeString(timeInMilliseconds));
            handler.postDelayed(this, 5);
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
        movesMade++;
    }

    public void flowCheck() {
        final boolean flowsCorrectly = TileHelper.checkPuzzleFlow(this.puzzleId);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flowsCorrectly) {
                    handler.removeCallbacksAndMessages(null);

                    displayPuzzleEndScreen();
                    Tile.executeQuery("UPDATE tile SET rotation = default_rotation WHERE puzzle_id = " + puzzleId);
                }
            }
        });
    }

    public void displayPuzzleEndScreen() {
        findViewById(R.id.puzzleTimer).setVisibility(View.GONE);
        TextView blockingMessage = (TextView) findViewById(R.id.blockingMessage);
        blockingMessage.setVisibility(View.VISIBLE);
        blockingMessage.setTextSize(40);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        Pair<Boolean, Boolean> newBests = PuzzleHelper.updateBest(puzzle, timeInMilliseconds, movesMade);
        int stars = PuzzleHelper.getStars(puzzle, timeInMilliseconds, movesMade);
        blockingMessage.setText(String.format(Locale.ENGLISH, Text.get(TextHelper.PUZZLE_END_TEXT),
                stars,
                DateHelper.getPuzzleTimeString(timeInMilliseconds),
                movesMade,
                DateHelper.getPuzzleTimeString(puzzle.getParTime()),
                puzzle.getParMoves(),
                DateHelper.getPuzzleTimeString(puzzle.getBestTime()),
                newBests.first ? "*" : "",
                puzzle.getBestMoves(),
                newBests.second ? "*" : ""));
    }
}

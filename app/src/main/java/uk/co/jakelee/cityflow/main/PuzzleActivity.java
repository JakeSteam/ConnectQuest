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
import android.widget.LinearLayout;
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
import uk.co.jakelee.cityflow.helper.StatisticsHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;
    private boolean isCustom;
    private int movesMade = 0;
    private long startTime = System.currentTimeMillis();
    private long timeInMilliseconds = 0L;
    private long timeLastMoved = 0L;
    private long timePaused = 0L;
    private long timeSpentPaused = 0L;
    private ImageView lastChangedImage;
    private Tile lastChangedTile;
    private boolean undoing = false;
    private boolean justUndone = false;

    private boolean timeBoostActive = false;
    private boolean moveBoostActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);
        isCustom = intent.getBooleanExtra(Constants.INTENT_PUZZLE_TYPE, true);

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        List<Tile> tiles = puzzle.getTiles();
        populateTiles(tiles);
        fetchImages(tiles);
        startCountdownTimer();

        updateBoostVisibility();
    }

    public void updateBoostVisibility() {
        int boostUndo = Boost.getOwnedCount(Constants.BOOST_UNDO);
        int boostTime = Boost.getOwnedCount(Constants.BOOST_TIME);
        int boostMove = Boost.getOwnedCount(Constants.BOOST_MOVE);
        int boostShuffle = Boost.getOwnedCount(Constants.BOOST_SHUFFLE);

        if (Setting.isTrue(Constants.SETTING_HIDE_UNSTOCKED_BOOSTS)) {
            findViewById(R.id.undoBoost).setVisibility(boostUndo > 0 ? View.VISIBLE : View.INVISIBLE);
            findViewById(R.id.timeBoost).setVisibility(boostTime > 0 ? View.VISIBLE : View.INVISIBLE);
            findViewById(R.id.moveBoost).setVisibility(boostMove > 0 ? View.VISIBLE : View.INVISIBLE);
            findViewById(R.id.shuffleBoost).setVisibility(boostShuffle > 0 ? View.VISIBLE : View.INVISIBLE);
        }

        ((TextView)findViewById(R.id.undoBoost)).setTextColor(boostUndo > 0 ? Color.BLACK : Color.LTGRAY);
        ((TextView)findViewById(R.id.timeBoost)).setTextColor(boostTime > 0 ? Color.BLACK : Color.LTGRAY);
        ((TextView)findViewById(R.id.moveBoost)).setTextColor(boostMove > 0 ? Color.BLACK : Color.LTGRAY);
        ((TextView)findViewById(R.id.shuffleBoost)).setTextColor(boostShuffle > 0 ? Color.BLACK : Color.LTGRAY);

        ((TextView)findViewById(R.id.undoCount)).setText(Integer.toString(boostUndo));
        ((TextView)findViewById(R.id.timeCount)).setText(Integer.toString(boostTime));
        ((TextView)findViewById(R.id.moveCount)).setText(Integer.toString(boostMove));
        ((TextView)findViewById(R.id.shuffleCount)).setText(Integer.toString(boostShuffle));
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
        findViewById(R.id.topUI).setVisibility(View.VISIBLE);

        startTime = SystemClock.uptimeMillis();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = (SystemClock.uptimeMillis() - startTime) - timeSpentPaused;
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
            if (tile.getTileTypeId() > 0) {
                ZoomableViewGroup.LayoutParams layoutParams = new ZoomableViewGroup.LayoutParams(ZoomableViewGroup.LayoutParams.WRAP_CONTENT, ZoomableViewGroup.LayoutParams.WRAP_CONTENT);
                int leftPadding = leftOffset + (tile.getY() + tile.getX()) * (dh.getTileWidth() / 2);
                int topPadding = topOffset + (tile.getX() + maxXY.second - tile.getY()) * (dh.getTileHeight() / 2);
                layoutParams.setMargins(leftPadding, topPadding, 0, 0);

                int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
                ImageView image = dh.createTileImageView(this, tile, drawableId);

                tileContainer.addView(image, layoutParams);
            }
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
        ((TextView) findViewById(R.id.undoBoost)).setTextColor(undoing ? Color.LTGRAY : Color.BLACK);

        timeLastMoved = SystemClock.uptimeMillis();

        if (undoing) {
            Boost.use(Constants.BOOST_UNDO);
            updateBoostVisibility();
        }

        undoing = false;
        justUndone = false;

        this.lastChangedImage = image;
        this.lastChangedTile = tile;
    }

    public void useBoostUndo(View v) {
        if (lastChangedImage != null && !justUndone && Boost.getOwnedCount(Constants.BOOST_UNDO) > 0) {
            undoing = true;
            movesMade = movesMade - 2;
            handleTileClick(lastChangedImage, lastChangedTile);
            justUndone = true;
        }
    }

    public void useBoostMove(View v) {
        if (Boost.getOwnedCount(Constants.BOOST_MOVE) > 0) {
            moveBoostActive = !moveBoostActive;
            findViewById(R.id.moveBoost).setBackgroundResource(moveBoostActive ? R.drawable.ui_button_city : R.drawable.ui_button_grey);
        }
    }

    public void useBoostTime(View v) {
        if (Boost.getOwnedCount(Constants.BOOST_TIME) > 0) {
            timeBoostActive = !timeBoostActive;
            findViewById(R.id.timeBoost).setBackgroundResource(timeBoostActive ? R.drawable.ui_button_city : R.drawable.ui_button_grey);
        }
    }

    public void useBoostShuffle(View v) {
        if (Boost.getOwnedCount(Constants.BOOST_SHUFFLE) > 0) {
            Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
            List<Tile> tiles = puzzle.getTiles();
            Puzzle.shuffle(tiles);
            populateTiles(tiles);
            Boost.use(Constants.BOOST_SHUFFLE);

            ((TextView) findViewById(R.id.moveCounter)).setText(Integer.toString(++movesMade));
            ((TextView)findViewById(R.id.shuffleBoost)).setTextColor(Boost.getOwnedCount(Constants.BOOST_SHUFFLE) > 0 ? Color.BLACK : Color.LTGRAY);

            updateBoostVisibility();
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
                displayPuzzleComplete();
            }
        });
    }

    public void displayPuzzleComplete() {
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        findViewById(R.id.puzzleTimer).setVisibility(View.GONE);
        findViewById(R.id.zoomIn).setVisibility(View.GONE);
        findViewById(R.id.zoomOut).setVisibility(View.GONE);
        findViewById(R.id.moveCounter).setVisibility(View.GONE);
        findViewById(R.id.topUI).setVisibility(View.GONE);

        movesMade = PuzzleHelper.getAdjustedMoves(movesMade, moveBoostActive);
        timeInMilliseconds = PuzzleHelper.getAdjustedTime(timeLastMoved, startTime, timeBoostActive);
        boolean isFirstComplete = puzzle.getBestTime() == 0;

        int originalStars = puzzle.getStarCount();
        Pair<Boolean, Boolean> newBests = PuzzleHelper.updateBest(puzzle, timeInMilliseconds, movesMade, isCustom);
        int stars = puzzle.getStarCount();

        populateStoryPuzzleCompleteScreen(puzzle, isFirstComplete, originalStars, stars, isCustom);
    }

    public void populateStoryPuzzleCompleteScreen(Puzzle puzzle, boolean isFirstComplete, int originalStars, int stars, boolean isCustom) {
        if (!isCustom) {
            List<Integer> boostsEarned = PuzzleHelper.getEarnedBoosts(isFirstComplete, originalStars != 3 && stars == 3, stars == 3);
            findViewById(R.id.boostsContainer).setVisibility(boostsEarned.size() > 0 ? View.VISIBLE : View.INVISIBLE);
            PuzzleHelper.populateBoostImages(dh, (LinearLayout) findViewById(R.id.boostsContainer), boostsEarned);

            List<TileType> tilesUnlocked = puzzle.getUnlockableTiles();
            findViewById(R.id.tilesContainer).setVisibility((tilesUnlocked.size() > 0 && isFirstComplete) ? View.VISIBLE : View.INVISIBLE);
            PuzzleHelper.populateTileImages(dh, (LinearLayout) findViewById(R.id.tilesContainer), tilesUnlocked, isFirstComplete);
        }

        findViewById(R.id.endGame).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.skyscraperCompletionTitle)).setText("Complete\n100%");
        ((TextView) findViewById(R.id.skyscraperCompletionTitle)).setTextColor(getResources().getColor(R.color.gold));
        ((ImageView) findViewById(R.id.skyscraperCompletion)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, 100, Constants.SKYSCRAPER_COMPLETE));
        ((TextView) findViewById(R.id.skyscraperCompletionText)).setText("Completed!");

        int timeProgress = StatisticsHelper.getPuzzleCriteriaProgress((int) timeInMilliseconds, (int) puzzle.getParTime());
        ((TextView) findViewById(R.id.skyscraperTimeTitle)).setText("Time\n" + timeProgress + "%");
        ((TextView) findViewById(R.id.skyscraperTimeTitle)).setTextColor(getResources().getColor(timeProgress == 100 ? R.color.gold : R.color.white));
        ((ImageView) findViewById(R.id.skyscraperTime)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, timeProgress, Constants.SKYSCRAPER_TIME));
        ((TextView) findViewById(R.id.skyscraperTimeText)).setText((timeInMilliseconds > 0 ? DateHelper.getPuzzleTimeString(timeInMilliseconds) : "0") + "/" + DateHelper.getPuzzleTimeString(puzzle.getParTime()));

        int moveProgress = StatisticsHelper.getPuzzleCriteriaProgress(movesMade, puzzle.getParMoves());
        ((TextView) findViewById(R.id.skyscraperMovesTitle)).setText("Moves\n" + moveProgress + "%");
        ((TextView) findViewById(R.id.skyscraperMovesTitle)).setTextColor(getResources().getColor(moveProgress == 100 ? R.color.gold : R.color.white));
        ((ImageView) findViewById(R.id.skyscraperMoves)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, moveProgress, Constants.SKYSCRAPER_MOVES));
        ((TextView) findViewById(R.id.skyscraperMovesText)).setText((movesMade > 0 ? movesMade : 0) + "/" + puzzle.getParMoves() + "\nmoves");

        ((TextView)findViewById(R.id.mainActionButton)).setText(isCustom ? R.string.icon_edit : R.string.icon_next);
    }

    public void pausePuzzle(View v) {
        pause();
    }

    private void pause() {
        findViewById(R.id.pauseScreen).setVisibility(View.VISIBLE);
        timePaused = System.currentTimeMillis();
        handler.removeCallbacksAndMessages(null);
    }

    public void resumePuzzle(View v) {
        findViewById(R.id.pauseScreen).setVisibility(View.GONE);
        timeSpentPaused += (System.currentTimeMillis() - timePaused);
        timePaused = 0;
        handler.post(updateTimerThread);
    }

    @Override
    public void onBackPressed() {
        if (timePaused > 0) {
            this.finish();
        } else {
            pause();
        }
    }

    public void closePuzzle(View v) {
        this.finish();
    }

    public void restartPuzzle(View v) {
        this.onStop();
        this.finish();
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra(Constants.INTENT_PUZZLE, puzzleId);
        intent.putExtra(Constants.INTENT_PUZZLE_TYPE, isCustom);
        startActivity(intent);
    }

    public void mainAction(View v) {
        if (isCustom) {
            this.finish();
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra(Constants.INTENT_PUZZLE, puzzleId);
            startActivity(intent);
        } else {
            int nextPuzzle = PuzzleHelper.getNextPuzzleId(puzzleId);
            if (nextPuzzle > 0) {
                Intent intent = new Intent(this, PuzzleActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, nextPuzzle);
                intent.putExtra(Constants.INTENT_PUZZLE_TYPE, false);
                startActivity(intent);
            }
            this.onStop();
            this.finish();
        }
    }
}

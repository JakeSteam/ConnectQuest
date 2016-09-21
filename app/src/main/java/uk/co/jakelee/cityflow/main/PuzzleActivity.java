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

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.ImageHelper;
import uk.co.jakelee.cityflow.helper.PuzzleHelper;
import uk.co.jakelee.cityflow.helper.StorageHelper;
import uk.co.jakelee.cityflow.helper.TileHelper;
import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class PuzzleActivity extends Activity {
    private static final Handler handler = new Handler();
    private DisplayHelper dh;
    private int puzzleId;
    private boolean isCustom;
    private PuzzleCustom puzzleCustom;
    private int boostsUsed = 0;
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
    private boolean exitedPuzzle = false;

    private boolean timeBoostActive = false;
    private boolean moveBoostActive = false;

    private List<Integer> changedTilesX = new ArrayList<>();
    private List<Integer> changedTilesY = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        puzzleId = intent.getIntExtra(Constants.INTENT_PUZZLE, 0);
        isCustom = intent.getBooleanExtra(Constants.INTENT_PUZZLE_TYPE, true);
        if(isCustom) {
            puzzleCustom = PuzzleCustom.get(puzzleId);
        }

        startCountdownTimer();
        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        puzzle.resetTileRotations();
        List<Tile> tiles = puzzle.getTiles();
        populateTiles(tiles);
        fetchImages(tiles);

        updateBoostVisibility();
        populateText();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.pauseScreenText)).setText(Text.get("WORD_PAUSED"));
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

        Puzzle puzzle = Puzzle.getPuzzle(puzzleId);
        puzzle.resetTileRotations();
        exitedPuzzle = true;
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
                    countdownTimer.setText(Text.get("WORD_FLOW") + "!");
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

        changedTilesX.add(tile.getX());
        changedTilesY.add(tile.getY());
        int drawableId = ImageHelper.getTileDrawableId(this, tile.getTileTypeId(), tile.getRotation());
        Picasso.with(this).load(drawableId).into(image);

        ((TextView) findViewById(R.id.moveCounter)).setText(Integer.toString(++movesMade));

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
            boostsUsed++;
        }
    }

    public void useBoostMove(View v) {
        if (!moveBoostActive && Boost.getOwnedCount(Constants.BOOST_MOVE) > 0) {
            moveBoostActive = true;
            findViewById(R.id.moveBoost).setBackgroundResource(moveBoostActive ? R.drawable.ui_button_city : R.drawable.ui_button_grey);
            boostsUsed++;
        }
    }

    public void useBoostTime(View v) {
        if (!timeBoostActive && Boost.getOwnedCount(Constants.BOOST_TIME) > 0) {
            timeBoostActive = true;
            findViewById(R.id.timeBoost).setBackgroundResource(timeBoostActive ? R.drawable.ui_button_city : R.drawable.ui_button_grey);
            boostsUsed++;
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
        List<Tile> badTiles = Puzzle.getPuzzle(puzzleId).getTiles();
        List<Integer> badTilesX = new ArrayList<>();
        List<Integer> badTilesY = new ArrayList<>();

        // Initially load up the arrays with all the tiles
        for (Tile tile : badTiles) {
            badTilesX.add(tile.getX());
            badTilesY.add(tile.getY());
        }

        boolean firstScan = true;
        while (badTilesX.size() > 0 && badTilesY.size() > 0 && !exitedPuzzle) {

            // Add any tiles that have changed to the array, so we recheck them
            badTilesX.addAll(changedTilesX);
            badTilesY.addAll(changedTilesY);

            // Empty the arrays since they've been added now
            changedTilesX.clear();
            changedTilesY.clear();

            // Check the tiles, then save the results back to the bad tiles arrays
            Pair<List<Integer>, List<Integer>> badTilesNew = TileHelper.checkPuzzleFlow(this, puzzleId, badTilesX, badTilesY, firstScan, (TextView)findViewById(R.id.puzzleLoadingIndicator));
            badTilesX = badTilesNew.first;
            badTilesY = badTilesNew.second;

            if (firstScan) {
                firstScan = false;
            }
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                if (!exitedPuzzle) {
                    displayPuzzleComplete();
                }
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
        int nextPuzzle = isCustom ? 0 : PuzzleHelper.getNextPuzzleId(puzzleId);
        Pair<Boolean, Boolean> newBests = PuzzleHelper.processPuzzleCompletion(puzzle, nextPuzzle == 0, timeInMilliseconds, movesMade, boostsUsed, puzzleCustom);
        int stars = puzzle.getStarCount();

        if (isCustom) {
            if (puzzle.getCustomData().isOriginalAuthor()) {
                final Activity activity = this;
                new Thread(new Runnable() {
                    public void run() {
                        StorageHelper.saveCustomPuzzleImage(activity, puzzleId);
                    }
                }).start();
            }
        }
        populatePuzzleCompleteScreen(puzzle, isFirstComplete, originalStars, stars, isCustom);
    }

    public void populatePuzzleCompleteScreen(Puzzle puzzle, boolean isFirstComplete, int originalStars, int stars, boolean isCustom) {
        if (!isCustom) {
            List<TileType> tilesUnlocked = puzzle.getUnlockableTiles();
            findViewById(R.id.tilesContainer).setVisibility((tilesUnlocked.size() > 0 && isFirstComplete) ? View.VISIBLE : View.INVISIBLE);
            PuzzleHelper.populateTileImages(dh, (LinearLayout) findViewById(R.id.tilesContainer), tilesUnlocked, isFirstComplete);
        }

        int currencyEarned = PuzzleHelper.getCurrencyEarned(puzzleCustom, isFirstComplete, originalStars, stars);
        Statistic.addCurrency(currencyEarned);
        ((TextView)findViewById(R.id.currencyEarned)).setText(String.format(Text.get("ALERT_COINS_EARNED"), currencyEarned));

        findViewById(R.id.endGame).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.skyscraperCompletionTitle)).setText(Text.get("UI_SKYSCRAPER_COMPLETE_TITLE"));
        ((TextView) findViewById(R.id.skyscraperCompletionTitle)).setTextColor(getResources().getColor(R.color.gold));
        ((ImageView) findViewById(R.id.skyscraperCompletion)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, 100, Constants.SKYSCRAPER_COMPLETE));
        ((TextView) findViewById(R.id.skyscraperCompletionText)).setText(Text.get("UI_SKYSCRAPER_COMPLETE_TEXT"));

        int timeProgress = PuzzleHelper.getPuzzleCriteriaProgress((int) timeInMilliseconds, (int) puzzle.getParTime());
        ((TextView) findViewById(R.id.skyscraperTimeTitle)).setText(String.format(Text.get("UI_SKYSCRAPER_TIME_TITLE"), timeProgress));
        ((TextView) findViewById(R.id.skyscraperTimeTitle)).setTextColor(getResources().getColor(timeProgress == 100 ? R.color.gold : R.color.white));
        ((ImageView) findViewById(R.id.skyscraperTime)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, timeProgress, Constants.SKYSCRAPER_TIME));
        ((TextView) findViewById(R.id.skyscraperTimeText)).setText(String.format(Text.get("UI_SKYSCRAPER_TIME_TEXT"),
                timeInMilliseconds > 0 ? DateHelper.getPuzzleTimeString(timeInMilliseconds) : "0",
                DateHelper.getPuzzleTimeString(puzzle.getParTime())));

        int moveProgress = PuzzleHelper.getPuzzleCriteriaProgress(movesMade, puzzle.getParMoves());
        ((TextView) findViewById(R.id.skyscraperMovesTitle)).setText(String.format(Text.get("UI_SKYSCRAPER_MOVES_TITLE"), moveProgress));
        ((TextView) findViewById(R.id.skyscraperMovesTitle)).setTextColor(getResources().getColor(moveProgress == 100 ? R.color.gold : R.color.white));
        ((ImageView) findViewById(R.id.skyscraperMoves)).setImageResource(PuzzleHelper.getSkyscraperDrawable(this, moveProgress, Constants.SKYSCRAPER_MOVES));
        ((TextView) findViewById(R.id.skyscraperMovesText)).setText(String.format(Text.get("UI_SKYSCRAPER_MOVES_TEXT"),
                movesMade > 0 ? movesMade : 0,
                puzzle.getParMoves()));

        if (puzzleCustom == null || puzzleCustom.isOriginalAuthor()) {
            ((TextView) findViewById(R.id.mainActionButton)).setText(isCustom ? R.string.icon_edit : R.string.icon_next);
        } else {
            findViewById(R.id.mainActionButton).setVisibility(View.GONE);
        }
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

    public void openShop(View view) {
        this.onStop();
        this.finish();
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void mainAction(View v) {
        if (isCustom) {
            this.finish();
            if (puzzleCustom.isOriginalAuthor()) {
                Intent intent = new Intent(this, EditorActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, puzzleId);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        } else {
            int nextPuzzle = PuzzleHelper.getNextPuzzleId(puzzleId);
            if (nextPuzzle > 0) {
                Intent intent = new Intent(this, PuzzleActivity.class);
                intent.putExtra(Constants.INTENT_PUZZLE, nextPuzzle);
                intent.putExtra(Constants.INTENT_PUZZLE_TYPE, false);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            this.onStop();
            this.finish();
        }
    }
}

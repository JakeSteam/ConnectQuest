package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Setting;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

import static com.orm.query.Select.from;

public class PuzzleHelper {
    public static Pair<Boolean, Boolean> processPuzzleCompletion(Context context, final Puzzle puzzle, final boolean isCompletingPack, long timeTaken, final int movesTaken, final int boostsUsed, PuzzleCustom puzzleCustom) {
        boolean newBestTime = false;
        boolean newBestMoves = false;
        if (timeTaken >= 0 && (timeTaken < puzzle.getBestTime() || puzzle.getBestTime() == 0)) {
            puzzle.setBestTime(timeTaken);
            newBestTime = true;
            if ((timeTaken <= puzzle.getParTime() || Setting.getSafeBoolean(Constants.SETTING_ZEN_MODE)) && !puzzle.hasTimeStar()) {
                puzzle.setTimeStar(true);
                if (puzzleCustom != null) {
                    puzzle.setParTime(timeTaken);
                    puzzle.setBestTime(timeTaken);
                }
            }
        }
        if (movesTaken >= 0 && (movesTaken < puzzle.getBestMoves() || puzzle.getBestMoves() == 0)) {
            puzzle.setBestMoves(movesTaken);
            newBestMoves = true;
            if (movesTaken <= puzzle.getParMoves() && !puzzle.hasMovesStar()) {
                puzzle.setMovesStar(true);
                if (puzzleCustom != null) {
                    puzzle.setParMoves(movesTaken);
                    puzzle.setBestMoves(movesTaken);
                }
            }
        }

        if (!puzzle.hasCompletionStar()) {
            puzzle.setCompletionStar(true);
        }

        puzzle.save();

        if (puzzleCustom == null) {
            performBackgroundTasks(context, puzzle, isCompletingPack, movesTaken, boostsUsed);
        } else if (puzzleCustom.isOriginalAuthor() && movesTaken > 0) {
            puzzleCustom.setHasBeenTested(true);
            puzzleCustom.save();
        }

        return new Pair<>(newBestTime, newBestMoves);
    }

    public static void performBackgroundTasks(final Context context, final Puzzle puzzle, final boolean isCompletingPack, final int movesTaken, final int boostsUsed) {
        new Thread(new Runnable() {
            public void run() {
                puzzle.unlockRelatedTiles();
                Pack pack = Pack.getPack(puzzle.getPackId());
                if (isCompletingPack) {
                    pack.increaseCompletedCount();
                }
                pack.refreshMetrics();
                TileType.executeQuery(String.format(Locale.ENGLISH, "UPDATE tile_type SET status = %1$d WHERE puzzle_required = %2$d",
                        Constants.TILE_STATUS_UNLOCKED,
                        puzzle.getPuzzleId()));
                puzzle.getUnlockableTiles();

                if (puzzle.hasCompletionStar() && puzzle.hasMovesStar() && puzzle.hasTimeStar()) {
                    GooglePlayHelper.UpdateEvent(Constants.EVENT_FULLY_COMPLETE_PUZZLE, 1); // Quests
                    Statistic.increaseByOne(Constants.STATISTIC_PUZZLES_COMPLETED_FULLY); // Achievements
                    GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_PUZZLES_FULLY_COMPLETED, Statistic.getInt(Constants.STATISTIC_PUZZLES_COMPLETED_FULLY)); // Leaderboards
                }

                Setting tutorialStage = Setting.get(Constants.SETTING_TUTORIAL_STAGE);
                if (tutorialStage.getIntValue() <= Constants.TUTORIAL_MAX) {
                    tutorialStage.setIntValue(tutorialStage.getIntValue() + 1);
                    tutorialStage.save();
                }

                // Update for quests
                GooglePlayHelper.UpdateEvent(Constants.EVENT_COMPLETE_PUZZLE, 1);
                GooglePlayHelper.UpdateEvent(Constants.EVENT_USE_BOOST, boostsUsed);
                GooglePlayHelper.UpdateEvent(Constants.EVENT_TILE_ROTATE, movesTaken);

                // Update for achievements
                Statistic.increaseByOne(Constants.STATISTIC_PUZZLES_COMPLETED);
                Statistic.increaseByX(Constants.STATISTIC_BOOSTS_USED, boostsUsed);
                Statistic.increaseByX(Constants.STATISTIC_TILES_ROTATED, movesTaken);
                GooglePlayHelper.UpdateAchievements();

                // Update for leaderboards
                GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_PUZZLES_COMPLETED, Statistic.getInt(Constants.STATISTIC_PUZZLES_COMPLETED));
                GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_BOOSTS_USED, Statistic.getInt(Constants.STATISTIC_BOOSTS_USED));

                if (GooglePlayHelper.shouldAutosave()) {
                    GooglePlayHelper.autosave(context);
                }
            }
        }).start();
    }

    public static int getCurrencyEarned(PuzzleCustom puzzleCustom, boolean isFirstComplete, int originalStars, int stars) {
        if (puzzleCustom != null && puzzleCustom.isOriginalAuthor()) {
            return 0;
        }

        boolean isCustom = puzzleCustom != null;
        boolean isFirstFullComplete = originalStars < 3 && stars == 3;
        int currency = 0;
        if (isFirstComplete) {
            currency += isCustom ? Constants.CURRENCY_CUSTOM_FIRST_COMPLETE : Constants.CURRENCY_FIRST_COMPLETE;
        }
        if (isFirstFullComplete) {
            currency += isCustom ? Constants.CURRENCY_CUSTOM_FIRST_COMPLETE_FULL : Constants.CURRENCY_FIRST_COMPLETE_FULL;
        }
        if (!isFirstComplete && !isFirstFullComplete && !isCustom) {
            currency += Constants.CURRENCY_RECOMPLETE;
        }
        return currency;
    }

    public static long getAdjustedTime(long timeLastMoved, long startTime, boolean timeBoostActive) {
        long time = timeLastMoved - startTime;
        if (timeBoostActive) {
            Boost timeBoost = Boost.get(Constants.BOOST_TIME);
            timeBoost.use();

            double multiplier = 1 - (0.1 * timeBoost.getLevel());
            time *= multiplier;
        }
        return time;
    }

    public static int getAdjustedMoves(int movesMade, boolean moveBoostActive) {
        if (moveBoostActive) {
            Boost moveBoost = Boost.get(Constants.BOOST_MOVE);
            moveBoost.use();

            movesMade = moveBoost.getLevel() > movesMade ? 0 : movesMade - moveBoost.getLevel();
        }
        return movesMade;
    }

    public static void populateTileImages(DisplayHelper dh, LinearLayout tilesContainer, List<TileType> tiles, boolean isFirstComplete) {
        if (!isFirstComplete) {
            return;
        }

        String tileString = "";
        for (TileType tile : tiles) {
            tileString += tile.getName() + ", ";
            tilesContainer.addView(dh.createTileIcon(tile, 50, 50, false));
        }

        if (tiles.size() > 0) {
            tileString = String.format(Locale.ENGLISH, Text.get("UI_TILE_UNLOCK"), tileString.substring(0, tileString.length() - 2));
        } else {
            tileString = Text.get("UI_TILE_NO_UNLOCK");
        }

        TextView textView = dh.createTextView(tileString, 18, Color.WHITE);
        textView.setSingleLine(false);
        tilesContainer.addView(textView);
    }

    public static int getNextPuzzleId(int puzzleId) {
        Puzzle currentPuzzle = Puzzle.getPuzzle(puzzleId);
        List<Puzzle> puzzles = Pack.getPack(currentPuzzle.getPackId()).getPuzzles();
        int nextPuzzleId = 0;
        boolean nextPuzzleIsNext = false;
        for (Puzzle puzzle : puzzles) {
            if (nextPuzzleIsNext) {
                nextPuzzleId = puzzle.getPuzzleId();
                break;
            }
            nextPuzzleIsNext = puzzle.getPuzzleId() == puzzleId;
        }

        return nextPuzzleId;
    }

    public static int getSkyscraperDrawable(Context context, int progress, int skyscraper) {
        int adjustedProgress = (progress / 10) * 10;
        return context.getResources().getIdentifier("building_" + skyscraper + "_" + adjustedProgress, "drawable", context.getPackageName());
    }

    public static int getNextCustomPuzzleId() {
        Puzzle firstCustomPuzzle = Puzzle.getPuzzle(Constants.PUZZLE_CUSTOM_ID_OFFSET);
        if (firstCustomPuzzle == null) {
            return Constants.PUZZLE_CUSTOM_ID_OFFSET;
        }

        return from(Puzzle.class).orderBy("puzzle_id DESC").first().getPuzzleId() + 1;
    }

    public static int getDefaultTileId(int environmentId) {
        switch (environmentId) {
            case Constants.ENVIRONMENT_NONE:
                return 0;
            case Constants.ENVIRONMENT_GRASS:
                return 6;
            case Constants.ENVIRONMENT_CITY:
                return 21;
            case Constants.ENVIRONMENT_FOREST:
                return 69;
            case Constants.ENVIRONMENT_MOUNTAIN:
                return 87;
            case Constants.ENVIRONMENT_DESERT:
                return 93;
            case Constants.ENVIRONMENT_GOLF:
                return 138;
            default:
                return 0;
        }
    }

    public static Puzzle createBasicPuzzleObject(int puzzleId) {
        Puzzle puzzle = new Puzzle();

        puzzle.setPuzzleId(puzzleId);
        puzzle.setParMoves(Constants.PUZZLE_DEFAULT_MOVES);
        puzzle.setParTime(Constants.PUZZLE_DEFAULT_TIME);
        puzzle.setPackId(0);
        puzzle.setBestMoves(Constants.PUZZLE_DEFAULT_MOVES);
        puzzle.setBestTime(Constants.PUZZLE_DEFAULT_TIME);
        puzzle.setCompletionStar(false);
        puzzle.setMovesStar(false);
        puzzle.setTimeStar(false);
        return puzzle;
    }

    public static PuzzleCustom createBasicPuzzleCustomObject(int puzzleId) {
        return createBasicPuzzleCustomObject(puzzleId, 0, 0);
    }

    public static PuzzleCustom createBasicPuzzleCustomObject(int puzzleId, int maxX, int maxY) {
        PuzzleCustom puzzleCustom = new PuzzleCustom();

        puzzleCustom.setPuzzleId(puzzleId);
        puzzleCustom.setName(String.format(Locale.ENGLISH, Text.get("PUZZLE_DEFAULT_NAME"),
                maxX,
                maxY,
                DateHelper.displayTime(System.currentTimeMillis(), DateHelper.date)));
        puzzleCustom.setDescription(Text.get("PUZZLE_DEFAULT_DESC"));
        puzzleCustom.setAuthor(Setting.getString(Constants.SETTING_PLAYER_NAME));
        puzzleCustom.setDateAdded(System.currentTimeMillis());
        puzzleCustom.setOriginalAuthor(true);
        return puzzleCustom;
    }

    public static int getPuzzleCriteriaProgress(int actualValue, int targetValue) {
        if (actualValue <= targetValue) {
            return 100;
        }
        return (int) Math.floor(((double) targetValue / (double) actualValue) * 100);
    }

    public static int getTotalStars() {
        int totalStars = 0;

        List<Pack> packs = Pack.listAll(Pack.class);
        for (Pack pack : packs) {
            totalStars += pack.getCurrentStars();
        }

        return totalStars;
    }

    public static void rotatePuzzle(int puzzleId, boolean clockwise) {
        List<Tile> tiles = Select.from(Tile.class).where(
                Condition.prop("puzzle_id").eq(puzzleId)).list();
        Pair<Integer, Integer> maxXY = TileHelper.getMaxXY(tiles);

        for (Tile tile : tiles) {
            int oldX = tile.getX();
            int oldY = tile.getY();
            tile.rotate(!clockwise);
            tile.setDefaultRotation(tile.getRotation());
            tile.setX(clockwise ? oldY : maxXY.second - oldY);
            tile.setY(clockwise ? maxXY.first - oldX : oldX);
            tile.save();
        }
    }

    public static void resizePuzzle(final int puzzleId, final int oldX, final int oldY, final int newX, final int newY) {
        List<Tile> tiles = new ArrayList<>();

        // Increasing width
        if (oldX < newX) {
            for (int x = newX; x > oldX; x--) {
                for (int y = newY; y > 0; y--) {
                    tiles.add(new Tile(puzzleId, 0, x - 1, y - 1, Constants.ROTATION_NORTH));
                }
            }
        }

        // Increasing height
        if (oldY < newY) {
            for (int y = newY; y > oldY; y--) {
                for (int x = newX; x > 0; x--) {
                    tiles.add(new Tile(puzzleId, 0, x - 1, y - 1, Constants.ROTATION_NORTH));
                }
            }
        }

        if (tiles.size() > 0) {
            Tile.saveInTx(tiles);
        }

        // Decreasing width
        if (oldX > newX) {
            Tile.deleteAll(Tile.class, "puzzle_id = " + puzzleId + " AND x >= " + newX);
        }

        // Decreasing height
        if (oldY > newY) {
            Tile.deleteAll(Tile.class, "puzzle_id = " + puzzleId + " AND y >= " + newY);
        }
    }
}

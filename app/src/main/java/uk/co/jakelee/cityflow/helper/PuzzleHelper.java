package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            if (timeTaken <= puzzle.getParTime() && !puzzle.hasTimeStar()) {
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
                Pack pack = Pack.getPack(puzzle.getPackId());
                if (isCompletingPack) {
                    pack.increaseCompletedCount();
                }
                pack.refreshMetrics();
                TileType.executeQuery(String.format("UPDATE tile_type SET status = %1$d WHERE puzzle_required = %2$d",
                        Constants.TILE_STATUS_UNLOCKED,
                        puzzle.getPuzzleId()));
                puzzle.getUnlockableTiles();

                if (puzzle.hasCompletionStar() && puzzle.hasMovesStar() && puzzle.hasTimeStar()) {
                    GooglePlayHelper.UpdateEvent(Constants.EVENT_FULLY_COMPLETE_PUZZLE, 1); // Quests
                    Statistic.increaseByOne(Constants.STATISTIC_PUZZLES_COMPLETED_FULLY); // Achievements
                    GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_PUZZLES_FULLY_COMPLETED, Statistic.getInt(Constants.STATISTIC_PUZZLES_COMPLETED_FULLY)); // Leaderboards
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
            tilesContainer.addView(dh.createTileIcon(tile, 50, 50));
        }

        if (tiles.size() > 0) {
            tileString = String.format(Text.get("UI_TILE_UNLOCK"), tileString.substring(0, tileString.length() - 2));
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

    public static int createNewPuzzle(int maxX, int maxY, final int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        int defaultTileId = getDefaultTileId(environmentId);
        createBasicPuzzleObject(newPuzzleId).save();
        createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY).save();

        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                tiles.add(new Tile(newPuzzleId, defaultTileId, x, y, Constants.ROTATION_NORTH));
            }
        }
        Tile.saveInTx(tiles);

        return newPuzzleId;
    }

    public static int createGeneratedPuzzle(int maxX, int maxY, int environmentId) {
        int newPuzzleId = getNextCustomPuzzleId();
        createBasicPuzzleObject(newPuzzleId).save();
        createBasicPuzzleCustomObject(newPuzzleId, maxX, maxY).save();

        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                List<Tile> potentialTiles = getPossibleTiles(newPuzzleId, tiles, x, y, maxX - 1, maxY - 1, Constants.ENVIRONMENT_CITY);
                if (potentialTiles.size() > 0) {
                    Tile selectedTile = potentialTiles.get(RandomHelper.getNumber(0, potentialTiles.size() - 1));
                    tiles.add(selectedTile);
                } else {
                    // Default tile should be based on environment
                    tiles.add(new Tile(newPuzzleId, 6, x, y, Constants.ROTATION_NORTH));
                }
            }
        }
        Tile.saveInTx(tiles);
        return newPuzzleId;
    }

    private static List<Tile> getPossibleTiles(int puzzleId, List<Tile> existingTiles, int tileX, int tileY, int maxX, int maxY, int environmentId) {
        Tile southTile = tileY == 0 ? new Tile() : existingTiles.get(existingTiles.size() - 1); // Get the south tile, or an empty one if we're starting a new column
        Tile westTile = tileX == 0 ? new Tile() : existingTiles.get(existingTiles.size() - (maxY + 1)); // Get the west tile (#Y tiles previous), or empty if new row

        int nFlow = tileY == maxY ? 0 : -1;
        int eFlow = tileX == maxX ? 0 : -1;
        int sFlow = southTile.getFlow(Constants.SIDE_NORTH);
        int wFlow = westTile.getFlow(Constants.SIDE_EAST);

        int nHeight = -1;
        int eHeight = -1;
        int sHeight = southTile.getHeight(Constants.SIDE_NORTH);
        int wHeight = westTile.getHeight(Constants.SIDE_EAST);

        // Make list
        List<Tile> tiles1 = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_NORTH, nFlow, eFlow, sFlow, wFlow);
        List<Tile> tiles2 = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_WEST, wFlow, nFlow, eFlow, sFlow);
        List<Tile> tiles3 = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_SOUTH, sFlow, wFlow, nFlow, eFlow);
        List<Tile> tiles4 = getPossibleTilesByRotation(puzzleId, tileX, tileY, environmentId, Constants.ROTATION_EAST, eFlow, sFlow, wFlow, nFlow);

        tiles1.addAll(tiles2);
        tiles1.addAll(tiles3);
        tiles1.addAll(tiles4);

        return tiles1;
    }

    private static List<Tile> getPossibleTilesByRotation(int puzzleId, int x, int y, int environmentId, int rotation, int nFlow, int eFlow, int sFlow, int wFlow) {
        String sql = String.format(Locale.getDefault(),
                "SELECT * FROM tile_type WHERE environment_id = %1$d AND flow_north %2$s %3$d AND flow_east %4$s %5$d AND flow_south %6$s %7$d AND flow_west %8$s %9$d AND height_north = 4 AND height_east = 4 AND height_south = 4 AND height_west = 4",
                environmentId,
                nFlow >= 0 ? "=" : ">=",
                nFlow,
                eFlow >= 0 ? "=" : ">=",
                eFlow,
                sFlow >= 0 ? "=" : ">=",
                sFlow,
                wFlow >= 0 ? "=" : ">=",
                wFlow);
        List<TileType> tileTypes = TileType.findWithQuery(TileType.class, sql);

        List<Tile> tiles = new ArrayList<>();
        for (TileType tile : tileTypes) {
            tiles.add(new Tile(puzzleId, tile.getTypeId(), x, y, rotation));
        }
        return tiles;
    }

    public static int getDefaultTileId(int environmentId) {
        switch(environmentId) {
            case Constants.ENVIRONMENT_NONE: return 0;
            case Constants.ENVIRONMENT_GRASS: return 6;
            case Constants.ENVIRONMENT_CITY: return 21;
            case Constants.ENVIRONMENT_FOREST: return 69;
            case Constants.ENVIRONMENT_MOUNTAIN: return 87;
            case Constants.ENVIRONMENT_DESERT: return 93;
            case Constants.ENVIRONMENT_GOLF: return 138;
            default: return 0;
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
        puzzleCustom.setName(String.format(Text.get("PUZZLE_DEFAULT_NAME"),
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
        return (int) Math.floor(((double)targetValue / (double)actualValue) * 100);
    }

    public static int getTotalStars() {
        int totalStars = 0;

        List<Pack> packs = Pack.listAll(Pack.class);
        for (Pack pack : packs) {
            totalStars += pack.getCurrentStars();
        }

        return totalStars;
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

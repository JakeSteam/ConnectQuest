package uk.co.jakelee.cityflow.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class PuzzleHelper {
    public static Pair<Boolean, Boolean> updateMetrics(final Puzzle puzzle, long timeTaken, final int movesTaken, final int boostsUsed, boolean isCustom) {
        boolean newBestTime = false;
        boolean newBestMoves = false;
        if (timeTaken >= 0 && (timeTaken < puzzle.getBestTime() || puzzle.getBestTime() == 0)) {
            puzzle.setBestTime(timeTaken);
            newBestTime = true;
            if (timeTaken <= puzzle.getParTime() && !puzzle.hasTimeStar()) {
                puzzle.setTimeStar(true);
            }
        }
        if (movesTaken >= 0 && (movesTaken < puzzle.getBestMoves() || puzzle.getBestMoves() == 0)) {
            puzzle.setBestMoves(movesTaken);
            newBestMoves = true;
            if (movesTaken <= puzzle.getParMoves() && !puzzle.hasMovesStar()) {
                puzzle.setMovesStar(true);
            }
        }

        if (!puzzle.hasCompletionStar()) {
            puzzle.setCompletionStar(true);
        }

        puzzle.save();

        if (!isCustom) {
            final Pack pack = Pack.getPack(puzzle.getPackId());
            new Thread(new Runnable() {
                public void run() {
                    pack.refreshMetrics();;

                    if (puzzle.hasCompletionStar() && puzzle.hasMovesStar() && puzzle.hasTimeStar()) {
                        GooglePlayHelper.UpdateEvent(Constants.EVENT_FULLY_COMPLETE_PUZZLE, 1); // Quests
                        Statistic.increaseByOne(Statistic.Fields.PuzzlesCompletedFully); // Achievements
                        GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_PUZZLES_FULLY_COMPLETED, Statistic.get(Statistic.Fields.PuzzlesCompletedFully)); // Leaderboards
                    }

                    // Update for quests
                    GooglePlayHelper.UpdateEvent(Constants.EVENT_COMPLETE_PUZZLE, 1);
                    GooglePlayHelper.UpdateEvent(Constants.EVENT_USE_BOOST, boostsUsed);
                    GooglePlayHelper.UpdateEvent(Constants.EVENT_TILE_ROTATE, movesTaken);

                    // Update for achievements
                    Statistic.increaseByOne(Statistic.Fields.PuzzlesCompleted);
                    Statistic.increaseByX(Statistic.Fields.BoostsUsed, boostsUsed);
                    Statistic.increaseByX(Statistic.Fields.TilesRotated, movesTaken);
                    GooglePlayHelper.UpdateAchievements();

                    // Update for leaderboards
                    GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_PUZZLES_COMPLETED, Statistic.get(Statistic.Fields.PuzzlesCompleted));
                    GooglePlayHelper.UpdateLeaderboards(Constants.LEADERBOARD_BOOSTS_USED, Statistic.get(Statistic.Fields.BoostsUsed));

                }
            }).start();
        }
        return new Pair<>(newBestTime, newBestMoves);
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

    public static List<Integer> getEarnedBoosts(boolean isFirstComplete, boolean isFirstFullComplete, boolean isFullComplete) {
        int earnedBoosts = 0;
        List<Integer> boosts = new ArrayList<>();
        if (isFirstComplete) {
            earnedBoosts++;
        }
        if (isFirstFullComplete) {
            earnedBoosts++;
        }
        if (!isFirstFullComplete && isFullComplete && RandomHelper.getBoolean(Constants.FULL_RECOMPLETE_BOOST_CHANCE)) {
            earnedBoosts++;
        }

        for (int i = 0; i < earnedBoosts; i++) {
            int boostId = RandomHelper.getNumber(Constants.BOOST_MIN, Constants.BOOST_MAX);
            boosts.add(boostId);
            Boost.add(boostId);
        }

        return boosts;
    }

    public static void populateBoostImages(DisplayHelper dh, LinearLayout boostContainer, List<Integer> boosts) {
        String boostString = "";
        for (Integer boostId : boosts) {
            Boost boost = Boost.get(boostId);
            boostString += boost.getName() + ", ";
            boostContainer.addView(dh.createBoostIcon(boostId, 50, 50));
        }

        if (boosts.size() > 0) {
            boostString = "Earned " + boostString.substring(0, boostString.length() - 2) + " boost(s)!";
        } else {
            boostString = "No boosts earned.";
        }

        TextView textView = dh.createTextView(boostString, 18, Color.WHITE);
        textView.setSingleLine(false);
        boostContainer.addView(textView);
    }

    public static void populateTileImages(DisplayHelper dh, LinearLayout tilesContainer, List<TileType> tiles, boolean isFirstComplete) {
        if (!isFirstComplete) {
            return;
        }

        String tileString = "";
        for (TileType tile : tiles) {
            tileString += tile.getName() + ", ";
            tilesContainer.addView(dh.createTileIcon(tile.getTypeId(), 50, 50));
        }

        if (tiles.size() > 0) {
            tileString = "Unlocked " + tileString.substring(0, tileString.length() - 2) + " tile(s)!";
        } else {
            tileString = "No tiles unlocked.";
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

        return Select.from(Puzzle.class).orderBy("id DESC").first().getPuzzleId() + 1;
    }

    public static int createNewPuzzle(int maxX, int maxY) {
        int nextPuzzleId = getNextCustomPuzzleId();
        createBasicPuzzleObject(nextPuzzleId).save();
        createBasicPuzzleCustomObject(nextPuzzleId).save();

        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                //tiles.add(new Tile(nextPuzzleId, 21, x, y, Constants.ROTATION_NORTH));
                tiles.add(new Tile(nextPuzzleId, 18, x, y, RandomHelper.getNumber(Constants.ROTATION_MIN, Constants.ROTATION_MAX)));
            }
        }
        Tile.saveInTx(tiles);

        return nextPuzzleId;
    }

    public static Puzzle createBasicPuzzleObject(int puzzleId) {
        Puzzle puzzle = new Puzzle();

        puzzle.setPuzzleId(puzzleId);
        puzzle.setParMoves(10);
        puzzle.setParTime(10000);
        puzzle.setPackId(0);
        puzzle.setBestMoves(0);
        puzzle.setBestTime(0);
        puzzle.setCompletionStar(false);
        puzzle.setMovesStar(false);
        puzzle.setTimeStar(false);
        return puzzle;
    }

    public static PuzzleCustom createBasicPuzzleCustomObject(int puzzleId) {
        PuzzleCustom puzzleCustom = new PuzzleCustom();

        puzzleCustom.setPuzzleId(puzzleId);
        puzzleCustom.setName("Untitled Puzzle");
        puzzleCustom.setDescription("");
        puzzleCustom.setAuthor("Current Player");
        puzzleCustom.setDateAdded(System.currentTimeMillis());
        puzzleCustom.setOriginalAuthor(true);
        puzzleCustom.save();
        return puzzleCustom;
    }
}

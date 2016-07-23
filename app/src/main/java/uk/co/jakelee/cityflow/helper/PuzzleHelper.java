package uk.co.jakelee.cityflow.helper;

import android.util.Pair;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Tile_Type;

public class PuzzleHelper {
    public static Pair<Boolean, Boolean> updateBest(Puzzle puzzle, long timeTaken, int movesTaken, int stars) {
        boolean newBestTime = false;
        boolean newBestMoves = false;
        if (timeTaken < puzzle.getBestTime() || puzzle.getBestTime() == 0) {
            puzzle.setBestTime(timeTaken);
            newBestTime = true;
            if (timeTaken <= puzzle.getParTime() && !puzzle.hasTimeStar()) {
                puzzle.setTimeStar(true);
            }
        }
        if (movesTaken < puzzle.getBestMoves() || puzzle.getBestMoves() == 0) {
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
        updatePackTotal(puzzle.getPackId());
        return new Pair<>(newBestTime, newBestMoves);
    }

    public static void updatePackTotal(int packId) {
        Pack pack = Pack.getPack(packId);
        List<Puzzle> puzzles = pack.getPuzzles();

        int stars = 0;
        for (Puzzle puzzle : puzzles) {
            if (puzzle.hasCompletionStar()) {
                stars++;
            }
            if (puzzle.hasMovesStar()) {
                stars++;
            }
            if (puzzle.hasTimeStar()) {
                stars++;
            }
        }

        pack.setCurrentStars(stars);
        pack.save();
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
        for (Integer boostId : boosts) {
            boostContainer.addView(dh.createBoostIcon(boostId, 50, 50));
        }
    }

    public static void populateTileImages(DisplayHelper dh, LinearLayout tilesContainer, List<Tile_Type> tiles, boolean isFirstComplete) {
        if (!isFirstComplete) {
            return;
        }

        for (Tile_Type tile : tiles) {
            tilesContainer.addView(dh.createTileIcon(tile.getTypeId(), 50, 50));
        }
    }
}

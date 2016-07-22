package uk.co.jakelee.cityflow.helper;

import android.util.Pair;

import java.util.List;

import uk.co.jakelee.cityflow.model.Boost;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.Puzzle;

public class PuzzleHelper {
    public static int getStars(Puzzle puzzle, long timeTaken, int moves) {
        int stars = 1;
        if (timeTaken <= puzzle.getParTime()) {
            stars++;
        }
        if (moves <= puzzle.getParMoves()) {
            stars++;
        }
        return stars;
    }

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
}

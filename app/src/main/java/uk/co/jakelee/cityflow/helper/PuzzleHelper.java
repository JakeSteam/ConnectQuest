package uk.co.jakelee.cityflow.helper;

import android.util.Pair;

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

    public static Pair<Boolean, Boolean> updateBest(Puzzle puzzle, long timeTaken, int movesTaken) {
        boolean newBestTime = false;
        boolean newBestMoves = false;
        if (timeTaken < puzzle.getBestTime() || puzzle.getBestTime() == 0) {
            puzzle.setBestTime(timeTaken);
            newBestTime = true;
        }
        if (movesTaken < puzzle.getBestMoves() || puzzle.getBestMoves() == 0) {
            puzzle.setBestMoves(movesTaken);
            newBestMoves = true;
        }

        puzzle.save();
        return new Pair<>(newBestTime, newBestMoves);
    }
}

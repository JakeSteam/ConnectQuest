package uk.co.jakelee.cityflow.helper;

import android.util.Log;

import java.util.List;

import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.Puzzle_Custom;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleShareHelper {
    private static final String sectionDelimiter = "|";
    private static final String tileDelimiter = "\\\\";
    private static final String tileElementDelimiter = "/";

    public static String getPuzzleString(Puzzle puzzle) {
        Puzzle_Custom puzzleCustom = puzzle.getCustomData();
        StringBuilder sb = new StringBuilder();
        sb.append(puzzleCustom.getName()).append(sectionDelimiter)
                .append(puzzleCustom.getDescription()).append(sectionDelimiter)
                .append(puzzleCustom.getAuthor()).append(sectionDelimiter)
                .append(puzzle.getParMoves()).append(sectionDelimiter)
                .append(puzzle.getParTime()).append(sectionDelimiter);

        List<Tile> tiles = puzzle.getTiles();
        for (Tile tile : tiles) {
            sb.append(tile.getTileTypeId()).append(tileElementDelimiter)
                    .append(tile.getX()).append(tileElementDelimiter)
                    .append(tile.getY()).append(tileDelimiter);
        }
        return ModificationHelper.encode(sb.toString(), 0);
    }

    public boolean importPuzzleString(String string) {
        Puzzle puzzle = new Puzzle();
        Puzzle_Custom puzzleCustom = new Puzzle_Custom();

        try {
            String puzzleString = ModificationHelper.decode(string, 0);
            String[] parts = puzzleString.split(sectionDelimiter);

            puzzleCustom.setName(parts[0]);
            puzzleCustom.setDescription(parts[1]);
            puzzleCustom.setAuthor(parts[2]);
            puzzleCustom.setDateAdded(System.currentTimeMillis());
            puzzleCustom.setOriginalAuthor(false);

            puzzle.setParMoves(Integer.parseInt(parts[3]));
            puzzle.setParTime(Integer.parseInt(parts[4]));
            puzzle.setPackId(0);
            puzzle.setBestMoves(0);
            puzzle.setCompletionStar(false);
            puzzle.setMovesStar(false);
            puzzle.setTimeStar(false);
            puzzle.save();

            int puzzleId = (int) (long) puzzle.getId();
            puzzle.setPuzzleId(puzzleId);
            puzzleCustom.setPuzzleId(puzzleId);

            puzzle.save();
            puzzleCustom.save();

            String[] tiles = parts[5].split(tileDelimiter);
            for (String tileString : tiles) {
                String[] tileData = tileString.split(tileElementDelimiter);
                Tile tile = new Tile();
                tile.setTileTypeId(Integer.parseInt(tileData[0]));
                tile.setX(Integer.parseInt(tileData[1]));
                tile.setY(Integer.parseInt(tileData[2]));
            }

            return true;
        } catch (Exception e) {
            Log.e("Decoding", e.getMessage());
            return false;
        }
    }
}

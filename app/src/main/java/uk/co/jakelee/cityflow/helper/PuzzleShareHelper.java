package uk.co.jakelee.cityflow.helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.model.Puzzle;
import uk.co.jakelee.cityflow.model.PuzzleCustom;
import uk.co.jakelee.cityflow.model.Tile;

public class PuzzleShareHelper {
    private static final String sectionDelimiter = "/S/";
    private static final String tileDelimiter = "/T/";
    private static final String tileElementDelimiter = "/E/";

    public static String getPuzzleSQL(Puzzle puzzle) {
        StringBuilder sb = new StringBuilder();
        sb.append("texts.add(new Text(Constants.LANGUAGE_EN_GB, \"PUZZLE_PUZZLE_ID_NAME\", \"PUZZLE_NAME\"));\n");
        sb.append(String.format("puzzles.add(new Puzzle(PUZZLE_ID, PACK_ID, %1$dL, %2$d, 0L, 0));\n",
                puzzle.getBestTime(),
                puzzle.getBestMoves()));
        List<Tile> tiles = puzzle.getTiles();
        for (Tile tile : tiles) {
            sb.append(String.format("tiles.add(new Tile(PUZZLE_ID, %1$d, %2$d, %3$d, %4$d));\n",
                    tile.getTileTypeId(),
                    tile.getX(),
                    tile.getY(),
                    tile.getRotation()));
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String getPuzzleString(Puzzle puzzle) {
        PuzzleCustom puzzleCustom = puzzle.getCustomData();
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
                    .append(tile.getY()).append(tileElementDelimiter)
                    .append(tile.getRotation()).append(tileDelimiter);
        }
        return EncryptHelper.encode(sb.toString(), 0);
    }

    public static boolean importPuzzleString(String string, boolean isCopy) {
        try {
            String puzzleString = EncryptHelper.decode(string, 0);
            String[] parts = puzzleString.split(sectionDelimiter);

            int puzzleId = PuzzleHelper.getNextCustomPuzzleId();
            Puzzle puzzle = PuzzleHelper.createBasicPuzzleObject(puzzleId);
            PuzzleCustom puzzleCustom = PuzzleHelper.createBasicPuzzleCustomObject(puzzleId);

            puzzleCustom.setName(parts[0] + (isCopy ? " (Copy)":""));
            puzzleCustom.setDescription(parts[1]);
            puzzleCustom.setAuthor(parts[2]);
            puzzleCustom.setOriginalAuthor(isCopy);
            puzzleCustom.save();

            puzzle.setParMoves(Integer.parseInt(parts[3]));
            puzzle.setParTime(Integer.parseInt(parts[4]));
            puzzle.save();

            String[] tiles = parts[5].split(tileDelimiter);
            List<Tile> finishedTiles = new ArrayList<>();
            for (String tileString : tiles) {
                String[] tileData = tileString.split(tileElementDelimiter);
                Tile tile = new Tile();
                tile.setPuzzleId(puzzleId);
                tile.setTileTypeId(Integer.parseInt(tileData[0]));
                tile.setX(Integer.parseInt(tileData[1]));
                tile.setY(Integer.parseInt(tileData[2]));
                tile.setRotation(Integer.parseInt(tileData[3]));
                finishedTiles.add(tile);
            }
            Tile.saveInTx(finishedTiles);

            return true;
        } catch (Exception e) {
            Log.e("DecodeError", e.getMessage());
            return false;
        }
    }
}

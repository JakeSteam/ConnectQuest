package uk.co.jakelee.cityflow.helper;

public class DateHelper {
    public static int MILLISECONDS_IN_SECOND = 1000;

    public static String getPuzzleTimeString(long timeTaken) {
        return String.format("%.2fs", (double)timeTaken/1000);
    }
}

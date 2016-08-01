package uk.co.jakelee.cityflow.helper;

public class StatisticsHelper {
    public static int getPuzzleCriteriaProgress(int actualValue, int targetValue) {
        if (actualValue <= targetValue) {
            return 100;
        }
        return (int) Math.floor(((double)targetValue / (double)actualValue) * 100);
    }
}

package uk.co.jakelee.cityflow.helper;

import java.util.List;

import uk.co.jakelee.cityflow.model.Pack;

public class StatisticsHelper {
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
}

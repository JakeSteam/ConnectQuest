package uk.co.jakelee.cityflow.helper;

import java.util.ArrayList;

import uk.co.jakelee.cityflow.model.Statistic;

public class StatisticHelper {
    public static String getStatisticString(Statistic statistic) {
        ArrayList<Integer> packStats = new ArrayList<>();
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_1);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_2);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_3);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_4);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_5);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_6);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_7);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_8);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_9);
        packStats.add(Constants.STATISTIC_COMPLETE_PACK_10);

        if (packStats.contains(statistic.getStatisticId())) {
            return statistic.getIntValue() == 1 ? "Yes" : "No";
        }

        switch (statistic.getStatisticType()) {
            case typeString:
                return statistic.getStringValue();
            case typeLong:
                return DateHelper.displayTime(statistic.getLongValue(), DateHelper.datetime);
            case typeInt:
                return Integer.toString(statistic.getIntValue());
        }
        return "";
    }

    public enum StatisticType {typeString, typeInt, typeBool, typeLong}
}

package uk.co.jakelee.cityflow.helper;

import uk.co.jakelee.cityflow.model.Statistic;

public class StatisticHelper {
    public enum StatisticType {typeString, typeInt, typeBool, typeLong}

    public static String getStatisticString(Statistic statistic) {
        if (statistic.getStatisticId() == Constants.STATISTIC_COMPLETE_PACK_1 ||
                statistic.getStatisticId() == Constants.STATISTIC_COMPLETE_PACK_2 ||
                statistic.getStatisticId() == Constants.STATISTIC_COMPLETE_PACK_3) {
            return statistic.getIntValue() == 1 ? "True" : "False";
        }

        switch(statistic.getStatisticType()) {
            case typeString:
                return statistic.getStringValue();
            case typeLong:
                return DateHelper.displayTime(statistic.getLongValue(), DateHelper.datetime);
            case typeInt:
                return Integer.toString(statistic.getIntValue());
        }
        return "";
    }
}

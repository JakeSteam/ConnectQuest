package uk.co.jakelee.cityflow.helper;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static final String date = "yyyy/MM/dd";
    public static final String time = "HH:mm";
    public static final String datetime = date + " " + time;

    public static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;

    public static String displayTime(Long timestamp, String timeFormat) {
        if (timestamp == Constants.PUZZLE_DEFAULT_TIME || timestamp == 0L) {
            return "N/A";
        }
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }

    public static String getHoursMinsRemaining(Long timestamp) {
        int minutes = (int) ((timestamp / (1000 * 60)) % 60);
        int hours = (int) ((timestamp / (1000 * 60 * 60)) % 24);

        return hours + " hr " + minutes + " min";
    }

    public static String getMinsSecsRemaining(Long timestamp) {
        int seconds = (int) (timestamp / 1000) % 60;
        int minutes = (int) ((timestamp / (1000 * 60)) % 60);

        return minutes + " min " + seconds + " s";
    }

    public static String getHoursMinsSecsRemaining(Long timestamp) {
        int seconds = (int) (timestamp / 1000) % 60;
        int minutes = (int) ((timestamp / (1000 * 60)) % 60);
        int hours = (int) ((timestamp / (1000 * 60 * 60)) % 24);

        return hours + " hr " + minutes + " min " + seconds + " s";
    }

    public static int minutesToMilliseconds(int minutes) {
        return minutes * (MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE);
    }

    public static int hoursToMilliseconds(int hours) {
        return hours * minutesToMilliseconds(MINUTES_IN_HOUR);
    }

    public static int getSecondsRoundUp(long milliseconds) {
        double secondsLeft = (double) milliseconds / MILLISECONDS_IN_SECOND;
        return (int) Math.ceil(secondsLeft);
    }

    public static int getMinutesInMilliseconds(long milliseconds) {
        return (int) (milliseconds / (1000 * 60));
    }

    public static String getPuzzleTimeString(long timeTaken) {
        if (timeTaken == 0 || timeTaken == Constants.PUZZLE_DEFAULT_TIME) {
            return "N/A";
        }
        return String.format(Locale.ENGLISH, "%.2fs", (double) timeTaken / 1000);
    }

    public static String getInGameTimeString(long timeTaken) {
        if (timeTaken == 0 || timeTaken == Constants.PUZZLE_DEFAULT_TIME) {
            return "N/A";
        }
        return String.format(Locale.ENGLISH, "%.1fs", (double) timeTaken / 1000);
    }
}

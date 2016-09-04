package uk.co.jakelee.cityflow.helper;

import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static TranslateAnimation moveSouthEast(int yOffset, int seconds) {
        TranslateAnimation southEast = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, -100,
                TranslateAnimation.ABSOLUTE, 1300,
                TranslateAnimation.ABSOLUTE, -100 + yOffset,
                TranslateAnimation.ABSOLUTE, 650 + yOffset);
        southEast.setDuration(seconds * DateHelper.MILLISECONDS_IN_SECOND);
        southEast.setFillAfter(false);
        southEast.setInterpolator(new LinearInterpolator());
        southEast.setRepeatCount(Animation.INFINITE);
        return southEast;
    }

    public static TranslateAnimation moveNorthEast(DisplayMetrics metrics, int duration) {
        int yOffset = RandomHelper.getNumber(100, metrics.heightPixels);
        return moveNorthEast(metrics, duration, 0, yOffset);
    }

    public static TranslateAnimation moveNorthEast(DisplayMetrics metrics, int duration, int xOffset, int yOffset) {
        float ratio = ((float)Constants.TILE_HEIGHT) / ((float)Constants.TILE_WIDTH);

        TranslateAnimation southEast = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, xOffset,
                TranslateAnimation.ABSOLUTE, xOffset + metrics.widthPixels,
                TranslateAnimation.ABSOLUTE, yOffset,
                TranslateAnimation.ABSOLUTE, yOffset - (metrics.widthPixels * ratio));
        southEast.setDuration(duration);
        southEast.setFillAfter(false);
        southEast.setInterpolator(new LinearInterpolator());
        southEast.setRepeatCount(Animation.INFINITE);
        return southEast;
    }
}

package uk.co.jakelee.cityflow.helper;

import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static TranslateAnimation move(DisplayMetrics metrics, int rotation, int duration) {
        if (rotation == Constants.ROTATION_NORTH || rotation == Constants.ROTATION_WEST) {
            int yOffset = RandomHelper.getNumber(100, metrics.heightPixels);
            return move(metrics, rotation, duration, 0, yOffset);
        } else {
            int yOffset = RandomHelper.getNumber(0, metrics.heightPixels - 100);
            return move(metrics, rotation, duration, 0, yOffset);
        }
    }

    public static TranslateAnimation move(DisplayMetrics metrics, int rotation, int duration, int xOffset, int yOffset) {
        float ratio = ((float)Constants.TILE_HEIGHT) / ((float)Constants.TILE_WIDTH);
        boolean goingRight = rotation == Constants.ROTATION_NORTH || rotation == Constants.ROTATION_EAST;
        boolean goingDown = rotation == Constants.ROTATION_SOUTH || rotation == Constants.ROTATION_EAST;

        TranslateAnimation northEast = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, -20 + (goingRight ? xOffset : xOffset + metrics.widthPixels),
                TranslateAnimation.ABSOLUTE, 20 + (!goingRight ? xOffset : xOffset + metrics.widthPixels),
                TranslateAnimation.ABSOLUTE, -20 + (!goingDown ? yOffset : yOffset - (metrics.widthPixels * ratio)),
                TranslateAnimation.ABSOLUTE, 20 + (goingDown ? yOffset : yOffset - (metrics.widthPixels * ratio)));
        northEast.setDuration(duration);
        northEast.setFillAfter(false);
        northEast.setInterpolator(new LinearInterpolator());
        northEast.setRepeatCount(Animation.INFINITE);
        return northEast;
    }
}

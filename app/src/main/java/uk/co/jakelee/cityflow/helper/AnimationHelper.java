package uk.co.jakelee.cityflow.helper;

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

    public static TranslateAnimation moveNorthEast(int yOffset, int seconds) {
        TranslateAnimation southEast = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, -100,
                TranslateAnimation.ABSOLUTE, 1300,
                TranslateAnimation.ABSOLUTE, 650 + yOffset,
                TranslateAnimation.ABSOLUTE, -100 + yOffset);
        southEast.setDuration(seconds * DateHelper.MILLISECONDS_IN_SECOND);
        southEast.setFillAfter(false);
        southEast.setInterpolator(new LinearInterpolator());
        southEast.setRepeatCount(Animation.INFINITE);
        return southEast;
    }
}

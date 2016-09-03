package uk.co.jakelee.cityflow.helper;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static TranslateAnimation topLeftToBottomRight() {
        TranslateAnimation topLeftToBottomRight = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, -0.1f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.1f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.1f);
        topLeftToBottomRight.setDuration(24000);
        topLeftToBottomRight.setFillAfter(false);
        topLeftToBottomRight.setInterpolator(new LinearInterpolator());
        topLeftToBottomRight.setRepeatCount(Animation.INFINITE);
        return topLeftToBottomRight;
    }

    public static TranslateAnimation midLeftToBottomMiddle() {
        TranslateAnimation topLeftToBottomRight = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT, 0.6f,
                TranslateAnimation.RELATIVE_TO_PARENT, 1.1f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.1f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.6f);
        topLeftToBottomRight.setDuration(12000);
        topLeftToBottomRight.setFillAfter(false);
        topLeftToBottomRight.setInterpolator(new LinearInterpolator());
        topLeftToBottomRight.setRepeatCount(Animation.INFINITE);
        return topLeftToBottomRight;
    }
}

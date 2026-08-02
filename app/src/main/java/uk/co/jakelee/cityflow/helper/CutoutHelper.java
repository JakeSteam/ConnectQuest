package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;

/**
 * Targeting 35+ pins the cutout mode to "always" and ignores the theme override, so without this a
 * hole-punch camera sits on top of whatever is drawn at the very top. There is no shared base
 * activity to hook, so this registers against the Application and covers every activity at once.
 */
public class CutoutHelper {

    public static void register(Application application) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return;
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                applyCutoutInsets(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    // Runs after the activity's onCreate, so setContentView has already happened. No effect on a
    // device without a cutout, where getDisplayCutout() is null.
    private static void applyCutoutInsets(Activity activity) {
        final View content = activity.findViewById(android.R.id.content);
        if (content == null) {
            return;
        }

        content.setOnApplyWindowInsetsListener((v, insets) -> {
            DisplayCutout cutout = insets.getDisplayCutout();
            if (cutout != null) {
                v.setPadding(cutout.getSafeInsetLeft(), cutout.getSafeInsetTop(),
                        cutout.getSafeInsetRight(), cutout.getSafeInsetBottom());
            }
            return insets;
        });
        content.requestApplyInsets();
    }
}

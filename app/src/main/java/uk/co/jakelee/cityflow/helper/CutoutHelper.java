package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;

/**
 * Targeting 35+ forces edge to edge and pins the window's cutout mode to "always", which a theme
 * cannot override. A hole-punch camera therefore sits on top of anything drawn at the very top of
 * the screen: titles, top controls, and messages.
 *
 * The game has no shared base activity - seventeen activities extend either Activity or
 * AllowMeActivity - so rather than reparent them all, this registers once against the Application
 * and insets every activity's content view as it is created.
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

    // Runs after the activity's own onCreate, so setContentView has already happened and the
    // content view exists. Does nothing on a device without a cutout, where getDisplayCutout()
    // returns null.
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

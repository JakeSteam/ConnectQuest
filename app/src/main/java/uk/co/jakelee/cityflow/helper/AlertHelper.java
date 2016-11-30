package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.model.Text;

public class AlertHelper {
    private static final Configuration.Builder configBuilder =  new Configuration.Builder()
            .setInAnimation(R.anim.bottom_up)
            .setOutAnimation(R.anim.bottom_down);
    private static final Configuration configNormal = configBuilder
            .build();
    private static final Configuration configInfinite = configBuilder
            .setDuration(Configuration.DURATION_INFINITE)
            .build();

    public static void success(Activity activity, String text) {
        display(activity, text, R.layout.custom_crouton_success, false);
    }

    public static void info(Activity activity, String text) {
        display(activity, text, R.layout.custom_crouton_info, false);
    }

    public static void error(Activity activity, String text, boolean infinite) {
        display(activity, text, R.layout.custom_crouton_error, infinite);
    }

    public static void error(Activity activity, String text) {
        error(activity, text, false);
    }

    private static void display(Activity activity, String text, int layoutId, boolean infinite) {
        Crouton.cancelAllCroutons();
        View layout = activity.getLayoutInflater().inflate(layoutId, null);
        ((TextView)layout.findViewById(R.id.text)).setText(text);
        Crouton.make(activity, layout, (ViewGroup)activity.findViewById(R.id.croutonview)).setConfiguration(infinite ? configInfinite : configNormal).show();
    }

    public static String getError(Error error) {
        return Text.get("ERROR_", error.toString());
    }

    public enum Error {
        NO_ERROR, ADVERT_NOT_LOADED, ADVERT_NOT_VERIFIED, FAILED_TO_CONNECT, SUPPORT_CODE_INVALID,
        NOT_ENOUGH_CURRENCY, MAX_PURCHASES, TECHNICAL, CLOUD_ERROR, PUZZLE_NOT_TESTED,
        PUZZLE_TOO_SMALL, CARD_NOT_SAVED, CAMERA_IMPORT_FAIL, FILE_IMPORT_FAIL, BACKGROUND_LOCKED,
        NO_IAB, IAB_FAILED, TEXT_IMPORT_FAIL, SUPPORT_CODE_USED, PUZZLE_SOLVED
    }
}

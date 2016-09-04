package uk.co.jakelee.cityflow.helper;

import de.keyboardsurfer.android.widget.crouton.Style;

public class StyleHelper {
    public static final Style ERROR;
    public static final Style SUCCESS;
    public static final Style INFO;

    static {
        ERROR = new Style.Builder()
                .setBackgroundColorValue(0xffff4444)
                .setPaddingInPixels(20)
                .build();
        SUCCESS = new Style.Builder()
                .setBackgroundColorValue(0xff99cc00)
                .setPaddingInPixels(20)
                .build();
        INFO = new Style.Builder()
                .setBackgroundColorValue(0xff33b5e5)
                .setPaddingInPixels(20)
                .build();
    }
}

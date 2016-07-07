package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewFont extends TextView {
    private static Typeface mTypeface;

    public TextViewFont(final Context context) {
        this(context, null);
    }

    public TextViewFont(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewFont(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/direction.ttf");
        }
        setTypeface(mTypeface);
    }
}

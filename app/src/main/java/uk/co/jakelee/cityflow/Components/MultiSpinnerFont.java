package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.thomashaertel.widget.MultiSpinner;

public class MultiSpinnerFont extends MultiSpinner {
    private static Typeface mTypeface;

    public MultiSpinnerFont(final Context context) {
        this(context, null);
    }

    public MultiSpinnerFont(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSpinnerFont(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "YagiUhfNo2.otf");
        }
        setTypeface(mTypeface);
    }
}

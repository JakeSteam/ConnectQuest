package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import me.grantland.widget.AutofitTextView;

public class AutofitTextViewFont extends AutofitTextView {
    private static Typeface mTypeface;

    public AutofitTextViewFont(final Context context) {
        this(context, null);
    }

    public AutofitTextViewFont(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutofitTextViewFont(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "YagiUhfNo2.otf");
        }
        setTypeface(mTypeface);
    }
}

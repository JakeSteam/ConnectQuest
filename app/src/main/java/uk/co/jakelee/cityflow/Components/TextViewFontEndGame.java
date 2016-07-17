package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewFontEndGame extends TextViewFont {
    private static Typeface mTypeface;

    public TextViewFontEndGame(final Context context) {
        this(context, null);
    }

    public TextViewFontEndGame(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewFontEndGame(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        setTextColor(Color.WHITE);
        setTextSize(20);
    }
}

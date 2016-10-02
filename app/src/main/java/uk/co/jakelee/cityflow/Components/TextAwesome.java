package uk.co.jakelee.cityflow.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextAwesome extends TextView {
	private static Typeface mTypeface;

	public TextAwesome(final Context context) {
		this(context, null);
	}

	public TextAwesome(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextAwesome(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);

		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
		}
		setTypeface(mTypeface);
	}

}



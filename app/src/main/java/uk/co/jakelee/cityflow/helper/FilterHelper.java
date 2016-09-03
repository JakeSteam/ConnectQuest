package uk.co.jakelee.cityflow.helper;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterHelper {
    private final static String allowedCharacters = "[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!&*(),.?:;+=_ \"%^'-]*";

    public static InputFilter getFilter(final int maxLength) {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dest.length() >= maxLength) {
                    return "";
                }
                for (int i = start; i < end; i++) {
                    String checkMe = String.valueOf(source.charAt(i));

                    Pattern pattern = Pattern.compile(allowedCharacters);
                    Matcher matcher = pattern.matcher(checkMe);
                    boolean valid = matcher.matches();
                    if (!valid) {
                        return "";
                    }
                }
                return null;
            }
        };
    }
}

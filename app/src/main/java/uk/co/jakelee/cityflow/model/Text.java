package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Locale;

import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.main.MainActivity;

public class Text extends SugarRecord{
    private int language;
    private String textId;
    private String text;

    public Text() {
    }

    public Text(int language, String textId, String text) {
        this.language = language;
        this.textId = textId;
        this.text = text;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static String get(String type, int value) {
        return get(type + value);
    }

    public static String get(String type, String value) {
        return get(type + value);
    }

    public static String get(String type, int value, String parameter) {
        return get(type + value + parameter);
    }

    public static String get(String type, String value, String parameter) {
        return get(type + value + parameter);
    }

    public static String get(String textId) {
        // Return selected language, fallback to english, fallback to text ID
        int languageId = Constants.LANGUAGE_EN;
        if (MainActivity.prefs != null) {
            languageId = MainActivity.prefs.getInt("language", Constants.LANGUAGE_EN);
        }

        Text text = Select.from(Text.class).where(
                Condition.prop("text_id").eq(textId),
                Condition.prop("language").eq(languageId)).first();

        if (text != null) {
            return text.getText();
        }

        text = Select.from(Text.class).where(
                Condition.prop("text_id").eq(textId),
                Condition.prop("language").eq(Constants.LANGUAGE_EN)).first();

        return text != null ? text.getText() : textId;
    }

    public static int getDefaultLanguage() {
        switch(Locale.getDefault().getLanguage()) {
            case "de": return Constants.LANGUAGE_DE;
            case "fr": return Constants.LANGUAGE_FR;
            case "pl": return Constants.LANGUAGE_PL;
            case "ru": return Constants.LANGUAGE_RU;
            case "es": return Constants.LANGUAGE_ES;
            case "nl": return Constants.LANGUAGE_NL;
            case "zh": return Constants.LANGUAGE_ZH;
            default: return Constants.LANGUAGE_EN;
        }
    }

    public static String getLanguageFlag(int languageId) {
        switch(languageId) {
            case Constants.LANGUAGE_EN: return new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7));
            case Constants.LANGUAGE_DE: return new String(Character.toChars(0x1F1E9)) + new String(Character.toChars(0x1F1EA));
            case Constants.LANGUAGE_FR: return new String(Character.toChars(0x1F1EB)) + new String(Character.toChars(0x1F1F7));
            case Constants.LANGUAGE_PL: return new String(Character.toChars(0x1F1F5)) + new String(Character.toChars(0x1F1F1));
            case Constants.LANGUAGE_RU: return new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA));
            case Constants.LANGUAGE_ES: return new String(Character.toChars(0x1F1EA)) + new String(Character.toChars(0x1F1F8));
            case Constants.LANGUAGE_NL: return new String(Character.toChars(0x1F1F3)) + new String(Character.toChars(0x1F1F1));
            case Constants.LANGUAGE_ZH: return new String(Character.toChars(0x1F1E8)) + new String(Character.toChars(0x1F1F3));
            default: return "";
        }
    }
}

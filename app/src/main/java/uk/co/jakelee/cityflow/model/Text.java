package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

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
        int languageId = MainActivity.prefs.getInt("language", Constants.LANGUAGE_EN_GB);
        String defaultText = "Failed to find text... please contact support!";
        Text text = Select.from(Text.class).where(
                Condition.prop("text_id").eq(textId),
                Condition.prop("language").eq(languageId)).first();

        return text != null ? text.getText() : defaultText;
    }
}

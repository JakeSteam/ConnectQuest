package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.TextViewFont;
import uk.co.jakelee.cityflow.helper.AlertHelper;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Background;
import uk.co.jakelee.cityflow.model.Text;

public class CreditsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);

        populateCredits();
        activateLinks();

        Background background = Background.get(Constants.BACKGROUND_SUMMER);
        if (!background.isUnlocked()) {
            background.unlock();
            AlertHelper.success(this, String.format(Locale.ENGLISH, Text.get("ALERT_BACKGROUND_UNLOCK"), background.getName()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateCredits() {
        ((TextView)findViewById(R.id.graphicsTitle)).setText(Text.get("CREDITS_GRAPHICS_TITLE"));
        ((TextView)findViewById(R.id.graphicsText)).setText(Text.get("CREDITS_GRAPHICS_TEXT"));

        ((TextView)findViewById(R.id.localisationTitle)).setText(Text.get("CREDITS_LOCAL_TITLE"));
        ((TextView)findViewById(R.id.localisationText)).setText(Text.get("CREDITS_LOCAL_TEXT"));

        ((TextView)findViewById(R.id.technologiesTitle)).setText(Text.get("CREDITS_TECHNOLOGIES_TITLE"));
        ((TextView)findViewById(R.id.technologiesText)).setText(Text.get("CREDITS_TECHNOLOGIES_TEXT"));

        ((TextView)findViewById(R.id.soundsTitle)).setText(Text.get("CREDITS_SOUNDS_TITLE"));
        ((TextView)findViewById(R.id.soundsText)).setText(Text.get("CREDITS_SOUNDS_TEXT"));

        ((TextView)findViewById(R.id.otherTitle)).setText(Text.get("CREDITS_OTHER_TITLE"));
        ((TextView)findViewById(R.id.otherText)).setText(Text.get("CREDITS_OTHER_TEXT"));
    }

    private void activateLinks() {
        LinearLayout root = (LinearLayout) findViewById(R.id.creditsContainer);
        for (int i = 0; i < root.getChildCount(); i++) {
            TextViewFont textViewFont = (TextViewFont)root.getChildAt(i);
            textViewFont.setText(Html.fromHtml(textViewFont.getText().toString()));
            textViewFont.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}

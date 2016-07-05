package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Chapter;

public class StoryActivity extends Activity {
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        dh = DisplayHelper.getInstance(this);

        LinearLayout chapterContainer = (LinearLayout) findViewById(R.id.chapterContainer);
        populateChapters(chapterContainer);
    }

    public void populateChapters(LinearLayout chapterContainer) {
        final Activity activity = this;
        List<Chapter> chapters = Chapter.listAll(Chapter.class);
        for (Chapter chapter : chapters) {
            TextView chapterText = new TextView(this);
            chapterText.setText(chapter.getName() + " - " + chapter.getDescription());
            chapterText.setTextColor(chapter.isUnlocked() ? Color.BLACK : Color.LTGRAY);
            chapterText.setTextSize(24);
            chapterText.setTag(chapter.getChapterId());
            chapterText.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ChapterActivity.class);
                    intent.putExtra(Constants.INTENT_CHAPTER, (int) v.getTag());
                    startActivity(intent);
                }
            });

            chapterContainer.addView(chapterText);
        }
    }
}

package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.Quests;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.GooglePlayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Text;

public class QuestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
        populateText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void populateText() {
        ((TextView)findViewById(R.id.currentQuests)).setText(Text.get("QUEST_CURRENT"));
        ((TextView)findViewById(R.id.availableQuests)).setText(Text.get("QUEST_AVAILABLE"));
        ((TextView)findViewById(R.id.upcomingQuests)).setText(Text.get("QUEST_UPCOMING"));
        ((TextView)findViewById(R.id.completedQuests)).setText(Text.get("QUEST_COMPLETED"));
        ((TextView)findViewById(R.id.failedQuests)).setText(Text.get("QUEST_FAILED"));
        ((TextView)findViewById(R.id.redditSchedule)).setText(Text.get("QUEST_SCHEDULE"));
    }

    public void currentQuests(View view) {
        if (GooglePlayHelper.mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, new int[]{Quests.SELECT_ACCEPTED}), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void availableQuests(View view) {
        if (GooglePlayHelper.mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, new int[]{Quests.SELECT_OPEN}), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void completedQuests(View view) {
        if (GooglePlayHelper.mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, new int[]{Quests.SELECT_COMPLETED}), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void failedQuests(View view) {
        if (GooglePlayHelper.mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, new int[]{Quests.SELECT_FAILED, Quest.STATE_EXPIRED}), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void upcomingQuests(View view) {
        if (GooglePlayHelper.mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Quests.getQuestsIntent(GooglePlayHelper.mGoogleApiClient, new int[]{Quests.SELECT_UPCOMING}), GooglePlayHelper.RC_QUESTS);
        }
    }

    public void redditSchedule(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/CityFlow/comments/5f4va6/quest_list/"));
        startActivity(browserIntent);
    }

    public void closePopup (View v) {
        this.finish();
    }
}

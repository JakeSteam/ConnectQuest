package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.DatabaseHelper;

public class MainActivity extends Activity {
    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);

        DatabaseHelper.handlePatches();
    }

    public void openChapters(View view) {
        Intent intent = new Intent(this, StoryActivity.class);
        startActivity(intent);
    }
}

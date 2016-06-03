package uk.co.jakelee.cityflow.main;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uk.co.jakelee.cityflow.R;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);
    }
}

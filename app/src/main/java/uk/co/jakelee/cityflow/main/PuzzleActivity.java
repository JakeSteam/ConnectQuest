package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.DisplayHelper;

public class PuzzleActivity extends Activity {
    private DisplayHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        dh = DisplayHelper.getInstance(this);

        RelativeLayout tileContainer = (RelativeLayout) findViewById(R.id.tileContainer);
        dh.populateTiles(tileContainer, 2);
    }
}

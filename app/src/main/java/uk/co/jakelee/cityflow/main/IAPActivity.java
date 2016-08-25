package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;

public class IAPActivity extends Activity {
    private String iapCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iap);

        Intent intent = getIntent();
        iapCode = intent.getStringExtra(Constants.INTENT_IAP);
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateIapInfo();
    }

    private void populateIapInfo() {
        ((TextView)findViewById(R.id.iapName)).setText("IAP Name");
        ((TextView)findViewById(R.id.iapDesc)).setText("IAP Desc");
    }

    public void closePopup (View v) {
        this.finish();
    }
}

package uk.co.jakelee.cityflow;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.orm.SugarContext;

import uk.co.jakelee.cityflow.helper.AdvertHelper;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        AdvertHelper.getInstance(this);
        Batch.Push.setGCMSenderId("596538793474");
        Batch.setConfig(new Config("5874F1A1BFB1E3CCEF77E5C7423924"));
        //Batch.setConfig(new Config("DEV5874F1A1BFE9D1F9587EFA528A6"));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}

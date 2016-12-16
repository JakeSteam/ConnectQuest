package uk.co.jakelee.cityflow;

import android.app.Application;

import com.orm.SugarContext;

import uk.co.jakelee.cityflow.helper.AdvertHelper;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        AdvertHelper.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}

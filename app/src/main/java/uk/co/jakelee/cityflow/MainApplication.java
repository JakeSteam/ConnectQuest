package uk.co.jakelee.cityflow;

import android.support.multidex.MultiDexApplication;

import com.orm.SugarContext;

import uk.co.jakelee.cityflow.helper.AdvertHelper;

public class MainApplication extends MultiDexApplication {

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

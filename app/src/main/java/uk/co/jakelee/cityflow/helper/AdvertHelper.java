package uk.co.jakelee.cityflow.helper;

import android.content.Context;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;

import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.main.ShopActivity;
import uk.co.jakelee.cityflow.model.Text;

public class AdvertHelper implements AppLovinAdRewardListener, AppLovinAdDisplayListener, AppLovinAdVideoPlaybackListener {
    private Context context;
    public AppLovinIncentivizedInterstitial advert;
    private ShopActivity callingActivity;
    private boolean verified;
    private static AdvertHelper dhInstance = null;

    public AdvertHelper(Context context) {
        this.context = context;

        AppLovinSdk.initializeSdk(context);
        advert = AppLovinIncentivizedInterstitial.create(context);
        advert.preload(null);
    }

    public static AdvertHelper getInstance(Context ctx) {
        if (dhInstance == null) {
            dhInstance = new AdvertHelper(ctx.getApplicationContext());
        }
        return dhInstance;
    }

    public void showAdvert(ShopActivity activity) {
        verified = false;
        callingActivity = activity;

        if (advert.isAdReadyToDisplay()) {
            advert.show(activity, this, this, this);
        } else {
            Crouton.showText(callingActivity, Text.get("ERROR_ADVERT_NOT_LOADED"), StyleHelper.ERROR);
        }
    }

    @Override
    public void adHidden(AppLovinAd appLovinAd) {
        if (verified) {
            callingActivity.advertWatched();
        } else {
            Crouton.showText(callingActivity, Text.get("ERROR_ADVERT_NOT_VERIFIED"), StyleHelper.ERROR);
        }

        advert.preload(null);
    }

    @Override
    public void userRewardVerified(AppLovinAd appLovinAd, Map map) {
        verified = true;
    }

    @Override public void videoPlaybackBegan(AppLovinAd appLovinAd) {}
    @Override public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {}
    @Override public void adDisplayed(AppLovinAd appLovinAd) {}
    @Override public void userOverQuota(AppLovinAd appLovinAd, Map map) {}
    @Override public void userRewardRejected(AppLovinAd appLovinAd, Map map) {}
    @Override public void validationRequestFailed(AppLovinAd appLovinAd, int i) {}
    @Override public void userDeclinedToViewAd(AppLovinAd appLovinAd) {}
}

package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;

import java.util.Locale;
import java.util.Map;

import uk.co.jakelee.cityflow.main.ShopActivity;
import uk.co.jakelee.cityflow.model.Iap;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;

public class AdvertHelper implements AppLovinAdRewardListener, AppLovinAdDisplayListener, AppLovinAdVideoPlaybackListener {
    private static AdvertHelper dhInstance = null;
    public AppLovinIncentivizedInterstitial advert;
    private Context context;
    private ShopActivity callingActivity;
    private boolean verified;

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

    public static boolean synchroniseCoins(Activity activity, int remoteCoins) {
        int coinsEarned = synchroniseCoins(remoteCoins);
        if (coinsEarned > 0) {
            AlertHelper.success(activity, String.format(Locale.ENGLISH, Text.get("ALERT_COINS_EARNED_FREE"), coinsEarned));
            if (coinsEarned == Constants.CURRENCY_ADVERT) {
                GooglePlayHelper.UpdateEvent(Constants.EVENT_WATCH_ADVERT, 1);
                return true;
            }
        }
        return false;
    }

    public static int synchroniseCoins(int remoteCoins) {
        Statistic localCoins = Statistic.find(Constants.STATISTIC_TAPJOY_COINS);
        int difference = remoteCoins - localCoins.getIntValue();

        if (difference > 0) {
            Statistic.addCurrency((Iap.hasCoinDoubler() ? 2 : 1) * difference);
            localCoins.setIntValue(remoteCoins);
            localCoins.save();
            return difference;
        }
        return 0;
    }

    public void showAdvert(ShopActivity activity) {
        verified = false;
        callingActivity = activity;

        if (advert.isAdReadyToDisplay()) {
            advert.show(activity, this, this, this);
        } else {
            AlertHelper.error(callingActivity, AlertHelper.getError(AlertHelper.Error.ADVERT_NOT_LOADED));
        }
    }

    @Override
    public void adHidden(AppLovinAd appLovinAd) {
        if (verified) {
            callingActivity.advertWatched();
        } else {
            AlertHelper.error(callingActivity, AlertHelper.getError(AlertHelper.Error.ADVERT_NOT_VERIFIED));
        }

        advert.preload(null);
    }

    @Override
    public void userRewardVerified(AppLovinAd appLovinAd, Map map) {
        verified = true;
    }

    @Override
    public void videoPlaybackBegan(AppLovinAd appLovinAd) {
    }

    @Override
    public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {
    }

    @Override
    public void adDisplayed(AppLovinAd appLovinAd) {
    }

    @Override
    public void userOverQuota(AppLovinAd appLovinAd, Map map) {
    }

    @Override
    public void userRewardRejected(AppLovinAd appLovinAd, Map map) {
    }

    @Override
    public void validationRequestFailed(AppLovinAd appLovinAd, int i) {
    }

    @Override
    public void userDeclinedToViewAd(AppLovinAd appLovinAd) {
    }
}

package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.main.ShopActivity;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;

public class AdvertHelper implements AppLovinAdRewardListener, AppLovinAdDisplayListener, AppLovinAdVideoPlaybackListener, TJPlacementListener {
    private Context context;
    public AppLovinIncentivizedInterstitial advert;
    private ShopActivity callingActivity;
    private boolean verified;
    private static AdvertHelper dhInstance = null;

    public AdvertHelper(Context context) {
        this.context = context;

        Tapjoy.connect(context, "kgX9Y9-zQuGTCApHM5TuUwECacqwGwpWQshWxLsTNMPC8QU65rl-psQ76RrV", null);
        Tapjoy.setGcmSender("596538793474");

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

    public void showAdvert(ShopActivity activity, TJPlacement adPlacement) {
        verified = false;
        callingActivity = activity;

        if (advert.isAdReadyToDisplay()) {
            advert.show(activity, this, this, this);
        } else if (adPlacement.isContentReady()) {
            adPlacement.showContent();
        } else {
            Crouton.showText(callingActivity, Text.get("ERROR_ADVERT_NOT_LOADED"), StyleHelper.ERROR, (ViewGroup)callingActivity.findViewById(R.id.croutonview));
        }
    }

    @Override
    public void adHidden(AppLovinAd appLovinAd) {
        if (verified) {
            callingActivity.advertWatched();
        } else {
            Crouton.showText(callingActivity, Text.get("ERROR_ADVERT_NOT_VERIFIED"), StyleHelper.ERROR, (ViewGroup)callingActivity.findViewById(R.id.croutonview));
        }

        advert.preload(null);
    }

    public static void synchroniseCoins(Activity activity, int remoteCoins) {
        int coinsEarned = synchroniseCoins(remoteCoins);
        if (coinsEarned > 0) {
            Crouton.showText(activity, String.format(Text.get("ALERT_COINS_EARNED_FREE"), coinsEarned), StyleHelper.SUCCESS, (ViewGroup)activity.findViewById(R.id.croutonview));
        }
    }

    public static int synchroniseCoins(int remoteCoins) {
        Statistic localCoins = Statistic.find(Constants.STATISTIC_TAPJOY_COINS);
        int difference = remoteCoins - localCoins.getIntValue();

        if (difference > 0) {
            Statistic.addCurrency(difference);
            localCoins.setIntValue(remoteCoins);
            localCoins.save();
            return difference;
        }
        return 0;
    }

    public void onContentDismiss(TJPlacement placement) {}
    public void onPurchaseRequest(TJPlacement placement, TJActionRequest tjActionRequest, String string) {} // Called when the SDK has made contact with Tapjoy's servers. It does not necessarily mean that any content is available.
    public void onRewardRequest(TJPlacement placement, TJActionRequest tjActionRequest, String string, int number) {} // Called when the SDK has made contact with Tapjoy's servers. It does not necessarily mean that any content is available.
    public void onRequestSuccess(TJPlacement placement) {} // Called when the SDK has made contact with Tapjoy's servers. It does not necessarily mean that any content is available.
    public void onRequestFailure(TJPlacement placement, TJError error) {} // Called when there was a problem during connecting Tapjoy servers.
    public void onContentReady(TJPlacement placement) {} // Called when the content is actually available to display.
    public void onContentShow(TJPlacement placement) {} // Called when the content is showed.

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

package com.buildbox.adapter.custom;

import android.app.Activity;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.buildbox.AdIntegratorInterface;
import com.buildbox.AdIntegratorManager;
import com.buildbox.AdIntegratorManagerInterface;
import com.buildbox.CustomIntegrator;
import com.buildbox.consent.ConsentHelper;
import com.buildbox.consent.SdkConsentInfo;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig;
//import com.usmartpenguinsio.apptutorialadtest.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class AdIntegrator implements AdIntegratorInterface, CustomIntegrator {


    private static String customNetworkName = "Yodo1 MAS";
    private static String adNetworkId = "custom";
    private static String TAG = "AdIntegratorCustom";
    private static WeakReference<Activity> activity;
    private static AdIntegratorManagerInterface adIntegratorManager;





    private RelativeLayout adContainerView;
    private boolean userConsent;
    private boolean isNewBannerLoaded;

    @Override
    public void initAds(HashMap<String, String> initValues, WeakReference<Activity> act, AdIntegratorManagerInterface managerInterface) {

        activity = act;
        adIntegratorManager = managerInterface;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.get());
        setUserConsent(preferences.getBoolean(ConsentHelper.getConsentKey("custom"),false));



        Yodo1MasAdBuildConfig config = new Yodo1MasAdBuildConfig.Builder().enableUserPrivacyDialog(true).build();
        Yodo1Mas.getInstance().setAdBuildConfig(config);

        Yodo1Mas.getInstance().init(activity.get(), "YourAppkey", new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {

                Log.d(TAG,"Yodo1 MAS : Successful initialization");
                networkLoaded();
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Failed initialization");
                networkFailed();
            }
        });
    }


    @Override
    public void initBanner() {

        FrameLayout frameLayout = activity.get().findViewById(android.R.id.content);
        adContainerView = new RelativeLayout(activity.get());
        frameLayout.addView(adContainerView);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        Log.d(TAG,"Yodo1 MAS : Banner initialization");

        bannerLoaded();

        Yodo1Mas.getInstance().setBannerListener(new Yodo1Mas.BannerListener() {
            @Override
            public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Banner opened");
            }

            @Override
            public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Banner failed");
                bannerFailed();
            }

            @Override
            public void onAdClosed(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Banner closed");
            }
        });

    }

    @Override
    public void showBanner() {

        Log.d(TAG,"Yodo1 MAS : Banner showed");

        Yodo1Mas.getInstance().showBannerAd(activity.get());

    }

    @Override
    public void hideBanner() {

        Log.d(TAG,"Yodo1 MAS : Banner dismissed");

        Yodo1Mas.getInstance().dismissBannerAd();

    }

    @Override
    public boolean isBannerVisible() {

        return  true;
    }

    @Override
    public boolean isRewardedVideoAvailable() {

        return  Yodo1Mas.getInstance().isRewardedAdLoaded();
    }

    @Override
    public void showInterstitial() {

        boolean isLoaded = Yodo1Mas.getInstance().isInterstitialAdLoaded();
        if(isLoaded) {

            Log.d(TAG,"Yodo1 MAS : Interstitial showed");

            Yodo1Mas.getInstance().showInterstitialAd(activity.get());
        }
        else
            Log.d(TAG,"Yodo1 MAS : Interstitial not loaded");

    }


    @Override
    public void initInterstitial() {

        Log.d(TAG,"Yodo1 MAS : Interstitial initialization");

        interstitialLoaded();

        Yodo1Mas.getInstance().setInterstitialListener(new Yodo1Mas.InterstitialListener() {
            @Override
            public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Interstitial opened");
            }

            @Override
            public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Interstitial failed");
                interstitialFailed();
            }

            @Override
            public void onAdClosed(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Interstitial closed");
                interstitialClosed();
            }
        });


    }

    @Override
    public void initRewardedVideo() {

        Log.d(TAG,"Yodo1 MAS : Rewarded ads initialization");

        rewardedVideoLoaded();

        Yodo1Mas.getInstance().setRewardListener(new Yodo1Mas.RewardListener() {
            @Override
            public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads opened");
            }

            @Override
            public void onAdvertRewardEarned(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads completed");
                rewardedVideoDidReward(true);
            }

            @Override
            public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads failed");
                rewardedVideoFailed();
            }

            @Override
            public void onAdClosed(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads closed");
                rewardedVideoDidEnd(true);
            }
        });

    }

    @Override
    public void setUserConsent(boolean consentGiven) {
        userConsent = consentGiven;

    }

    @Override
    public void setTargetsChildren(boolean b) {

    }


    @Override
    public void showRewardedVideo() {

        boolean isLoaded = Yodo1Mas.getInstance().isRewardedAdLoaded();
        if(isLoaded) {
            Log.d(TAG,"Yodo1 MAS : Rewarded ads showed");
            Yodo1Mas.getInstance().showRewardedAd(activity.get());
        }
        else
            Log.d(TAG,"Yodo1 MAS : Rewarded ads not loaded");
    }

    /**
     * Call this method when a user closes an interstitial
     */
    @Override
    public void interstitialClosed() {
        Log.d(TAG, "interstitial closed");
        adIntegratorManager.interstitialClosed(adNetworkId);
    }

    /**
     *  Call this method passing true if a rewarded video reward is successfully received.
     *  Call this method passing false if a rewarded video
     * @param value - was a reward received
     */
    @Override
    public void rewardedVideoDidReward(boolean value) {
        Log.d(TAG, "rewarded video did reward " + value);
        adIntegratorManager.rewardedVideoDidReward(adNetworkId, value);
    }

    /**
     * Call this method passing true if a rewarded video completes without failure
     * Call this method passing false if a rewarded video fails to show
     * @param value - did the video complete without failure
     */
    @Override
    public void rewardedVideoDidEnd(boolean value) {
        Log.d(TAG, "rewarded video did end " + value);
        adIntegratorManager.rewardedVideoDidEnd(adNetworkId, value);
    }

    /**
     * Call this method when the network is initialized
     */
    @Override
    public void networkLoaded() {
        Log.d(TAG, "Network loaded");
        adIntegratorManager.sdkLoaded(adNetworkId);
    }

    /**
     * Call this method when a banner is successfully loaded
     */
    @Override
    public void bannerLoaded() {
        Log.d(TAG, "Banner Loaded");
        adIntegratorManager.bannerLoaded(adNetworkId);
    }

    /**
     * Call this method when an interstitial is successfully loaded
     */
    @Override
    public void interstitialLoaded() {
        Log.d(TAG, "Interstitial Loaded");
        adIntegratorManager.interstitialLoaded(adNetworkId);
    }

    /**
     * Call this method when a rewarded video is successfully loaded
     */
    @Override
    public void rewardedVideoLoaded() {
        Log.d(TAG, "Rewarded Loaded");
        adIntegratorManager.rewardedVideoLoaded(adNetworkId);
    }

    /**
     * Call this method when if a network fails
     */
    @Override
    public void networkFailed() {
        Log.d(TAG, "network failed");
        adIntegratorManager.sdkFailed(adNetworkId);
    }

    /**
     * Call this method when a banner fails for any reason
     */
    @Override
    public void bannerFailed() {
        Log.d(TAG, "banner failed");
        adIntegratorManager.bannerFailed(adNetworkId);
    }

    /**
     * Call this method when an interstitial fails for any reason
     */
    @Override
    public void interstitialFailed() {
        Log.d(TAG, "interstitial failed");
        adIntegratorManager.interstitialFailed(adNetworkId);
    }

    /**
     * Call this method when a rewarded video fails for any reason
     */
    @Override
    public void rewardedVideoFailed() {
        Log.d(TAG, "rewarded video failed");
        adIntegratorManager.rewardedVideoFailed(adNetworkId);
    }

    @Override
    public void onActivityCreated(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Use this method for handling activity lifecycle if the network requires it
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void loadingDidComplete() {

    }

    @Override
    public void screenOnEnter(String screenName) {

    }

    @Override
    public void screenOnExit(String screenName) {

    }

    @Override
    public void sceneOnEnter(String sceneName) {

    }

    @Override
    public void sceneOnExit(String sceneName) {

    }

    @Override
    public void buttonActivated(String buttonName) {

    }

    @Override
    public boolean buttonVisible(String buttonName) {
        return false;
    }
}

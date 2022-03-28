package com.buildbox;

import java.lang.ref.WeakReference;
import android.app.Activity;

import androidx.annotation.NonNull;
import android.util.Log;

import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.event.Yodo1MasAdEvent;
import com.yodo1.mas.helper.model.Yodo1MasAdBuildConfig;

public class AdIntegrator {

    private static String TAG = "AdIntegratorCustom";

    public static native boolean rewardedVideoDidEnd();

    private static WeakReference<Activity> activity;

    public static void initBridge(Activity act){
        activity = new WeakReference<>(act);
    }

    public static void initAds(){

        Yodo1MasAdBuildConfig config = new Yodo1MasAdBuildConfig.Builder().enableUserPrivacyDialog(true).build();
        Yodo1Mas.getInstance().setAdBuildConfig(config);
        
        Yodo1Mas.getInstance().init(activity.get(), "YourAppkey", new Yodo1Mas.InitListener() {
            @Override
            public void onMasInitSuccessful() {

                Log.d(TAG,"Yodo1 MAS : Successful initialization");
            }

            @Override
            public void onMasInitFailed(@NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Failed initialization");
            }
        });

        initInterstitial();
        initRewardedVideo();
    }

    public static void initInterstitial() {

        Log.d(TAG,"Yodo1 MAS : Interstitial initialization");

        Yodo1Mas.getInstance().setInterstitialListener(new Yodo1Mas.InterstitialListener() {
            @Override
            public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Interstitial opened");
            }

            @Override
            public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Interstitial failed");
            }

            @Override
            public void onAdClosed(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Interstitial closed");
            }
        });

    }

    public static void initRewardedVideo() {

        Log.d(TAG,"Yodo1 MAS : Rewarded ads initialization");

        Yodo1Mas.getInstance().setRewardListener(new Yodo1Mas.RewardListener() {
            @Override
            public void onAdOpened(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads opened");
            }

            @Override
            public void onAdvertRewardEarned(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads completed");

                rewardedVideoDidEnd();
            }

            @Override
            public void onAdError(@NonNull Yodo1MasAdEvent event, @NonNull Yodo1MasError error) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads failed");
            }

            @Override
            public void onAdClosed(@NonNull Yodo1MasAdEvent event) {

                Log.d(TAG,"Yodo1 MAS : Rewarded ads closed");

            }
        });

    }

    public static void showBanner(){

        Log.d(TAG,"Yodo1 MAS : Banner showed");

        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                Yodo1Mas.getInstance().showBannerAd(activity.get());
            }
        });


    }

    public static void hideBanner(){

        Log.d(TAG,"Yodo1 MAS : Banner dismissed");

        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                Yodo1Mas.getInstance().dismissBannerAd();
            }
        });
    }

    public static boolean isBannerVisible(){
        return true;
    }

    public static boolean isRewardedVideoAvialable(){
        return true;
    }

    public static void showInterstitial(){

        boolean isLoaded = Yodo1Mas.getInstance().isInterstitialAdLoaded();
        if(isLoaded) {

            Log.d(TAG,"Yodo1 MAS : Interstitial showed");

            Yodo1Mas.getInstance().showInterstitialAd(activity.get());
        }
        else
            Log.d(TAG,"Yodo1 MAS : Interstitial not loaded");
    }

    public static void showRewardedVideo(){

        boolean isLoaded = Yodo1Mas.getInstance().isRewardedAdLoaded();
        if(isLoaded) {
            Log.d(TAG,"Yodo1 MAS : Rewarded ads showed");
            Yodo1Mas.getInstance().showRewardedAd(activity.get());
        }
        else
            Log.d(TAG,"Yodo1 MAS : Rewarded ads not loaded");

    }

    public static void buttonActivated(){

    }

    public static boolean buttonVisible(){
        return true;
    }
}

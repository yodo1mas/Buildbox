// 
// CustomAdIntegrator.mm
// BBPlayer
// 
// Edit this file to integrate a 3rd party ad network.
// 
// Update the methods in the "Initialization" section.
// Update the methods in the "Banner", "Interstitial", and "RewardedVideo" sections if supported in your ad network.
// Call the methods in the "AdCallbacks" section when appropriate.
// Leave any lines that begin with "[super" untouched.
// 
// NOTE: This class is ignored in the free version of Buildbox.
// 

#import <UIKit/UIKit.h>
#import "CustomAdIntegrator.h"
#import "CustomAdIntegrator.h"
#import <Yodo1Mas.h>

@interface CustomAdIntegrator ()<Yodo1MasRewardAdDelegate, Yodo1MasInterstitialAdDelegate, Yodo1MasBannerAdDelegate>

@end

// TODO - Set to implemented ad network name here
#define CustomAdNetworkName @""

@implementation CustomAdIntegrator

- (id)initWithListener:(id<AdSdkListener>)listener {
    if (self = [super initWithListener:listener]) {
        _sdkId = @"custom";
        _logTag = CustomAdNetworkName;
    }
    
    return self;
}

/** 
 * Complete the following stubbed methods. They will be called automatically by the Buildbox player.
 */
#pragma mark Initialization

/**
 * Pass the consent result to your ad network. This method will be called when the user has decided to change their acceptance of a privacy policy.
 */
- (void)setUserConsent:(BOOL)consentGiven {
    [super setUserConsent:consentGiven];
}

/**
 * Initialize your ad network. Do not start loading ads here. This method should either call "sdkLoaded" or "sdkFailed" on completion.
 */
- (void)initSdkWithValues:(NSDictionary<NSString*, NSString*>*)keyValuePairs withViewController:(UIViewController*)viewController {
    [super initSdkWithValues:keyValuePairs withViewController:viewController];

    [self log:@"Custom ad integrator needs to be implemented!"];
    
    [[Yodo1Mas sharedInstance] initWithAppKey:@"YourAppKey" successful:^{
            printf("[Yodo1 Mas] Successful initialization");
            [self sdkLoaded];

        } fail:^(NSError * _Nonnull error) {
            printf("[Yodo1 Mas] Failed initialization");
            [self sdkFailed];
        }];
    
    
}

/** 
 * Update these methods to implement banners with your ad network.
 */
#pragma mark Banner

/**
 * Load a banner.
 * If banner is one time use, then initialize a new one before loading.
 * Make sure the banner view starts as hidden (e.g. [_bannerView setHidden:YES]).
 * This method should either call "bannerLoaded" or "bannerFailed" on completion (e.g. [self bannerLoaded]).
 */
- (void)initBanner {
    [super initBanner];
            
    [self bannerLoaded];
    
    printf("[Yodo1 Mas] Banner loaded");

}

/**
 * Show a banner.
 */
- (void)showBanner {
    [super showBanner];
    
    Yodo1MasAdBannerAlign align = Yodo1MasAdBannerAlignTop;
    
    [[Yodo1Mas sharedInstance] showBannerAdWithAlign:align];

    
    printf("[Yodo1 Mas] Banner displayed");

}

/**
 * Hide the banner.
 */
- (void)hideBanner {
    [super hideBanner];
    
    [[Yodo1Mas sharedInstance] dismissBannerAd];
    
    printf("[Yodo1 Mas] Banner hidden");

}

/**
 * @return true if the banner is currently visible (e.g. return [_bannerView isHidden]).
 */
- (BOOL)isBannerVisible {
    return true;
}

/** 
 * Update these methods to implement interstitials with your ad network.
 */
#pragma mark Interstitial

/**
 * Load an interstitial ad.
 * This method should either call "interstitialLoaded" or "interstitialFailed" on completion (e.g. [self interstitialLoaded]).
 */
- (void)initInterstitial {
    [super initInterstitial];
    
   // [Yodo1Mas sharedInstance].interstitialAdDelegate = self;
    
    [self interstitialLoaded];

}

/**
 * Show an interstitial.
 */
- (void)showInterstitial {
    [super showInterstitial];
    
    [[Yodo1Mas sharedInstance] showInterstitialAd];
    
    printf("[Yodo1 Mas] Interstitial displayed");

}

/** 
 * Update these methods to implement rewarded videos with your ad network.
 */
#pragma mark RewardedVideo

/**
 * Load a rewarded video.
 * This method should either call "rewardedVideoLoaded" or "rewardedVideoFailed" on completion (e.g. [self rewardedVideoLoaded]).
 */
- (void)initRewardedVideo {
    [super initRewardedVideo];
    
    [self rewardedVideoLoaded];


}

/**
 * Show a rewarded video.
 */
- (void)showRewardedVideo {
    [super showRewardedVideo];
    
    [[Yodo1Mas sharedInstance] showRewardAd];
    [self rewardedVideoDidReward];
    printf("[Yodo1 Mas] Rewarded displayed");

}

/**
 * @return true if a rewarded video is loaded and ready to show.
 */
- (BOOL)isRewardedVideoAvailable {
    return [[Yodo1Mas sharedInstance] isRewardAdLoaded];
}


#pragma mark - Yodo1MasRewardAdDelegate
- (void)onAdRewardEarned:(Yodo1MasAdEvent *)event {
    [self rewardedVideoDidReward];
    printf("[Yodo1 Mas] Reward received");
}

#pragma mark - Yodo1MasAdDelegate
- (void)onAdOpened:(Yodo1MasAdEvent *)event {
    printf("[Yodo1 Mas] Ad opened");
}

- (void)onAdClosed:(Yodo1MasAdEvent *)event {
    printf("[Yodo1 Mas] Ad Closed");
}

- (void)onAdError:(Yodo1MasAdEvent *)event error:(Yodo1MasError *)error {
    printf("[Yodo1 Mas] Ad Error");
}



/**
 * The following methods are used to report ad network events to Buildbox.
 * Call these methods when appropriate (e.g. call interstitialLoaded when an interstitial is ready to be played).
 */
#pragma mark AdCallbacks

/**
 * Call this when you are done configuring the ad network (e.g. at the end of initSdkWithValues).
 */
- (void)sdkLoaded {
    [self.listener sdkLoaded:self.sdkId];
}

/**
 * Call this if you are unable to configure the ad network.
 * Buildbox will stop trying to run methods in the integrator after you call this.
 */
- (void)sdkFailed {
    [self.listener sdkFailed:self.sdkId];
}

/**
 * Call this when the ad network reports that it is ready to display a banner ad.
 */
- (void)bannerLoaded {
    [self.listener bannerLoaded:self.sdkId];
}

/**
 * Call this if a banner ad failed to load or be displayed.
 */
- (void)bannerFailed {
    [self.listener bannerFailed:self.sdkId];
}

/**
 * Call this when the ad network reports an impression for a banner ad.
 */
- (void)bannerImpression {
    [self.listener bannerImpression:[NSString stringWithFormat:@"%@ - %@", _sdkId, CustomAdNetworkName]];
}

/**
 * Call this when the ad network reports that it is ready to display an interstitial ad.
 */
- (void)interstitialLoaded {
    [self.listener interstitialLoaded:self.sdkId];
}

/**
 * Call this if an interstitial ad failed to load or be displayed.
 */
- (void)interstitialFailed {
    [self.listener interstitialFailed:self.sdkId];
}

/**
 * Call this when the ad network reports an impression for an interstitial ad.
 */
- (void)interstitialImpression {
    [self.listener interstitialImpression:[NSString stringWithFormat:@"%@ - %@", _sdkId, CustomAdNetworkName]];
}

/**
 * Call this after the interstitial ad was closed.
 */
- (void)interstitialClosed {
    [self.listener interstitialClosed:self.sdkId];
}

/**
 * Call this when the ad network reports that it is ready to display a rewarded video.
 */
- (void)rewardedVideoLoaded {
    [self.listener rewardedVideoLoaded:self.sdkId];
}

/**
 * Call this if a rewarded video failed to load.
 */
- (void)rewardedVideoFailed {
    [self.listener rewardedVideoFailed:self.sdkId];
}

/**
 * Call this when the ad network reports an impression for a rewarded video.
 */
- (void)rewardedVideoImpression {
    [self.listener rewardedVideoImpression:[NSString stringWithFormat:@"%@ - %@", _sdkId, CustomAdNetworkName]];
}

/**
 * Call this if the ad network reported a reward was received (i.e. the user watched the entire rewarded video).
 */
- (void)rewardedVideoDidReward {
    [self.listener rewardedVideoDidReward:self.sdkId withValue:true];
}

/**
 * Call this after the rewarded video was closed.
 */
- (void)rewardedVideoDidEnd {
    [self.listener rewardedVideoDidEnd:self.sdkId withValue:true];
}

/** 
 * These methods are legacy calls for plugging your ad network into the game lifecycle.
 * If unsure, leave this be.
 */
#pragma mark LifecycleCallbacks

- (void)loadingDidComplete {
}

- (void)screenOnEnter:(NSString*)screenName {
}

- (void)screenOnExit:(NSString*)screenName {
}

- (void)sceneOnEnter:(NSString*)sceneName {
}

- (void)sceneOnExit:(NSString*)sceneName {
}

- (void)buttonActivated:(NSString*)buttonName {
}

- (bool)buttonVisible:(NSString*)buttonName; {
    return true;
}

@end

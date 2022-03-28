#import "AdIntegrator.h"
#import <Yodo1Mas.h>


@interface AdIntegrator() <Yodo1MasRewardAdDelegate, Yodo1MasInterstitialAdDelegate, Yodo1MasBannerAdDelegate>

@end

@implementation AdIntegrator

+ (id)shared{
    static AdIntegrator* integrator = nil;
    @synchronized(self){
        if(integrator == nil){
            integrator = [[self alloc] init];
        }
    }
    return integrator;
}

#pragma mark Core Methods

- (void)initAds{
    NSLog(@"[Ads] initialization");
    
    [[Yodo1Mas sharedInstance] initWithAppKey:@"YourAppKey" successful:^{
            printf("[Yodo1 Mas] Successful initialization");

        } fail:^(NSError * _Nonnull error) {
            printf("[Yodo1 Mas] Failed initialization");
        }];
}

-(void)showBanner{
    NSLog(@"[Ads] show banner");
    
    Yodo1MasAdBannerAlign align = Yodo1MasAdBannerAlignTop;
    
    [[Yodo1Mas sharedInstance] showBannerAdWithAlign:align];

    
    printf("[Yodo1 Mas] Banner displayed");
}

-(void)hideBanner{
    NSLog(@"[Ads] hide banner");
    
    [[Yodo1Mas sharedInstance] dismissBannerAd];
    
    printf("[Yodo1 Mas] Banner hidden");
}

-(bool)isBannerVisible{
    return true;
}

-(bool)isRewardedVideoAvialable{
    return [[Yodo1Mas sharedInstance] isRewardAdLoaded];
}

-(void)showInterstitial{
    NSLog(@"[Ads] show interstitial");
    
    [[Yodo1Mas sharedInstance] showInterstitialAd];
    
    printf("[Yodo1 Mas] Interstitial displayed");
}

-(void)showRewardedVideo{
    NSLog(@"[Ads] show rewarded video");
    
    [[Yodo1Mas sharedInstance] showRewardAd];
    printf("[Yodo1 Mas] Rewarded displayed");
}

#pragma mark - Yodo1MasRewardAdDelegate
- (void)onAdRewardEarned:(Yodo1MasAdEvent *)event {
    printf("[Yodo1 Mas] Reward received");
   // PTAdController::shared()->rewardedVideoDidEnd();
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

-(void)buttonActivated:(NSString*) name{

}
-(bool)buttonVisible:(NSString*)name{
    return true;
}
#pragma mark Integration

@end


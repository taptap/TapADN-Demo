package com.tapsdk.tapad.addemo.splash;

import com.tapsdk.tapad.TapSplashAd;

public class SplashAdManager {

    private TapSplashAd portraitSplashAd;

    private TapSplashAd landscapeSplashAd;

    static class Holder {
        static SplashAdManager INSTANCE = new SplashAdManager();
    }

    public static SplashAdManager getInstance() {
        return Holder.INSTANCE;
    }

    public void cachePortraitSplashAd(TapSplashAd tapSplashAd) {
        this.portraitSplashAd = tapSplashAd;
    }

    public void cacheLandscapeSplashAd(TapSplashAd tapSplashAd) {
        this.landscapeSplashAd = tapSplashAd;
    }

    public TapSplashAd getCachedPortraitSplashAd() {
        return portraitSplashAd;
    }

    public TapSplashAd getCachedLandscapeSplashAd() {
        return landscapeSplashAd;
    }
}

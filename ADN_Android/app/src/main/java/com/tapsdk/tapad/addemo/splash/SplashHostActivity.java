package com.tapsdk.tapad.addemo.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapSplashAd;
import com.tapsdk.tapad.addemo.utils.DeviceUtils;
import com.tapsdk.tapad.addemo.utils.UIUtils;
import com.tapsdk.tapad.internal.utils.TapADLogger;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.widget.TDSToastManager;

public class SplashHostActivity extends Activity {

    private TapAdNative tapAdNative;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_host);

        tapAdNative = TapAdManager.get().createAdNative(this);

        findViewById(R.id.portraitSplashAdButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDSToastManager.instance().showLoading(SplashHostActivity.this);
                int width = DeviceUtils.getScreenWidth(SplashHostActivity.this);
                int height = DeviceUtils.getScreenHeight(SplashHostActivity.this) - UIUtils.dp2px(SplashHostActivity.this, 40);
                TDSToastManager.instance().showLoading(SplashHostActivity.this);
                tapAdNative.loadSplashAd(new AdRequest.Builder().withSpaceId(1000057)
                        .withExpressViewAcceptedSize(width, height)
                        .build(), new TapAdNative.SplashAdListener() {

                    @Override
                    public void onError(int code, String message) {
                        TDSToastManager.instance().dismiss();
                        Toast.makeText(SplashHostActivity.this, "获取竖屏开屏广告失败(开屏广告会)", Toast.LENGTH_SHORT).show();
                        TapADLogger.e("code:" + code + ",message:" + message);
                        SplashAdManager.getInstance().cachePortraitSplashAd(null);
                    }

                    @Override
                    public void onSplashAdLoad(TapSplashAd splashAd) {
                        TDSToastManager.instance().dismiss();
                        TapADLogger.d("获取竖屏开屏广告成功");
                        Toast.makeText(SplashHostActivity.this, "获取竖屏开屏广告成功", Toast.LENGTH_SHORT).show();
                        SplashAdManager.getInstance().cachePortraitSplashAd(splashAd);
                    }
                });
            }
        });

        findViewById(R.id.landscapeSplashAdButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDSToastManager.instance().showLoading(SplashHostActivity.this);
                tapAdNative.loadSplashAd(new AdRequest.Builder().withSpaceId(1000051).build(), new TapAdNative.SplashAdListener() {

                    @Override
                    public void onError(int code, String message) {
                        TDSToastManager.instance().dismiss();
                        Toast.makeText(SplashHostActivity.this, "获取横开屏广告失败", Toast.LENGTH_SHORT).show();
                        TapADLogger.e("code:" + code + ",message:" + message);
                        SplashAdManager.getInstance().cacheLandscapeSplashAd(null);
                    }

                    @Override
                    public void onSplashAdLoad(TapSplashAd splashAd) {
                        TDSToastManager.instance().dismiss();
                        TapADLogger.d("获取横屏开屏广告成功");
                        Toast.makeText(SplashHostActivity.this, "获取横屏开屏广告成功", Toast.LENGTH_SHORT).show();
                        SplashAdManager.getInstance().cacheLandscapeSplashAd(splashAd);
                    }
                });
            }
        });

        findViewById(R.id.showPortraitSplashAdButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SplashAdManager.getInstance().getCachedPortraitSplashAd() != null) {
                    Intent intent = new Intent(SplashHostActivity.this, PortraitSplashActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SplashHostActivity.this, "请先获取竖屏的开屏广告", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.showLandscapeSplashAdButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SplashAdManager.getInstance().getCachedLandscapeSplashAd() != null) {
                    Intent intent = new Intent(SplashHostActivity.this, LandscapeSplashActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SplashHostActivity.this, "请先获取竖屏的开屏广告", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

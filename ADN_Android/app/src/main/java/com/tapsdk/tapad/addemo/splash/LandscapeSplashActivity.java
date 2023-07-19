package com.tapsdk.tapad.addemo.splash;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.tapsdk.tapad.TapSplashAd;
import com.tapsdk.tapad.addemo.R;

public class LandscapeSplashActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_landscape);

        SplashAdManager.getInstance().getCachedLandscapeSplashAd().setSplashInteractionListener(new TapSplashAd.AdInteractionListener() {
            @Override
            public void onAdSkip() {
                SplashAdManager.getInstance().getCachedLandscapeSplashAd().dispose();
                Toast.makeText(LandscapeSplashActivity.this, "点击了跳过", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onAdTimeOver() {
                SplashAdManager.getInstance().getCachedLandscapeSplashAd().dispose();
                Toast.makeText(LandscapeSplashActivity.this, "开屏光告时间已到", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        SplashAdManager.getInstance().getCachedLandscapeSplashAd().show(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SplashAdManager.getInstance().getCachedLandscapeSplashAd().dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplashAdManager.getInstance().getCachedLandscapeSplashAd().dispose();
    }
}

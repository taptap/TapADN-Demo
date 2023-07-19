package com.tapsdk.tapad.addemo.splash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tapsdk.tapad.TapSplashAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.utils.DeviceUtils;
import com.tapsdk.tapad.addemo.utils.UIUtils;

public class PortraitSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_portrait);
        SplashAdManager.getInstance().getCachedPortraitSplashAd().setSplashInteractionListener(new TapSplashAd.AdInteractionListener() {
            @Override
            public void onAdSkip() {
                SplashAdManager.getInstance().getCachedPortraitSplashAd().dispose();
                Toast.makeText(PortraitSplashActivity.this, "点击了跳过", Toast.LENGTH_SHORT).show();
                PortraitSplashActivity.this.finish();
            }

            @Override
            public void onAdTimeOver() {
                SplashAdManager.getInstance().getCachedPortraitSplashAd().dispose();
                Toast.makeText(PortraitSplashActivity.this, "开屏光告时间已到", Toast.LENGTH_SHORT).show();
                PortraitSplashActivity.this.finish();
            }
        });

        int width = DeviceUtils.getScreenWidth(this);
        int height = DeviceUtils.getScreenHeight(this) - UIUtils.dp2px(this, 40);

        View splashView = SplashAdManager.getInstance().getCachedPortraitSplashAd().getSplashView(this);
        RelativeLayout relativeLayout = ((RelativeLayout)findViewById(R.id.portraitSplashRootView));
        relativeLayout.addView(splashView, new RelativeLayout.LayoutParams(width, height));
        TextView localView = new TextView(this);
        localView.setText("测试");
        localView.setGravity(Gravity.CENTER);
        localView.setBackgroundColor(R.color.purple_200);
        splashView.setId(splashView.hashCode());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, splashView.getId());
        relativeLayout.addView(localView, lp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SplashAdManager.getInstance().getCachedPortraitSplashAd().dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplashAdManager.getInstance().getCachedPortraitSplashAd().dispose();
    }
}

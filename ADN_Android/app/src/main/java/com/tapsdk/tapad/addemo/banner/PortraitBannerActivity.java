package com.tapsdk.tapad.addemo.banner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapBannerAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.widget.TDSToastManager;

public class PortraitBannerActivity extends Activity {

    private TapAdNative tapAdNative;

    private Button fetchBannerAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_banner_portrait);

        tapAdNative = TapAdManager.get().createAdNative(this);
        fetchBannerAdButton = (Button) findViewById(R.id.fetchBannerAdButton);
        fetchBannerAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDSToastManager.instance().showLoading(PortraitBannerActivity.this);
                tapAdNative.loadBannerAd(new AdRequest.Builder().withSpaceId(1000059).build(), new TapAdNative.BannerAdListener() {
                    @Override
                    public void onError(int code, String message) {
                        TDSToastManager.instance().dismiss();
                        Toast.makeText(PortraitBannerActivity.this, "get banner error:(" + code + "," + message + ")", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBannerAdLoad(TapBannerAd bannerAd) {
                        TDSToastManager.instance().dismiss();
                        Toast.makeText(PortraitBannerActivity.this, "get banner success", Toast.LENGTH_SHORT).show();
//                        ((LinearLayout)((FrameLayout)getWindow().getDecorView().findViewById(android.R.id.content)).findViewById(R.id.bannerContainer)).addView(bannerAd.getBannerView());

                        bannerAd.setBannerInteractionListener(new TapBannerAd.BannerInteractionListener() {
                            @Override
                            public void onAdClose() {
                                Toast.makeText(PortraitBannerActivity.this, "banner onAdClose", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClick() {
                                Toast.makeText(PortraitBannerActivity.this, "banner onAdClick", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadClick() {
                                Toast.makeText(PortraitBannerActivity.this, "banner onDownloadClick", Toast.LENGTH_SHORT).show();
                            }
                        });
                        bannerAd.show(PortraitBannerActivity.this, 1, 100);
                    }
                });
            }
        });
    }
}

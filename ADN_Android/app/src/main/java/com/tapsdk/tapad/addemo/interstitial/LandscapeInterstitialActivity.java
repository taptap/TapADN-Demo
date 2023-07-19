package com.tapsdk.tapad.addemo.interstitial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapInterstitialAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.widget.TDSToastManager;

public class LandscapeInterstitialActivity extends Activity {

    private static final String TAG = "LandscapeInterstitial";
    private TapAdNative tapAdNative;
    private TapInterstitialAd interstitialAd;

    private int adId = 1000728;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstital);
        tapAdNative = TapAdManager.get().createAdNative(this);
        ((Button)findViewById(R.id.load_interstitial_ad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(LandscapeInterstitialActivity.this);
            }
        });

        ((Button)findViewById(R.id.show_interstitial_ad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd(LandscapeInterstitialActivity.this);
            }
        });

    }


    /**
     * 加载广告
     * @param activity
     */
    private void loadAd(Activity activity){
        TDSToastManager.instance().showLoading(activity);

        AdRequest adRequest = new AdRequest.Builder()
                .withSpaceId(adId)
                .withExtra1("{}")
                .withUserId("123")
                .build();
        LandscapeInterstitialActivity.AdLoadListener adLoadListener= new LandscapeInterstitialActivity.AdLoadListener(this);
        tapAdNative.loadInterstitialAd(adRequest,adLoadListener);

    }

    /**
     * 显示广告
     * @param activity
     */
    public void showAd(Activity activity){
        if(interstitialAd!=null){
            interstitialAd.setInteractionListener(new LandscapeInterstitialActivity.AdInteractionListener());
            interstitialAd.show(activity);
        }
    }


    private  class AdLoadListener implements TapAdNative.InterstitialAdListener {

        private final Activity mActivity;
        public AdLoadListener(Activity activity){
            this.mActivity = activity;
        }

        @Override
        public void onInterstitialAdLoad(TapInterstitialAd ad) {
            Log.d(TAG,"广告加载完成");
            Toast.makeText(mActivity.getBaseContext(),"广告加载完成",Toast.LENGTH_SHORT).show();
            interstitialAd = ad;
            TDSToastManager.instance().dismiss();
        }


        @Override
        public void onError(int var1, String var2) {
            TDSToastManager.instance().dismiss();
            Log.d(TAG,"广告加载异常："+var2);
        }
    }

    private class AdInteractionListener implements TapInterstitialAd.InterstitialAdInteractionListener {

        @Override
        public void onAdShow() {
            Log.d(TAG,"onShow");
        }
        @Override
        public void onAdClose() {
            Log.d(TAG,"onAdClose");
        }
        @Override
        public void onAdError() {
            Log.d(TAG,"onAdError");
        }
    }


}

package com.tapsdk.tapad.addemo.reward;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapRewardVideoAd;
import com.tapsdk.tapad.internal.utils.TapADLogger;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.widget.TDSToastManager;

public class RewardHostActivity extends AppCompatActivity {

    private TapRewardVideoAd horizontalCachedAdInfo;

    private TapRewardVideoAd verticalCachedAdInfo;

    private TapAdNative tapAdNative;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reward);

        tapAdNative = TapAdManager.get().createAdNative(this);

        Button fetchHorizontalAdButton = (Button) findViewById(R.id.fetchHorizontalAdButton);
        fetchHorizontalAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDSToastManager.instance().showLoading(RewardHostActivity.this);
                AdRequest adRequest = new AdRequest.Builder()
                        .withSpaceId(1000052)
                        .withExtra1("{}")
                        .withUserId("123")
                        .withRewordName("rewardName")
                        .withRewordAmount(10)
                        .build();
                tapAdNative.loadRewardVideoAd(adRequest, new TapAdNative.RewardVideoAdListener() {
                    @Override
                    public void onError(int code, String message) {
                        TapADLogger.d("get ad fail(" + code + "/" + message + ")");
                        Toast.makeText(RewardHostActivity.this, "获取广告失败(" + code + "/" + message + ")", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }

                    @Override
                    public void onRewardVideoAdLoad(TapRewardVideoAd rewardVideoAd) {
                        TapADLogger.d("get ad success" + rewardVideoAd);
                        horizontalCachedAdInfo = rewardVideoAd;
                        Toast.makeText(RewardHostActivity.this, "获取广告成功", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }

                    @Override
                    public void onRewardVideoCached(TapRewardVideoAd rewardVideoAd) {
                        horizontalCachedAdInfo = rewardVideoAd;
                        Toast.makeText(RewardHostActivity.this, "获取广告素材成功", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }
                });
            }
        });

        Button showHorizontalAdButton = (Button) findViewById(R.id.showHorizontalAdButton);
        showHorizontalAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalCachedAdInfo != null) {
                    horizontalCachedAdInfo.setRewardAdInteractionListener(new TapRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            Toast.makeText(RewardHostActivity.this, "onAdShow", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            Toast.makeText(RewardHostActivity.this, "onAdClose", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVideoComplete() {
                            Toast.makeText(RewardHostActivity.this, "onVideoComplete", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVideoError() {

                        }

                        @Override
                        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int code, String msg) {
                            Toast.makeText(RewardHostActivity.this, "获得奖励", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSkippedVideo() {
                            Toast.makeText(RewardHostActivity.this, "onSkippedVideo", Toast.LENGTH_SHORT).show();
                        }
                    });
                    horizontalCachedAdInfo.showRewardVideoAd(RewardHostActivity.this);

                } else {
                    Toast.makeText(RewardHostActivity.this, "请先获取横屏广告", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button fetchVerticalAdButton = (Button) findViewById(R.id.fetchVerticalAdButton);
        fetchVerticalAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDSToastManager.instance().showLoading(RewardHostActivity.this);
                AdRequest adRequest = new AdRequest.Builder()
                        .withSpaceId(1000058)
                        .withExtra1("{}")
                        .withUserId("123")
                        .build();
                tapAdNative.loadRewardVideoAd(adRequest, new TapAdNative.RewardVideoAdListener() {
                    @Override
                    public void onError(int code, String message) {
                        TapADLogger.d("get ad fail(" + code + "/" + message + ")");
                        Toast.makeText(RewardHostActivity.this, "获取广告失败(" + code + "/" + message + ")", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }

                    @Override
                    public void onRewardVideoAdLoad(TapRewardVideoAd rewardVideoAd) {
                        verticalCachedAdInfo = rewardVideoAd;
                        Toast.makeText(RewardHostActivity.this, "获取广告成功", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }

                    @Override
                    public void onRewardVideoCached(TapRewardVideoAd rewardVideoAd) {
                        Toast.makeText(RewardHostActivity.this, "获取广告素材成功", Toast.LENGTH_SHORT).show();
                        TDSToastManager.instance().dismiss();
                    }
                });
            }
        });

        Button showVerticalAdButton = (Button) findViewById(R.id.showVerticalAdButton);
        showVerticalAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verticalCachedAdInfo != null) {
                    verticalCachedAdInfo.showRewardVideoAd(RewardHostActivity.this);
                    verticalCachedAdInfo.setRewardAdInteractionListener(new TapRewardVideoAd.RewardAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            Toast.makeText(RewardHostActivity.this, "onAdShow", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            Toast.makeText(RewardHostActivity.this, "onAdClose", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVideoComplete() {
                            Toast.makeText(RewardHostActivity.this, "onVideoComplete", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVideoError() {

                        }

                        @Override
                        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int code, String msg) {
                            Toast.makeText(RewardHostActivity.this, "获得奖励", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSkippedVideo() {
                            Toast.makeText(RewardHostActivity.this, "onSkippedVideo", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RewardHostActivity.this, "请先获竖屏取广告", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

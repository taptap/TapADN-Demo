package com.tapsdk.tapad.addemo.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapFeedAd;
import com.tapsdk.tapad.addemo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FeedRecyclerViewActivity2 extends AppCompatActivity {


    private TapAdNative tapAdNative;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed2);
        tapAdNative = TapAdManager.get().createAdNative(this);
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } catch (Throwable ignore) {
        }

        initViews();
    }

    private void initViews() {
        final FrameLayout rootView = (FrameLayout) findViewById(R.id.native_ad_container);


        findViewById(R.id.loadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapAdNative.loadFeedAd(new AdRequest.Builder()
//                        .withSpaceId(1000408).build()
                                .withSpaceId(1000063).build()
                        , new TapAdNative.FeedAdListener() {
                            @Override
                            public void onFeedAdLoad(List<TapFeedAd> tapFeedAds) {

                                View adContainerView  = LayoutInflater.from(FeedRecyclerViewActivity2.this).inflate(R.layout.itemview_video_feed2, rootView, false);

                                rootView.removeAllViews();
                                rootView.addView(adContainerView);
                                final TapFeedAd tapFeedAd = tapFeedAds.get(0);

                                Button closeButton = (Button) adContainerView.findViewById(R.id.closeButton);

                                closeButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        rootView.removeAllViews();

                                    }
                                });

                                Glide.with(FeedRecyclerViewActivity2.this).load(tapFeedAd).into((ImageView)adContainerView.findViewById(R.id.adLogoImageView));
//            Glide.with(this.context).load(tapFeedAd.getImageInfoList().get(0).imageUrl).into(videoAdViewHolder.adImageView);
                                ((TextView)adContainerView.findViewById(R.id.appNameTextView)).setText(tapFeedAd.getComplianceInfo().getAppName());
                                ((TextView)adContainerView.findViewById(R.id.appDescriptionTextView)).setText(tapFeedAd.getDescription());
                                ((TextView)adContainerView.findViewById(R.id.appVersionTextView)).setText("版本 " + tapFeedAd.getComplianceInfo().getAppVersion());
                                ((TextView)adContainerView.findViewById(R.id.developerNameTextView)).setText(tapFeedAd.getComplianceInfo().getDeveloperName());
                                ((TextView)adContainerView.findViewById(R.id.creativeTextView)).setText("立即下载");

                                List<View> clickViewList = new ArrayList<>();
                                List<View> creativeViewList = Collections.singletonList(((TextView)adContainerView.findViewById(R.id.creativeTextView)));

                                List<View> privacyViewList = Collections.singletonList(((TextView)adContainerView.findViewById(R.id.privacyTextView)));

                                List<View> permissionViewList = Collections.singletonList(((TextView)adContainerView.findViewById(R.id.permissionTextView)));

                                tapFeedAd.registerViewForInteraction((ViewGroup) adContainerView, clickViewList, creativeViewList, privacyViewList, permissionViewList, new TapFeedAd.AdInteractionListener() {
                                    @Override
                                    public void onAdClicked(View view, TapFeedAd ad) {
                                        if (ad != null) {
                                            Toast.makeText(FeedRecyclerViewActivity2.this, "广告" + tapFeedAd.getTitle() + "被点击", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onAdCreativeClick(View view, TapFeedAd ad) {
                                        if (ad != null) {
                                            Toast.makeText(FeedRecyclerViewActivity2.this, "游戏 " + ad.getTitle() + " 的交互按钮被点击", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override

                                    public void onAdShow(TapFeedAd ad) {
                                        if (ad != null) {
                                            Log.d("hxh", "广告" + ad.getTitle() + "展示");
                                        }
                                    }
                                });

                                FrameLayout videoFrameLayout = adContainerView.findViewById(R.id.videoFrameLayout);
                                if (videoFrameLayout != null) {
                                    View video = tapFeedAd.getAdView();
                                    if (video != null) {
                                        if (video.getParent() == null) {
                                            videoFrameLayout.removeAllViews();
                                            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            videoFrameLayout.addView(video, flp);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("hxh", "code:" + code + ",message:" + message);
                            }
                        });
            }
        });

        findViewById(R.id.cleanLog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (pagerAdapter != null) pagerAdapter.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    void buttonClick() {
        Toast.makeText(this, "button click", Toast.LENGTH_SHORT).show();
    }
}

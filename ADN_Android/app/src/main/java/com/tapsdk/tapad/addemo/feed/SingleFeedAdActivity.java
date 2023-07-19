package com.tapsdk.tapad.addemo.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapAppDownloadListener;
import com.tapsdk.tapad.TapFeedAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.utils.UIUtils;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Created by Quin on 2023/7/4.
 **/
public class SingleFeedAdActivity extends AppCompatActivity {

    ImageView adImageView;

    TextView creativeTextView;

    private TextView appNameTextView;

    private TextView appDescriptionTextView;

    private TextView appVersionTextView;

    private TextView developerNameTextView;

    private TextView privacyTextView;

    private TextView permissionTextView;

    private TextView realScoreTitleTextView;

    private ImageView realScoreStarImageView;

    private TextView realScoreTextView;

    private TapAdNative tapAdNative;
    private TapFeedAd tapFeedAd;
    private FrameLayout videoFrameLayout;
    private ImageView adLogoImageView;
    private ViewGroup itemView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_itemview_video_feed);

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } catch (Throwable ignore) {
        }
        tapAdNative = TapAdManager.get().createAdNative(this);
        initView();
        loadAd();
    }

    private void initView() {
        adImageView = (ImageView) findViewById(R.id.adImageView);
        adImageView.setVisibility(View.GONE);
        creativeTextView = (TextView) findViewById(R.id.creativeTextView);
        appNameTextView = (TextView) findViewById(R.id.appNameTextView);
        appDescriptionTextView = (TextView) findViewById(R.id.appDescriptionTextView);
        appVersionTextView = (TextView) findViewById(R.id.appVersionTextView);
        developerNameTextView = (TextView) findViewById(R.id.developerNameTextView);
        privacyTextView = (TextView) findViewById(R.id.privacyTextView);
        permissionTextView = (TextView) findViewById(R.id.permissionTextView);
        realScoreTitleTextView = (TextView) findViewById(R.id.realScoreTitleTextView);
        realScoreStarImageView = (ImageView) findViewById(R.id.realScoreStarImageView);
        realScoreTextView = (TextView) findViewById(R.id.realScoreTextView);
        videoFrameLayout = findViewById(R.id.videoFrameLayout);
        adLogoImageView = findViewById(R.id.adLogoImageView);
        itemView = findViewById(R.id.rl_item_view);
    }

    private void loadAd() {
        tapAdNative.loadFeedAd(new AdRequest.Builder()
                .withSpaceId(1000063).build(), new TapAdNative.FeedAdListener() {
            @Override
            public void onFeedAdLoad(List<TapFeedAd> list) {
                tapFeedAd = list != null && list.size() > 0 ? list.get(0) : null;
                initAdView();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initAdView() {
        Glide.with(this).load(tapFeedAd.getIconUrl()).into(adLogoImageView);
//        Glide.with(this).load(tapFeedAd.getImageInfoList().get(0).imageUrl).into(adImageView);
        appNameTextView.setText(tapFeedAd.getComplianceInfo().getAppName());
        appDescriptionTextView.setText(tapFeedAd.getDescription());
        appVersionTextView.setText("版本 " + tapFeedAd.getComplianceInfo().getAppVersion());
        developerNameTextView.setText(tapFeedAd.getComplianceInfo().getDeveloperName());
        creativeTextView.setText("立即下载");

        List<View> clickViewList = Collections.singletonList(adImageView);

        List<View> creativeViewList = Collections.singletonList(creativeTextView);

        List<View> privacyViewList = Collections.singletonList(privacyTextView);

        List<View> permissionViewList = Collections.singletonList(permissionTextView);
//
        tapFeedAd.registerViewForInteraction((ViewGroup) itemView, clickViewList, creativeViewList, privacyViewList, permissionViewList, new TapFeedAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TapFeedAd ad) {
                if (ad != null) {
                    Toast.makeText(SingleFeedAdActivity.this, "广告" + tapFeedAd.getTitle() + "被点击", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdCreativeClick(View view, TapFeedAd ad) {
                if (ad != null) {
                    Toast.makeText(SingleFeedAdActivity.this, "游戏 " + ad.getTitle() + " 的交互按钮被点击", Toast.LENGTH_SHORT).show();
                }
            }

            @Override

            public void onAdShow(TapFeedAd ad) {
                if (ad != null) {
                    Log.d("hxh", "广告" + ad.getTitle() + "展示");
                }
            }
        });
        if (videoFrameLayout != null) {
            View video = tapFeedAd.getAdView();
            if (video != null) {
                if (video.getParent() == null) {
                    videoFrameLayout.removeAllViews();
                    FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    videoFrameLayout.addView(video, flp);
                }
            }
        }
        bindVideoAdDownloadListener(creativeTextView, tapFeedAd);
        boolean hasScore = (tapFeedAd.getScore() > 0);
        realScoreStarImageView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
        realScoreTextView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
        if (hasScore) {
            realScoreTextView.setText(String.valueOf(tapFeedAd.getScore()));
            realScoreTitleTextView.setText("评分");
        } else {
            realScoreTitleTextView.setText("暂无评分");
        }
    }

    private void bindVideoAdDownloadListener(final TextView adCreativeButton, TapFeedAd ad) {
        TapAppDownloadListener downloadListener = new TapAppDownloadListener() {

            @Override
            public void onIdle() {
                adCreativeButton.setText("立即下载");
            }

            @Override
            public void onDownloadStart() {
                adCreativeButton.setText("下载中");
            }

            @Override
            public void onDownloadComplete() {
                adCreativeButton.setText("安装");
            }

            @Override
            public void onUpdateDownloadProgress(int percent) {
                // update your progress;
            }

            @Override
            public void onDownloadError() {
                adCreativeButton.setText("重新下载");
            }

            @Override
            public void onInstalled() {
                adCreativeButton.setText("打开");
            }
        };
        ad.setDownloadListener(downloadListener); // 注册下载监听器
//        mTapAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tapAdNative != null) {
            tapAdNative.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tapAdNative != null) {
            tapAdNative.dispose();
            tapAdNative = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tapAdNative != null) {
            tapAdNative.resume();
        }
    }
}

package com.tapsdk.tapad.addemo.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapAppDownloadListener;
import com.tapsdk.tapad.TapFeedAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.utils.UIUtils;
import com.tapsdk.tapad.addemo.widget.LoadMoreView;
import com.tapsdk.tapad.constants.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_LOAD_MORE = -1;

    private static final int ITEM_VIEW_TYPE_NORMAL = 0;

    private static final int ITEM_VIEW_TYPE_LARGE_PIC_AD = 1;

    private static final int ITEM_VIEW_TYPE_VIDEO = 2;

    private static final int ITEM_VIEW_TYPE_SMALL_PIC_AD = 3;

    private final Context context;

    List<? super RecyclerViewItem> dataList;

    private Map<LargeAdViewHolder, TapAppDownloadListener> mTapAppDownloadListenerMap = new WeakHashMap<>();

//    private RequestManager requestManager;
    private TapAdNative tapAdNative;

    public FeedAdapter(Context context, List<? super RecyclerViewItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public FeedAdapter(Context context, List<? super RecyclerViewItem> dataList, TapAdNative tapAdNative) {
        this.context = context;
        this.dataList = dataList;
        this.tapAdNative = tapAdNative;
    }

    @Override
    public int getItemCount() {
        int count = dataList == null ? 0 : dataList.size();
        return count + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ITEM_VIEW_TYPE_LOAD_MORE:
                return new LoadMoreViewHolder(new LoadMoreView(this.context));
            case ITEM_VIEW_TYPE_LARGE_PIC_AD:
                return new LargeAdViewHolder(LayoutInflater.from(this.context).inflate(R.layout.itemview_ad_large_pic, parent, false));

            case ITEM_VIEW_TYPE_SMALL_PIC_AD:
                return new SmallAdViewHolder(LayoutInflater.from(this.context).inflate(R.layout.itemview_ad_small_pic, parent, false));
//                case ITEM_VIEW_TYPE_LARGE_PIC_AD:
//                    return new LargeAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_large_pic, parent, false));
//                case ITEM_VIEW_TYPE_VERTICAL_PIC_AD:
//                    return new VerticalAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_vertical_pic, parent, false));
//                case ITEM_VIEW_TYPE_GROUP_PIC_AD:
//                    return new GroupAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_group_pic, parent, false));
            case ITEM_VIEW_TYPE_VIDEO:
                return new VideoAdViewHolder(LayoutInflater.from(this.context).inflate(R.layout.itemview_video_feed, parent, false));
            default:
                return new NormalViewHolder(LayoutInflater.from(this.context).inflate(R.layout.itemview_normal_feed, parent, false));
//                    return new VideoAdViewHolder(LayoutInflater.from(this.context).inflate(R.layout.itemview_video_feed, parent, false));
//                default:
//                    return null;
        }
    }


    private void bindLargeAdDownloadListener(final TextView adCreativeTextView, TapFeedAd ad) {
        TapAppDownloadListener downloadListener = new TapAppDownloadListener() {

            @Override
            public void onIdle() {
                adCreativeTextView.setText("立即下载");
            }

            @Override
            public void onDownloadStart() {
                adCreativeTextView.setText("下载中");
            }

            @Override
            public void onDownloadComplete() {
                adCreativeTextView.setText("安装");
            }

            @Override
            public void onUpdateDownloadProgress(int percent) {
                adCreativeTextView.setText("下载中");
            }

            @Override
            public void onDownloadError() {
                adCreativeTextView.setText("重新下载");
            }

            @Override
            public void onInstalled() {
                adCreativeTextView.setText("打开");
            }
        };
        ad.setDownloadListener(downloadListener); // 注册下载监听器
//        mTapAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }

    private void bindVideoAdDownloadListener(final TextView adCreativeButton, VideoAdViewHolder adViewHolder, TapFeedAd ad) {
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
    public int getItemViewType(int position) {
        if (dataList != null) {
            int count = dataList.size();
            if (position >= count) {
                return ITEM_VIEW_TYPE_LOAD_MORE;
            } else {
                RecyclerViewItem recyclerViewItem = (RecyclerViewItem) dataList.get(position);
                if (recyclerViewItem instanceof NoteRecyclerViewItem) {
                    return ITEM_VIEW_TYPE_NORMAL;
                } else if (recyclerViewItem instanceof AdRecyclerViewItem) {
                    return ITEM_VIEW_TYPE_LARGE_PIC_AD;
                } else if (recyclerViewItem instanceof SmallAdRecyclerViewItem) {
                    return ITEM_VIEW_TYPE_SMALL_PIC_AD;
                } else if (recyclerViewItem instanceof VideoAdRecyclerViewItem) {
                    return ITEM_VIEW_TYPE_VIDEO;
                } else {
                    return ITEM_VIEW_TYPE_NORMAL;
                }
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NoteRecyclerViewItem noteRecyclerViewItem = (NoteRecyclerViewItem) dataList.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.feedTitleTextView.setText(noteRecyclerViewItem.title);
            normalViewHolder.feedDescriptionTextView.setText(noteRecyclerViewItem.description);
            normalViewHolder.feedImageView.setBackgroundColor(noteRecyclerViewItem.colorRes);
        } else if (holder instanceof LargeAdViewHolder) {
            AdRecyclerViewItem adRecyclerViewItem = (AdRecyclerViewItem) dataList.get(position);
            LargeAdViewHolder largeAdViewHolder = (LargeAdViewHolder) holder;
            TapFeedAd tapFeedAd = adRecyclerViewItem.tapFeedAd;
            Glide.with(this.context).load(tapFeedAd.getIconUrl()).into(largeAdViewHolder.adLogoImageView);
            Glide.with(this.context).load(tapFeedAd.getImageInfoList().get(0).imageUrl).into(largeAdViewHolder.adImageView);
            largeAdViewHolder.creativeTextView.setText("立即下载");
            largeAdViewHolder.appNameTextView.setText(tapFeedAd.getComplianceInfo().getAppName());
            largeAdViewHolder.appVersionTextView.setText("版本 " + tapFeedAd.getComplianceInfo().getAppVersion());
            largeAdViewHolder.appDescriptionTextView.setText(tapFeedAd.getDescription());
            largeAdViewHolder.developerNameTextView.setText(tapFeedAd.getComplianceInfo().getDeveloperName());
            largeAdViewHolder.adImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int width = largeAdViewHolder.adImageView.getWidth();
                    UIUtils.setViewSize(largeAdViewHolder.adImageView, width, (int) ((double) width / (16 / (double) 9)));
                }
            }, 100);

            List<View> clickViewList = Collections.singletonList(largeAdViewHolder.adImageView);

            List<View> creativeViewList = Collections.singletonList(largeAdViewHolder.creativeTextView);

            List<View> privacyViewList = Collections.singletonList(largeAdViewHolder.privacyTextView);

            List<View> permissionViewList = Collections.singletonList(largeAdViewHolder.permissionTextView);

            tapFeedAd.registerViewForInteraction((ViewGroup) largeAdViewHolder.itemView, clickViewList, creativeViewList, privacyViewList, permissionViewList, new TapFeedAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "广告" + tapFeedAd.getTitle() + "被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "游戏 " + ad.getTitle() + " 的交互按钮被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override

                public void onAdShow(TapFeedAd ad) {
                    if (ad != null) {
//                        Toast.makeText(FeedAdapter.this.context, "广告" + ad.getTitle() + "展示", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            boolean hasScore = (tapFeedAd.getScore() > 0);
            largeAdViewHolder.realScoreStarImageView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            largeAdViewHolder.realScoreTextView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            if (hasScore) {
                largeAdViewHolder.realScoreTextView.setText(String.valueOf(tapFeedAd.getScore()));
                largeAdViewHolder.realScoreTitleTextView.setText("评分");
            } else {
                largeAdViewHolder.realScoreTitleTextView.setText("暂无评分");
            }

            bindLargeAdDownloadListener(largeAdViewHolder.creativeTextView, tapFeedAd);
        } else if (holder instanceof SmallAdViewHolder) {
            SmallAdRecyclerViewItem adRecyclerViewItem = (SmallAdRecyclerViewItem) dataList.get(position);
            SmallAdViewHolder smallAdViewHolder = (SmallAdViewHolder) holder;
            TapFeedAd tapFeedAd = adRecyclerViewItem.tapFeedAd;
            Glide.with(this.context).load(tapFeedAd.getImageInfoList().get(0).imageUrl).into(smallAdViewHolder.adImageView);
            smallAdViewHolder.creativeTextView.setText("立即下载");
            smallAdViewHolder.appNameTextView.setText(tapFeedAd.getComplianceInfo().getAppName());
            smallAdViewHolder.appVersionTextView.setText("版本 " + tapFeedAd.getComplianceInfo().getAppVersion());
            smallAdViewHolder.appDescriptionTextView.setText(tapFeedAd.getDescription());
            smallAdViewHolder.developerNameTextView.setText(tapFeedAd.getComplianceInfo().getDeveloperName());
//            smallAdViewHolder.adImageView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int width = smallAdViewHolder.adImageView.getWidth();
//                    UIUtils.setViewSize(smallAdViewHolder.adImageView, width, (int) ((double) width / (16 / (double) 9)));
//                }
//            }, 100);

            List<View> clickViewList = Collections.singletonList(smallAdViewHolder.adImageView);

            List<View> creativeViewList = Collections.singletonList(smallAdViewHolder.creativeTextView);

            List<View> privacyViewList = Collections.singletonList(smallAdViewHolder.privacyTextView);

            List<View> permissionViewList = Collections.singletonList(smallAdViewHolder.permissionTextView);

            tapFeedAd.registerViewForInteraction((ViewGroup) smallAdViewHolder.itemView, clickViewList, creativeViewList, privacyViewList, permissionViewList, new TapFeedAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "广告" + tapFeedAd.getTitle() + "被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "游戏 " + ad.getTitle() + " 的交互按钮被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override

                public void onAdShow(TapFeedAd ad) {
                    if (ad != null) {
//                        Toast.makeText(FeedAdapter.this.context, "广告" + ad.getTitle() + "展示", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            boolean hasScore = (tapFeedAd.getScore() > 0);
            smallAdViewHolder.realScoreStarImageView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            smallAdViewHolder.realScoreTextView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            if (hasScore) {
                smallAdViewHolder.realScoreTextView.setText(String.valueOf(tapFeedAd.getScore()));
                smallAdViewHolder.realScoreTitleTextView.setText("评分");
            } else {
                smallAdViewHolder.realScoreTitleTextView.setText("暂无评分");
            }

            bindLargeAdDownloadListener(smallAdViewHolder.creativeTextView, tapFeedAd);
        } else if (holder instanceof VideoAdViewHolder) {
            VideoAdRecyclerViewItem item = (VideoAdRecyclerViewItem) dataList.get(position);
            VideoAdViewHolder videoAdViewHolder = (VideoAdViewHolder) holder;
            TapFeedAd tapFeedAd = item.tapFeedAd;
            Glide.with(this.context).load(tapFeedAd.getIconUrl()).into(videoAdViewHolder.adLogoImageView);
            Glide.with(this.context).load(tapFeedAd.getImageInfoList().get(0).imageUrl).into(videoAdViewHolder.adImageView);
            videoAdViewHolder.appNameTextView.setText(tapFeedAd.getComplianceInfo().getAppName());
            videoAdViewHolder.appDescriptionTextView.setText(tapFeedAd.getDescription());
            videoAdViewHolder.appVersionTextView.setText("版本 " + tapFeedAd.getComplianceInfo().getAppVersion());
            videoAdViewHolder.developerNameTextView.setText(tapFeedAd.getComplianceInfo().getDeveloperName());
            videoAdViewHolder.creativeTextView.setText("立即下载");

            List<View> clickViewList = Collections.singletonList(videoAdViewHolder.adImageView);

            List<View> creativeViewList = Collections.singletonList(videoAdViewHolder.creativeTextView);

            List<View> privacyViewList = Collections.singletonList(videoAdViewHolder.privacyTextView);

            List<View> permissionViewList = Collections.singletonList(videoAdViewHolder.permissionTextView);
//
            tapFeedAd.registerViewForInteraction((ViewGroup) videoAdViewHolder.itemView, clickViewList, creativeViewList, privacyViewList, permissionViewList, new TapFeedAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "广告" + tapFeedAd.getTitle() + "被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TapFeedAd ad) {
                    if (ad != null) {
                        Toast.makeText(FeedAdapter.this.context, "游戏 " + ad.getTitle() + " 的交互按钮被点击", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override

                public void onAdShow(TapFeedAd ad) {
                    if (ad != null) {
                        Log.d("hxh", "广告" + ad.getTitle() + "展示");
                    }
                }
            });
            if (videoAdViewHolder.videoFrameLayout != null) {
                View video = tapFeedAd.getAdView();
                videoAdViewHolder.videoFrameLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = videoAdViewHolder.videoFrameLayout.getWidth();
                        UIUtils.setViewSize(videoAdViewHolder.adImageView, width, (int) ((double) width / (16 / (double) 9)));
                        UIUtils.setViewSize(videoAdViewHolder.videoFrameLayout, width, (int) ((double) width / (16 / (double) 9)));
                    }
                });
                if (video != null) {
                    if (video.getParent() == null) {
                        videoAdViewHolder.videoFrameLayout.removeAllViews();
                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        videoAdViewHolder.videoFrameLayout.addView(video, flp);

                        if (tapAdNative != null) {
                            tapAdNative.resume();
                        }
                    }
                }
            }
            bindVideoAdDownloadListener(videoAdViewHolder.creativeTextView, videoAdViewHolder, tapFeedAd);
            boolean hasScore = (tapFeedAd.getScore() > 0);
            videoAdViewHolder.realScoreStarImageView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            videoAdViewHolder.realScoreTextView.setVisibility(hasScore ? View.VISIBLE : View.GONE);
            if (hasScore) {
                videoAdViewHolder.realScoreTextView.setText(String.valueOf(tapFeedAd.getScore()));
                videoAdViewHolder.realScoreTitleTextView.setText("评分");
            } else {
                videoAdViewHolder.realScoreTitleTextView.setText("暂无评分");
            }
        }
    }

    static class FeedAdapterViewHolder extends RecyclerView.ViewHolder {
        public FeedAdapterViewHolder(View view) {
            super(view);
        }
    }

    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        TextView loadMoreTipTextView;
        ProgressBar loadMoreProgressBar;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

            loadMoreTipTextView = (TextView) itemView.findViewById(R.id.loadMoreTipTextView);
            loadMoreProgressBar = (ProgressBar) itemView.findViewById(R.id.loadMoreProgressBar);
        }
    }

    private static class NormalViewHolder extends RecyclerView.ViewHolder {

        ImageView feedImageView;

        TextView feedTitleTextView;

        TextView feedDescriptionTextView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImageView);
            feedTitleTextView = (TextView) itemView.findViewById(R.id.feedTitleTextView);
            feedDescriptionTextView = (TextView) itemView.findViewById(R.id.feedDescriptionTextView);
        }
    }

    private static class VideoAdViewHolder extends RecyclerView.ViewHolder {

        ImageView adLogoImageView;

        FrameLayout videoFrameLayout;

        ImageView adImageView;

        TextView creativeTextView;

        private final TextView appNameTextView;

        private final TextView appDescriptionTextView;

        private final TextView appVersionTextView;

        private final TextView developerNameTextView;

        private final TextView privacyTextView;

        private final TextView permissionTextView;

        private final TextView realScoreTitleTextView;

        private final ImageView realScoreStarImageView;

        private final TextView realScoreTextView;

        public VideoAdViewHolder(View itemView) {
            super(itemView);
            adLogoImageView = (ImageView) itemView.findViewById(R.id.adLogoImageView);
            videoFrameLayout = (FrameLayout) itemView.findViewById(R.id.videoFrameLayout);
            adImageView = (ImageView) itemView.findViewById(R.id.adImageView);
            creativeTextView = (TextView) itemView.findViewById(R.id.creativeTextView);
            appNameTextView = (TextView) itemView.findViewById(R.id.appNameTextView);
            appDescriptionTextView = (TextView) itemView.findViewById(R.id.appDescriptionTextView);
            appVersionTextView = (TextView) itemView.findViewById(R.id.appVersionTextView);
            developerNameTextView = (TextView) itemView.findViewById(R.id.developerNameTextView);
            privacyTextView = (TextView) itemView.findViewById(R.id.privacyTextView);
            permissionTextView = (TextView) itemView.findViewById(R.id.permissionTextView);
            realScoreTitleTextView = (TextView) itemView.findViewById(R.id.realScoreTitleTextView);
            realScoreStarImageView = (ImageView) itemView.findViewById(R.id.realScoreStarImageView);
            realScoreTextView = (TextView) itemView.findViewById(R.id.realScoreTextView);
        }
    }

    private static class LargeAdViewHolder extends RecyclerView.ViewHolder {

        ImageView adLogoImageView;

        ImageView adImageView;

        TextView creativeTextView;

        private final TextView appNameTextView;

        private final TextView appDescriptionTextView;

        private final TextView appVersionTextView;

        private final TextView developerNameTextView;

        private final TextView privacyTextView;

        private final TextView permissionTextView;

        private final TextView realScoreTitleTextView;

        private final ImageView realScoreStarImageView;

        private final TextView realScoreTextView;

        public LargeAdViewHolder(View itemView) {
            super(itemView);
            adLogoImageView = (ImageView) itemView.findViewById(R.id.adLogoImageView);
            adImageView = (ImageView) itemView.findViewById(R.id.adImageView);
            creativeTextView = (TextView) itemView.findViewById(R.id.creativeTextView);
            appNameTextView = (TextView) itemView.findViewById(R.id.appNameTextView);
            appDescriptionTextView = (TextView) itemView.findViewById(R.id.appDescriptionTextView);
            appVersionTextView = (TextView) itemView.findViewById(R.id.appVersionTextView);
            developerNameTextView = (TextView) itemView.findViewById(R.id.developerNameTextView);
            privacyTextView = (TextView) itemView.findViewById(R.id.privacyTextView);
            permissionTextView = (TextView) itemView.findViewById(R.id.permissionTextView);
            realScoreTitleTextView = (TextView) itemView.findViewById(R.id.realScoreTitleTextView);
            realScoreStarImageView = (ImageView) itemView.findViewById(R.id.realScoreStarImageView);
            realScoreTextView = (TextView) itemView.findViewById(R.id.realScoreTextView);
        }
    }

    private static class SmallAdViewHolder extends RecyclerView.ViewHolder {


        ImageView adImageView;

        TextView creativeTextView;

        private final TextView appNameTextView;

        private final TextView appDescriptionTextView;

        private final TextView appVersionTextView;

        private final TextView developerNameTextView;

        private final TextView privacyTextView;

        private final TextView permissionTextView;

        private final TextView realScoreTitleTextView;

        private final ImageView realScoreStarImageView;

        private final TextView realScoreTextView;

        public SmallAdViewHolder(View itemView) {
            super(itemView);
            adImageView = (ImageView) itemView.findViewById(R.id.adImageView);
            creativeTextView = (TextView) itemView.findViewById(R.id.creativeTextView);
            appNameTextView = (TextView) itemView.findViewById(R.id.appNameTextView);
            appDescriptionTextView = (TextView) itemView.findViewById(R.id.appDescriptionTextView);
            appVersionTextView = (TextView) itemView.findViewById(R.id.appVersionTextView);
            developerNameTextView = (TextView) itemView.findViewById(R.id.developerNameTextView);
            privacyTextView = (TextView) itemView.findViewById(R.id.privacyTextView);
            permissionTextView = (TextView) itemView.findViewById(R.id.permissionTextView);
            realScoreTitleTextView = (TextView) itemView.findViewById(R.id.realScoreTitleTextView);
            realScoreStarImageView = (ImageView) itemView.findViewById(R.id.realScoreStarImageView);
            realScoreTextView = (TextView) itemView.findViewById(R.id.realScoreTextView);
        }
    }
}

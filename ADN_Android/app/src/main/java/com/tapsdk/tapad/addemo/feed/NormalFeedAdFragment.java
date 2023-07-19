package com.tapsdk.tapad.addemo.feed;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.tapsdk.tapad.AdRequest;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdNative;
import com.tapsdk.tapad.TapFeedAd;
import com.tapsdk.tapad.addemo.R;
import com.tapsdk.tapad.addemo.utils.ActivityUtils;
import com.tapsdk.tapad.addemo.widget.ILoadMoreListener;
import com.tapsdk.tapad.addemo.widget.LoadMoreRecyclerView;
import com.tapsdk.tapad.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NormalFeedAdFragment extends Fragment {

    private final static int CARD_SIZE = 10;

    LoadMoreRecyclerView feedRecyclerView;

    FeedAdapter feedAdapter;

    private List<Object> dataList;

    private volatile static int id = 0;

    private TapAdNative tapAdNative;

    private volatile boolean fetched = false;

    private volatile boolean isFetching = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!fetched) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFeedsAndFeedAd();
                    }
                }, 200);
                fetched = true;
            }
            if (tapAdNative != null) tapAdNative.resume();
        } else {
            if (tapAdNative != null) tapAdNative.pause();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed1, container, false);
        initRecyclerView(view);
        Activity activity = ActivityUtils.getActivity(getContext());
        tapAdNative = TapAdManager.get().createAdNative(activity);
        if (!fetched) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadFeedsAndFeedAd();
                }
            }, 200);

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tapAdNative != null) tapAdNative.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tapAdNative != null) tapAdNative.resume();
    }

    private void initRecyclerView(View view) {
        Activity activity = ActivityUtils.getActivity(getContext());
        feedRecyclerView = view.findViewById(R.id.normalFeedRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        feedRecyclerView.setLayoutManager(layoutManager);
        dataList = new ArrayList<>();
        feedAdapter = new FeedAdapter(getContext(), dataList);
        feedRecyclerView.setAdapter(feedAdapter);
        feedRecyclerView.setLoadMoreListener(new ILoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadFeedsAndFeedAd();
            }
        });

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(feedRecyclerView.getContext(),
//                layoutManager.getOrientation());
//        feedRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private int getColorRandom(int position) {
        if (position % 2== 0 ) {
            return Color.parseColor("#ff33b5e5");
        } else {
            return Color.parseColor("#ffaa66cc");
        }
    }

    private synchronized List<Object> constructNoteFeedList(int feedSize) {
        List<Object> resultList = new ArrayList<>();
        for (int i = 0; i < CARD_SIZE - feedSize; i ++) {
            id = id + 1;
            String title = String.format(Locale.US,"笔记:# %d", id);
            String description = String.format(Locale.US, "关于 笔记# %d 的描述", id);
            resultList.add(new NoteRecyclerViewItem(title, description, getColorRandom(i)));
        }
        return resultList;
    }

    private synchronized void loadFeedsAndFeedAd() {
        if (isFetching) return;
        isFetching = true;
        final Activity activity = getActivity();
        if (activity == null || activity.isDestroyed()) return;
        final List<? super RecyclerViewItem> newElementItems = constructNoteFeedList(8);

        tapAdNative.loadFeedAd(new AdRequest.Builder()
                .withSpaceId(1000432).build()
                , new TapAdNative.FeedAdListener() {
            @Override
            public void onFeedAdLoad(List<TapFeedAd> tapFeedAd) {
                if (feedRecyclerView != null) {
                    feedRecyclerView.setLoadingFinish();
                }
                List<? super RecyclerViewItem> resultItems = new ArrayList<>();
                int halfIndex = tapFeedAd.size() / 2;
                for (int i = 0 ; i < tapFeedAd.size(); i ++) {
                    TapFeedAd feedAd = tapFeedAd.get(i);
                    if (i < halfIndex) {
                        resultItems.add(new SmallAdRecyclerViewItem(feedAd));
                    } else {
                        resultItems.add(new AdRecyclerViewItem(feedAd));
                    }
                }
                for (int i = 0; i < newElementItems.size() ; i++) {
                    resultItems.add((NoteRecyclerViewItem)newElementItems.get(i));
                }

                dataList.addAll(resultItems);

                feedRecyclerView.getAdapter().notifyDataSetChanged();

                Toast.makeText(activity, "获取广告成功:" + tapFeedAd.size() + "条"
                        , Toast.LENGTH_SHORT).show();
                isFetching = false;
            }

            @Override
            public void onError(int var1, String var2) {
                if (feedRecyclerView != null) {
                    feedRecyclerView.setLoadingFinish();
                }
                feedRecyclerView.getAdapter().notifyDataSetChanged();
                Activity activity = getActivity();
                if (null != activity && !activity.isDestroyed()){
                    Toast.makeText(activity
                            , "获取广告失败:code:" + var1 + ",message:" + var2, Toast.LENGTH_SHORT).show();
                }
                isFetching = false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tapAdNative != null) {
            tapAdNative.pause();
            tapAdNative.dispose();
        }
        fetched = false;
    }
}

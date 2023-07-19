package com.tapsdk.tapad.addemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;


public class LoadMoreRecyclerView extends RecyclerView {


    private boolean isLoading = false;

    private ILoadMoreListener loadMoreListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView == null || recyclerView.getLayoutManager() == null) {
                    return;
                }
                if (SCROLL_STATE_IDLE == newState && !isLoading) {
                    int lastCompletePosition = -1;
                    LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                        int[] lastPos = manager.findLastCompletelyVisibleItemPositions(new int[manager.getSpanCount()]);
                        int maxVal = Integer.MIN_VALUE;
                        for (int pos : lastPos) {
                            if (pos > maxVal) {
                                maxVal = pos;
                            }
                        }
                        lastCompletePosition = maxVal;
                    }

                    if (lastCompletePosition == layoutManager.getItemCount() - 1) {
                        if (loadMoreListener != null) {
                            isLoading = true;
                            loadMoreListener.onLoadMore();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout != null && getAdapter() != null) {
            getAdapter().onAttachedToRecyclerView(this);//手动调用下，否则加载更多异常
        }
    }

    public ILoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(ILoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setLoadingFinish() {
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }
}

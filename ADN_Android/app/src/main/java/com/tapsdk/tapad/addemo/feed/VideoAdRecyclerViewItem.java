package com.tapsdk.tapad.addemo.feed;

import com.tapsdk.tapad.TapFeedAd;

public class VideoAdRecyclerViewItem extends RecyclerViewItem {
    public final TapFeedAd tapFeedAd;

    public VideoAdRecyclerViewItem(TapFeedAd ad) {
        this.tapFeedAd = ad;
    }
}

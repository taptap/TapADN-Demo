package com.tapsdk.tapad.addemo.feed;

import com.tapsdk.tapad.TapFeedAd;

public class AdRecyclerViewItem extends RecyclerViewItem {
    public final TapFeedAd tapFeedAd;

    public AdRecyclerViewItem(TapFeedAd ad) {
        this.tapFeedAd = ad;
    }
}

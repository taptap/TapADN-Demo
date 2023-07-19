package com.tapsdk.tapad.addemo.feed;

public class NoteRecyclerViewItem extends RecyclerViewItem {

    String title;

    String description;

    int colorRes;

    public NoteRecyclerViewItem(String title, String description, int colorRes) {
        this.title = title;
        this.description = description;
        this.colorRes = colorRes;
    }
}

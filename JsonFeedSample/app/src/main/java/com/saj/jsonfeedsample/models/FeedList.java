package com.saj.jsonfeedsample.models;

import java.util.List;

/**
 * Created by thekkesa on 10/02/2015.
 */
public class FeedList {

    private String title;

    private List<FeedItem> feedItemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FeedItem> getFeedItemList() {
        return feedItemList;
    }

    public void setFeedItemList(List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
    }
}

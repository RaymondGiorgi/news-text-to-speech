package com.raymondgiorgi.newstexttospeech;

/**
 * Created by no-vivisimo on 3/23/2016.
 */
public class FeedObject {
    private String title;
    private String url;
    public FeedObject(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }
}

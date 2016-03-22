package com.raymondgiorgi.newstexttospeech;

import android.app.Activity;
import android.os.Bundle;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by no-vivisimo on 3/20/2016.
 */
public class FeedSelectorActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
        FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
        SyndFeed feed = null;
        try {
            feed = feedFetcher.retrieveFeed(new URL("http://blogs.sun.com/roller/rss/pat"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FetcherException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }
        System.out.println(feed);
    }
}
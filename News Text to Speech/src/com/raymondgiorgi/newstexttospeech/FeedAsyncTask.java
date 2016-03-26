package com.raymondgiorgi.newstexttospeech;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by no-vivisimo on 3/22/2016.
 */
public class FeedAsyncTask extends AsyncTask<String, Void, List<FeedObject>> {
    private FeedListener feedListener;
    public FeedAsyncTask(FeedListener feedListener) {
        this.feedListener = feedListener;
    }

    @Override
    protected List<FeedObject> doInBackground(String... params) {
        List<FeedObject> retval = new LinkedList<FeedObject>();

        FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
        FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
        SyndFeed feed = null;
        try {
            List<String> titles = new LinkedList<String>();
            feed = feedFetcher.retrieveFeed(new URL(params[0]));
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                String title = entry.getTitle();
                String url = entry.getLink();
                retval.add(new FeedObject(title, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FetcherException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }
        return retval;
    }

    @Override
    protected void onPostExecute(List<FeedObject> list) {
        this.feedListener.alertFeedDone(list);
    }
}

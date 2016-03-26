package com.raymondgiorgi.newstexttospeech;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by no-vivisimo on 3/20/2016.
 */
public class FeedSelectorActivity extends Activity implements FeedListener {
    private TextToSpeech textToSpeech;
    private ListView lv;
    private List<String> urls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.feed_selector);
        this.lv = (ListView) this.findViewById(R.id.feed_selector_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (textToSpeech != null) {
                    if (textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                        textToSpeech.shutdown();
                    }
                    String url = urls.get(position);
                    URLConnection urlConnection = null;
                    try {
                        urlConnection = new URL(url).openConnection();

                        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                        urlConnection.connect();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuffer sb = new StringBuffer();
                        String inputLine;
                        while ((inputLine = reader.readLine()) != null) {
                            sb.append(inputLine);
                        }
                        if (sb.toString() != null) {
                            StringBuffer toRead = new StringBuffer();
                            Document doc = Jsoup.parse(sb.toString());
                            Elements elements = doc.select("div[class=pf-content]>p");
                            for (Element element : elements) {
                                toRead.append(element.text());
                                toRead.append("\r\n");
                            }
                            textToSpeech.speak(toRead.toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        new FeedAsyncTask(this).execute("http://feeds.feedburner.com/NakedCapitalism?format=xml");
    }

    @Override
    public void alertFeedDone(List<FeedObject> list) {
        List<String> titles = new LinkedList<String>();
        for (FeedObject fo : list) {
            titles.add(fo.getTitle());
            urls.add(fo.getUrl());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(arrayAdapter);
    }
}
package com.noverish.restapi.twitter;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-09-27.
 */
public class TwitterClient {
    private Context context;
    private HtmlParseWebView webView;

    private TwitterClientCallback twitterClientCallback;

    private boolean isNewerVersion;
    private boolean isLogined;

    private static TwitterClient instance;
    public static TwitterClient getInstance() {
        if(instance == null)
            instance = new TwitterClient();

        return instance;
    }

    private TwitterClient() {}

    public void setData(HtmlParseWebView webView) {
        this.webView = webView;
        this.context = webView.getContext();
    }

    public void reload() {
        webView.loadUrl("https://mobile.twitter.com/");
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                Document document = Jsoup.parse(htmlCode);

                if(document.body().classNames().contains("AppPage-body")) {
                    Log.i("Twitter","Newer Version");

                    isNewerVersion = true;
                    isLogined = document.select("div._1eF_MiFx").size() != 0;
                } else {
                    Log.i("Twitter","Older Version");

                    isNewerVersion = false;
                    isLogined = document.select("table.tweet").size() != 0;
                }

                Log.i("Twitter","isLogined - " + isLogined);

                if(isLogined) {
                    if(twitterClientCallback != null)
                        twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(webView.getHtmlCode()));
                } else {
                    if(twitterClientCallback != null)
                        twitterClientCallback.onNotLogin();
                }
            }
        });
    }

    public void setTwitterClientCallback(TwitterClientCallback twitterClientCallback) {
        this.twitterClientCallback = twitterClientCallback;
    }

    public void loadNextPage() {
        webView.loadUrl("https://mobile.twitter.com" + TwitterHtmlProcessor.nextPageUrl);
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(twitterClientCallback != null)
                    twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(webView.getHtmlCode()));
            }
        });
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public interface TwitterClientCallback {
        void onSuccess(ArrayList<TwitterArticleItem> items);

        void onNotLogin();

        void onFailure();
    }
}

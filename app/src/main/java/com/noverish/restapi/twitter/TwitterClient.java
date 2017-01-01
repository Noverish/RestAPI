package com.noverish.restapi.twitter;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;
import com.noverish.restapi.webview.OnPageFinishedListener;

import org.jsoup.Jsoup;

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
    private String userScreenName;

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
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(Jsoup.parse(htmlCode).select("html[class=\"AppPage CorePage\"]").size() == 0) {
                    Log.i("<twitter login>","Twitter login");

                    isLogined = true;
                    if(twitterClientCallback != null)
                        twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(htmlCode));
                } else {
                    Log.i("<twitter login>","Twitter not login");

                    isLogined = false;
                    if(twitterClientCallback != null)
                        twitterClientCallback.onNotLogin();
                }
            }
        };
        webView.loadUrl("https://mobile.twitter.com/home", loaded, null, null);
    }

    public void setTwitterClientCallback(TwitterClientCallback twitterClientCallback) {
        this.twitterClientCallback = twitterClientCallback;
    }

    public void loadNextPage() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(twitterClientCallback != null)
                    twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(webView.getLastParsedHtml()));
            }
        };
        webView.loadUrl("https://mobile.twitter.com" + TwitterHtmlProcessor.nextPageUrl, loaded, null, null);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public void getNotification(OnHtmlLoadSuccessListener listener) {
        OnPageFinishedListener finished =  new OnPageFinishedListener() {
            @Override
            public void onPageFinished(HtmlParseWebView webView, String url) {
                webView.goBack();
            }
        };
        webView.loadUrl("https://mobile.twitter.com/i/connect", listener, null, finished);
    }

    public interface TwitterClientCallback {
        void onSuccess(ArrayList<TwitterArticleItem> items);

        void onNotLogin();

        void onFailure();
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }
}

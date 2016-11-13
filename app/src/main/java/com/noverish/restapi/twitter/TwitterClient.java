package com.noverish.restapi.twitter;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;
import com.noverish.restapi.webview.OnPageFinishedListener;

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
        webView.setOnHtmlLoadSuccessListener(false, new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                Document document = Jsoup.parse(htmlCode);

                if(document.select("[class=\"_2tOLusnc _2ZBD52R7\"]").size() > 0) { //Post Button
                    System.out.println("Twitter - Newer Version, Logged in");
                    isNewerVersion = true;
                    isLogined = true;
                } else if(document.body().classNames().contains("AppPage-body")) { //Body Class Name
                    System.out.println("Twitter - Newer Version, Logged out");
                    isNewerVersion = true;
                    isLogined = false;
                } else {
                    isNewerVersion = false;
                    isLogined = document.select("table.tweet").size() != 0;

                    Log.i("Twitter","Older Version");
                    Log.i("Twitter","isLogined - " + isLogined);
                }

                if(isLogined) {
                    if(twitterClientCallback != null)
                        if(isNewerVersion)
                            twitterClientCallback.onSuccess(TwitterHtmlProcessor.processNew(webView.getHtmlCode()));
                        else
                            twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(webView.getHtmlCode()));
                } else {
                    if(twitterClientCallback != null)
                        twitterClientCallback.onNotLogin();
                }
            }
        });
        webView.setExtractHtmlWhenPageFinished(true);
        webView.loadUrl("https://mobile.twitter.com/");
    }

    public void setTwitterClientCallback(TwitterClientCallback twitterClientCallback) {
        this.twitterClientCallback = twitterClientCallback;
    }

    public void loadNextPage() {
        webView.setOnHtmlLoadSuccessListener(true, new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(twitterClientCallback != null)
                    twitterClientCallback.onSuccess(TwitterHtmlProcessor.process(webView.getHtmlCode()));
            }
        });
        webView.setExtractHtmlWhenPageFinished(true);
        webView.loadUrl("https://mobile.twitter.com" + TwitterHtmlProcessor.nextPageUrl);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public void getNotification(OnHtmlLoadSuccessListener listener) {
        webView.setOnHtmlLoadSuccessListener(true, listener);
        webView.setOnPageFinishedListener(true, new OnPageFinishedListener() {
            @Override
            public void onPageFinished(HtmlParseWebView webView, String url) {
                webView.goBack();
            }
        });
        webView.setExtractHtmlWhenPageFinished(true);
        webView.loadUrl("https://mobile.twitter.com/i/connect");
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

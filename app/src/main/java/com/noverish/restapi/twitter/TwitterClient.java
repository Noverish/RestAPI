package com.noverish.restapi.twitter;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-09-27.
 */
public class TwitterClient {
    private TwitterClientCallback twitterClientCallback;
    private HtmlParseWebView webView;
    private Context context;
    private boolean isNewerVersion;
    private boolean isLogined;
    private String userScreenName;
    private ArrayList<TwitterArticleItem> showedItems = new ArrayList<>();

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
        showedItems.clear();
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(Jsoup.parse(htmlCode).select("html[class=\"AppPage CorePage\"]").size() == 0) {
                    Log.i("<twitter login>","Twitter login");

                    ArrayList<TwitterArticleItem> newItems = TwitterHtmlProcessor.process(htmlCode);
                    newItems.removeAll(showedItems);
                    showedItems.addAll(newItems);

                    isLogined = true;
                    if(twitterClientCallback != null)
                        twitterClientCallback.onSuccess(newItems);
                } else {
                    Log.i("<twitter login>","Twitter not login");

                    isLogined = false;
                    if(twitterClientCallback != null)
                        twitterClientCallback.onNotLogin();
                }
            }
        };
        webView.loadUrl("https://mobile.twitter.com/home", loaded, null, null, HtmlParseWebView.SNSType.Twitter);
    }

    public void setTwitterClientCallback(TwitterClientCallback twitterClientCallback) {
        this.twitterClientCallback = twitterClientCallback;
    }

    public void loadNextPage() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<TwitterArticleItem> newItems = TwitterHtmlProcessor.process(htmlCode);
                newItems.removeAll(showedItems);
                showedItems.addAll(newItems);

                if(twitterClientCallback != null)
                    twitterClientCallback.onSuccess(newItems);
            }
        };
        webView.scrollBottom(loaded);
    }

    public void post(final String content) {
        webView.loadUrl("https://mobile.twitter.com/compose/tweet", new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView1, String htmlCode) {
                webView.setContentInTextArea(content, "_2wjpwbis _1YGC8xFq _2RmultvD _1VqMahaT _2Z8UymHS", new Runnable() {
                    @Override
                    public void run() {
                        webView.clickButton("MmJh82_T _1xFtK706 SpbPGaHr _2Rz0TobF", null);
                    }
                });
            }
        }, null, null, HtmlParseWebView.SNSType.Twitter);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public void getNotification(final TwitterNotificationCallback callback) {
        OnHtmlLoadSuccessListener listener = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                webView.goBack();

                callback.onSuccess(TwitterHtmlProcessor.processNotification(htmlCode));
            }
        };
        webView.loadUrl("https://mobile.twitter.com/notifications", listener, null, null, HtmlParseWebView.SNSType.Twitter);
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


    public interface TwitterArticleCallback {
        void onSuccess(ArrayList<TwitterArticleItem> items);
        void onNotLogin();
    }

    public interface TwitterNotificationCallback {
        void onSuccess(ArrayList<TwitterNotificationItem> items);
        void onNotLogin();
    }
}

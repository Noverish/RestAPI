package com.noverish.restapi.facebook;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-09-27.
 */
public class FacebookClient {
    private FacebookArticleCallback callback;
    private Context context;
    private HtmlParseWebView webView;
    private boolean isLogined;


    private ArrayList<FacebookArticleItem> showedItems = new ArrayList<>();
    
    private static FacebookClient instance;
    public static FacebookClient getInstance() {
        if(instance == null)
            instance = new FacebookClient();
        
        return instance;
    }
    
    private FacebookClient() {}
    
    public void setWebView(HtmlParseWebView webView) {
        this.webView = webView;
        this.context = webView.getContext();
    }

    public void setCallback(FacebookArticleCallback callback) {
        this.callback = callback;
    }

    public void loadFirstPage() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if (Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0) {
                    Log.i("<facebook load first>","Facebook login");

                    ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(htmlCode);
                    newItems.removeAll(showedItems);
                    showedItems.addAll(newItems);

                    isLogined = true;
                    if(callback != null)
                        callback.onSuccess(newItems);
                } else {
                    Log.i("<facebook load first>","Facebook not login");

                    isLogined = false;
                    if(callback != null)
                        callback.onNotLogin();
                }
            }
        };
        webView.loadUrl(context.getString(R.string.facebook_url), loaded, null, null, HtmlParseWebView.SNSType.Facebook);
    }

    public void loadNextPage() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if (Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0) {
                    Log.i("<facebook load next>","Facebook login");

                    ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(htmlCode);
                    newItems.removeAll(showedItems);
                    showedItems.addAll(newItems);

                    isLogined = true;
                    if(callback != null)
                        callback.onSuccess(newItems);
                } else {
                    Log.i("<facebook load next>","Facebook not login");

                    isLogined = false;
                    if(callback != null)
                        callback.onNotLogin();
                }
            }
        };
        webView.scrollBottom(loaded);
    }

    public void getNotification(final FacebookNotificationCallback callback) {
        OnHtmlLoadSuccessListener listener = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                webView.goBack();

                callback.onSuccess(FacebookHtmlCodeProcessor.processNotification(htmlCode));
            }
        };
        webView.loadUrl("https://m.facebook.com/notifications.php?more", listener, null, null, HtmlParseWebView.SNSType.Facebook);
    }

    public void post(String content) {
        webView.setContentInTextArea(content, "_1svy _2ya3 _3jce _26wa", new Runnable() {
            @Override
            public void run() {
                webView.clickButton("_54k8 _56bs _56bu", new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("clicked");
                    }
                });
            }
        });
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public interface FacebookArticleCallback {
        void onSuccess(ArrayList<FacebookArticleItem> items);
        void onNotLogin();
    }

    public interface FacebookNotificationCallback {
        void onSuccess(ArrayList<FacebookNotificationItem> items);
        void onNotLogin();
    }
}

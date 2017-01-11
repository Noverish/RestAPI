package com.noverish.restapi.facebook;

import android.content.Context;
import android.util.Log;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;
import com.noverish.restapi.webview.OnPageFinishedListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-09-27.
 */
public class FacebookClient {
    private Context context;
    private HtmlParseWebView webView;
    private boolean isLogined;

    private FacebookClientCallback facebookClientCallback;

    private ArrayList<FacebookArticleItem> showedItems = new ArrayList<>();
    
    private static FacebookClient instance;
    public static FacebookClient getInstance() {
        if(instance == null)
            instance = new FacebookClient();
        
        return instance;
    }
    
    private FacebookClient() {}
    
    public void setData(HtmlParseWebView webView) {
        this.webView = webView;
        this.context = webView.getContext();
    }

    public void reload() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if (Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0) {
                    Log.i("<facebook login>","Facebook login");

                    ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(htmlCode);
                    newItems.removeAll(showedItems);
                    showedItems.addAll(newItems);

                    isLogined = true;
                    if(facebookClientCallback != null)
                        facebookClientCallback.onSuccess(newItems);
                } else {
                    Log.i("<facebook login>","Facebook login");

                    isLogined = false;
                    if(facebookClientCallback != null)
                        facebookClientCallback.onNotLogin();
                }
            }
        };
        webView.loadUrl(context.getString(R.string.facebook_url), loaded, null, null, HtmlParseWebView.SNSType.Facebook);
    }

    public void loadNextPage() {
        OnHtmlLoadSuccessListener loaded = new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(htmlCode);

                for(int i = 0;i<newItems.size();i++) {
                    FacebookArticleItem item = newItems.get(i);

                    if (showedItems.contains(item)) {
                        newItems.remove(i);
                        i--;
                    } else {
                        showedItems.add(item);
                    }
                }

                if(facebookClientCallback != null)
                    facebookClientCallback.onSuccess(newItems);
            }
        };
        webView.scrollBottom(loaded);
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

    public void setFacebookClientCallback(FacebookClientCallback facebookClientCallback) {
        this.facebookClientCallback = facebookClientCallback;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public void getNotification(OnHtmlLoadSuccessListener loaded) {
        OnPageFinishedListener finished = new OnPageFinishedListener() {
            @Override
            public void onPageFinished(HtmlParseWebView webView, String url) {
                webView.goBack();
            }
        };
        webView.loadUrl("https://m.facebook.com/notifications.php?more", loaded, null, finished, HtmlParseWebView.SNSType.Facebook);
    }

    public interface FacebookClientCallback {
        void onSuccess(ArrayList<FacebookArticleItem> items);

        void onNotLogin();

        void onFailure();
    }
}

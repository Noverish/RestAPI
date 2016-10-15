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
    private Context context;
    private HtmlParseWebView webView;
    private boolean isLogined;

    private FacebookClientCallback facebookClientCallback;

    private ArrayList<FacebookArticleItem> items;
    
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
        webView.loadUrl(context.getString(R.string.facebook_url));
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                isLogined = Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0;
                Log.i("facebook","isLogined - " + isLogined);

                if (isLogined) {
                    items = FacebookHtmlCodeProcessor.process(webView.getHtmlCode());
                    if(facebookClientCallback != null)
                        facebookClientCallback.onSuccess(items);
                } else {
                    if(facebookClientCallback != null)
                        facebookClientCallback.onNotLogin();
                }
            }
        });
    }

    public void loadNextPage() {
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(webView.getHtmlCode());

                for(FacebookArticleItem item : items) {
                    System.out.println("exist Item : " + item.getArticleId() + " " + item.getTitle());
                }

                for(int i = 0;i<newItems.size();i++) {
                    FacebookArticleItem item = newItems.get(i);

                    System.out.println("new Item : " + item.getArticleId() + " " + item.getTitle());

                    if (items.contains(item)) {
                        newItems.remove(i);
                        i--;
                        System.out.println("contain");
                    } else {
                        items.add(item);
                        System.out.println("not contain");
                    }
                }

                if(facebookClientCallback != null)
                    facebookClientCallback.onSuccess(newItems);
            }
        });
        webView.scrollBottom();
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

    public interface FacebookClientCallback {
        void onSuccess(ArrayList<FacebookArticleItem> items);

        void onNotLogin();

        void onFailure();
    }
}

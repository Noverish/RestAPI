package com.noverish.restapi.facebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.activity.SettingActivity;
import com.noverish.restapi.activity.LoginManageActivity;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Noverish on 2016-09-27.
 */
public class FacebookClient {
    private Context context;
    private android.os.Handler handler;
    private HtmlParseWebView webView;
    private LinearLayout layout;
    private boolean isLogined;

    private ArrayList<FacebookArticleItem> items;
    
    private static FacebookClient instance;
    public static FacebookClient getInstance() {
        if(instance == null)
            instance = new FacebookClient();
        
        return instance;
    }
    
    private FacebookClient() {}
    
    public void setData(HtmlParseWebView webView, LinearLayout layout, android.os.Handler handler) {
        this.webView = webView;
        this.context = webView.getContext();
        this.layout = layout;
        this.handler = handler;

        webView.loadUrl(context.getString(R.string.facebook_url));
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                isLogined = Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0;
                Log.i("facebook","isLogined - " + isLogined);

                if (isLogined) {
                    loadFirstPage();
                } else {
                    context.startActivity(new Intent(context, SettingActivity.class));
                    context.startActivity(new Intent(context, LoginManageActivity.class));
                    context.startActivity(new Intent(context, FacebookLoginWebViewActivity.class));
                }
            }
        });

    }

    private void loadFirstPage() {
        items = FacebookHtmlCodeProcessor.process(webView.getHtmlCode());

        for(FacebookArticleItem item : items) {
            layout.addView(new FacebookArticleView(context, item));
        }
    }

    public void loadNextPage() {
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(webView.getHtmlCode());

                Iterator<FacebookArticleItem> iterator = newItems.iterator();
                while(iterator.hasNext()) {
                    FacebookArticleItem item = iterator.next();
                    if (items.contains(item)) {
                        iterator.remove();
                    } else {
                        items.add(item);
                    }
                }

                for(FacebookArticleItem item : newItems) {
                    layout.addView(new FacebookArticleView(context, item));
                }
            }
        });
        webView.scrollBottom();
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }
}

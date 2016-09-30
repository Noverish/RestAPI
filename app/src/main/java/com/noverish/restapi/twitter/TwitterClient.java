package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import com.noverish.restapi.activity.SettingActivity;
import com.noverish.restapi.activity.LoginManageActivity;
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
    private android.os.Handler handler;
    private HtmlParseWebView webView;
    private LinearLayout layout;

    private boolean isNewerVersion;
    private boolean isLogined;

    private static TwitterClient instance;
    public static TwitterClient getInstance() {
        if(instance == null)
            instance = new TwitterClient();

        return instance;
    }

    private TwitterClient() {}

    public void setData(HtmlParseWebView webView, LinearLayout layout, android.os.Handler handler) {
        this.webView = webView;
        this.context = webView.getContext();
        this.layout = layout;
        this.handler = handler;

        webView.loadUrl("https://mobile.twitter.com/");
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
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

                if(isLogined) {
                    loadFirstPage();
                } else {
                    context.startActivity(new Intent(context, SettingActivity.class));
                    context.startActivity(new Intent(context, LoginManageActivity.class));
                    context.startActivity(new Intent(context, TwitterLoginWebViewActivity.class));
                }
            }
        });
    }

    private void loadFirstPage() {
        ArrayList<TwitterArticleItem> items = TwitterHtmlProcessor.process(webView.getHtmlCode());

        for(TwitterArticleItem item : items) {
            layout.addView(new TwitterArticleView(context, item, handler));
        }
    }

    public void loadNextPage() {
        webView.loadUrl("https://mobile.twitter.com" + TwitterHtmlProcessor.nextPageUrl);
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                ArrayList<TwitterArticleItem> items = TwitterHtmlProcessor.process(webView.getHtmlCode());

                for(TwitterArticleItem item : items) {
                    layout.addView(new TwitterArticleView(context, item, handler));
                }
            }
        });
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }
}

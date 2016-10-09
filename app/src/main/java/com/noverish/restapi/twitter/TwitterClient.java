package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noverish.restapi.activity.LoginManageActivity;
import com.noverish.restapi.activity.SettingActivity;
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

    private OnTwitterLoadedListener onTwitterLoadedListener;

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

                if(isLogined) {
                    if(onTwitterLoadedListener != null)
                        onTwitterLoadedListener.onTwitterLoaded(TwitterHtmlProcessor.process(webView.getHtmlCode()));
                } else {
                    context.startActivity(new Intent(context, SettingActivity.class));
                    context.startActivity(new Intent(context, LoginManageActivity.class));
                    context.startActivity(new Intent(context, TwitterLoginWebViewActivity.class));
                }
            }
        });
    }

    public void setOnTwitterLoadedListener(OnTwitterLoadedListener onTwitterLoadedListener) {
        this.onTwitterLoadedListener = onTwitterLoadedListener;
    }

    public void loadNextPage() {
        webView.loadUrl("https://mobile.twitter.com" + TwitterHtmlProcessor.nextPageUrl);
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                if(onTwitterLoadedListener != null)
                    onTwitterLoadedListener.onTwitterLoaded(TwitterHtmlProcessor.process(webView.getHtmlCode()));
            }
        });
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public interface OnTwitterLoadedListener {
        void onTwitterLoaded(ArrayList<TwitterArticleItem> items);
    }
}

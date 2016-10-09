package com.noverish.restapi.facebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noverish.restapi.R;
import com.noverish.restapi.activity.LoginManageActivity;
import com.noverish.restapi.activity.SettingActivity;
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

    private OnFacebookLoadedListener onFacebookLoadedListener;

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
                    if(onFacebookLoadedListener != null)
                        onFacebookLoadedListener.onFacebookLoaded(items);
                } else {
                    context.startActivity(new Intent(context, SettingActivity.class));
                    context.startActivity(new Intent(context, LoginManageActivity.class));
                    context.startActivity(new Intent(context, FacebookLoginWebViewActivity.class));
                }
            }
        });
    }

    public void loadNextPage() {
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(webView.getHtmlCode());

                for(int i = 0;i<newItems.size();i++) {
                    FacebookArticleItem item = newItems.get(i);
                    if (items.contains(item)) {
                        newItems.remove(i);
                        i--;
                    } else {
                        items.add(item);
                    }
                }

                if(onFacebookLoadedListener != null)
                    onFacebookLoadedListener.onFacebookLoaded(newItems);
            }
        });
        webView.scrollBottom();
    }

    public void setOnFacebookLoadedListener(OnFacebookLoadedListener onFacebookLoadedListener) {
        this.onFacebookLoadedListener = onFacebookLoadedListener;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    public interface OnFacebookLoadedListener {
        void onFacebookLoaded(ArrayList<FacebookArticleItem> items);
    }
}

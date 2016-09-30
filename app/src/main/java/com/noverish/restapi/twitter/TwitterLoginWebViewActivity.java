package com.noverish.restapi.twitter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageStartedListener;

/**
 * Created by Noverish on 2016-09-14.
 */
public class TwitterLoginWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login_web_view);

        HtmlParseWebView webView = (HtmlParseWebView) findViewById(R.id.activity_facebook_login_web_view);

        if(TwitterWebViewClient.getInstance().isLogined()) {
            webView.loadUrl(getString(R.string.twitter_logout_url));
        } else {
            webView.loadUrl(getString(R.string.twitter_login_url));
            webView.setOnPageStartedListener(new OnPageStartedListener() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Log.d("twitter page started",url);
                    if(url.contains("https://mobile.twitter.com/")) {
                        Log.i("twitter login successed",url);
                        TwitterWebViewClient.getInstance().setLogined(true);
                        finish();
                    }
                }
            });
        }
    }
}

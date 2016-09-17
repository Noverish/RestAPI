package com.noverish.restapi.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageStartedListener;

/**
 * Created by Noverish on 2016-09-14.
 */
public class FacebookLoginWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login_web_view);

        HtmlParseWebView webView = (HtmlParseWebView) findViewById(R.id.activity_facebook_login_web_view);

        if(LoginDatabase.getInstance().isFacebookLogined()) {
            webView.loadUrl(getString(R.string.facebook_setting_url));
        } else {
            webView.loadUrl(getString(R.string.facebook_login_url));
            webView.setOnPageStartedListener(new OnPageStartedListener() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if(url.equals(getString(R.string.facebook_url))) {
                        LoginDatabase.getInstance().setFacebookLogined(true);
                        finish();
                    }
                }
            });
        }
    }
}
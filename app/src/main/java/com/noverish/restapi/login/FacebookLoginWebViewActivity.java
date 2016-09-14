package com.noverish.restapi.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.HtmlParseWebView;

/**
 * Created by Noverish on 2016-09-14.
 */
public class FacebookLoginWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login_web_view);

        HtmlParseWebView webView = (HtmlParseWebView) findViewById(R.id.activity_facebook_login_web_view);
        webView.loadUrl(getString(R.string.facebook_url));
    }
}

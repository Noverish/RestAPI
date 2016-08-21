package com.noverish.restapi.facebook;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookWebView extends Fragment {
    private WebView webView;

    private final String url = "http://www.facebook.com";
    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        webView = new WebView(getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        return webView;
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading : " + url);
            return false;
        }

        @Override
        public void onPageFinished(final WebView view, String url) {

        }
    }

    public void evaluateJavascript(String script, ValueCallback<String> resultCallBack) {
        webView.evaluateJavascript(script, resultCallBack);
    }
}

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

import com.noverish.restapi.other.OnHtmlLoadSuccessListener;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookWebView extends Fragment {
    private WebView webView;
    private String htmlCode;

    private static FacebookWebView instance;
    private OnHtmlLoadSuccessListener listener;

    private final String url = "https://m.facebook.com/?_rdr";
    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        instance = this;

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
        public void onPageFinished(final WebView view, String url) {
            Log.d("onPageFinished",url);
            extractHtml();
        }
    }

    public void extractHtml() {
        webView.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        htmlCode = html;

                        if(listener != null) {
                            listener.onHtmlLoadSuccess(html);
                        }
                    }
                });
    }

    public void setOnHtmlLoadSuccessListener(OnHtmlLoadSuccessListener listener) {
        this.listener = listener;
    }

    public void scrollBottom() {
        Log.d("scroll","bottom");
        webView.scrollBy(0, 10000);
    }

    public static FacebookWebView getInstance() {
        return instance;
    }
}

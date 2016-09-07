package com.noverish.restapi.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.noverish.restapi.other.OnHtmlLoadSuccessListener;

/**
 * Created by Noverish on 2016-09-07.
 */
public class HtmlParseWebView extends WebView {
    private OnHtmlLoadSuccessListener listener;
    private String url;

    public HtmlParseWebView(Context context) {
        super(context);
        init();
    }

    public HtmlParseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HtmlParseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE

        WebSettings webSettings = getSettings();
        webSettings.setBuiltInZoomControls(true);
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.
        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("onPageFinished",url);

            extractHtml();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    public void extractHtml() {
        Log.d("extractHtml","extractHtml");
        evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {

                        html = html.replaceAll("(\\\\){1,7}\"","\"");
                        html = html.replaceAll("&amp;","&");
                        html = html.replaceAll("(\\\\){0,2}&quot;","\"");
                        html = html.replaceAll("(\\\\){2,3}x3C", "<");
                        html = html.replaceAll("(\\\\){1,2}u003C","<");
                        html = html.replaceAll("(\\\\){1,2}u003E",">");
                        html = html.replaceAll("(\\\\){1,2}/","/");

                        if(listener != null) {
                            listener.onHtmlLoadSuccess(html);
                        }
                    }
                });
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        this.url = url;
    }

    public void setOnHtmlLoadSuccessListener(OnHtmlLoadSuccessListener listener) {
        this.listener = listener;
    }

    public void scrollBottom() {
        scrollBy(0, 10000);
    }

    public void refresh() {
        loadUrl(url);
    }
}

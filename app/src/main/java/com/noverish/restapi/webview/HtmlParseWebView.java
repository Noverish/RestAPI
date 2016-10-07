package com.noverish.restapi.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Noverish on 2016-09-07.
 */
public class HtmlParseWebView extends WebView {
    private OnHtmlLoadSuccessListener onHtmlLoadSuccessListener;
    private OnPageFinishedListener onPageFinishedListener;
    private OnPageStartedListener onPageStartedListener;
    private String originUrl;
    private String nowUrl;
    private String htmlCode;

    public HtmlParseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
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
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(onPageStartedListener != null)
                onPageStartedListener.onPageStarted(HtmlParseWebView.this, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("onPageFinished",url);

            nowUrl = url;

            if(onPageFinishedListener != null)
                onPageFinishedListener.onPageFinished(HtmlParseWebView.this, url);

            extractHtml();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            //Log.d("onLoadResource",url);
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

                        htmlCode = html;

                        if(onHtmlLoadSuccessListener != null)
                            onHtmlLoadSuccessListener.onHtmlLoadSuccess(HtmlParseWebView.this, html);
                    }
                });
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        this.originUrl = url;
    }

    public void setOnHtmlLoadSuccessListener(OnHtmlLoadSuccessListener listener) {
        this.onHtmlLoadSuccessListener = listener;
    }

    public void setOnPageFinishedListener(OnPageFinishedListener listener) {
        this.onPageFinishedListener = listener;
    }

    public void setOnPageStartedListener(OnPageStartedListener listener) {
        this.onPageStartedListener = listener;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public String getNowUrl() {
        return nowUrl;
    }

    public void scrollBottom() {
        scrollBy(0, 10000);
    }

    public void refresh() {
        loadUrl(originUrl);
    }
}

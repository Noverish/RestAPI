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
    private String htmlCode;

    private boolean extractHtmlWhenPageFinished = false;
    private boolean htmlLoadOnce = false;
    private boolean pageFinishedOnce = false;
    private boolean pageStartedOnce = false;

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

            if(onPageStartedListener != null) {
                onPageStartedListener.onPageStarted(HtmlParseWebView.this, url, favicon);
                if(pageStartedOnce) {
                    pageStartedOnce = false;
                    onPageStartedListener = null;
                }
            }
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("onPageFinished",url);

            if(onPageFinishedListener != null) {
                onPageFinishedListener.onPageFinished(HtmlParseWebView.this, url);
                if(pageFinishedOnce) {
                    pageFinishedOnce = false;
                    onPageFinishedListener = null;
                }
            }

            if(extractHtmlWhenPageFinished)
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

                        if(onHtmlLoadSuccessListener != null) {
                            onHtmlLoadSuccessListener.onHtmlLoadSuccess(HtmlParseWebView.this, html);
                            if(htmlLoadOnce) {
                                htmlLoadOnce = false;
                                onHtmlLoadSuccessListener = null;
                            }
                        }
                    }
                });
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    public void setOnHtmlLoadSuccessListener(boolean once, OnHtmlLoadSuccessListener listener) {
        this.htmlLoadOnce = once;
        this.onHtmlLoadSuccessListener = listener;
    }

    public void setOnPageFinishedListener(boolean once, OnPageFinishedListener listener) {
        this.pageFinishedOnce = once;
        this.onPageFinishedListener = listener;
    }

    public void setOnPageStartedListener(boolean once, OnPageStartedListener listener) {
        this.pageStartedOnce = once;
        this.onPageStartedListener = listener;
    }

    public void setExtractHtmlWhenPageFinished(boolean extractHtmlWhenPageFinished) {
        this.extractHtmlWhenPageFinished = extractHtmlWhenPageFinished;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void scrollBottom() {
        scrollTo(0, computeVerticalScrollRange());
    }
}

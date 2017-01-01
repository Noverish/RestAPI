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
    private android.os.Handler handler = new android.os.Handler();
    private String lastParsedHtml;

    public HtmlParseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; IM-A920S Build/KVT49L) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.85 Mobile Safari/537.36");
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.
        private OnHtmlLoadSuccessListener loaded;
        private OnPageStartedListener started;
        private OnPageFinishedListener finished;

        public Callback(OnHtmlLoadSuccessListener loaded, OnPageStartedListener started, OnPageFinishedListener finished) {
            this.loaded = loaded;
            this.started = started;
            this.finished = finished;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(started != null)
                started.onPageStarted(HtmlParseWebView.this, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("<onPageFinished>", url);

            if (finished != null)
                finished.onPageFinished(HtmlParseWebView.this, url);

            if (loaded != null) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    extractHtml(loaded);
                                }
                            });
                        } catch (Exception e) {

                        }
                    }
                })).start();
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            //Log.d("onLoadResource",url);
        }
    }

    private void extractHtml(final OnHtmlLoadSuccessListener listener) {
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

                        lastParsedHtml = html;

                        if(listener != null)
                            listener.onHtmlLoadSuccess(HtmlParseWebView.this, lastParsedHtml);
                    }
                });

    }

    public void loadUrl(String url, OnHtmlLoadSuccessListener loaded, OnPageStartedListener started, OnPageFinishedListener finished) {
        setWebViewClient(new Callback(loaded, started, finished));
        loadUrl(url);
    }

    public void scrollBottom(OnHtmlLoadSuccessListener loaded) {
        setWebViewClient(new Callback(loaded, null, null));
        scrollTo(0, computeVerticalScrollRange());
    }

    public void scrollBottom(final OnHtmlLoadSuccessListener loaded, final int delay) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollTo(0, computeVerticalScrollRange());
                    }
                });

                try {
                    Thread.sleep(delay);
                } catch (Exception e) {

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        extractHtml(loaded);
                    }
                });
            }
        })).start();
    }

    public String getLastParsedHtml() {
        return lastParsedHtml;
    }
}

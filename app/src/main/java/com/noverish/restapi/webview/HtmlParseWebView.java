package com.noverish.restapi.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Noverish on 2016-09-07.
 */
public class HtmlParseWebView extends WebView {
    public enum SNSType {Twitter, Facebook}
    private SNSType snsType;

    public HtmlParseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            init();
    }

    private void init() {
        getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; IM-A920S Build/KVT49L) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.85 Mobile Safari/537.36");
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(getContext())
                        .setTitle("AlertDialog")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });
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
//            Log.d("<onPageStarted>", url);

            if(started != null)
                started.onPageStarted(HtmlParseWebView.this, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("<onPageFinished>", url);

            if (finished != null)
                finished.onPageFinished(HtmlParseWebView.this, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
//            Log.d("<onLoadResource>", (snsType == SNSType.Twitter ? "twitter - " : "facebook - ") + url);

            HtmlParseAsyncTask.execute(HtmlParseWebView.this, loaded, snsType);
        }
    }

    public void loadUrl(String url, OnHtmlLoadSuccessListener loaded, OnPageStartedListener started, OnPageFinishedListener finished, SNSType snsType) {
        this.snsType = snsType;
        setWebViewClient(new Callback(loaded, started, finished));
        loadUrl(url);
    }

    public void scrollBottom(OnHtmlLoadSuccessListener loaded) {
        setWebViewClient(new Callback(loaded, null, null));
        scrollTo(0, computeVerticalScrollRange());
    }

    public void setContentInTextArea(String content, String classNames, final Runnable runnable) {
        setWebViewClient(new WebViewClient());
        evaluateJavascript("javascript:(function(){" +
                        "l=document.getElementsByClassName('" + classNames + "');" +
                        "l[0].value = '" + content + "';" +
                        "})()",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        if (runnable != null)
                            runnable.run();
                    }
                });
    }

    public void clickButton(String classNames, final Runnable runnable) {
        classNames = classNames.trim().replace(" ",".");
        setWebViewClient(new WebViewClient());
        evaluateJavascript("javascript:(function(){" +
                        "l=document.querySelectorAll('button." + classNames + "');" +
                        "l[0].removeAttribute('disabled');" +
//                        "alert(l[0].outerHTML);" +
                        "l[0].click();" +
                        "})()",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        if (runnable != null)
                            runnable.run();
                    }
                });
    }

    public void submitForm(String classNames, final Runnable runnable) {
        setWebViewClient(new WebViewClient());
        evaluateJavascript("javascript:(function(){" +
                        "l=document.getElementsByClassName('" + classNames + "');" +
//                        "alert(l[0]);" +
                        "l[0].submit();" +
                        "})()",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        if (runnable != null)
                            runnable.run();
                    }
                });
    }

    public void clearCallback() {
        setWebViewClient(new WebViewClient());
    }
}

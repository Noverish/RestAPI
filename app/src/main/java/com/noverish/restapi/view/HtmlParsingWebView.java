package com.noverish.restapi.view;

import android.support.v4.app.Fragment;
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

import com.noverish.restapi.http.ImagePicassoThread;
import com.noverish.restapi.kakao.ExtractHtmlThread;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class HtmlParsingWebView extends Fragment {
    private WebView webView;

    private static HtmlParsingWebView instance;
    private OnHtmlParsingSuccessListener listener;
    private ArrayList<ImagePicassoThread> threads = new ArrayList<>();
    private android.os.Handler handler = new android.os.Handler();

    private String url;
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

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        url = args.getString("url");
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.
        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("onPageFinished",url);

            if(!url.equals("https://story.kakao.com/")) {
                extractHtml();
            }

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            if(url.contains("kakao") && (url.contains(".jpg") || url.contains(".png"))) {
                ExtractHtmlThread.post(handler, new ExtractHtmlThread.CustomListener() {
                    @Override
                    public void listen() {
                        extractHtml();
                    }
                });
            }

            super.onLoadResource(view, url);
        }
    }



    public void extractHtml() {
        Log.d("extractHtml","extractHtml");
        webView.evaluateJavascript(
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
                            listener.onHtmlParsingSuccess(webView, html);
                        }
                    }
                });
    }

    public void setOnHtmlParsingSuccessListener(OnHtmlParsingSuccessListener listener) {
        this.listener = listener;
    }

    public void scrollBottom() {
        webView.scrollBy(0, 10000);
    }

    public static HtmlParsingWebView getInstance() {
        return instance;
    }

    public ArrayList<ImagePicassoThread> getThreads() {
        return threads;
    }

    public void refresh() {
        webView.loadUrl(url);
    }

    public interface OnHtmlParsingSuccessListener {
        void onHtmlParsingSuccess(WebView webView, String html);
    }
}

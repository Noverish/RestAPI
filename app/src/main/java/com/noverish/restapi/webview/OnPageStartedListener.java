package com.noverish.restapi.webview;

import android.graphics.Bitmap;

/**
 * Created by Noverish on 2016-09-14.
 */
public interface OnPageStartedListener {
    void onPageStarted(HtmlParseWebView webView, String url, Bitmap favicon);
}

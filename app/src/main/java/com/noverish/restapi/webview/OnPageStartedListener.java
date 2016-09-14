package com.noverish.restapi.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by Noverish on 2016-09-14.
 */
public interface OnPageStartedListener {
    void onPageStarted(WebView view, String url, Bitmap favicon);
}

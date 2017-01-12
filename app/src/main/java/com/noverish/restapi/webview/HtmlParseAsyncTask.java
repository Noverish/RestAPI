package com.noverish.restapi.webview;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.ValueCallback;

/**
 * Created by Noverish on 2017-01-10.
 */

public class HtmlParseAsyncTask extends AsyncTask<Void, Void, Void> {
    public static HtmlParseAsyncTask twitter;
    public static HtmlParseAsyncTask facebook;

    private OnHtmlLoadSuccessListener listener;
    private HtmlParseWebView webView;
    private HtmlParseWebView.SNSType snsType;

    private HtmlParseAsyncTask(HtmlParseWebView webView, OnHtmlLoadSuccessListener listener, HtmlParseWebView.SNSType snsType) {
        super();
        this.listener = listener;
        this.webView = webView;
        this.snsType = snsType;
    }

    public static void execute(HtmlParseWebView webView, OnHtmlLoadSuccessListener listener, HtmlParseWebView.SNSType SNSType) {
        HtmlParseAsyncTask htmlParseAsyncTask = new HtmlParseAsyncTask(webView, listener, SNSType);
        htmlParseAsyncTask.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(snsType == HtmlParseWebView.SNSType.Twitter) {
            if(twitter != null)
                twitter.cancel(true);
            twitter = this;
        } else if(snsType == HtmlParseWebView.SNSType.Facebook) {
            if(facebook != null)
                facebook.cancel(true);
            facebook = this;
        }
    }

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            Thread.sleep(2500);
        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        webView.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        Log.d("<html parse>",(snsType == HtmlParseWebView.SNSType.Twitter) ? "twitter" : "facebook");
                        webView.clearCallback();

                        html = html.replaceAll("(\\\\){1,7}\"","\"");
                        html = html.replaceAll("&amp;","&");
                        html = html.replaceAll("(\\\\){0,2}&quot;","\"");
                        html = html.replaceAll("(\\\\){2,3}x3C", "<");
                        html = html.replaceAll("(\\\\){1,2}u003C","<");
                        html = html.replaceAll("(\\\\){1,2}u003E",">");
                        html = html.replaceAll("(\\\\){1,2}/","/");

                        if(listener != null)
                            listener.onHtmlLoadSuccess(webView, html);
                    }
                });
    }
}

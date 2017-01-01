package com.noverish.restapi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.facebook.FacebookHtmlCodeProcessor;
import com.noverish.restapi.facebook.FacebookNotificationItem;
import com.noverish.restapi.facebook.FacebookNotificationView;
import com.noverish.restapi.twitter.TwitterArticleItem;
import com.noverish.restapi.twitter.TwitterArticleView;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.twitter.TwitterHtmlProcessor;
import com.noverish.restapi.twitter.TwitterNotificationItem;
import com.noverish.restapi.twitter.TwitterNotificationView;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-10-29.
 */

public class NotificationFragment extends Fragment {
    private TwitterClient twitterClient;
    private FacebookClient facebookClient;
    private LinearLayout layout;
    private android.os.Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification, container, false);

        layout = (LinearLayout) view.findViewById(R.id.activity_notification_layout);

        twitterClient = TwitterClient.getInstance();
        twitterClient.getNotification(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<TwitterNotificationItem> items = TwitterHtmlProcessor.processNotification(htmlCode);

                for(TwitterNotificationItem item : items) {
                    if(item.getType() == TwitterNotificationItem.ACTIVITY) {
                        layout.addView(new TwitterNotificationView(getActivity(), item));
                    } else if(item.getType() == TwitterNotificationItem.TWEET) {
                        TwitterArticleItem articleItem = item.toTwtiterArticleItem();
                        layout.addView(new TwitterArticleView(getActivity(), articleItem));
                    } else {
                        Log.e("ERROR","NotificationFragment.onHtmlLoadSuccess() : The type of this TwitterNotificationItem is unknown - " + item.getType());
                    }
                }
            }
        });

        facebookClient = FacebookClient.getInstance();
        facebookClient.getNotification(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                ArrayList<FacebookNotificationItem> items = FacebookHtmlCodeProcessor.processNotification(htmlCode);

                for(FacebookNotificationItem item : items) {
                    layout.addView(new FacebookNotificationView(getActivity(), item, handler));
                }
            }
        });

        return view;
    }
}

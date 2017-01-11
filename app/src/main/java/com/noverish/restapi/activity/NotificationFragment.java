package com.noverish.restapi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.facebook.FacebookNotificationItem;
import com.noverish.restapi.facebook.FacebookNotificationView;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.twitter.TwitterNotificationItem;
import com.noverish.restapi.twitter.TwitterNotificationView;

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
        twitterClient.getNotification(new TwitterClient.TwitterNotificationCallback(){
            @Override
            public void onSuccess(ArrayList<TwitterNotificationItem> items) {
                for(TwitterNotificationItem item : items) {
                    layout.addView(new TwitterNotificationView(getActivity(), item));
                }
            }

            @Override
            public void onNotLogin() {

            }
        });

        facebookClient = FacebookClient.getInstance();
        facebookClient.getNotification(new FacebookClient.FacebookNotificationCallback() {
            @Override
            public void onSuccess(ArrayList<FacebookNotificationItem> items) {
                for(FacebookNotificationItem item : items) {
                    layout.addView(new FacebookNotificationView(getActivity(), item, handler));
                }
            }

            @Override
            public void onNotLogin() {

            }
        });

        return view;
    }
}

package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.facebook.FacebookMessageItem;
import com.noverish.restapi.facebook.FacebookMessageView;
import com.noverish.restapi.other.GlobalProgressDialog;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.twitter.TwitterMessageItem;
import com.noverish.restapi.twitter.TwitterMessageView;

import java.util.ArrayList;

/**
 * Created by Noverish on 2017-01-11.
 */

public class MessageFragment extends Fragment {
    private LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        GlobalProgressDialog.showDialog(getActivity());

        layout = (LinearLayout) view.findViewById(R.id.fragment_message_layout);

        TwitterClient twitterClient = TwitterClient.getInstance();
        twitterClient.getMessage(new TwitterClient.TwitterMessageCallback() {
            @Override
            public void onSuccess(ArrayList<TwitterMessageItem> items) {
                for(TwitterMessageItem item : items) {
                    layout.addView(new TwitterMessageView(getActivity(), item));
                }

                GlobalProgressDialog.dismissDialog();
            }

            @Override
            public void onNotLogin() {

            }
        });

        FacebookClient facebookClient = FacebookClient.getInstance();
        facebookClient.getMessage(new FacebookClient.FacebookMessageCallback() {
            @Override
            public void onSuccess(ArrayList<FacebookMessageItem> items) {
                for(FacebookMessageItem item : items) {
                    layout.addView(new FacebookMessageView(getActivity(), item));
                }

                GlobalProgressDialog.dismissDialog();
            }

            @Override
            public void onNotLogin() {

            }
        });

        return view;
    }
}

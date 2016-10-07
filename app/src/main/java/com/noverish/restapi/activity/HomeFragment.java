package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookArticleItem;
import com.noverish.restapi.facebook.FacebookArticleView;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;
import com.noverish.restapi.webview.HtmlParseWebView;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class HomeFragment extends Fragment {
    private Runnable onFirstLoadFinishedRunnable;
    private boolean facebookFirstLoaded = false;
    private boolean twitterFirstLoaded = false;

    private TwitterClient twitterClient;
    private FacebookClient facebookClient;

    private ScrollBottomDetectScrollview scrollBottomDetectScrollview;
    private LinearLayout mainLayout;

    private android.os.Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        scrollBottomDetectScrollview = (ScrollBottomDetectScrollview) view.findViewById(R.id.fragment_home_scroll_view);
        scrollBottomDetectScrollview.setHandler(new CustomHandler());
        mainLayout = (LinearLayout) view.findViewById(R.id.fragment_home_layout_main);
        
        HtmlParseWebView facebookWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_facebook_web_view);
        HtmlParseWebView twitterWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_twitter_web_view);

        facebookClient = FacebookClient.getInstance();
        facebookClient.setData(facebookWebView);
        facebookClient.setOnFacebookLoadedListener(new FacebookClient.OnFacebookLoadedListener() {
            @Override
            public void onFacebookLoaded(ArrayList<FacebookArticleItem> items) {
                for(FacebookArticleItem item : items) {
                    mainLayout.addView(new FacebookArticleView(getActivity(), item));
                }
            }
        });

        twitterClient = TwitterClient.getInstance();
        twitterClient.setData(twitterWebView, mainLayout, handler);

        onFirstLoadFinishedRunnable.run();

        return view;
    }

    private class CustomHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i("ScrollView","bottom");

            twitterClient.loadNextPage();
            facebookClient.loadNextPage();
        }
    }

    public void setOnFirstLoadFinishedRunnable(Runnable onFirstLoadFinishedRunnable) {
        this.onFirstLoadFinishedRunnable = onFirstLoadFinishedRunnable;
    }
}

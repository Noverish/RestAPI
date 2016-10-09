package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookArticleItem;
import com.noverish.restapi.facebook.FacebookArticleView;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.twitter.TwitterArticleItem;
import com.noverish.restapi.twitter.TwitterArticleView;
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
        scrollBottomDetectScrollview.setRunnable(new Runnable() {
            @Override
            public void run() {
                twitterClient.loadNextPage();
                facebookClient.loadNextPage();
            }
        });

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

                if(twitterFirstLoaded)
                    onFirstLoadFinishedRunnable.run();
                else
                    facebookFirstLoaded = true;
            }
        });
        facebookClient.reload();

        twitterClient = TwitterClient.getInstance();
        twitterClient.setData(twitterWebView);
        twitterClient.setOnTwitterLoadedListener(new TwitterClient.OnTwitterLoadedListener() {
            @Override
            public void onTwitterLoaded(ArrayList<TwitterArticleItem> items) {
                for(TwitterArticleItem item : items) {
                    mainLayout.addView(new TwitterArticleView(getActivity(), item, handler));
                }

                if(facebookFirstLoaded)
                    onFirstLoadFinishedRunnable.run();
                else
                    twitterFirstLoaded = true;
            }
        });
        twitterClient.reload();

        return view;
    }

    public void setOnFirstLoadFinishedRunnable(Runnable onFirstLoadFinishedRunnable) {
        this.onFirstLoadFinishedRunnable = onFirstLoadFinishedRunnable;
    }
}

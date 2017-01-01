package com.noverish.restapi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.article.ArticleItem;
import com.noverish.restapi.facebook.FacebookArticleItem;
import com.noverish.restapi.facebook.FacebookArticleView;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.twitter.TwitterArticleItem;
import com.noverish.restapi.twitter.TwitterArticleView;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;
import com.noverish.restapi.webview.HtmlParseWebView;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class HomeFragment extends BaseFragment {
    private Runnable onFirstLoadFinishedRunnable;
    private boolean facebookFirstLoaded = false;
    private boolean twitterFirstLoaded = false;

    private TwitterClient twitterClient;
    private FacebookClient facebookClient;

    private ScrollBottomDetectScrollview scrollBottomDetectScrollview;
    private LinearLayout mainLayout;

    private ArrayList<ArticleItem> newArticleItems = new ArrayList<>();
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
        facebookClient.setFacebookClientCallback(new FacebookClient.FacebookClientCallback() {
            @Override
            public void onSuccess(ArrayList<FacebookArticleItem> items) {
                newArticleItems.addAll(items);

                facebookFirstLoaded = true;

                if(twitterFirstLoaded)
                    allLoaded();
            }

            @Override
            public void onNotLogin() {
                facebookFirstLoaded = true;

                if(twitterFirstLoaded)
                    allLoaded();
            }

            @Override
            public void onFailure() {

            }
        });
        facebookClient.reload();

        twitterClient = TwitterClient.getInstance();
        twitterClient.setData(twitterWebView);
        twitterClient.setTwitterClientCallback(new TwitterClient.TwitterClientCallback() {
            @Override
            public void onSuccess(ArrayList<TwitterArticleItem> items) {
                newArticleItems.addAll(items);

                twitterFirstLoaded = true;

                if(facebookFirstLoaded)
                    allLoaded();
            }

            @Override
            public void onNotLogin() {
                twitterFirstLoaded = true;

                if(facebookFirstLoaded)
                    allLoaded();
            }

            @Override
            public void onFailure() {

            }
        });
        twitterClient.reload();

        return view;
    }

    public void setOnFirstLoadFinishedRunnable(Runnable onFirstLoadFinishedRunnable) {
        this.onFirstLoadFinishedRunnable = onFirstLoadFinishedRunnable;
    }

    public void allLoaded() {
        if(onFirstLoadFinishedRunnable != null)
            onFirstLoadFinishedRunnable.run();

//        Collections.sort(newArticleItems, ArticleItem.comparator);

        for(ArticleItem item : newArticleItems) {
            try {
                if(item instanceof FacebookArticleItem) {
                    FacebookArticleItem facebookArticleItem = (FacebookArticleItem) item;
                    Log.d("facebook new item",item.getArticleId());
                    mainLayout.addView(new FacebookArticleView(getActivity(), facebookArticleItem));
                } else if(item instanceof TwitterArticleItem) {
                    TwitterArticleItem twitterArticleItem = (TwitterArticleItem) item;
                    Log.d("twitter new item",item.getArticleId());
                    mainLayout.addView(new TwitterArticleView(getActivity(), twitterArticleItem));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        twitterFirstLoaded = false;
        facebookFirstLoaded = false;
        newArticleItems.clear();
        scrollBottomDetectScrollview.stopLoading();
    }

    @Override
    public void onPostButtonClicked(String content) {

    }

    @Override
    public void onFreshButtonClicked() {
        mainLayout.removeAllViews();
        facebookClient.reload();
        twitterClient.reload();
    }
}

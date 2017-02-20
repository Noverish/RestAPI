package com.noverish.restapi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.base.ArticleItem;
import com.noverish.restapi.base.ArticleView;
import com.noverish.restapi.base.BaseFragment;
import com.noverish.restapi.facebook.FacebookArticleItem;
import com.noverish.restapi.facebook.FacebookArticleView;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.other.GlobalProgressDialog;
import com.noverish.restapi.rss.cnn.CNNAsyncTask;
import com.noverish.restapi.twitter.TwitterArticleItem;
import com.noverish.restapi.twitter.TwitterArticleView;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;

import static android.view.View.GONE;

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

        facebookClient = FacebookClient.getInstance();
        facebookClient.setCallback(new FacebookClient.FacebookArticleCallback() {
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
        });
        facebookClient.loadFirstPage();

        twitterClient = TwitterClient.getInstance();
        twitterClient.setCallback(new TwitterClient.TwitterArticleCallback() {
            @Override
            public void onSuccess(ArrayList<TwitterArticleItem> items) {
                System.out.println(items);
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
        });
        twitterClient.loadFirstPage();

        new CNNAsyncTask(mainLayout).execute();

        return view;
    }

    public void setOnFirstLoadFinishedRunnable(Runnable onFirstLoadFinishedRunnable) {
        this.onFirstLoadFinishedRunnable = onFirstLoadFinishedRunnable;
    }

    public void allLoaded() {
        Log.e("ASDF","ALL LOADED");

        GlobalProgressDialog.dismissDialog();

        if(onFirstLoadFinishedRunnable != null)
            onFirstLoadFinishedRunnable.run();

//        Collections.sort(newArticleItems, ArticleItem.comparator);

        for(ArticleItem item : newArticleItems) {
            try {
                if(item instanceof FacebookArticleItem) {
                    FacebookArticleItem facebookArticleItem = (FacebookArticleItem) item;
                    FacebookArticleView facebookArticleView = new FacebookArticleView(getActivity(), facebookArticleItem);

                    if(!MainActivity.instance.getNowCategory().equals("전체보기"))
                        facebookArticleView.setVisibility(GONE);

                    mainLayout.addView(facebookArticleView);
                } else if(item instanceof TwitterArticleItem) {
                    TwitterArticleItem twitterArticleItem = (TwitterArticleItem) item;
                    TwitterArticleView twitterArticleView = new TwitterArticleView(getActivity(), twitterArticleItem);

                    if(!MainActivity.instance.getNowCategory().equals("전체보기"))
                        twitterArticleView.setVisibility(GONE);

                    mainLayout.addView(twitterArticleView);
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
        facebookClient.loadFirstPage();
        twitterClient.loadFirstPage();
        GlobalProgressDialog.showDialog(getActivity());
    }

    @Override
    public void showArticleByCategory(String category) {
        for(int i = 0;i < mainLayout.getChildCount(); i++) {
            try {
                ArticleView articleView = (ArticleView) mainLayout.getChildAt(i);
                articleView.setVisiblityByCategory(category);
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public void scrollTop() {
        scrollBottomDetectScrollview.scrollTo(0, 0);
    }
}

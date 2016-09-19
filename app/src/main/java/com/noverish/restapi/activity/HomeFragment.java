package com.noverish.restapi.activity;

import android.app.Fragment;
import android.content.Intent;
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
import com.noverish.restapi.facebook.FacebookHtmlCodeProcessor;
import com.noverish.restapi.login.FacebookLoginWebViewActivity;
import com.noverish.restapi.login.LoginDatabase;
import com.noverish.restapi.login.LoginManageActivity;
import com.noverish.restapi.login.TwitterLoginWebViewActivity;
import com.noverish.restapi.other.OnHtmlLoadSuccessListener;
import com.noverish.restapi.twitter.TwitterArticleItem;
import com.noverish.restapi.twitter.TwitterArticleView;
import com.noverish.restapi.twitter.TwitterHtmlProcessor;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;
import com.noverish.restapi.webview.HtmlParseWebView;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class HomeFragment extends Fragment {
    private HtmlParseWebView facebookWebView;
    private HtmlParseWebView twitterWebView;

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
        
        facebookWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_facebook_web_view);
        facebookWebView.loadUrl(getString(R.string.facebook_url));
        facebookWebView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                LoginDatabase.getInstance().setFacebookLogined(Jsoup.parse(htmlCode).select("button[name=\"login\"][class=\"_54k8 _56bs _56b_ _56bw _56bu\"]").size() == 0);

                if(LoginDatabase.getInstance().isFacebookLogined()) {
                    ArrayList<FacebookArticleItem> items = FacebookHtmlCodeProcessor.process(htmlCode);
                    ArrayList<FacebookArticleView> views = new ArrayList<>();

                    for(FacebookArticleItem item : items) {
                        views.add(new FacebookArticleView(getActivity(), item));
                    }

                    HomeFragment fragment = (HomeFragment) getActivity().getFragmentManager().findFragmentByTag("HomeFragment");
                    if(fragment.getView() != null) {
                        LinearLayout mainLayout = (LinearLayout) fragment.getView().findViewById(R.id.fragment_home_layout_main);

                        for (FacebookArticleView view : views) {
                            mainLayout.addView(view);
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    startActivity(new Intent(getActivity(), LoginManageActivity.class));
                    startActivity(new Intent(getActivity(), FacebookLoginWebViewActivity.class));
                }
            }
        });

        twitterWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_twitter_web_view);
        twitterWebView.loadUrl("https://mobile.twitter.com/");
        twitterWebView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                LoginDatabase.getInstance().setTwitterLogined(Jsoup.parse(htmlCode).select("span.w-button-common.w-button-default").size() == 0);

                if(LoginDatabase.getInstance().isTwitterLogined()) {
                    ArrayList<TwitterArticleItem> items = TwitterHtmlProcessor.process(htmlCode);
                    ArrayList<TwitterArticleView> views = new ArrayList<>();

                    for(TwitterArticleItem item : items) {
                        views.add(new TwitterArticleView(getActivity(), item, handler));
                    }

                    HomeFragment fragment = (HomeFragment) getActivity().getFragmentManager().findFragmentByTag("HomeFragment");
                    if(fragment.getView() != null) {
                        LinearLayout mainLayout = (LinearLayout) fragment.getView().findViewById(R.id.fragment_home_layout_main);

                        for (TwitterArticleView view : views) {
                            mainLayout.addView(view);
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    startActivity(new Intent(getActivity(), LoginManageActivity.class));
                    startActivity(new Intent(getActivity(), TwitterLoginWebViewActivity.class));
                }
            }
        });

        return view;
    }

    private class CustomHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i("ScrollView","bottom");
        }
    }
}

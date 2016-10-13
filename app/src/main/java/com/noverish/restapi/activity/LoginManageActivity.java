package com.noverish.restapi.activity;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.kakao.KakaoClient;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageFinishedListener;
import com.noverish.restapi.webview.OnPageStartedListener;

/**
 * Created by Noverish on 2016-09-14.
 */
public class LoginManageActivity extends Fragment {
    private HtmlParseWebView facebookWebView;
    private HtmlParseWebView twitterWebView;
    private FrameLayout main, level1, level2;

    private Button facebookButton;
    private Button kakaoButton;
    private Button twitterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_manage, container, false);

        facebookWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_facebook_web_view);
        twitterWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_twitter_web_view);
        main = (FrameLayout) getActivity().findViewById(R.id.content_main_fragment_layout);
        level1 = (FrameLayout) getActivity().findViewById(R.id.content_main_fragment_level_1);
        level2 = (FrameLayout) getActivity().findViewById(R.id.content_main_fragment_level_2);


        facebookButton = (Button) view.findViewById(R.id.activity_login_manage_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        kakaoButton = (Button) view.findViewById(R.id.activity_login_manage_kakao);
        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        twitterButton = (Button) view.findViewById(R.id.activity_login_manage_twitter);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TwitterClient.getInstance().isLogined()) {
                    twitterWebView.loadUrl("https://mobile.twitter.com/logout");
                    twitterWebView.setOnPageFinishedListener(new OnPageFinishedListener() {
                        @Override
                        public void onPageFinished(HtmlParseWebView webView, String url) {
                            System.out.println("twitterWebView - onPageFinished" + url);
                            if(url.equals("https://mobile.twitter.com/logout")) {
                                webView.loadUrl("javascript:(function(){" +
                                        "l=document.getElementById('logout_button');" +
                                        "e=document.createEvent('HTMLEvents');" +
                                        "e.initEvent('click',true,true);" +
                                        "l.dispatchEvent(e);" +
                                        "})()");
                            } else if(url.equals("https://mobile.twitter.com/login")) {
                                level2.setVisibility(View.INVISIBLE);
                                level1.setVisibility(View.INVISIBLE);
                                main.setVisibility(View.INVISIBLE);
                                facebookWebView.setVisibility(View.INVISIBLE);

                                onResume();
                                webView.setOnPageFinishedListener(null);
                            }
                        }
                    });
                } else {
                    twitterWebView.loadUrl("https://mobile.twitter.com/login");
                    twitterWebView.setOnPageFinishedListener(new OnPageFinishedListener() {
                        @Override
                        public void onPageFinished(HtmlParseWebView webView, String url) {
                            if(url.equals("https://mobile.twitter.com/login")) {
                                level2.setVisibility(View.INVISIBLE);
                                level1.setVisibility(View.INVISIBLE);
                                main.setVisibility(View.INVISIBLE);
                                facebookWebView.setVisibility(View.INVISIBLE);
                                webView.setOnPageFinishedListener(null);
                            }
                        }
                    });
                    twitterWebView.setOnPageStartedListener(new OnPageStartedListener() {
                        @Override
                        public void onPageStarted(HtmlParseWebView webView, String url, Bitmap favicon) {
                            if(url.equals("https://mobile.twitter.com/")) {
                                TwitterClient.getInstance().setLogined(true);
                                getActivity().onBackPressed();

                                onResume();
                                webView.setOnPageStartedListener(null);
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FacebookClient.getInstance().isLogined()) {
            facebookButton.setText(getText(R.string.activity_login_center_facebook_login));
        } else {
            facebookButton.setText(getText(R.string.activity_login_center_facebook));
        }

        if (KakaoClient.getInstance().isLogined()) {
            kakaoButton.setText(getString(R.string.activity_login_center_kakao_login));
        } else {
            kakaoButton.setText(getString(R.string.activity_login_center_kakao));
        }

        if (TwitterClient.getInstance().isLogined()) {
            twitterButton.setText(getString(R.string.activity_login_manage_twitter_login));
        } else {
            twitterButton.setText(getString(R.string.activity_login_manage_twitter));
        }
    }


}

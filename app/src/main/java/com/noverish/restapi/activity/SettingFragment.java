package com.noverish.restapi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.kakao.KakaoClient;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnHtmlLoadSuccessListener;
import com.noverish.restapi.webview.OnPageFinishedListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static com.noverish.restapi.activity.MainActivity.Status.FOLLOWER_TWITTER;
import static com.noverish.restapi.activity.MainActivity.Status.FOLLOWING_TWITTER;
import static com.noverish.restapi.activity.MainActivity.Status.FRIEND_FACEBOOK;
import static com.noverish.restapi.activity.MainActivity.Status.PROFILE_FACEBOOK;
import static com.noverish.restapi.activity.MainActivity.Status.PROFILE_TWITTER;

/**
 * Created by Noverish on 2016-09-14.
 */
public class SettingFragment extends Fragment {
    private HtmlParseWebView anotherWebView;
    private HtmlParseWebView facebookWebView;
    private HtmlParseWebView twitterWebView;

    private Button facebookLoginButton;
    private Button twitterLoginButton;
    private Button kakaoLoginButton;

    private boolean loginStatusChanged = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);

        anotherWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_another_web_view);
        facebookWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_facebook_web_view);
        twitterWebView = (HtmlParseWebView) getActivity().findViewById(R.id.activity_main_twitter_web_view);

        facebookLoginButton = (Button) view.findViewById(R.id.activity_setting_facebook_login);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        twitterLoginButton = (Button) view.findViewById(R.id.activity_setting_twitter_login);
        twitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TwitterClient.getInstance().isLogined()) {
                    loginStatusChanged = true;

                    OnPageFinishedListener finished =  new OnPageFinishedListener() {
                        @Override
                        public void onPageFinished(HtmlParseWebView webView, String url) {
//                            System.out.println("twitterWebView - onPageFinished" + url);
//                            if(url.equals("https://mobile.twitter.com/logout")) {
                                webView.loadUrl("javascript:(function(){" +
                                        "l=document.getElementById('logout_button');" +
                                        "e=document.createEvent('HTMLEvents');" +
                                        "e.initEvent('click',true,true);" +
                                        "l.dispatchEvent(e);" +
                                        "})()");
//                            } else if(url.equals("https://mobile.twitter.com/login")) {
//                                TwitterClient.getInstance().setLogined(false);
//                                ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_TWITTER);
//                                onResume();
//                            }
                        }
                    };
                    twitterWebView.loadUrl("https://mobile.twitter.com/logout", null, null, finished, HtmlParseWebView.SNSType.Twitter);
                } else {
                    twitterWebView.loadUrl("https://mobile.twitter.com/login");
//                    twitterWebView.setOnPageFinishedListener(true, new OnPageFinishedListener() {
//                        @Override
//                        public void onPageFinished(HtmlParseWebView webView, String url) {
//                            if(url.equals("https://mobile.twitter.com/login")) {
//                                ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_TWITTER);
//
//                                twitterWebView.setOnPageStartedListener(false, new OnPageStartedListener() {
//                                    @Override
//                                    public void onPageStarted(HtmlParseWebView webView, String url, Bitmap favicon) {
//                                        if(url.equals("https://mobile.twitter.com/")) {
//                                            webView.setOnPageStartedListener(false, null);
//
//                                            loginStatusChanged = true;
//
//                                            TwitterClient.getInstance().setLogined(true);
//                                            ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_3);
//
//                                            onResume();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
                }
            }
        });

        kakaoLoginButton = (Button) view.findViewById(R.id.activity_setting_kakao_login);
        kakaoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button facebookProfileButton = (Button) view.findViewById(R.id.activity_setting_facebook_profile);
        facebookProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPageFinishedListener listener = new OnPageFinishedListener() {
                    @Override
                    public void onPageFinished(HtmlParseWebView webView, String url) {
                        MainActivity.instance.setStatus(PROFILE_FACEBOOK);
                        ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                    }
                };
                anotherWebView.loadUrl("https://m.facebook.com/profile.php?ref=bookmarks", null, null, listener, HtmlParseWebView.SNSType.Facebook);
            }
        });

        Button facebookFriendsButton = (Button) view.findViewById(R.id.activity_setting_facebook_friends);
        facebookFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPageFinishedListener listener = new OnPageFinishedListener() {
                    @Override
                    public void onPageFinished(HtmlParseWebView webView, String url) {
                        MainActivity.instance.setStatus(FRIEND_FACEBOOK);
                        ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                    }
                };
                anotherWebView.loadUrl("https://m.facebook.com/friends/center/friends/?fb_ref=fbm&ref=bookmarks&app_id=2356318349", null, null, listener, HtmlParseWebView.SNSType.Facebook);
            }
        });

        Button twitterProfileButton = (Button) view.findViewById(R.id.activity_setting_twitter_profile);
        twitterProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnHtmlLoadSuccessListener listener = new OnHtmlLoadSuccessListener() {
                    @Override
                    public void onHtmlLoadSuccess(HtmlParseWebView webView, String htmlCode) {
                        Document document = Jsoup.parse(htmlCode);

                        TwitterClient.getInstance().setUserScreenName(document.select("a[class=\"_1y-7iMRs\"]").attr("href").replaceAll("/",""));

                        String profileUrl = "https://mobile.twitter.com/" + TwitterClient.getInstance().getUserScreenName();

                        webView.loadUrl(profileUrl, null, null, null, HtmlParseWebView.SNSType.Twitter);
                        ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                        MainActivity.instance.setStatus(PROFILE_TWITTER);
                    }
                };
                anotherWebView.loadUrl("https://mobile.twitter.com/account", listener, null, null, HtmlParseWebView.SNSType.Twitter);
            }
        });

        Button twitterFollowingButton = (Button) view.findViewById(R.id.activity_setting_twitter_following);
        twitterFollowingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPageFinishedListener listener = new OnPageFinishedListener() {
                    @Override
                    public void onPageFinished(HtmlParseWebView webView, String url) {
                        MainActivity.instance.setStatus(FOLLOWING_TWITTER);
                        ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                    }
                };
                anotherWebView.loadUrl("https://mobile.twitter.com/" + TwitterClient.getInstance().getUserScreenName() + "/following", null, null, listener, HtmlParseWebView.SNSType.Twitter);
            }
        });

        Button twitterFollowerButton = (Button) view.findViewById(R.id.activity_setting_twitter_follower);
        twitterFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnPageFinishedListener listener = new OnPageFinishedListener() {
                    @Override
                    public void onPageFinished(HtmlParseWebView webView, String url) {
                        MainActivity.instance.setStatus(FOLLOWER_TWITTER);
                        ((MainActivity) getActivity()).changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                    }
                };
                anotherWebView.loadUrl("https://mobile.twitter.com/" + TwitterClient.getInstance().getUserScreenName() + "/followers", null, null, listener, HtmlParseWebView.SNSType.Twitter);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("onResume");

        if (FacebookClient.getInstance().isLogined()) {
            facebookLoginButton.setText(getText(R.string.activity_setting_facebook_logout));
        } else {
            facebookLoginButton.setText(getText(R.string.activity_setting_facebook_login));
        }

        if (KakaoClient.getInstance().isLogined()) {
            kakaoLoginButton.setText(getString(R.string.activity_setting_kakao_logout));
        } else {
            kakaoLoginButton.setText(getString(R.string.activity_setting_kakao_login));
        }

        if (TwitterClient.getInstance().isLogined()) {
            twitterLoginButton.setText(getString(R.string.activity_setting_twitter_logout));
        } else {
            twitterLoginButton.setText(getString(R.string.activity_setting_twitter_login));
        }
    }

    public boolean isLoginStatusChanged() {
        return loginStatusChanged;
    }
}

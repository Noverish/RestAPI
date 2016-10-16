package com.noverish.restapi.facebook;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.noverish.restapi.R;
import com.noverish.restapi.activity.SettingFragment;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.other.Essentials;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends BaseFragment {
    private FacebookClient client;

    private ScrollBottomDetectScrollview scrollView;
    private LinearLayout list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        list = (LinearLayout) view.findViewById(R.id.activity_facebook_text_view_list);

        scrollView = (ScrollBottomDetectScrollview) view.findViewById(R.id.fragment_facebook_scroll_view);
        scrollView.setRunnable(new Runnable() {
            @Override
            public void run() {
                client.loadNextPage();
            }
        });
        scrollView.startLoading();

        client = FacebookClient.getInstance();
        client.setFacebookClientCallback(new FacebookClient.FacebookClientCallback() {
            @Override
            public void onSuccess(ArrayList<FacebookArticleItem> items) {
                for(FacebookArticleItem item : items) {
                    list.addView(new FacebookArticleView(getActivity(), item));
                }
                scrollView.stopLoading();
            }

            @Override
            public void onNotLogin() {
                Essentials.changeFragment(getActivity(), R.id.activity_main_fragment_level_1, new SettingFragment());
            }

            @Override
            public void onFailure() {

            }
        });
        client.reload();

        return view;
    }

    @Override
    public void onPostButtonClicked(String content) {
        ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new
                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.d("onSuccess","onSuccess - " + result.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("onCancel","onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("onError","onError - " + error.toString());
                    }
                });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    public void onFreshButtonClicked() {
        list.removeAllViews();
        client.reload();
    }
}

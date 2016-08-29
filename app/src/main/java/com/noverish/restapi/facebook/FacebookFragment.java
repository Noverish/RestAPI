package com.noverish.restapi.facebook;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.view.HtmlParsingWebView;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends BaseFragment {
    private LinearLayout list;

    private ArrayList<FacebookArticleItem> items = new ArrayList<>();
    private ArrayList<FacebookArticleView> views = new ArrayList<>();
    ScrollBottomDetectScrollview scrollView;
    private static FacebookFragment instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        instance = this;

        if(getView() != null) {
            list = (LinearLayout) getView().findViewById(R.id.activity_facebook_text_view_list);

            scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_facebook_scroll_view);
            scrollView.setHandler(new ScrollBottomHandler());
        } else {
            Log.e("ERROR!","view is null");
        }



    }
    private static class ScrollBottomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            HtmlParsingWebView.getInstance().scrollBottom();
        }
    }


    public static FacebookFragment getInstance() {
        return instance;
    }

    public void htmlHasChanged(String html) {
        ArrayList<FacebookArticleItem> newItems = FacebookHtmlCodeProcessor.process(html);

        Iterator<FacebookArticleItem> iterator = newItems.iterator();
        while(iterator.hasNext()) {
            FacebookArticleItem item = iterator.next();
            if (items.contains(item)) {
                iterator.remove();
            } else {
                items.add(item);
            }
        }


        for (FacebookArticleItem item : newItems) {
            Log.d("facebook",item.toString());

            FacebookArticleView view = new FacebookArticleView(getActivity(), item);

            views.add(view);

            if(list != null)
                list.addView(view);
            else
                Log.e("ERROR","list is null");
        }

        if(scrollView != null)
            scrollView.stopLoading();
    }

    public void startLoading() {
        if(scrollView != null)
            scrollView.startLoading();
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

        /*Bundle params = new Bundle();
        params.putString("message", content);
        *//* make the API call *//*
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            *//* handle the result *//*
                        Log.d("onPostButtonClicked","onCompleted - " + response.toString());
                    }
                }
        ).executeAsync();*/

    }

    @Override
    public void onFreshButtonClicked() {

    }
}

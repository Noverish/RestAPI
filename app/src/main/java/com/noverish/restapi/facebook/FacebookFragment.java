package com.noverish.restapi.facebook;

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
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends Fragment {
    private String htmlCode;
    private LinearLayout list;
    private ScrollBottomDetectScrollview scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getView() != null) {
            list = (LinearLayout) getView().findViewById(R.id.activity_facebook_text_view_list);
            scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_facebook_scroll_view);
        } else {
            Log.e("ERROR!","view is null");
        }

        ArrayList<FacebookArticleItem> items = FacebookHtmlCodeProcessor.process(htmlCode);
        ArrayList<FacebookArticleView> views = new ArrayList<>();

        if(items != null) {
            for (FacebookArticleItem item : items) {
                Log.d("facebook",item.toString());
                views.add(new FacebookArticleView(getActivity(), item));
            }

            for (FacebookArticleView article : views) {
                if (list != null)
                    list.addView(article);
                else
                    Log.e("ERROR!", "list is null");
            }
        }

        scrollView.setHandler(new ScrollBottomHandler());
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        htmlCode = args.getString("html");
    }

    private static class ScrollBottomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("handle","message");
            FacebookWebView.getInstance().scrollBottom();
        }
    }
}

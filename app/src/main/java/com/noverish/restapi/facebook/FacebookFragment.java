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
import java.util.Iterator;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends Fragment {
    private LinearLayout list;

    private ArrayList<FacebookArticleItem> items = new ArrayList<>();
    private ArrayList<FacebookArticleView> views = new ArrayList<>();

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

            ScrollBottomDetectScrollview scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_facebook_scroll_view);
            scrollView.setHandler(new ScrollBottomHandler());
        } else {
            Log.e("ERROR!","view is null");
        }



    }

    private static class ScrollBottomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            FacebookWebView.getInstance().scrollBottom();
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
            Log.d("newItem",newItems.size() + "");
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
    }
}

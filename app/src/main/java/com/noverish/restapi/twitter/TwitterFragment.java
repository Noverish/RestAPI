package com.noverish.restapi.twitter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.List;

import twitter4j.Status;

public class TwitterFragment extends BaseFragment {
    private LinearLayout textViewList;
    private android.os.Handler handler = new Handler();
    private ScrollBottomDetectScrollview scrollView;

    private int nowPage;

    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_twitter, container, false);

        //editText = (EditText) findViewById(R.id.activity_main_edit_text);

        /*Button semanticButton = (Button) findViewById(R.id.activity_main_button);
        semanticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semanticOnClick(v);
            }
        });*/


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.nowPage = 1;

        if (getView() != null) {
            textViewList = (LinearLayout) getView().findViewById(R.id.activity_main_text_view_list);
            scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_twitter_scroll_view);
            scrollView.setHandler(new ScrollBottomHandler());
        }

        loadTweets(nowPage);
    }

    private void loadTweets(final int page) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TwitterClient twitterClient = TwitterClient.getInstance();
                List<Status> statuses = twitterClient.getTimeLine(page);

                for (Status status : statuses) {
                    handler.post(new AddViewRunnable(textViewList, new TwitterArticleView(getActivity(), status)));
                }
            }
        });
        thread.start();
    }

    class AddViewRunnable implements Runnable {
        private ViewGroup viewGroup;
        private View view;

        AddViewRunnable(ViewGroup viewGroup, View view) {
            this.viewGroup = viewGroup;
            this.view = view;
        }

        @Override
        public void run() {
            viewGroup.addView(view);
        }
    }

    private class ScrollBottomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            loadTweets(++nowPage);
        }
    }

    @Override
    public void onPostButtonClicked(String content) {
        TwitterClient twitterClient = TwitterClient.getInstance();
        twitterClient.updateStatus(content);
    }

    @Override
    public void onFreshButtonClicked() {

    }
}

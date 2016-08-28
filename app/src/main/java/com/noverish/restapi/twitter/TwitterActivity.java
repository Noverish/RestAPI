package com.noverish.restapi.twitter;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;

import java.util.List;

import twitter4j.Status;

public class TwitterActivity extends Fragment {
    private LinearLayout textViewList;
    private android.os.Handler handler = new Handler();

    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_twitter, container, false);

        textViewList = (LinearLayout) view.findViewById(R.id.activity_main_text_view_list);
        //editText = (EditText) findViewById(R.id.activity_main_edit_text);

        /*Button semanticButton = (Button) findViewById(R.id.activity_main_button);
        semanticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semanticOnClick(v);
            }
        });*/

        timeLineOnClick();

        return view;
    }

    private void timeLineOnClick() {
        textViewList.removeAllViews();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TwitterClient twitterClient = TwitterClient.getInstance();
                List<Status> statuses = twitterClient.getTimeLine();

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
}

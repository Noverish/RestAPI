package com.noverish.restapi.twitter;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;

import java.util.List;

import twitter4j.Status;

public class TwitterActivity extends Fragment {
    private LinearLayout textViewList;
    private EditText editText;
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

        Button twitButton = (Button) view.findViewById(R.id.activity_main_twitter_button);
        twitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetOnClick();
            }
        });


        editText = (EditText) view.findViewById(R.id.activity_main_twitter_edit_text);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return view;
    }

    private void semanticOnClick(View v) {
        RestAPIClient client = new RestAPIClient(editText.getText().toString());

        TextView textView = new TextView(getActivity());
        textView.setText(client.getHtmlCode());

        textViewList.addView(textView);

        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    private void tweetOnClick() {
        EditText editText = (EditText) getView().findViewById(R.id.activity_main_twitter_edit_text);
        String twit = editText.getText().toString();

        TwitterClient twitterClient = TwitterClient.getInstance();
        twitterClient.updateStatus(twit + " - RestAPIExample 에서 작성");

        editText.setText("");
        Toast.makeText(getActivity(), "트윗을 보냈습니다.",Toast.LENGTH_SHORT).show();
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

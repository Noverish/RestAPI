package com.noverish.restapi.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.noverish.restapi.custom_view.StatusView;
import com.noverish.restapi.facebook.FaceBookActivity;
import com.noverish.restapi.twitter.TwitterClient;

import java.util.List;

import twitter4j.Status;

public class MainActivity extends AppCompatActivity{
    private LinearLayout textViewList;
    private EditText editText;
    private android.os.Handler handler = new Handler();
    private Context context;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        textViewList = (LinearLayout) findViewById(R.id.activity_main_text_view_list);
        //editText = (EditText) findViewById(R.id.activity_main_edit_text);

        /*Button semanticButton = (Button) findViewById(R.id.activity_main_button);
        semanticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semanticOnClick(v);
            }
        });*/
        Button twitButton = (Button) findViewById(R.id.activity_main_twitter_button);
        twitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetOnClick();
            }
        });
        Button timeLineButton = (Button) findViewById(R.id.acitivty_main_twitter_time_line_get_button);
        timeLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLineOnClick();
            }
        });
        Button faceBookButton = (Button) findViewById(R.id.activity_main_face_book);
        faceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookOnClick();
            }
        });


        editText = (EditText) findViewById(R.id.activity_main_twitter_edit_text);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void semanticOnClick(View v) {
        RestAPIClient client = new RestAPIClient(editText.getText().toString());

        TextView textView = new TextView(this);
        textView.setText(client.getHtmlCode());

        textViewList.addView(textView);


        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    handler.post(new AddViewRunnable(textViewList, new StatusView(context, status)));
                }
            }
        });
        thread.start();
    }

    private void tweetOnClick() {
        EditText editText = (EditText) findViewById(R.id.activity_main_twitter_edit_text);
        String twit = editText.getText().toString();

        TwitterClient twitterClient = TwitterClient.getInstance();
        twitterClient.updateStatus(twit + " - RestAPIExample 에서 작성");

        editText.setText("");
        Toast.makeText(this, "트윗을 보냈습니다.",Toast.LENGTH_SHORT).show();
    }

    private void facebookOnClick() {
        Intent intent = new Intent(this, FaceBookActivity.class);
        context.startActivity(intent);
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

package com.noverish.restapi.twitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noverish.restapi.R;
import com.noverish.restapi.custom_view.StatusView;

import twitter4j.Status;

/**
 * Created by Noverish on 2016-05-30.
 */
public class ReplyTwitterActivity extends AppCompatActivity {
    private Context context;
    private EditText editText;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_reply_twitter);
        this.context = this;

        editText = (EditText) findViewById(R.id.activity_reply_twitter_edit_text);
        StatusView statusView = (StatusView) findViewById(R.id.activity_reply_twitter_status_view);
        statusView.setStatus(StatusView.nowSelectedStatus);
        Button button = (Button) findViewById(R.id.activity_reply_twitter_reply_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyOnClick();
            }
        });
    }

    private void replyOnClick() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TwitterClient twitterClient = TwitterClient.getInstance();

                Status status = StatusView.nowSelectedStatus;
                long inReplyToStatusId = status.getId();
                String screenName = status.getUser().getScreenName();
                String text = editText.getText().toString();

                twitterClient.reply(inReplyToStatusId, screenName, text);

                finish();

//                Toast.makeText(context, "답장을 보냈습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        thread.start();
    }

}

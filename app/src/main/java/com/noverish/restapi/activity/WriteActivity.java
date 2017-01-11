package com.noverish.restapi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.twitter.TwitterClient;

/**
 * Created by cscoi019 on 2017. 1. 11..
 */

public class WriteActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Button post = (Button) findViewById(R.id.activity_write_post_button);
        post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText editText = (EditText) findViewById(R.id.activity_write_edit_text);
        String content = editText.getText().toString();

        finish();

        FacebookClient.getInstance().post(content);
        TwitterClient.getInstance().post(content);
    }
}

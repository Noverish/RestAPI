package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.noverish.restapi.R;
import com.noverish.restapi.kakao.KakaoLoginActivity;
import com.noverish.restapi.twitter.TwitterActivity;

/**
 * Created by Noverish on 2016-07-28.
 */
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button twitterButton = (Button) findViewById(R.id.activity_home_twitter_button);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TwitterActivity.class));
            }
        });

        Button facebookButton = (Button) findViewById(R.id.activity_home_facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button kakaoButton = (Button) findViewById(R.id.activity_home_kakao_button);
        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, KakaoLoginActivity.class));
            }
        });

    }
}

package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookLoginClient;
import com.noverish.restapi.kakao.KakaoLoginClient;
import com.noverish.restapi.other.OnLoginSuccessListener;

/**
 * Created by Noverish on 2016-08-29.
 */
public class LoginCenterActivity extends AppCompatActivity implements OnLoginSuccessListener {
    private FacebookLoginClient facebookLoginClient;
    private KakaoLoginClient kakaoLoginClient;

    private boolean alreadyBackButtonPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facebookLoginClient = new FacebookLoginClient(this);
        facebookLoginClient.setOnLoginSuccessListener(this);

        kakaoLoginClient = KakaoLoginClient.getInstance(this);
        kakaoLoginClient.setOnLoginSuccessListener(this);

        LinearLayout kakaoLoginButton = (LinearLayout) findViewById(R.id.login_layout_kakaotalk);
        kakaoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoLoginClient.login();
            }
        });

        LinearLayout facebookLoginButton = (LinearLayout)findViewById(R.id.login_layout_facebook);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLoginClient.login();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        facebookLoginClient.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookLoginClient.onActivityResult(requestCode, resultCode, data);
        kakaoLoginClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess() {

    }
}

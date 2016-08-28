package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookWebView;
import com.noverish.restapi.other.Essentials;
import com.noverish.restapi.other.OnHtmlLoadSuccessListener;

/**
 * Created by Noverish on 2016-08-25.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FacebookWebView webView = new FacebookWebView();
        webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
            @Override
            public void onHtmlLoadSuccess(String htmlCode) {
                if(htmlCode.contains("_54k8 _56bs _56b_ _56bw _56bu")) {
                    Toast.makeText(SplashActivity.this, "로그인 해야되염", Toast.LENGTH_SHORT).show();
                    ImageView splash = (ImageView) SplashActivity.this.findViewById(R.id.activity_splash_image);
                    if(splash != null) {
                        ((ViewGroup) splash.getParent()).removeView(splash);
                    } else {
                        Log.e("ERROR", "ImageView splash is null");
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "로그인 되있어염", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        Essentials.changeFragment(this, R.id.activity_splash_web_view_layout, webView);

    }
}

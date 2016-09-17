package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;

/**
 * Created by Noverish on 2016-08-25.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RestAPIClient.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        thread.start();
    }
}

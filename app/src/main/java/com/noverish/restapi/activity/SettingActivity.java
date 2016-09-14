package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.noverish.restapi.R;
import com.noverish.restapi.login.LoginManageActivity;

/**
 * Created by Noverish on 2016-09-14.
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button loginButton = (Button) findViewById(R.id.activity_setting_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, LoginManageActivity.class));
            }
        });
    }
}

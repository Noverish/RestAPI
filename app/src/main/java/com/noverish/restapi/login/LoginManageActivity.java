package com.noverish.restapi.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-09-14.
 */
public class LoginManageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manage);

        Button facebookButton = (Button) findViewById(R.id.activity_login_manage_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginManageActivity.this, FacebookLoginWebViewActivity.class));
            }
        });
    }
}

package com.noverish.restapi.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.noverish.restapi.R;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Noverish on 2016-07-12.
 */
public class FacebookLoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager = null;
    private AccessTokenTracker accessTokenTracker = null;
    private com.facebook.login.widget.LoginButton loginButton = null;
    private AccessToken accessToken;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getApplicationContext(), loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
            // Toast.makeText(getActivity().getApplicationContext(), loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
            Log.e("profile",profile.getFirstName());
            Log.e("profile",profile.getLastName());
            Log.e("profile",profile.getId());
            Log.e("profile",profile.getMiddleName());
            Log.e("profile",profile.getName());
            Log.e("profile",profile.getLinkUri().toString());
            Log.e("profile",profile.getProfilePictureUri(100, 100).toString());



            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("GraphRequest", response.toString());
                            Log.e("GraphRequest", object.toString());

                            // Application code
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "User sign in canceled!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException e) {
            e.printStackTrace();
        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // xml을 불러 오기 전에 해야 됨
        setContentView(R.layout.activity_facebook_login);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
// App code
                Log.e("eNuri", "Current Token : " + currentAccessToken);
            }
        };



        accessTokenTracker.startTracking();


        LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}

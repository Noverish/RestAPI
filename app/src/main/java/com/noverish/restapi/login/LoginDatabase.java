package com.noverish.restapi.login;

import android.util.Log;

/**
 * Created by Noverish on 2016-09-14.
 */
public class LoginDatabase {
    private boolean isFacebookLogined = false;
    private boolean isTwitterLogined = false;
    private boolean isKakaoLogined = false;

    private static LoginDatabase instance;
    public static LoginDatabase getInstance() {
        if(instance == null)
            instance = new LoginDatabase();

        return instance;
    }

    private LoginDatabase() {}

    public boolean isFacebookLogined() {
        return isFacebookLogined;
    }

    public boolean isTwitterLogined() {
        return isTwitterLogined;
    }

    public boolean isKakaoLogined() {
        return isKakaoLogined;
    }


    public void setFacebookLogined(boolean facebookLogined) {
        Log.i("facebook","facebook Login is " + facebookLogined);
        isFacebookLogined = facebookLogined;
    }

    public void setTwitterLogined(boolean twitterLogined) {
        isTwitterLogined = twitterLogined;
    }

    public void setKakaoLogined(boolean kakaoLogined) {
        Log.i("kakao","kakao Login is " + kakaoLogined);
        isKakaoLogined = kakaoLogined;
    }
}

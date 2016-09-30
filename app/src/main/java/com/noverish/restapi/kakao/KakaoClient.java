package com.noverish.restapi.kakao;

/**
 * Created by Noverish on 2016-09-29.
 */
public class KakaoClient {
    private boolean isLogined;

    private static KakaoClient instance;
    public static KakaoClient getInstance() {
        if(instance == null)
            instance = new KakaoClient();

        return instance;
    }

    private KakaoClient() {}

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }
}

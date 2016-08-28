package com.noverish.restapi.kakao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.request.PostRequest;
import com.kakao.kakaostory.response.ProfileResponse;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.network.ErrorResult;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;

/**
 * Created by Noverish on 2016-08-24.
 */
public class KakaoMainActivity extends AppCompatActivity {
    private String execParam = "";
    private String marketParam = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            requestPostNote("test");
            Toast.makeText(this, "잘 됬다", Toast.LENGTH_SHORT).show();
        } catch (KakaoParameterException ex) {
            ex.printStackTrace();
        }
    }

    private void redirectLoginActivity() {
        final Intent intent = new Intent(this, KakaoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectSignupActivity() {
        final Intent intent = new Intent(this, KakaoLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private abstract class KakaoStoryResponseCallback<T> extends StoryResponseCallback<T> {
        @Override
        public void onNotKakaoStoryUser() {
            Logger.d("not KakaoStory user");
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            Logger.e("KakaoStoryResponseCallback : failure : " + errorResult);
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            redirectSignupActivity();
        }
    }

    public void readProfile() {
        KakaoStoryService.requestProfile(new KakaoStoryResponseCallback<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse profile) {
                Logger.d("succeeded to get story profile");
            }
        });
    }

    private void requestIsStoryUser() {
        KakaoStoryService.requestIsStoryUser(new KakaoStoryResponseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Logger.d("check story user : " + result);
            }
        });
    }

    private void requestPostNote(String content) throws KakaoParameterException {
        KakaoStoryService.requestPostNote(new KakaoStoryResponseCallback<MyStoryInfo>() {
            @Override
            public void onSuccess(MyStoryInfo result) {
                Logger.d(result.toString());
            }
        }, content, PostRequest.StoryPermission.PUBLIC, true, execParam, execParam, marketParam, marketParam);
    }
}

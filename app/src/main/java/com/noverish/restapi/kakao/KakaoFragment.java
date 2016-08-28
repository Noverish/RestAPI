package com.noverish.restapi.kakao;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.request.PostRequest;
import com.kakao.kakaostory.response.ProfileResponse;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;
import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-08-24.
 */
public class KakaoFragment extends Fragment {
    private String execParam = "";
    private String marketParam = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kakao, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        requestUserProfile();
    }

    private void redirectLoginActivity() {
        final Intent intent = new Intent(getActivity(), KakaoLoginActivity.class);
        startActivity(intent);
    }

    private void redirectSignupActivity() {
        final Intent intent = new Intent(getActivity(), KakaoLoginActivity.class);
        startActivity(intent);
    }

    private abstract class KakaoStoryResponseCallback<T> extends StoryResponseCallback<T> {
        @Override
        public void onNotKakaoStoryUser() {
            Log.d("KakaoResponseCallback", "onNotKakaoStoryUser");
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            Log.d("KakaoResponseCallback", "onFailure - " + errorResult.toString());
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            Log.d("KakaoResponseCallback", "onSessionClosed - " + errorResult.toString());
//            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            Log.d("KakaoResponseCallback", "onNotSignedUp");
//            redirectSignupActivity();
        }
    }

    @Deprecated
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

    private void requestPostArticle(String content) throws KakaoParameterException {
        KakaoStoryService.requestPostNote(new KakaoStoryResponseCallback<MyStoryInfo>() {
            @Override
            public void onSuccess(MyStoryInfo result) {
                Logger.d(result.toString());
            }
        }, content, PostRequest.StoryPermission.PUBLIC, true, execParam, execParam, marketParam, marketParam);
    }

    private void requestUserProfile() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("requestUserProfile", "onFailure - " + errorResult.toString());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("requestUserProfile", "onSessionClosed - " + errorResult.toString());
                startActivity(new Intent(getActivity(), KakaoLoginActivity.class));
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("requestUserProfile", "onSuccess - " + userProfile.toString());
            }

            @Override
            public void onNotSignedUp() {
                Log.d("requestUserProfile", "onNotSignedUp");
            }
        });
    }
}

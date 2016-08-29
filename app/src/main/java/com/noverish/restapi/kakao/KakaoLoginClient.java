package com.noverish.restapi.kakao;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.request.PostRequest;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.noverish.restapi.other.OnLoginSuccessListener;

/**
 * Created by Noverish on 2016-07-22.
 */
public class KakaoLoginClient {
    private SessionCallback mKakaocallback;
    private Activity activity;
    private OnLoginSuccessListener listener;

    private static KakaoLoginClient kakaoLoginClient;
    public static KakaoLoginClient getInstance(Activity activity) {
        if(kakaoLoginClient == null) {
            kakaoLoginClient = new KakaoLoginClient(activity);
        }

        kakaoLoginClient.activity = activity;
        GlobalApplication.setCurrentActivity(activity);
        return kakaoLoginClient;
    }

    private KakaoLoginClient(Activity activity) {
        this.activity = activity;
    }

    public void login() {
        // 카카오 세션을 오픈한다
//        Log.e("open","open");
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, activity);
    }

    public void logout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d("logout","logout");
            }
        });
    }

    public void signOut() {
        UserManagement.requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
//                redirectSignupActivity();
            }

            @Override
            public void onSuccess(Long userId) {
                redirectLoginActivity();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            Log.e("result",requestCode + " " + resultCode + " " + data);
        }
    }

    public void setOnLoginSuccessListener(OnLoginSuccessListener listener) {
        this.listener = listener;
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
//            Log.e("TAG" , "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("TAG" , exception.getMessage());
            }
        }
    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
//        Log.e("TAG","request me");

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(activity, "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("KakaoLoginSuccess","Id : " + userProfile.getId() + ", ServiceUserID : " + userProfile.getServiceUserId() +", nickName : " + userProfile.getNickname());

                if(listener != null) {
                    listener.onLoginSuccess();
                }
            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp","onNotSignedUp");
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    private void redirectLoginActivity() {
        Intent intent = new Intent(activity, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }

    private String execParam = "";
    private String marketParam = "";

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

    public void requestPostArticle(String content) throws KakaoParameterException {
        KakaoStoryService.requestPostNote(new KakaoStoryResponseCallback<MyStoryInfo>() {
            @Override
            public void onSuccess(MyStoryInfo result) {
                Logger.d(result.toString());
            }
        }, content, PostRequest.StoryPermission.PUBLIC, true, execParam, execParam, marketParam, marketParam);
    }
}

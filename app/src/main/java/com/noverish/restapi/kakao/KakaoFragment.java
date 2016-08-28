package com.noverish.restapi.kakao;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.view.HtmlParsingWebView;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Noverish on 2016-08-24.
 */
public class KakaoFragment extends Fragment {

    private LinearLayout list;

    private ArrayList<KakaoArticleItem> items = new ArrayList<>();
    private ArrayList<KakaoArticleView> views = new ArrayList<>();

    private static KakaoFragment instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kakao, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        instance = this;

        if(getView() != null) {
            list = (LinearLayout) getView().findViewById(R.id.activity_kakao_text_view_list);

            ScrollBottomDetectScrollview scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_kakao_scroll_view);
            scrollView.setHandler(new ScrollBottomHandler());
        } else {
            Log.e("ERROR!","view is null");
        }



    }

    private static class ScrollBottomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            HtmlParsingWebView.getInstance().scrollBottom();
        }
    }

    public static KakaoFragment getInstance() {
        return instance;
    }

    public void htmlHasChanged(String html) {
        Log.d("htmlHasChanged","htmlHasChanged");
        ArrayList<KakaoArticleItem> newItems = KakaoHtmlCodeProcessor.process(html);

        Iterator<KakaoArticleItem> iterator = newItems.iterator();
        while(iterator.hasNext()) {
            KakaoArticleItem item = iterator.next();
            if (items.contains(item)) {
                iterator.remove();
            } else {
                items.add(item);
            }
            Log.d("newItem",newItems.size() + "");
        }


        for (KakaoArticleItem item : newItems) {
            Log.d("kakao",item.toString());

            KakaoArticleView view = new KakaoArticleView(getActivity(), item);

            views.add(view);

            if(list != null)
                list.addView(view);
            else
                Log.e("ERROR","list is null");
        }
    }

    /*private String execParam = "";
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
    }*/
}

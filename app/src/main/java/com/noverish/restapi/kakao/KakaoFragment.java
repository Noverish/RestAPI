package com.noverish.restapi.kakao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-24.
 */
public class KakaoFragment extends BaseFragment {

    private LinearLayout list;

    private ScrollBottomDetectScrollview scrollView;

    private ArrayList<KakaoArticleItem> items = new ArrayList<>();
    private ArrayList<KakaoArticleView> views = new ArrayList<>();


    private static KakaoFragment instance;

    private boolean init = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kakao, container, false);
    }
/*
    @Override
    public void onStart() {
        super.onStart();

        instance = this;

        if(getView() != null) {
            list = (LinearLayout) getView().findViewById(R.id.activity_kakao_text_view_list);

            scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_kakao_scroll_view);
            scrollView.setRunnable(new Runnable() {
                @Override
                public void run() {
                    HtmlParsingWebView.getInstance().scrollBottom();
                }
            });
        } else {
            Log.e("ERROR!","view is null");
        }



    }

    public static KakaoFragment getInstance() {
        return instance;
    }

    public void htmlHasChanged(String html) {
        Log.d("init",init + "");
        if(init) {
            HtmlParsingWebView.getInstance().scrollBottom();
            init = false;
            return;
        }

        Log.d("htmlHasChanged","htmlHasChanged");
        ArrayList<KakaoArticleItem> newItems = KakaoHtmlCodeProcessor.processArticle(html);

        Iterator<KakaoArticleItem> iterator = newItems.iterator();
        while(iterator.hasNext()) {
            KakaoArticleItem item = iterator.next();
            if (items.contains(item)) {
                iterator.remove();
            } else {
                items.add(item);
            }
        }


        for (KakaoArticleItem item : newItems) {
            Log.d("kakao",item.toString());

            KakaoArticleView view = new KakaoArticleView(getActivity(), item);

            views.add(view);

            if(list != null) {
                list.addView(view);
                Log.d("list","addView(view)");
            }
            else
                Log.e("ERROR","list is null");
        }

        Log.d("scrollView","stopLoading");
        if(scrollView != null)
            scrollView.stopLoading();

        if(!canScroll(scrollView)) {
            HtmlParsingWebView.getInstance().scrollBottom();
        }

        if(MainActivity.dialog != null) {
            Log.d("mainActivity","dialog dismiss");
            MainActivity.dialog.dismiss();
        }
    }*/

    @Override
    public void onPostButtonClicked(String content) {
        /*KakaoLoginClient client = KakaoLoginClient.getInstance(getActivity());
        try {
            client.requestPostArticle(content);
        } catch (KakaoParameterException ex) {
            ex.printStackTrace();
        }*/
    }

    @Override
    public void onFreshButtonClicked() {
        /*init = true;
        list.removeAllViews();

        if(webView != null)
            webView.refresh();

        if(scrollView != null) {
            scrollView.startLoading();
        }*/

    }
/*

    private boolean canScroll(ScrollView scrollView) {
        View child = (View) scrollView.getChildAt(0);
        if (child != null) {
            int childHeight = (child).getHeight();
            return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
        }
        return false;
    }

    public void setWebView(HtmlParsingWebView webView) {
        this.webView = webView;
    }
*/

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

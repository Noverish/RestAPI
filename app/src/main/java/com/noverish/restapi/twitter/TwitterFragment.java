package com.noverish.restapi.twitter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.view.ScrollBottomDetectScrollview;

public class TwitterFragment extends BaseFragment {
    private LinearLayout textViewList;
    private android.os.Handler handler = new Handler();
    private ScrollBottomDetectScrollview scrollView;

    private LinearLayout list;
    private int nowPage;

    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_twitter, container, false);

        /*list = (LinearLayout) view.findViewById(R.id.activity_facebook_text_view_list);

        scrollView = (ScrollBottomDetectScrollview) view.findViewById(R.id.fragment_facebook_scroll_view);
        scrollView.setRunnable(new Runnable() {
            @Override
            public void run() {
                client.loadNextPage();
            }
        });
        scrollView.startLoading();

        client = FacebookClient.getInstance();
        client.setFacebookClientCallback(new FacebookClient.FacebookClientCallback() {
            @Override
            public void onSuccess(ArrayList<FacebookArticleItem> items) {
                for(FacebookArticleItem item : items) {
                    list.addView(new FacebookArticleView(getActivity(), item));
                }
                scrollView.stopLoading();
            }

            @Override
            public void onNotLogin() {
                Essentials.changeFragment(getActivity(), R.id.activity_main_fragment_level_1, new SettingFragment());
                Essentials.changeFragment(getActivity(), R.id.activity_main_fragment_level_2, new LoginManageFragment());
            }

            @Override
            public void onFailure() {

            }
        });
        client.reload();*/


        return view;
    }
/*
    @Override
    public void onStart() {
        super.onStart();

        this.nowPage = 1;

        if (getView() != null) {
            textViewList = (LinearLayout) getView().findViewById(R.id.activity_main_text_view_list);
            scrollView = (ScrollBottomDetectScrollview) getView().findViewById(R.id.fragment_twitter_scroll_view);
            scrollView.setRunnable(new Runnable() {
                @Override
                public void run() {
                    loadTweets(++nowPage);
                }
            });
        }

        loadTweets(nowPage);
    }

    private void loadTweets(final int page) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Twitter4jClient twitter4jClient = Twitter4jClient.getInstance();
                List<Status> statuses = twitter4jClient.getTimeLine(page);

                Log.d("loadTweets",page + " " + statuses.size());

                for (Status status : statuses) {
                    CustomThread thread1 = new CustomThread(textViewList, getActivity(), status);
                    thread1.start();
                }

                Log.d("scrollView","stopLoading");
                scrollView.stopLoading();
            }
        });
        thread.start();
    }

    class AddViewRunnable implements Runnable {
        private ViewGroup viewGroup;
        private View view;

        AddViewRunnable(ViewGroup viewGroup, View view) {
            this.viewGroup = viewGroup;
            this.view = view;
        }

        @Override
        public void run() {
            viewGroup.addView(view);
        }
    }

    class CustomThread extends Thread {
        private ViewGroup viewGroup;
        private Context context;
        private Status status;

        CustomThread(ViewGroup viewGroup, Context context, Status status) {
            this.viewGroup = viewGroup;
            this.context = context;
            this.status = status;
        }

        @Override
        public void run() {
//            handler.post(new AddViewRunnable(textViewList, new TwitterArticleView(getActivity(), status)));
        }
    }*/

    @Override
    public void onPostButtonClicked(String content) {
        /*Twitter4jClient twitter4jClient = Twitter4jClient.getInstance();
        twitter4jClient.updateStatus(content);
        Toast.makeText(getActivity(), "트윗을 보냈습니다",Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onFreshButtonClicked() {
        /*textViewList.removeAllViews();
        nowPage = 1;
        loadTweets(nowPage);*/
    }
}

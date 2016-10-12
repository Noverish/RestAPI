package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.kakao.KakaoClient;
import com.noverish.restapi.twitter.TwitterClient;

/**
 * Created by Noverish on 2016-09-14.
 */
public class LoginManageActivity extends Fragment {
    private Button facebookButton;
    private Button kakaoButton;
    private Button twitterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_manage, container, false);

        facebookButton = (Button) view.findViewById(R.id.activity_login_manage_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View level2 = getActivity().findViewById(R.id.content_main_fragment_level_2);
                View level1 = getActivity().findViewById(R.id.content_main_fragment_level_1);
                View main = getActivity().findViewById(R.id.content_main_fragment_layout);
                View back = getActivity().findViewById(R.id.content_main_background_layout);

                level2.setVisibility(View.INVISIBLE);
                level1.setVisibility(View.INVISIBLE);
                main.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
            }
        });

        kakaoButton = (Button) view.findViewById(R.id.activity_login_manage_kakao);
        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        twitterButton = (Button) view.findViewById(R.id.activity_login_manage_twitter);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View level2 = getActivity().findViewById(R.id.content_main_fragment_level_2);
                View level1 = getActivity().findViewById(R.id.content_main_fragment_level_1);
                View main = getActivity().findViewById(R.id.content_main_fragment_layout);
                View back = getActivity().findViewById(R.id.content_main_background_layout);
                View facebook = getActivity().findViewById(R.id.activity_main_facebook_web_view);

                level2.setVisibility(View.INVISIBLE);
                level1.setVisibility(View.INVISIBLE);
                main.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
                facebook.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FacebookClient.getInstance().isLogined()) {
            facebookButton.setText(getText(R.string.activity_login_center_facebook_login));
        } else {
            facebookButton.setText(getText(R.string.activity_login_center_facebook));
        }

        if (KakaoClient.getInstance().isLogined()) {
            kakaoButton.setText(getString(R.string.activity_login_center_kakao_login));
        } else {
            kakaoButton.setText(getString(R.string.activity_login_center_kakao));
        }

        if (TwitterClient.getInstance().isLogined()) {
            twitterButton.setText(getString(R.string.activity_login_manage_twitter_login));
        } else {
            twitterButton.setText(getString(R.string.activity_login_manage_twitter));
        }
    }
}

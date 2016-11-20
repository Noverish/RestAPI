package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.view.TmpMessageView;

/**
 * Created by Noverish on 2016-11-21.
 */

public class TmpMessageActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tmp_message, container, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.tmp_layout);

        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_twitter_profile_1, "아무생각없는인생...","@Skyrhim","Sep 14","아쉽 즐추"));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_twitter_profile_2, "똘기는 낙타를 탑니다","@__DDoLgi","Sep 9","야 너 컴퓨터 os 깔때 쓴 usb 어딨냐"));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_twitter_profile_3, "아무생각없는인생...","@liciaris","Jul 25","ㅇㅋㅇㅋ 감사함다"));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_facebook_profile_1, "채호경님","","토","단톡팜"));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_facebook_profile_2, "현지고님","","11월 5일",""));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_facebook_profile_1, "고병욱님","","11월 1일","ㅂㅂ"));
        layout.addView(new TmpMessageView(getActivity(), R.drawable.tmp_facebook_profile_3, "윤지훈님","","10월 16일","윤지훈님과 친구가 되었습니다. 메세지를 보내세요!"));

        return view;
    }
}

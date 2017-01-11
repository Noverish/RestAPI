package com.noverish.restapi.kakao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noverish.restapi.R;
import com.noverish.restapi.base.BaseFragment;

/**
 * Created by Noverish on 2016-08-24.
 */
public class KakaoFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kakao, container, false);
    }

    @Override
    public void onPostButtonClicked(String content) {

    }

    @Override
    public void onFreshButtonClicked() {

    }

    @Override
    public void showArticleByCategory(String category) {

    }

    @Override
    public void scrollTop() {

    }
}

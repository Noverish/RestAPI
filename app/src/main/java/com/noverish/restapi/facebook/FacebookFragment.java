package com.noverish.restapi.facebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noverish.restapi.R;
import com.noverish.restapi.base.BaseFragment;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);

        return view;
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

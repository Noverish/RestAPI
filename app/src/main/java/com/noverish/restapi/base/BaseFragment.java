package com.noverish.restapi.base;

import android.support.v4.app.Fragment;

;

/**
 * Created by Noverish on 2016-08-29.
 */
public abstract class BaseFragment extends Fragment {
    abstract public void onPostButtonClicked(String content);
    abstract public void onFreshButtonClicked();
    abstract public void showArticleByCategory(String category);
    abstract public void scrollTop();
}

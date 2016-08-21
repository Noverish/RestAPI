package com.noverish.restapi.facebook;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookFragment extends Fragment {
    private String htmlCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_facebook, container, false);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        htmlCode = args.getString("html");
    }
}

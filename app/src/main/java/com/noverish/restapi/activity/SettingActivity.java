package com.noverish.restapi.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noverish.restapi.R;
import com.noverish.restapi.other.Essentials;

/**
 * Created by Noverish on 2016-09-14.
 */
public class SettingActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);

        Button loginButton = (Button) view.findViewById(R.id.activity_setting_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Essentials.changeFragment(getActivity(), R.id.content_main_fragment_level_2, new LoginManageActivity());
            }
        });

        return view;
    }
}

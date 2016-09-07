package com.noverish.restapi.other;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by Noverish on 2016-08-21.
 */
public class Essentials {

    public static void changeFragment(Activity activity, int layoutId, Fragment fr) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(layoutId, fr, fr.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}

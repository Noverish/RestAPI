package com.noverish.restapi.other;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Noverish on 2016-08-21.
 */
public class Essentials {

    public static void changeFragment(Activity activity, int layoutId, android.app.Fragment fr) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(layoutId, fr, fr.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static void changeFragment(FragmentActivity activity, int layoutId, Fragment fr) {
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId, fr);
        fragmentTransaction.commit();
    }
}

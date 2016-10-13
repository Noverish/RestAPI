package com.noverish.restapi.other;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.Calendar;

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

    public static long stringToMillisInTwitter(String str) {
        Calendar calendar = Calendar.getInstance();

        if(str.contains("초")) {
            str = str.replaceAll("\\D","");
            calendar.add(Calendar.SECOND, -Integer.parseInt(str));
        } else if(str.contains("분")) {
            str = str.replaceAll("\\D","");
            calendar.add(Calendar.MINUTE, -Integer.parseInt(str));
        } else if(str.contains("시간")) {
            str = str.replaceAll("\\D","");
            calendar.add(Calendar.HOUR, -Integer.parseInt(str));
        } else if(str.contains("월") && str.contains("일")) {
            if(str.contains("년")) {
                String year = str.split("[\\D]+")[0];
                String month = str.split("[\\D]+")[1];
                String day = str.split("[\\D]+")[2];

                if(day.length() <= 2)
                    day = "20" + day;

                calendar.set(Calendar.YEAR, Integer.parseInt(year));
                calendar.set(Calendar.MONTH, Integer.parseInt(month));
                calendar.set(Calendar.DATE, Integer.parseInt(day));
            } else {
                String month = str.split("[\\D]+")[0];
                String day = str.split("[\\D]+")[1];

                calendar.set(Calendar.MONTH, Integer.parseInt(month));
                calendar.set(Calendar.DATE, Integer.parseInt(day));
            }
        } else {
            Log.e("ERROR!","stringToMillisInTwitter - " + str);
        }

        return calendar.getTimeInMillis();
    }

    public static long stringToMillisInFacebook(String str) {
        Calendar calendar = Calendar.getInstance();

        if(str.contains("분")) {
            str = str.replaceAll("\\D","");
            calendar.add(Calendar.MINUTE, -Integer.parseInt(str));
        } else if(str.contains("시간")) {
            str = str.replaceAll("\\D","");
            calendar.add(Calendar.HOUR, -Integer.parseInt(str));
        } else if(str.contains("어제")) {
            if(str.contains("오전")) {
                calendar.set(Calendar.AM_PM, Calendar.AM);
            } else if(str.contains("오후")) {
                calendar.set(Calendar.AM_PM, Calendar.PM);
            } else {
                Log.e("ERROR!","stringToMillisInFacebook - " + str);
            }

            String hour = str.split("[\\D]+")[0];
            String minute = str.split("[\\D]+")[1];

            calendar.set(Calendar.HOUR, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        } else if(str.contains("년")) {
            if(str.contains("오전")) {
                calendar.set(Calendar.AM_PM, Calendar.AM);
            } else if(str.contains("오후")) {
                calendar.set(Calendar.AM_PM, Calendar.PM);
            } else {
                Log.e("ERROR!","stringToMillisInFacebook - " + str);
            }

            String year = str.split("[\\D]+")[0];
            String month = str.split("[\\D]+")[1];
            String day = str.split("[\\D]+")[2];
            String hour = str.split("[\\D]+")[3];
            String minute = str.split("[\\D]+")[4];

            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            calendar.set(Calendar.MONTH, Integer.parseInt(month));
            calendar.set(Calendar.DATE, Integer.parseInt(day));
            calendar.set(Calendar.HOUR, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        } else if(str.contains("월")) {
            if(str.contains("오전")) {
                calendar.set(Calendar.AM_PM, Calendar.AM);
            } else if(str.contains("오후")) {
                calendar.set(Calendar.AM_PM, Calendar.PM);
            } else {
                Log.e("ERROR!","stringToMillisInFacebook - " + str);
            }

            String month = str.split("[\\D]+")[0];
            String day = str.split("[\\D]+")[1];
            String hour = str.split("[\\D]+")[2];
            String minute = str.split("[\\D]+")[3];

            calendar.set(Calendar.MONTH, Integer.parseInt(month));
            calendar.set(Calendar.DATE, Integer.parseInt(day));
            calendar.set(Calendar.HOUR, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
        } else {
            Log.e("ERROR!","stringToMillisInFacebook - " + str);
        }

        return calendar.getTimeInMillis();
    }
}

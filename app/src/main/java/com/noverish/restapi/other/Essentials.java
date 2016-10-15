package com.noverish.restapi.other;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String tmp;

        if((tmp = getMatches("[\\d]+초",str)) != null)
            calendar.add(Calendar.SECOND, -Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+분",str)) != null)
            calendar.add(Calendar.MINUTE, -Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+시간",str)) != null)
            calendar.add(Calendar.HOUR, -Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+일", str)) != null)
            calendar.set(Calendar.DATE, Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+월", str)) != null)
            calendar.set(Calendar.MONTH, Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+년", str)) != null) {
            if((tmp = tmp.replaceAll("[\\D]", "")).length() <= 2)
                tmp = "20" + tmp;
            calendar.set(Calendar.YEAR, Integer.parseInt(tmp));
        }

        return calendar.getTimeInMillis();
    }

    public static long stringToMillisInFacebook(String str) {
        Calendar calendar = Calendar.getInstance();
        String tmp;

        if((tmp = getMatches("[\\d]+분",str)) != null)
            calendar.add(Calendar.MINUTE, -Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+시간",str)) != null)
            calendar.add(Calendar.HOUR, -Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if(str.contains("어제"))
            calendar.add(Calendar.DATE, -1);

        if(str.contains("오전"))
            calendar.set(Calendar.AM_PM, Calendar.AM);
        else if(str.contains("오후"))
            calendar.set(Calendar.AM_PM, Calendar.PM);

        if((tmp = getMatches("[\\d]+:[\\d]+", str)) != null) {
            calendar.set(Calendar.HOUR, Integer.parseInt(tmp.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(tmp.split(":")[1]));
        }

        if((tmp = getMatches("[\\d]+일", str)) != null)
            calendar.set(Calendar.DATE, Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+월", str)) != null)
            calendar.set(Calendar.MONTH, Integer.parseInt(tmp.replaceAll("[\\D]","")));

        if((tmp = getMatches("[\\d]+년", str)) != null)
           calendar.set(Calendar.YEAR, Integer.parseInt(tmp.replaceAll("[\\D]","")));

        return calendar.getTimeInMillis();
    }

    public static String getMatches(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(matcher.find())
            return matcher.group();
        else
            return null;
    }
}

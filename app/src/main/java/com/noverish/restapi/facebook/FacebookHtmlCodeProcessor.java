package com.noverish.restapi.facebook;

import com.noverish.restapi.http.HttpConnectionThread;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookHtmlCodeProcessor {
    public static ArrayList<FacebookArticleItem> process(String htmlCode) {
        ArrayList<FacebookArticleItem> items = new ArrayList<>();

        htmlCode = htmlCode.replaceAll("(\\\\){1,7}\"","\"");
        htmlCode = htmlCode.replaceAll("&amp;","&");
        htmlCode = htmlCode.replaceAll("(\\\\){0,2}&quot;","\"");
        htmlCode = htmlCode.replaceAll("(\\\\){2,3}x3C", "<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003C","<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003E",">");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}/","/");
        htmlCode = HttpConnectionThread.unicodeToString(htmlCode);

        Pattern pattern = Pattern.compile("<article(\\S|\\s)*?</article>");
        Matcher matcher = pattern.matcher(htmlCode);

        while (matcher.find()) {
            String tmp = matcher.group();
            items.add(new FacebookArticleItem(tmp));
        }

        return items;
    }
}

package com.noverish.restapi.twitter;

import android.util.Log;

import com.noverish.restapi.http.HttpConnectionThread;
import com.noverish.restapi.other.Essentials;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-09-18.
 */
public class TwitterHtmlProcessor {
    public static String nextPageUrl;

    public static ArrayList<TwitterArticleItem> process(String html) {
        ArrayList<TwitterArticleItem> items = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements articles = document.select("div._1eF_MiFx");

        Log.d("<process>","twitter article is " + articles.size());

        for(Element article : articles) {
            TwitterArticleItem item = new TwitterArticleItem();

            Elements headerEle = article.select("div[class=\"_1axCTvm5 _2rKrV7oY _3f2NsD-H\"]");
            Elements profileEle = article.select("div[class=\"_3hLw5mbC _1LUwi_k5 _3kJ8i5k7 _3f2NsD-H\"]");
            Elements timeEle = article.select("time");
            Elements nameEle = article.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _32vFsOSj _2DggF3sL _3WJqTbOE\"]");
            Elements screenNameEle = article.select("span[class=\"_1Zp5zVT9 _1rTfukg4\"]");
            Elements contentEle = article.select("span[class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"]");
            Element replyEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(0);
            Element retweetEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(1);
            Element likeEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(2);
            Element dmEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(3);

            String header = headerEle.html();
            String profileImg = Essentials.getMatches("url[(][^)]*[)]",profileEle.outerHtml()).replaceAll("url[(]|[)]","");
            String time = timeEle.attr("aria-label");
            String name = nameEle.html().replaceAll("<[^>]*>","");
            String screenName = screenNameEle.html();
            String content = contentEle.html().replaceAll("<[^>]*>","");

            header = HttpConnectionThread.unicodeToString(header);
            time = HttpConnectionThread.unicodeToString(time);
            name = HttpConnectionThread.unicodeToString(name);
            content = HttpConnectionThread.unicodeToString(content);

            item.setHeader(header);
            item.setProfileImageUrl(profileImg);
            item.setTimeString(time);
            item.setName(name);
            item.setScreenName(screenName);
            item.setContent(content);

            Log.i("<process>",item.toString());

            items.add(item);
        }

        return items;
    }

    public static ArrayList<TwitterNotificationItem> processNotification(String html) {
        ArrayList<TwitterNotificationItem> items = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements notifications = document.select("table.activity, table.tweet");

        System.out.println("notification number - " + notifications.size());

        for(Element notification : notifications) {
            TwitterNotificationItem item = new TwitterNotificationItem();

            if(notification.classNames().contains("activity")) {
                Elements activityTypeElement = notification.select("div.activity-icon");
                Elements contentElement = notification.select("td.user-info");
                Elements timeElement = notification.select("td.timestamp");

                item.setType(TwitterNotificationItem.ACTIVITY);
                item.setContent(HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("(<[^>]*>|\\\\n)","").replaceAll("\\s+"," ").trim()));
                item.setTimeString(HttpConnectionThread.unicodeToString(timeElement.html().replaceAll("<[^>]*>","")));

                if(activityTypeElement.html().contains("_follow_")) {
                    item.setActivityType(TwitterNotificationItem.FOLLOW);
                } else if(activityTypeElement.html().contains("_rt_")) {
                    item.setActivityType(TwitterNotificationItem.RETWEET);
                } else if(activityTypeElement.html().contains("_heart_")) {
                    item.setActivityType(TwitterNotificationItem.FAVORITE);
                } else {
                    item.setActivityType(0);
                }
            }

            if(notification.classNames().contains("tweet")) {
                Elements profileImageElement = notification.select("td.avatar").select("img");
                Elements nameElement = notification.select("strong.fullname");
                Elements screenNameElement = notification.select("div.username");
                Elements timeElement = notification.select("td.timestamp").select("a");
                Elements contentElement = notification.select("div.dir-ltr");

                item.setType(TwitterNotificationItem.TWEET);
                item.setProfileImageUrl(profileImageElement.attr("src"));
                item.setName(HttpConnectionThread.unicodeToString(nameElement.html()));
                item.setScreenName(screenNameElement.html().replaceAll("(<[^>]*>|\\\\n)","").replaceAll("\\s+"," ").trim());
                item.setTimeString(HttpConnectionThread.unicodeToString(timeElement.html().replaceAll("<[^>]*>","")));
                item.setContent(HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("(<[^>]*>|\\\\n)","").replaceAll("\\s+"," ").trim()));
            }

            items.add(item);
        }

        return items;
    }

    public static String getUserScreenName(String html) {
        return Jsoup.parse(html).select("span.screen-name").html();
    }
}

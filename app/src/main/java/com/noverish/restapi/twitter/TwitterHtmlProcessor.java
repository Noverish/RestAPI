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
        Elements articles = document.select("table.tweet");

        for(Element article : articles) {
            TwitterArticleItem item = new TwitterArticleItem();

            Elements headerElement = article.select("span.context");
            Elements profileImageElement = article.select("td.avatar").select("img");
            Elements timeElement = article.select("td.timestamp").select("a");
            Elements nameElement = article.select("strong.fullname");
            Elements screenNameElement = article.select("div.username");
            Elements contentElement = article.select("div.dir-ltr");
            contentElement.select("a").remove();
            Elements mediaElement = article.select("a.twitter_external_link.dir-ltr.tco-link.has-expanded-path");

            Elements tweetActionElement = article.select("span.tweet-actions");
            Elements replyElement = tweetActionElement.select("a.first");
            Elements retweetElement = tweetActionElement.select("a[href*=retweet]");
            Elements favoriteElement = tweetActionElement.select("a.favorite");

            item.setArticleId(article.attr("href").split("/")[3].replaceAll("\\D",""));
            item.setHeader(HttpConnectionThread.unicodeToString(headerElement.html()));
            item.setProfileImageUrl(profileImageElement.attr("src"));
            item.setTimeString(HttpConnectionThread.unicodeToString(timeElement.html()));
            item.setTimeMillis(Essentials.stringToMillisInTwitter(item.getTimeString()));
            item.setName(HttpConnectionThread.unicodeToString(nameElement.html()));
            item.setPosterUrl("https://mobile.twitter.com" + nameElement.parents().attr("href"));
            item.setScreenName(screenNameElement.html().replaceAll("\\n|(\\\\n)|([<][/]?span[>])","").trim());
            item.setContent(HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("\\\\n","\n").trim()));
            item.setMedia(mediaElement.attr("data-url"));

            item.setReplyUrl(replyElement.attr("href"));
            item.setRetweetUrl(retweetElement.attr("href"));
            item.setFavoriteUrl(favoriteElement.attr("href"));

            if(retweetElement.select("span").size() != 0)
                item.setRetweeted(retweetElement.select("span").first().className().contains("active"));
            item.setFavorited(favoriteElement.select("span").first().className().contains("active"));

            items.add(item);
        }

        String moreUrl = document.select("div.w-button-more").select("a").attr("href");
        Log.e("moreUrl", moreUrl);
        nextPageUrl = moreUrl;

        return items;
    }

    public static ArrayList<TwitterArticleItem> processNew(String html) {
        ArrayList<TwitterArticleItem> items = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements articles = document.select("div._1eF_MiFx");

        System.out.println("articles - " + articles.size());

        for(Element article : articles) {
            TwitterArticleItem item = new TwitterArticleItem();

            Elements headerElement = article.select("div[class=\"_1axCTvm5 _2rKrV7oY _3f2NsD-H\"]");
            Elements profileImageElement = article.select("img[class=\"_1RntlttV _1-I0zYji\"]");
            Elements nameElement = article.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _32vFsOSj _2DggF3sL _3WJqTbOE\"]");
            Elements screenNameElement = article.select("span[class=\"_1Zp5zVT9 _1rTfukg4\"]");
            Elements timeElement = article.select("time");
            Elements contentElement = article.select("span[class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"]");
            Elements mediaElement = article.select("div[class=\"_2di_LxCm\"]");
            Elements retweetButtonElement = article.select("button[class=\"RQ5ECnGZ _1m0pnxeJ\"][aria-label*=Retweet]");
            Elements favoriteButtonElement = article.select("button[class=\"RQ5ECnGZ _1m0pnxeJ\"][aria-label*=Like]");

            item.setHeader(headerElement.html());
            item.setProfileImageUrl(profileImageElement.attr("src"));
            item.setName(nameElement.html().replaceAll("<!--[^>]*-->","").trim());
            item.setScreenName(screenNameElement.html());
            item.setTimeString(HttpConnectionThread.unicodeToString(timeElement.html()));
            item.setContent(contentElement.html().replaceAll("<[^>]*>",""));

            System.out.println(item.toString());

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

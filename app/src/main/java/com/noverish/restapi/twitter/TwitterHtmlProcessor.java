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

        Log.i("<twitter article>","twitter article is " + articles.size());

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

            String articleId = timeEle.first().parent().attr("href").replaceAll("\\D","");
            String header = headerEle.html();
            String profileImg = Essentials.getMatches("url[(][^)]*[)]",profileEle.outerHtml()).replaceAll("url[(]|[)]","");
            String timeDetail = timeEle.attr("aria-label");
            String timeStr = timeEle.html();
            String name = nameEle.html().replaceAll("<[^>]*>","");
            String screenName = screenNameEle.html();
            String content = contentEle.html().replaceAll("<[^>]*>","");
            boolean isRetweeted = retweetEle.attr("data-testid").contains("un");
            boolean isLiked = likeEle.attr("data-testid").contains("un");
            int retweetNum = Integer.parseInt(retweetEle.select("span._1H8Mn9AA").html().equals("") ? "0" : retweetEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));
            int likeNum = Integer.parseInt(likeEle.select("span._1H8Mn9AA").html().equals("") ? "0" : likeEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));

            header = HttpConnectionThread.unicodeToString(header);
            timeStr = HttpConnectionThread.unicodeToString(timeStr);
            name = HttpConnectionThread.unicodeToString(name);
            content = HttpConnectionThread.unicodeToString(content);

            item.setArticleId(articleId);
            item.setHeader(header);
            item.setProfileImageUrl(profileImg);
            item.setTimeMillis(Essentials.stringToMillisInTwitter(timeDetail));
            item.setTimeString(timeStr);
            item.setName(name);
            item.setScreenName(screenName);
            item.setContent(content);
            item.setRetweeted(isRetweeted);
            item.setRetweetNumber(retweetNum);
            item.setFavorited(isLiked);
            item.setFavoriteNumber(likeNum);

            Elements mediaEle;
            if((mediaEle = article.select("a[class=\"_3kGl_FG7\"]")).size() != 0) { //link
                Elements linkImgEle = mediaEle.select("div[class=\"MLZaeRvv _1Yv_hemU i1xnVt31\"]");
                Elements linkContentEle = mediaEle.select("span[class=\"Fe7ul3Lt _2DggF3sL _1HXcreMa\"]");
                Elements linkDomainEle = mediaEle.select("span[class=\"Fe7ul3Lt _2DggF3sL _34Ymm628\"]");

                String linkUrl = mediaEle.attr("href");
                String linkImg = Essentials.getMatches("url[(][^)]*[)]",linkImgEle.outerHtml()).replaceAll("url[(]|[)]","");
                String linkContent = HttpConnectionThread.unicodeToString(linkContentEle.html());
                String linkDomain = HttpConnectionThread.unicodeToString(linkDomainEle.html());

                item.addImageUrl(linkImg);
                item.setLinkUrl(linkUrl);
                item.setLinkContent(linkContent);
                item.setLinkDomain(linkDomain);
            } else if ((mediaEle = article.select("div[class=\"_2zP-4IzO _3f2NsD-H\"]")).size() != 0) { //video
                String videoImg = mediaEle.select("img").first().attr("src");
                String videoUrl = mediaEle.select("a").first().attr("href");

                item.addImageUrl(videoImg);
                item.setVideoUrl(videoUrl);
            } else if ((mediaEle = article.select("div[class=\"_2di_LxCm\"]")).size() != 0) { //image
                for(Element imgEle : mediaEle.select("img")) {
                    item.addImageUrl(imgEle.attr("src"));
                }
            }

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

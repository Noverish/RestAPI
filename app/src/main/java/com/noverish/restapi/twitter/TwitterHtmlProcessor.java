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

    public static ArrayList<TwitterArticleItem> processArticle(String html) {
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
            Elements articleUrlEle = timeEle.parents();

            contentEle.select("[aria-hidden=\"true\"]").remove();

            String articleId = timeEle.first().parent().attr("href").replaceAll("\\D","");
            String header = headerEle.html();
            String profileImg = Essentials.getMatches("url[(][^)]*[)]",profileEle.outerHtml()).replaceAll("url[(]|[)]","");
            String timeDetail = timeEle.attr("aria-label");
            String timeStr = timeEle.html();
            String name = nameEle.html().replaceAll("<[^>]*>","");
            String screenName = screenNameEle.html();
            String content = contentEle.html();
            boolean isRetweeted = retweetEle.attr("data-testid").contains("un");
            boolean isLiked = likeEle.attr("data-testid").contains("un");
            int retweetNum = Integer.parseInt(retweetEle.select("span._1H8Mn9AA").html().equals("") ? "0" : retweetEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));
            int likeNum = Integer.parseInt(likeEle.select("span._1H8Mn9AA").html().equals("") ? "0" : likeEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));
            String articleUrl = "https://mobile.twitter.com" + articleUrlEle.attr("href");

            header = HttpConnectionThread.unicodeToString(header);
            timeStr = HttpConnectionThread.unicodeToString(timeStr);
            name = HttpConnectionThread.unicodeToString(name);
            content = HttpConnectionThread.unicodeToString(content);
            content = content.replaceAll("href=\"/","href=\"https://mobile.twitter.com/");

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
            item.setArticleUrl(articleUrl);

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
        Elements notifications = document.select("div[class=\"_1eF_MiFx\"]");

        Log.i("<twitter noti>","twitter noti is " + notifications.size());

        for(Element notification : notifications) {
            TwitterNotificationItem item = new TwitterNotificationItem();

            Elements article = notification.select("article");

            if(article.hasClass("Ldgs22Bf")) {
                item.setNotificationType(TwitterNotificationItem.NotificationType.Tweet);

                TwitterArticleItem articleItem = new TwitterArticleItem();

                Elements profileEle = article.select("div[class=\"_3hLw5mbC _1LUwi_k5 _3kJ8i5k7 _3f2NsD-H\"]");
                Elements nameEle = article.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _32vFsOSj _2DggF3sL _3WJqTbOE\"]");
                Elements screenNameEle = article.select("span[class=\"_1Zp5zVT9 _1rTfukg4\"]");
                Elements timeEle = article.select("time");
                Elements contentEle = article.select("span[class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"]");
                Element replyEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(0);
                Element retweetEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(1);
                Element likeEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(2);
                Element dmEle = article.select("button.RQ5ECnGZ._1m0pnxeJ").get(3);

                contentEle.select("[aria-hidden=\"true\"]").remove();

                String articleId = timeEle.first().parent().attr("href").replaceAll("\\D","");
                String profileImg = Essentials.getMatches("url[(][^)]*[)]",profileEle.outerHtml()).replaceAll("url[(]|[)]","");
                String timeDetail = timeEle.attr("aria-label");
                String timeStr = timeEle.html();
                String name = nameEle.html().replaceAll("<[^>]*>","");
                String screenName = screenNameEle.html();
                String content = contentEle.html();
                boolean isRetweeted = retweetEle.attr("data-testid").contains("un");
                boolean isLiked = likeEle.attr("data-testid").contains("un");
                int retweetNum = Integer.parseInt(retweetEle.select("span._1H8Mn9AA").html().equals("") ? "0" : retweetEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));
                int likeNum = Integer.parseInt(likeEle.select("span._1H8Mn9AA").html().equals("") ? "0" : likeEle.select("span._1H8Mn9AA").html().replaceAll("\\D",""));

                timeStr = HttpConnectionThread.unicodeToString(timeStr);
                name = HttpConnectionThread.unicodeToString(name);
                content = HttpConnectionThread.unicodeToString(content);
                content = content.replaceAll("href=\"/","href=\"https://mobile.twitter.com/");

                articleItem.setArticleId(articleId);
                articleItem.setProfileImageUrl(profileImg);
                articleItem.setTimeMillis(Essentials.stringToMillisInTwitter(timeDetail));
                articleItem.setTimeString(timeStr);
                articleItem.setName(name);
                articleItem.setScreenName(screenName);
                articleItem.setContent(content);
                articleItem.setRetweeted(isRetweeted);
                articleItem.setRetweetNumber(retweetNum);
                articleItem.setFavorited(isLiked);
                articleItem.setFavoriteNumber(likeNum);

                item.setArticleItem(articleItem);
            } else {
                item.setNotificationType(TwitterNotificationItem.NotificationType.Activity);

                Elements svg = article.select("svg");
                if(svg.hasClass("_1xsIYIFr"))
                    item.setActivityType(TwitterNotificationItem.ActivityType.Follow);
                else if(svg.hasClass("PaLKewJ6"))
                    item.setActivityType(TwitterNotificationItem.ActivityType.Like);
                else if(svg.hasClass("_201bML4Q"))
                    item.setActivityType(TwitterNotificationItem.ActivityType.Retweet);

                ArrayList<String> profileImgs = new ArrayList<>();
                Elements profileListEles = article.select("div[class=\"BVd8eVsI _3f2NsD-H\"]").select("div[class=\"_3hLw5mbC _1LUwi_k5 _3kJ8i5k7 _3f2NsD-H\"]");
                for(Element ele : profileListEles) {
                    profileImgs.add(Essentials.getMatches("url[(][^)]*[)]",ele.outerHtml()).replaceAll("url[(]|[)]",""));
                }
                item.setProfileImgs(profileImgs);

                Elements contentEle = article.select("span[class=\"Fe7ul3Lt rlkX4fnX _2DggF3sL\"]");
                String content = contentEle.html();
                content = HttpConnectionThread.unicodeToString(content);
                item.setContent(content);

                try {
                    Elements innerArticle = article.select("div[class=\"_3eCUxUEm _3f2NsD-H\"]");

                    Elements imgEle = innerArticle.select("a[class=\"_2YXT0EI-\"]");
                    Elements nameEle = innerArticle.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _2DggF3sL _3WJqTbOE\"]");
                    Elements screenNameEle = innerArticle.select("span[class=\"class=\"_1Zp5zVT9 _1rTfukg4\"\"]");
                    Elements tweetContentEle = innerArticle.select("class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"");

                    String imgUrl = imgEle.first().attr("src");
                    String name = nameEle.html().replaceAll("<[^>]*>", "");
                    String screenName = screenNameEle.html();
                    String tweetContent = tweetContentEle.html();

                    name = HttpConnectionThread.unicodeToString(name);
                    tweetContent = HttpConnectionThread.unicodeToString(tweetContent);

                    item.setImg(imgUrl);
                    item.setName(name);
                    item.setScreenName(screenName);
                    item.setTweetContent(tweetContent);
                } catch (Exception ex) {

                }
            }

            items.add(item);
        }

        return items;
    }

    public static ArrayList<TwitterMessageItem> processMessage(String html) {
        ArrayList<TwitterMessageItem> items = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements messages = document.select("main").select("div[tabindex]");

        Log.i("<twitter message>","twitter message is " + messages.size());

        for(Element message : messages) {
            TwitterMessageItem item = new TwitterMessageItem();

            Elements profileEle = message.select("div[class=\"_3hLw5mbC _1LUwi_k5 _3kJ8i5k7 _3f2NsD-H\"]");
            Elements timeEle = message.select("time");
            Elements nameEle = message.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _2DggF3sL _3WJqTbOE\"]");
            Elements screenNameEle = message.select("span[class=\"_1Zp5zVT9 _1rTfukg4\"]");
            Elements contentEle = message.select("span[class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"]");

            String profileImg = Essentials.getMatches("url[(][^)]*[)]", profileEle.outerHtml()).replaceAll("url[(]|[)]", "");
            String timeStr = timeEle.html();
            String name = nameEle.html().replaceAll("<[^>]*>", "");
            String screenName = screenNameEle.html();
            String content = contentEle.html();

            timeStr = HttpConnectionThread.unicodeToString(timeStr);
            name = HttpConnectionThread.unicodeToString(name);
            content = HttpConnectionThread.unicodeToString(content);

            item.setProfileImg(profileImg);
            item.setTimeStr(timeStr);
            item.setName(name);
            item.setScreenName(screenName);
            item.setContent(content);

            Log.d("<twitter message>",item.toString());

            items.add(item);
        }

        return items;
    }
}

package com.noverish.restapi.twitter;

import android.util.Log;

import com.noverish.restapi.http.HttpConnectionThread;

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
            Elements headerElement = article.select("span.context");
            Elements profileImageElement = article.select("td.avatar").select("img");
            Elements timeElement = article.select("td.timestamp").select("a");
            Elements nameElement = article.select("strong.fullname");
            Elements screenNameElement = article.select("div.username");
            Elements contentElement = article.select("div.dir-ltr");
            Elements mediaElement = article.select("a.twitter_external_link.dir-ltr.tco-link.has-expanded-path");

            Elements tweetActionElement = article.select("span.tweet-actions");
            Element replyElement = tweetActionElement.select("a").get(0);
            Element retweetElement = tweetActionElement.select("a").get(1);
            Element favoriteElement = tweetActionElement.select("a").get(2);

            String header = HttpConnectionThread.unicodeToString(headerElement.html());
            String profileImageUrl = profileImageElement.attr("src");
            String time = HttpConnectionThread.unicodeToString(timeElement.html());
            String name = HttpConnectionThread.unicodeToString(nameElement.html());
            String screenName = screenNameElement.html().replaceAll("\\n|(\\\\n)|([<][/]?span[>])","").trim();
            contentElement.select("a").remove();
            String content = HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("\\\\n",""));
            String media = mediaElement.attr("data-url");

            String replyUrl = replyElement.attr("href");
            String retweetUrl = retweetElement.attr("href");
            String favoriteUrl = favoriteElement.attr("href");

            boolean retweeted = false;
            boolean favorited = false;
            try {
                retweeted = retweetElement.select("span").first().className().contains("active");
                favorited = favoriteElement.select("span").first().className().contains("active");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(article.outerHtml());
            }

            items.add(new TwitterArticleItem(header, profileImageUrl, name, screenName, time, content, media, replyUrl, retweeted, retweetUrl, favorited, favoriteUrl));
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

        for(Element article : articles) {
            Element headerElement = article.select("div[class=\"_1axCTvm5 _2rKrV7oY _3f2NsD-H\"]").first();
            Element profileImageElement = article.select("div[class=\"_1RntlttV _1-I0zYji\"]").first();
            Element timeElement = article.select("time").first();
            Element nameElement = article.select("span[class=\"Fe7ul3Lt _3ZSf8YGw _32vFsOSj _2DggF3sL _3WJqTbOE\"]").first();
            Element screenNameElement = article.select("span[class=\"_1Zp5zVT9 _1rTfukg4\"]").first();
            Element contentElement = article.select("span[class=\"Fe7ul3Lt _10YWDZsG _1rTfukg4 _2DggF3sL\"]").first();
            Element mediaElement = article.select("div[class=\"_2di_LxCm\"]").first();
        }

        return null;
    };
}

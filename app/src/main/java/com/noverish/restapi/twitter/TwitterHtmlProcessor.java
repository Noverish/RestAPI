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
            Element replyElement = tweetActionElement.select("a").get(0);
            Element retweetElement = tweetActionElement.select("a").get(1);
            Element favoriteElement = tweetActionElement.select("a").get(2);

            item.setTweetId(Long.parseLong(article.attr("href").split("/")[3].replaceAll("\\D","")));
            item.setHeader(HttpConnectionThread.unicodeToString(headerElement.html()));
            item.setProfileImageUrl(profileImageElement.attr("src"));
            item.setTimeString(HttpConnectionThread.unicodeToString(timeElement.html()));
            item.setTimeMillis(Essentials.stringToMillisInTwitter(item.getTimeString()));
            item.setFullName(HttpConnectionThread.unicodeToString(nameElement.html()));
            item.setScreenName(screenNameElement.html().replaceAll("\\n|(\\\\n)|([<][/]?span[>])","").trim());
            item.setContent(HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("\\\\n","")));
            item.setMedia(mediaElement.attr("data-url"));

            item.setReplyUrl(replyElement.attr("href"));
            item.setRetweetUrl(retweetElement.attr("href"));
            item.setFavoriteUrl(favoriteElement.attr("href"));

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

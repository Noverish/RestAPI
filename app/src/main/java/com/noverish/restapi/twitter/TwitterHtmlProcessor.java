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
            String screenName = screenNameElement.html().replaceAll("\\n|(\\\\n)|([<][/]?span[>])","");
            contentElement.select("a").remove();
            String content = HttpConnectionThread.unicodeToString(contentElement.html().replaceAll("\\\\n",""));
            String media = mediaElement.attr("data-url");

            String replyUrl = replyElement.attr("href");
            String retweetUrl = retweetElement.attr("href");
            String favoriteUrl = favoriteElement.attr("href");

            boolean retweeted = retweetElement.select("span").first().className().contains("active");
            boolean favorited = favoriteElement.select("span").first().className().contains("active");

            items.add(new TwitterArticleItem(header, profileImageUrl, name, screenName, time, content, media, replyUrl, retweeted, retweetUrl, favorited, favoriteUrl));
        }

        String moreUrl = document.select("div.w-button-more").select("a").attr("href");
        Log.e("moreUrl", moreUrl);
        nextPageUrl = moreUrl;

        return items;
    }
}

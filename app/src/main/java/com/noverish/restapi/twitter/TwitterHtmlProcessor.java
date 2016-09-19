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

            /*Log.d("headerDiv",headerElement.html());
            Log.d("profileImage",profileImageElement.html());
            Log.d("timeElement",timeElement.html());
            Log.d("nameElement",nameElement.html());
            Log.d("screenNameElement",screenNameElement.html());
            Log.d("contentElement",contentElement.html());
            Log.d("mediaElement",mediaElement.html());
            Log.d("asdf","---");*/

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

            /*Log.d("header",header);
            Log.d("profileImageUrl",profileImageUrl);
            Log.d("time",time);
            Log.d("name",name);
            Log.d("screenName",screenName);
            Log.d("content",content);
            Log.d("media",media);
            Log.d("asdf","---");
            Log.d("reply",replyUrl);
            Log.d("retweet",retweetUrl);
            Log.d("favorite",favoriteUrl);*/

            items.add(new TwitterArticleItem(header, profileImageUrl, name, screenName, time, content, media, replyUrl, retweetUrl, favoriteUrl));
        }

        String moreUrl = document.select("div.w-button-more").select("a").attr("href");
        Log.e("moreUrl", moreUrl);

        return items;
    }
}

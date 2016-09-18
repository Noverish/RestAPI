package com.noverish.restapi.twitter;

import android.util.Log;

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
        Document document = Jsoup.parse(html);
        Elements articles = document.select("table.tweet");

        for(Element article : articles) {
            Elements headerDiv = article.select("span.context");
            Elements profileImage = article.select("td.avatar");
            Elements timeElement = article.select("td.timestamp");
            Elements nameElement = article.select("strong.fullname");
            Elements screenNameElement = article.select("div.username");
            Elements contentElement = article.select("div.dir-ltr");
            Elements mediaElement = article.select("a.twitter_external_link.dir-ltr.tco-link.has-expanded-path");
        }

        String moreUrl = document.select("div.w-button-more").select("a").attr("href");
        Log.e("moreUrl", moreUrl);

        return null;
    }
}

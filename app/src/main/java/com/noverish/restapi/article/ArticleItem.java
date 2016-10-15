package com.noverish.restapi.article;

import java.util.Comparator;

/**
 * Created by Noverish on 2016-10-06.
 */

public class ArticleItem {
    protected String articleId;
    protected long timeMillis;
    protected String timeString;

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }


    public String getArticleId() {
        return articleId;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public String getTimeString() {
        return timeString;
    }


    public static Comparator<ArticleItem> comparator = new Comparator<ArticleItem>() {
        @Override
        public int compare(ArticleItem item1, ArticleItem item2) {
            return (int)(item2.timeMillis - item1.timeMillis);
        }
    };
}

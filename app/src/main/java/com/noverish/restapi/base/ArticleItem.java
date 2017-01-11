package com.noverish.restapi.base;

import java.util.Comparator;

/**
 * Created by Noverish on 2016-10-06.
 */

public class ArticleItem {
    private String articleId;
    private long timeMillis;
    private String timeString;
    private String posterUrl;
    private String articleUrl;
    private String category = "";

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getCategory() {
        return category;
    }


    @Override
    public boolean equals(Object obj) {
        return ((ArticleItem) obj).getArticleId().equals(articleId);
    }

    public static Comparator<ArticleItem> comparator = new Comparator<ArticleItem>() {
        @Override
        public int compare(ArticleItem item1, ArticleItem item2) {
            return (int)(item2.timeMillis - item1.timeMillis);
        }
    };

    @Override
    public String toString() {
        return "ArticleItem{" +
                "articleId='" + articleId + '\'' +
                ", timeMillis=" + timeMillis +
                ", timeString='" + timeString + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}

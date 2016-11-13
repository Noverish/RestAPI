package com.noverish.restapi.twitter;

import com.noverish.restapi.article.ArticleItem;

/**
 * Created by Noverish on 2016-09-18.
 */
public class TwitterArticleItem extends ArticleItem {
    private String header = "";
    private String profileImageUrl = "";
    private String name = "";
    private String screenName = "";
    private String content = "";
    private String media = "";
    private String replyUrl = "";
    private boolean retweeted = false;
    private String retweetUrl = "";
    private int retweetNumber;
    private boolean favorited = false;
    private String favoriteUrl = "";
    private int favoriteNumber;

    void setHeader(String header) {
        this.header = header;
    }

    void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    void setName(String name) {
        this.name = name;
    }

    void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    void setContent(String content) {
        this.content = content;
    }

    void setMedia(String media) {
        this.media = media;
    }

    void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    void setRetweetUrl(String retweetUrl) {
        this.retweetUrl = retweetUrl;
    }

    void setRetweetNumber(int retweetNumber) {
        this.retweetNumber = retweetNumber;
    }

    void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    void setFavoriteUrl(String favoriteUrl) {
        this.favoriteUrl = favoriteUrl;
    }

    void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }


    public String getHeader() {
        return header;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getContent() {
        return content;
    }

    public String getMedia() {
        return media;
    }

    public String getReplyUrl() {
        return replyUrl;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public String getRetweetUrl() {
        return retweetUrl;
    }

    public int getRetweetNumber() {
        return retweetNumber;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getFavoriteUrl() {
        return favoriteUrl;
    }

    public int getFavoriteNumber() {
        return favoriteNumber;
    }

    @Override
    public String toString() {
        return "TwitterArticleItem{" +
                "header='" + header + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", content='" + content + '\'' +
                ", media='" + media + '\'' +
                ", replyUrl='" + replyUrl + '\'' +
                ", retweeted=" + retweeted +
                ", retweetUrl='" + retweetUrl + '\'' +
                ", favorited=" + favorited +
                ", favoriteUrl='" + favoriteUrl + '\'' +
                "} " + super.toString();
    }
}

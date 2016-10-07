package com.noverish.restapi.twitter;

import com.noverish.restapi.article.ArticleItem;

/**
 * Created by Noverish on 2016-09-18.
 */
public class TwitterArticleItem extends ArticleItem{
    private String header = "";
    private String profileImageUrl = "";
    private String fullName = "";
    private String screenName = "";
    private String content = "";
    private String media = "";
    private String replyUrl = "";
    private boolean retweeted = false;
    private String retweetUrl = "";
    private boolean favorited = false;
    private String favoriteUrl = "";

    public void setHeader(String header) {
        this.header = header;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setRetweetUrl(String retweetUrl) {
        this.retweetUrl = retweetUrl;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setFavoriteUrl(String favoriteUrl) {
        this.favoriteUrl = favoriteUrl;
    }


    public String getHeader() {
        return header;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getFullName() {
        return fullName;
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

    public boolean isFavorited() {
        return favorited;
    }

    public String getFavoriteUrl() {
        return favoriteUrl;
    }
}

package com.noverish.restapi.twitter;

/**
 * Created by Noverish on 2016-09-18.
 */
public class TwitterArticleItem {
    private String header;
    private String profileImageUrl;
    private String fullName;
    private String screenName;
    private String time;
    private String content;
    private String media;

    public TwitterArticleItem(String header, String profileImageUrl, String fullName, String screenName, String time, String content, String media) {
        this.header = header;
        this.profileImageUrl = profileImageUrl;
        this.fullName = fullName;
        this.screenName = screenName;
        this.time = time;
        this.content = content;
        this.media = media;
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

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getMedia() {
        return media;
    }
}

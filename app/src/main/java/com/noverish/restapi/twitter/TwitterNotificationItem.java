package com.noverish.restapi.twitter;

import android.util.Log;

import com.noverish.restapi.article.ArticleItem;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-10-29.
 */

public class TwitterNotificationItem {
    enum NotificationType {Activity, Tweet}
    enum ActivityType {Follow, Like, Retweet}

    private NotificationType notificationType;
    private ActivityType activityType;

    private ArticleItem articleItem;

    private ArrayList<String> profileImgs = new ArrayList<>();
    private String content;
    private String img;
    private String name;
    private String screenName;
    private String tweetContent;

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setArticleItem(ArticleItem articleItem) {
        this.articleItem = articleItem;
        if(notificationType == NotificationType.Activity)
            Log.e("<ERROR>","set article item on activity - " + activityType);
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setActivityType on tweet - " + activityType);
    }

    public void setProfileImgs(ArrayList<String> profileImgs) {
        this.profileImgs = profileImgs;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setProfileImgs on tweet - " + profileImgs);
    }

    public void setContent(String content) {
        this.content = content;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setContent on tweet - " + content);
    }

    public void setImg(String img) {
        this.img = img;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setImg on tweet - " + img);
    }

    public void setName(String name) {
        this.name = name;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setName on tweet - " + name);
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setScreenName on tweet - " + screenName);
    }

    public void setTweetContent(String tweetContent) {
        this.tweetContent = tweetContent;
        if(notificationType == NotificationType.Tweet)
            Log.e("<ERROR>","setTweetContent on tweet - " + tweetContent);
    }


    public NotificationType getNotificationType() {
        return notificationType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public ArticleItem getArticleItem() {
        return articleItem;
    }

    public ArrayList<String> getProfileImgs() {
        return profileImgs;
    }

    public String getContent() {
        return content;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getTweetContent() {
        return tweetContent;
    }


    @Override
    public String toString() {
        return "TwitterNotificationItem{" +
                "notificationType=" + notificationType +
                ", activityType=" + activityType +
                ", articleItem=" + articleItem +
                ", profileImgs=" + profileImgs +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", tweetContent='" + tweetContent + '\'' +
                '}';
    }
}

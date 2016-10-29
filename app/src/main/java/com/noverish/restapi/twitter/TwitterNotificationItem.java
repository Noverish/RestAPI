package com.noverish.restapi.twitter;

import android.util.Log;

/**
 * Created by Noverish on 2016-10-29.
 */

public class TwitterNotificationItem {
    public static final int ACTIVITY = 0;
    public static final int TWEET = 1;

    public static final int FOLLOW = 3;
    public static final int FAVORITE = 4;
    public static final int RETWEET = 5;

    private int type = 0;
    private int activityType = 0;
    private String profileImageUrl;
    private String name;
    private String screenName;
    private String content;
    private String timeString;


    public int getType() {
        return type;
    }

    public int getActivityType() {
        return activityType;
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

    public String getTimeString() {
        return timeString;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }


    public TwitterArticleItem toTwtiterArticleItem() {
        if(type == TWEET) {
            TwitterArticleItem item = new TwitterArticleItem();

            item.setProfileImageUrl(profileImageUrl);
            item.setFullName(name);
            item.setScreenName(screenName);
            item.setContent(content);

            return item;
        } else if(type == ACTIVITY){
            Log.e("ERROR","TwitterNotificationItem.toTwtiterArticleItem() : The type of this TwitterNotificationItem is ACTIVITY - CAN NOT Convert");
            return null;
        } else {
            Log.e("ERROR","TwitterNotificationItem.toTwtiterArticleItem() : The type of this TwitterNotificationItem is unknown - " + type);
            return null;
        }
    }
}

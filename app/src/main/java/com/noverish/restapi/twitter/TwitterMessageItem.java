package com.noverish.restapi.twitter;

/**
 * Created by Noverish on 2017-01-11.
 */

public class TwitterMessageItem {
    private String profileImg;
    private String name;
    private String screenName;
    private String timeStr;
    private String content;

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getProfileImg() {
        return profileImg;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return "TwitterMessageItem{" +
                "profileImg='" + profileImg + '\'' +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

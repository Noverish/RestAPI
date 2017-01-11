package com.noverish.restapi.facebook;

/**
 * Created by Noverish on 2017-01-11.
 */

public class FacebookMessageItem {
    private String url;
    private String profileImg;
    private String name;
    private String timeStr;
    private String content;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUrl() {
        return url;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getName() {
        return name;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return "FacebookMessageItem{" +
                "profileImg='" + profileImg + '\'' +
                ", name='" + name + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

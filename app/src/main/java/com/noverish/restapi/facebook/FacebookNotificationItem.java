package com.noverish.restapi.facebook;

/**
 * Created by Noverish on 2016-10-30.
 */

public class FacebookNotificationItem {
    public static final int LIKE = 0;
    public static final int AMAZING = 1;
    public static final int LOVE = 2;
    public static final int STATUS = 3;
    public static final int LOCATION = 4;
    public static final int FACEBOOK = 5;
    public static final int DOYOUKNOW = 6;
    public static final int COMMENT = 7;
    public static final int PICTURE = 8;
    public static final int GROUP = 9;

    private int type;
    private String profileImageUrl;
    private String content;
    private String timeString;
    private String extensionUrl;

    public int getType() {
        return type;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTimeString() {
        return timeString;
    }

    public String getExtensionUrl() {
        return extensionUrl;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setExtensionUrl(String extensionUrl) {
        this.extensionUrl = extensionUrl;
    }


    @Override
    public String toString() {
        return "FacebookNotificationItem{" +
                "profileImageUrl='" + profileImageUrl + '\'' +
                ", content='" + content + '\'' +
                ", timeString='" + timeString + '\'' +
                ", extensionUrl='" + extensionUrl + '\'' +
                '}';
    }
}

package com.noverish.restapi.facebook;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleItem {
    public String header;
    public String profileImgUrl;
    public String title;
    public String time;
    public String location;
    public String content;
    public ArrayList<String> media;
    public String sympathyNum;
    public String commentNum;
    public String sharingNum;

    @Override
    public String toString() {
        return "FacebookArticleItem{" +
                "header='" + header + '\'' +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", media=" + media +
                ", sympathyNum='" + sympathyNum + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", sharingNum='" + sharingNum + '\'' +
                '}';
    }
}

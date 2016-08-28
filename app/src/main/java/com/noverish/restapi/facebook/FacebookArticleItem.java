package com.noverish.restapi.facebook;

import android.util.Log;

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
    public boolean equals(Object obj) {
        FacebookArticleItem item = (FacebookArticleItem) obj;

        if(item.profileImgUrl.equals(profileImgUrl)) {
            if (item.title.equals(title)) {
                if (item.time.equals(time)) {
                    return true;
                } else {
                    Log.w("equal", item.toString());
                    Log.w("equal", this.toString());
                    Log.w("equal", "프로필과 제목은 같은데 시간이 다름");
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "FacebookArticleItem{" +
                "header='" + header + '\'' +
                ", profileImgUrl='" + "url" + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", media=" + "media" +
                ", sympathyNum='" + sympathyNum + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", sharingNum='" + sharingNum + '\'' +
                '}';
    }
}

package com.noverish.restapi.kakao;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-28.
 */
public class KakaoArticleItem {
    public String profileImgUrl;
    public String title;
    public String time;
    public String content;
    public ArrayList<String> imageUrls;
    public ArrayList<String> videoUrls;

    @Override
    public boolean equals(Object obj) {
        KakaoArticleItem item = (KakaoArticleItem) obj;

        if(profileImgUrl.equals(item.profileImgUrl))
            if(title.equals(item.title))
                if(time.equals(item.time))
                    return true;

        return false;
    }

    @Override
    public String toString() {
        return "KakaoArticleItem{" +
                "profileImgUrl='" + profileImgUrl + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", imageUrls=" + imageUrls +
                ", videoUrls=" + videoUrls +
                '}';
    }
}

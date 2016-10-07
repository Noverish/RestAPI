package com.noverish.restapi.facebook;

import com.noverish.restapi.article.ArticleItem;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleItem extends ArticleItem {
    private String header;
    private String profileImgUrl;
    private String title;
    private String location;
    private String content;
    private ArrayList<String> media;
    private String video;
    private String sympathyNum;
    private String commentNum;
    private String sharingNum;


    public void setHeader(String header) {
        this.header = header;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMedia(ArrayList<String> media) {
        this.media = media;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setSympathyNum(String sympathyNum) {
        this.sympathyNum = sympathyNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public void setSharingNum(String sharingNum) {
        this.sharingNum = sharingNum;
    }


    public String getHeader() {
        return header;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getMedia() {
        return media;
    }

    public String getVideo() {
        return video;
    }

    public String getSympathyNum() {
        return sympathyNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public String getSharingNum() {
        return sharingNum;
    }


    @Override
    public boolean equals(Object obj) {
        FacebookArticleItem item = (FacebookArticleItem) obj;

        if(item.profileImgUrl.equals(profileImgUrl)) {
            if (item.title.equals(title)) {
                if (item.timeString.equals(timeString)) {
                    return true;
                }/* else {
                    Log.w("equal", item.toString());
                    Log.w("equal", this.toString());
                    Log.w("equal", "프로필과 제목은 같은데 시간이 다름");
                    return true;
                }*/
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
                ", timeString='" + timeString + '\'' +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", media=" + "media" +
                ", video=" + video +
                ", sympathyNum='" + sympathyNum + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", sharingNum='" + sharingNum + '\'' +
                '}';
    }
}

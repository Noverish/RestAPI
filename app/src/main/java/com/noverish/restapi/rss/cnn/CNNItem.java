package com.noverish.restapi.rss.cnn;

import com.noverish.restapi.base.ArticleItem;

/**
 * Created by Noverish on 2017-02-21.
 */

public class CNNItem extends ArticleItem {
    private String title;
    private String content;
    private String link;
    private String guid;
    private String date;
    private String img;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

    public String getDate() {
        return date;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "CNNItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", guid='" + guid + '\'' +
                ", date='" + date + '\'' +
                ", img=" + img +
                '}';
    }
}



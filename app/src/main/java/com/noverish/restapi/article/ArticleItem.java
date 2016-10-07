package com.noverish.restapi.article;

/**
 * Created by Noverish on 2016-10-06.
 */

public class ArticleItem {
    protected long timeMillis;
    protected String timeString;

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }


    public long getTimeMillis() {
        return timeMillis;
    }

    public String getTimeString() {
        return timeString;
    }
}

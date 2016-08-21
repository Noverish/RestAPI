package com.noverish.restapi.facebook;

import android.util.Log;

import com.noverish.restapi.http.HttpConnectionThread;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleItem {
    public String htmlCode;
    public Header header;
    public Title title;
    public Body body;
    public Media media;
    public Footer footer;

    public FacebookArticleItem(String htmlCode) {
        this.htmlCode = htmlCode;

        try {
            header = new Header(HttpConnectionThread.extractHtmlTag(htmlCode, "header", "_4g33 _ung _5qc1"));
        } catch (HttpConnectionThread.NoSuchTagException ex) {
            header = new Header();
        }

        try {
            title = new Title(HttpConnectionThread.extractHtmlTag(htmlCode, "header", "_4g33 _5qc1"));
        } catch (HttpConnectionThread.NoSuchTagException ex) {
            title = null;
        }

        try {
            body = new Body(HttpConnectionThread.extractHtmlTag(htmlCode, "div", "_5rgt _5msi"));
        } catch (HttpConnectionThread.NoSuchTagException ex) {
            body = new Body();
        }

        try {
            media = new Media(HttpConnectionThread.extractHtmlTag(htmlCode, "div", "_5rgu"));
        } catch (HttpConnectionThread.NoSuchTagException ex) {
            media = new Media();
        }

        try {
            footer = new Footer(HttpConnectionThread.extractHtmlTag(htmlCode, "footer", ""));
        } catch (HttpConnectionThread.NoSuchTagException ex) {
            footer = null;
        }

        Log.e("article",toString());
    }

    @Override
    public String toString() {
        return "FacebookArticleItem{" +
                "header=" + header +
                ", title=" + title +
                ", body=" + body +
                ", media=" + media +
                ", footer=" + footer +
                '}';
    }

    public class Header {
        private String htmlCode;
        public String msg;

        public Header() {
            msg = "";
        }

        public Header(String htmlCode) {
            this.htmlCode = htmlCode;

            extractMsg();
        }

        private void extractMsg() {
            try {
                msg = HttpConnectionThread.extractHtmlTag(htmlCode, "h3", "_52jd _52jb _52jg _5qc3");
                msg = msg.replaceAll("<[^>]*>","");
            } catch (HttpConnectionThread.NoSuchTagException ex) {
                msg = "";
            }
        }

        @Override
        public String toString() {
            return "Header{" +
                    "msg='" + msg + '\'' +
                    '}';
        }
    }

    public class Title {
        private String htmlCode;
        public String title;
        public String postedTime;
        public String profileImgUrl;

        public Title(String htmlCode) {
            this.htmlCode = htmlCode;

            extractTitle();
            extractPostedTime();
            extractProfileImgUrl();
        }

        private void extractTitle() {
            try {
                title = HttpConnectionThread.extractHtmlTag(htmlCode, "h3", "_52jd _52jb");
                title = title.replaceAll("<[^>]*>","");
            } catch (HttpConnectionThread.NoSuchTagException ex) {
                title = "";
            }
        }

        private void extractPostedTime() {
            Pattern pattern = Pattern.compile("<abbr>[^>]*</abbr>");
            Matcher matcher = pattern.matcher(htmlCode);
            if(matcher.find()) {
                postedTime = matcher.group();
                postedTime = postedTime.replaceAll("<[^>]*>", "");
            } else {
                postedTime = "";
            }
        }

        private void extractProfileImgUrl() {
            Pattern pattern = Pattern.compile("<i class=\"img profpic\"[^>]*>");
            Matcher matcher = pattern.matcher(htmlCode);
            if(matcher.find()) {
                String tmp = matcher.group();

                pattern = Pattern.compile("[(]\".*\"[)]");
                matcher = pattern.matcher(tmp);
                if(matcher.find()) {
                    tmp = matcher.group();
                    profileImgUrl = tmp.substring(2, tmp.length() - 2);
                    return;
                }
            }
            profileImgUrl = "";
        }

        @Override
        public String toString() {
            return "Title{" +
                    "title='" + title + '\'' +
                    ", postedTime='" + postedTime + '\'' +
                    ", profileImgUrl='" + profileImgUrl + '\'' +
                    '}';
        }
    }

    public class Body {
        private String htmlCode;
        public String content;

        public Body() {
            content = "";
        }

        public Body(String htmlCode) {
            this.htmlCode = htmlCode;

            extractContent();
        }

        private void extractContent() {
            try {
                content = HttpConnectionThread.extractHtmlTag(htmlCode, "span", "");

                content = content.replaceAll("<br>","\n");
                content = content.replaceAll("</p>","\n");
                content = content.replaceAll("<[^>]*>", "");
                content = content.trim();
            } catch (HttpConnectionThread.NoSuchTagException ex) {
                content = "";
            }
        }

        @Override
        public String toString() {
            return "Body{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }

    public class Media {
        private String htmlCode;
        public ArrayList<String> urls = new ArrayList<>();
        public String video;

        public Media() {
            video = "";
        }

        public Media(String htmlCode) {
            this.htmlCode = htmlCode;

            extractUrls();

            extractVideo();
        }

        private void extractUrls() {
            Pattern pattern = Pattern.compile("url[(]\".*?\"[)]");
            Matcher matcher = pattern.matcher(htmlCode);

            while(matcher.find()) {
                String tmp = matcher.group();
                urls.add(tmp.substring(5, tmp.length() - 2));
            }
        }

        private void extractVideo() {
            Pattern pattern = Pattern.compile("src\":\"[^\"]*\"");
            Matcher matcher = pattern.matcher(htmlCode);

            if(matcher.find())
                video = matcher.group();
            else
                video = "";
        }

        @Override
        public String toString() {
            StringBuilder tmp = new StringBuilder();
            tmp.append("{");
            int size = urls.size();
            for (int i = 0; i < size; i++) {
                tmp.append("'");
                tmp.append(urls.get(i));
                tmp.append("'");

                if (i != size - 1)
                    tmp.append(", ");
            }
            tmp.append("}");

            return "Media{" +
                    "urls=" + tmp.toString() +
                    ", video='" + video + "'" +
                    '}';
        }
    }

    public class Footer {
        private String htmlCode;
        public String sympathyNum;
        public String commentNum = "";
        public String sharingNum = "";

        public Footer(String htmlCode) {
            this.htmlCode = htmlCode;

            extractSympathyNum();
            extractCommentSharingNum();
        }

        private void extractSympathyNum() {
            try {
                sympathyNum = HttpConnectionThread.extractHtmlTag(htmlCode, "div", "_1g06");
                sympathyNum = sympathyNum.replaceAll("<[^>]*>","");
            } catch (HttpConnectionThread.NoSuchTagException ex) {
                sympathyNum = "";
            }
        }

        private void extractCommentSharingNum() {
            Pattern pattern = Pattern.compile("<span class=\"_1j-c\">[^<]*</span>");
            Matcher matcher = pattern.matcher(htmlCode);

            ArrayList<String> str = new ArrayList<>();

            if(matcher.find())
                str.add(matcher.group().replaceAll("<[^>]*>",""));

            if(matcher.find())
                str.add(matcher.group().replaceAll("<[^>]*>",""));

            for(String tmp : str) {
                if(tmp.contains("공유"))
                    sharingNum = tmp;
                else if(tmp.contains("댓글"))
                    commentNum = tmp;
            }
        }

        @Override
        public String toString() {
            return "Footer{" +
                    "sympathyNum='" + sympathyNum + '\'' +
                    ", commentNum='" + commentNum + '\'' +
                    ", sharingNum='" + sharingNum + '\'' +
                    '}';
        }
    }
}

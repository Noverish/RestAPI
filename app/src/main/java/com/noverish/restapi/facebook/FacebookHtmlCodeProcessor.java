package com.noverish.restapi.facebook;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookHtmlCodeProcessor {
    public static ArrayList<FacebookArticleItem> process(String htmlCode) {
        ArrayList<FacebookArticleItem> items = new ArrayList<>();

        /*htmlCode = htmlCode.replaceAll("(\\\\){1,7}\"","\"");
        htmlCode = htmlCode.replaceAll("&amp;","&");
        htmlCode = htmlCode.replaceAll("(\\\\){0,2}&quot;","\"");
        htmlCode = htmlCode.replaceAll("(\\\\){2,3}x3C", "<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003C","<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003E",">");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}/","/");
        htmlCode = HttpConnectionThread.unicodeToString(htmlCode);

        Pattern pattern = Pattern.compile("<article(\\S|\\s)*?</article>");
        Matcher matcher = pattern.matcher(htmlCode);

        while (matcher.find()) {
            String tmp = matcher.group();
            items.add(new FacebookArticleItem(tmp));
        }*/

        FacebookArticleItem item1 = new FacebookArticleItem();
        item1.title = "고려대학교 대나무숲";
        item1.profileImgUrl = "https://scontent.xx.fbcdn.net/v/t1.0-1/cp0/e15/q65/p120x120/13256022_471381826398469_3981194554379871927_n.jpg?efg=eyJpIjoidCJ9&oh=b2c5e8e25e12fe33c1f409a336b491b0&oe=58164817";
        item1.content = "#20828번째포효\n방학이... 진다...";
        item1.sympathyNum = "서원빈님 외 729명";
        item1.commentNum = "댓글 42개";
        item1.time = "22시간 전";

        FacebookArticleItem item3 = new FacebookArticleItem();
        item3.header = "박재준님이 댓글에 답글을 남겼습니다.";
        item3.title = "박재준";
        item3.time = "8월 6일 오후 2:04";
        item3.content = "싸지방 꿀잼";
        item3.sympathyNum = "4명";
        item3.commentNum = "댓글 7개";

        FacebookArticleItem item2 = new FacebookArticleItem();
        item2.title = "김하빈님이 커버 사진을 업데이트했습니다.";
        item2.profileImgUrl = "https://scontent.xx.fbcdn.net/v/t1.0-1/cp0/e15/q65/p40x40/14079599_1030702723703267_1804260766174292601_n.jpg?efg=eyJpIjoidCJ9&oh=ad3a75d9dd2afb1d85e3f2cadc9eb93b&oe=5812222F";
        item2.time = "어제 오후 5:14";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://scontent.xx.fbcdn.net/v/t1.0-0/cp0/e15/q65/p320x320/14089046_1030697773703762_5405793657730754675_n.jpg?efg=eyJpIjoidCJ9&oh=95abb3b99a9a8abbab7b2f0065c78f56&oe=58502AF5");
        item2.media = urls;
        item2.sympathyNum = "김성현님 외 5명";

        items.add(item1);
        items.add(item3);
        items.add(item2);

        return items;
    }
}

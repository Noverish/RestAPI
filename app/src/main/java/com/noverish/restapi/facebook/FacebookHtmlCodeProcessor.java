package com.noverish.restapi.facebook;

import android.util.Log;
import android.util.Pair;

import com.noverish.restapi.http.HttpConnectionThread;
import com.noverish.restapi.other.Essentials;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookHtmlCodeProcessor {
    /*
        헤더있는 _55wo _5rgr _5gh8
        헤더없는 _55wo _5rgr _5gh8 async_like
        커뮤니티 추천 _57_6 fullwidth carded _58vs _5o5d _5o5c _t26 _45js _29d0 _51s _55wr acw apm
        페이지를 좋아합니다 _55wo _5rgr _5gh8 async_like
        비어있음 _55wo _5rgr _5gh8 _35au
         */


    public static ArrayList<FacebookArticleItem> process(String htmlCode) {
        ArrayList<FacebookArticleItem> items = new ArrayList<>();

        Document document = Jsoup.parse(htmlCode);
        Elements articles = document.select("article");

        for(Element article : articles) {
            FacebookArticleItem item = new FacebookArticleItem();

//            Log.d("article",article.classNames().toString());
            if(article.classNames().contains("_35au"))
                continue;
            if(article.select("header[class=\"_5rgs _5sg5\"]").size() > 0) //추천 게시물
                continue;
            if(article.classNames().contains("fullwidth")) // 누가 좋아합니다 하고 좌우로 스크롤 할 수 있는 페이지 추천 글 _57_6 fullwidth carded _58vs _5o5d _5o5c _t26 _45js _29d0 _51s _55wr acw apm
                continue;
            if(article.classNames().contains("_d2r")) //보이지 않는 무언가
                continue;
            if(article.classNames().containsAll(Arrays.asList("_56be", "_4hkg", "_5rgr", "_5s1m", "async_like"))) //내부 아티클 _56be _4hkg _5rgr _5s1m async_like
                continue;
            if(article.classNames().containsAll(Arrays.asList("_2hrn", "_5t8z", "acw", "apm"))) //보이지 않는 무언가의 내부 아티클 (누가 무슨 페이지를 좋아합니다 인 듯하다) _2hrn _5t8z acw apm
                continue;

            item.setArticleId(Essentials.getMatches("mf_story_key[.][-]?[\\d]*", article.attributes().toString()).replaceAll("[^-0-9]", ""));
            if(item.getArticleId().equals(""))
                Log.e("ERROR","No mf_story_key : " + article.attributes());


            Elements headerPart = article.select("header[class=\"_4g33 _ung _5qc1\"]").select("h3[class=\"_52jd _52jb _52jg _5qc3\"]");
            item.setHeader(HttpConnectionThread.unicodeToString(headerPart.outerHtml().replaceAll("<[^>]*>","")));

            Elements titlePart = article.select("header[class=\"_4g33 _5qc1\"]");

            Elements profileImage = titlePart.select("i.img.profpic");
            item.setProfileImgUrl(findOriginOfJsoupBuggedUrl(profileImage.outerHtml(), htmlCode));

            Elements title = titlePart.select("h3._52jd._52jb._5qc3"); //제목에 행동이 있는 경우 _52jd _52jb _52jg _5qc3 이고 이름만 있는 경우 _52jd _52jb _52jh _5qc3 이다.
            item.setTitle(HttpConnectionThread.unicodeToString(title.outerHtml().replaceAll("<[^>]*>","")));

            Elements timeLocationPart = titlePart.select("div._52jc._5qc4._24u0").select("a");
            if(timeLocationPart.size() == 1) {
                item.setTimeString(HttpConnectionThread.unicodeToString(timeLocationPart.get(0).outerHtml().replaceAll("<[^>]*>","")));
                item.setTimeMillis(Essentials.stringToMillisInFacebook(item.getTimeString()));
            } else if(timeLocationPart.size() == 2) {
                item.setLocation(HttpConnectionThread.unicodeToString(timeLocationPart.get(1).outerHtml().replaceAll("<[^>]*>","")));
            }

            Elements content = article.select("div[class=\"_5rgt _5nk5 _5msi\"]");
            item.setContent(HttpConnectionThread.unicodeToString(content.outerHtml().replaceAll("<[^>]*>","").trim()));

            Elements mediaPart = article.select("div._5rgu._27x0");

            ArrayList<String> imageUrls = new ArrayList<>();
            Elements imageElements = mediaPart.select("a._39pi, a._26ih");
            for(Element imageElement : imageElements) {

                String key = Essentials.getMatches("oh=[0-9a-z]+",imageElement.select("i").outerHtml());
                if(key.equals(""))
                    key = Essentials.getMatches("[\\d]+_[\\d]+_[\\d]+",imageElement.select("i").outerHtml());

                String value = Essentials.getMatches("https[^\"]*" + key + "[^\"]*",htmlCode);

                imageUrls.add(value);
            }
            item.setImageUrls(imageUrls);

            Elements videoElements;
            if((videoElements = mediaPart.select("div._53mw._4gbu")).size() > 0) { //동영상만 올라옴

                String videoKey = Essentials.getMatches("oh=[0-9a-z]+",videoElements.outerHtml());

                String videoValue = Essentials.getMatches("https[^\"]*" + videoKey +"[^\"]*",htmlCode);

                String imageKey = Essentials.getMatches("oh=[0-9a-z]+",videoElements.select("i").outerHtml());
                if(imageKey.equals(""))
                    imageKey = Essentials.getMatches("[\\d]+_[\\d]+_[\\d]+",videoElements.select("i").outerHtml());

                String imageValue = Essentials.getMatches("https[^\"]*" + imageKey + "[^\"]*",htmlCode);

                item.setVideo(new Pair<>(imageValue, videoValue));
            } else if((videoElements = mediaPart.select("div._53mw")).size() > 0) { //동영상과 사진 같이 올라움
                System.out.println(videoElements.outerHtml());
            }

            Elements likeButton = article.select("a._15ko._5a-2.touchable");
            item.setLiked(Boolean.parseBoolean(likeButton.attr("aria-pressed")));
            item.setLikeUrl(likeButton.attr("data-uri"));

            if(item.getTitle().equals(""))
                System.out.println(article.outerHtml());

            items.add(item);
        }

        return items;
    }

    private static String findOriginOfJsoupBuggedUrl(String buggedHtml, String htmlCode) {
        Pattern pattern1 = Pattern.compile("[\\d_]*[\\S][.]jpg");
        Matcher matcher1 = pattern1.matcher(buggedHtml);

        if(matcher1.find()) {
            Pattern pattern2 = Pattern.compile("https[^>]*" + matcher1.group() + "[^>]*?\"");
            Matcher matcher2 = pattern2.matcher(htmlCode);

            if(matcher2.find()) {
                return matcher2.group().replace("\"","");
            } else {
                return "matcher2 is empty";
            }
        } else {
            return "matcher1 is empty";
        }
    }
}

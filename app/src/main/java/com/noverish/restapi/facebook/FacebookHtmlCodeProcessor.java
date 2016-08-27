package com.noverish.restapi.facebook;

import android.util.Log;

import com.noverish.restapi.http.HttpConnectionThread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Noverish on 2016-08-21.
 */
public class FacebookHtmlCodeProcessor {
    private static final String HEADER_PART_QUERY = "header[class=\"_4g33 _ung _5qc1\"]";
    private static final String HEADER_QUERY = "h3[class=\"_52jd _52jb _52jg _5qc3\"]";

    private static final String TITLE_PART_QUERY = "header[class=\"_4g33 _5qc1\"]";
    private static final String PROFILE_IMAGE_PART_QUERY = "i.img.profpic";
    private static final String TITLE_QUERY = "h3._52jd._52jb._5qc3"; //제목에 행동이 있는 경우 _52jd _52jb _52jg _5qc3 이고 이름만 있는 경우 _52jd _52jb _52jh _5qc3 이다.
    private static final String TIME_LOCATION_QUERY = "div._52jc._5qc4._24u0";

    private static final String CONTENT_QUERY = "div[class=\"_5rgt _5nk5 _5msi\"]";

    private static final String MEDIA_PART_QUERY = "div[class=\"_5rgu\"]";
    private static final String MEDIA_QUERY = "i[style]";


    public static ArrayList<FacebookArticleItem> process(String htmlCodeParam) {
        ArrayList<FacebookArticleItem> items = new ArrayList<>();

        String htmlCode = htmlCodeParam;
        htmlCode = htmlCode.replaceAll("(\\\\){1,7}\"","\"");
        htmlCode = htmlCode.replaceAll("&amp;","&");
        htmlCode = htmlCode.replaceAll("(\\\\){0,2}&quot;","\"");
        htmlCode = htmlCode.replaceAll("(\\\\){2,3}x3C", "<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003C","<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003E",">");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}/","/");
        htmlCode = HttpConnectionThread.unicodeToString(htmlCode);

        Document document = Jsoup.parse(htmlCode);
        Elements articles = document.select("article");

        for(Element article : articles) {
            FacebookArticleItem item = new FacebookArticleItem();

            Log.d("article",article.classNames().toString());
            if(article.classNames().contains("_35au"))
                continue;

            Elements headerPart = article.select(HEADER_PART_QUERY);
            if(headerPart != null && headerPart.size() != 0) {
                if(headerPart.size() == 1) {
                    Elements header = headerPart.first().select(HEADER_QUERY);
                    if(header != null && header.size() != 0) {
                        if(header.size() == 1) {

                            item.header = header.outerHtml().replaceAll("<[^>]*>","");
                            item.header = HttpConnectionThread.unicodeToString(item.header);

                        } else {
                            Log.e("header","There are " + header.size() + " header");
                            for(Element ele : header) {
                                Log.e("ele",ele.outerHtml());
                            }
                        }
                    }
                } else {
                    Log.e("headerPart","There are " + headerPart.size() + " headerPart");
                }
            }


            Elements titlePart = article.select(TITLE_PART_QUERY);
            if(titlePart != null && titlePart.size() != 0) {
                if(titlePart.size() == 1) {

                    Elements profileImage = titlePart.first().select(PROFILE_IMAGE_PART_QUERY);
                    if(profileImage != null && profileImage.size() != 0) {
                        if(profileImage.size() == 1) {

                            item.profileImgUrl = findOriginOfJsoupBuggedUrl(profileImage.outerHtml(), htmlCode);

                        } else {
                            Log.e("profileImage", "There are " + profileImage.size() + " profileImage");
                        }
                    }

                    Elements title = titlePart.first().select(TITLE_QUERY);
                    if(title != null && title.size() != 0) {
                        if(title.size() == 1) {

                            item.title = title.outerHtml().replaceAll("<[^>]*>","");
                            item.title = HttpConnectionThread.unicodeToString(item.title);

                        } else {
                            Log.e("title", "There are " + title.size() + " title");
                        }
                    }

                    Elements timeLocationPart = titlePart.first().select(TIME_LOCATION_QUERY);
                    if(timeLocationPart != null && timeLocationPart.size() != 0) {
                        if(timeLocationPart.size() == 1) {

                            Elements timeLocation = timeLocationPart.first().getElementsByTag("a");
                            if(timeLocation.size() >= 1) {

                                item.time = timeLocation.get(0).outerHtml().replaceAll("<[^>]*>","");
                                item.time = HttpConnectionThread.unicodeToString(item.time);

                            }
                            if(timeLocation.size() >= 2) {

                                item.location = timeLocation.get(1).outerHtml().replaceAll("<[^>]*>","");
                                item.location = HttpConnectionThread.unicodeToString(item.location);

                            }
                            if(timeLocation.size() >= 3) {
                                Log.e("ERROR", "timeLocation size is " + timeLocation.size());
                            }

                        } else {
                            Log.e("timeLocationPart", "There are " + timeLocationPart.size() + " timeLocationPart");
                        }
                    }


                } else {
                    Log.e("titlePart","There are " + titlePart.size() + " titlePart");
                    for(Element ele : titlePart) {
                        Log.e("ele",ele.outerHtml());
                    }
                }
            }

            Elements content = article.select(CONTENT_QUERY);
            if(content != null && content.size() != 0) {
                if(content.size() == 1) {

                    item.content = content.outerHtml().replaceAll("<[^>]*>","");
                    item.content = HttpConnectionThread.unicodeToString(item.content);

                } else {
                    Log.e("content","There are " + content.size() + " content");
                    for(Element ele : content) {
                        Log.e("ele",ele.outerHtml());
                    }
                }
            }

            Elements mediaPart = article.select(MEDIA_PART_QUERY);
            if(mediaPart != null && mediaPart.size() != 0) {
                if(mediaPart.size() == 1) {

                    ArrayList<String> mediaArrayList = new ArrayList<>();

                    Elements medias = mediaPart.select(MEDIA_QUERY);
                    if(medias != null && medias.size() != 0) {
                        for(Element media : medias) {
                            mediaArrayList.add(findOriginOfJsoupBuggedUrl(media.outerHtml(), htmlCode));
                        }
                    }

                    item.media = mediaArrayList;

                } else {
                    Log.e("mediaPart","There are " + mediaPart.size() + " mediaPart");
                    for(Element ele : mediaPart) {
                        Log.e("ele",ele.outerHtml());
                    }
                }
            }

            items.add(item);
        }

        /*

        헤더있는 _55wo _5rgr _5gh8
        헤더없는 _55wo _5rgr _5gh8 async_like
        내부 아티클 _56be _4hkg _5rgr _5s1m async_like
        커뮤니티 추천 _57_6 fullwidth carded _58vs _5o5d _5o5c _t26 _45js _29d0 _51s _55wr acw apm
        비어있음 _55wo _5rgr _5gh8 _35au

         */



        return items;
    }

    private static String findOriginOfJsoupBuggedUrl(String buggedHtml, String htmlCode) {
        Pattern pattern1 = Pattern.compile("oh=.*?&");
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

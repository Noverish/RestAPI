package com.noverish.restapi.kakao;

import android.util.Log;

import com.noverish.restapi.http.HttpConnectionThread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-08-28.
 */
public class KakaoHtmlCodeProcessor {

    private static final String ARTICLE = "div.story_section";


    private static final String TITLE_PART = "div[class=\"post_head\"]";
    private static final String PROFILE_IMAGE = "img";
    private static final String TITLE = "strong[class=\"tit_name\"]";
    private static final String TIME = "a[class=\"_linkPost link_time\"]";

    private static final String BODY_PART = "div[class=\"post_cont\"]";
    private static final String CONTENT = "div[class=\"txt_wrap\"]";
    private static final String IMAGE = "div.img_wrap";
    private static final String VIDEO = "div[class=\"movie_wrap v2\"]";

    public static ArrayList<KakaoArticleItem> process(String htmlCodeParam) {

        ArrayList<KakaoArticleItem> items = new ArrayList<>();

        htmlCodeParam = HttpConnectionThread.unicodeToString(htmlCodeParam);

        Document document = Jsoup.parse(htmlCodeParam);
        Elements articles = document.select(ARTICLE);

        for(Element article : articles) {
            KakaoArticleItem item = new KakaoArticleItem();


            Elements titlePart = article.select(TITLE_PART);
            if(titlePart != null && titlePart.size() != 0) {
                if(titlePart.size() == 1) {

                    Elements profileImage = titlePart.first().select(PROFILE_IMAGE);
                    if(profileImage != null && profileImage.size() != 0) {
                        if(profileImage.size() == 1) {

                            item.profileImgUrl = profileImage.attr("src");

                        } else {
                            Log.e("profileImage", "There are " + profileImage.size() + " profileImage");
                        }
                    }

                    Elements title = titlePart.first().select(TITLE);
                    if(title != null && title.size() != 0) {
                        if(title.size() == 1) {

                            item.title = title.outerHtml().replaceAll("<[^>]*>","");
                            item.title = HttpConnectionThread.unicodeToString(item.title);

                        } else {
                            Log.e("title", "There are " + title.size() + " title");
                        }
                    }

                    Elements time = titlePart.first().select(TIME);
                    if(time != null && time.size() != 0) {
                        if(time.size() == 1) {

                            item.time = time.attr("title");
                            item.time = HttpConnectionThread.unicodeToString(item.time);

                        } else {
                            Log.e("time", "There are " + time.size() + " time");
                        }
                    }


                } else if(titlePart.size() == 0) {
                    Log.w("titlePart","there is no titlePart");
                    Log.w("titlePart",article.outerHtml());
                } else {
                    Log.e("titlePart", "There are " + titlePart.size() + " titlePart");
                    for (Element ele : titlePart) {
                        Log.e("ele", ele.outerHtml());
                    }
                }
            }

            Elements bodyPart = article.select(BODY_PART);
            if(bodyPart != null && bodyPart.size() != 0) {
                if(bodyPart.size() == 1) {

                    Elements content = bodyPart.first().select(CONTENT);
                    if(content != null && content.size() != 0) {
                        if(content.size() == 1) {

                            item.content = content.outerHtml().replaceAll("<[^>]*>","");
                            item.content = HttpConnectionThread.unicodeToString(item.content);

                        } else {
                            Log.e("content", "There are " + content.size() + " content");
                        }
                    }

                    Elements imagePart = bodyPart.first().select(IMAGE);
                    if(imagePart != null && imagePart.size() != 0) {
                        if(imagePart.size() == 1) {

                            ArrayList<String> imageArrayList = new ArrayList<>();

                            Elements images = imagePart.select("img");
                            if(images != null && images.size() != 0) {
                                for(Element image : images) {
                                    Log.d("image",image.attr("src"));
                                    imageArrayList.add(image.attr("src"));
                                }
                            }

                            item.imageUrls = imageArrayList;

                        } else {
                            Log.e("imagePart", "There are " + imagePart.size() + " imagePart");
                        }
                    }

                    Elements videoPart = bodyPart.first().select(VIDEO);
                    if(videoPart != null && videoPart.size() != 0) {
                        if(videoPart.size() == 1) {

                            ArrayList<String> videoArrayList = new ArrayList<>();

                            Elements videos = videoPart.select("a");
                            if(videos != null && videos.size() != 0) {
                                for(Element video : videos) {
                                    videoArrayList.add(video.attr("data-url"));
                                }
                            }

                            item.videoUrls = videoArrayList;

                        } else {
                            Log.e("videoPart", "There are " + videoPart.size() + " videoPart");
                        }
                    }


                } else if(bodyPart.size() == 0) {
                    Log.w("bodyPart","there is no titlePart");
                    Log.w("bodyPart",article.outerHtml());
                } else {
                    Log.e("bodyPart", "There are " + bodyPart.size() + " bodyPart");
                    for (Element ele : bodyPart) {
                        Log.e("ele", ele.outerHtml());
                    }
                }
            }

            if(item.profileImgUrl == null || item.title == null || item.time == null) {
                if(item.profileImgUrl == null)
                    Log.w("null","profile is null");

                if(item.title == null)
                    Log.w("null","title is null");

                if(item.time == null)
                    Log.w("null","time is null");

                Log.w("null",article.outerHtml());
            } else {
//                items.add(item);
            }
        }

        Log.d("items",items.size() + "");

        return items;
    }
}

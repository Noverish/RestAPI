package com.noverish.restapi.rss.cnn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Noverish on 2017-02-20.
 */

public class RssCNNClient {
    public static final String url = "http://rss.cnn.com/rss/cnn_topstories.rss";
    public static final int PAGE_SIZE = 10;

    private static RssCNNClient instance;
    public static RssCNNClient getInstance() {
        if(instance == null)
            instance = new RssCNNClient();
        return instance;
    }

    private ArrayList<CNNItem> items = new ArrayList<>();

    private RssCNNClient() {
        // xmlPullParser
        try {
            Document document = Jsoup.connect(url).get();
            String html = document.outerHtml();
            InputStream is = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            CNNItem item = null;

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("item")) {
                            item = new CNNItem();
                        }
                        if(item != null) {
                            if (startTag.equals("title")) {
                                item.setTitle(parser.nextText());
                            }
                            if (startTag.equals("description")) {
                                item.setContent(parser.nextText().replaceAll("<[^>]*>","").trim());
                            }
                            if (startTag.equals("link")) {
                                item.setLink(parser.nextText());
                            }
                            if (startTag.equals("guid")) {
                                item.setGuid(parser.nextText());
                            }
                            if (startTag.equals("pudDate")) {
                                item.setDate(parser.nextText());
                            }
                            if (startTag.equals("media:content")) {
                                String url = parser.getAttributeValue(parser.getNamespace(), "url");
                                if(url.contains("super"))
                                    item.setImg(url);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("item")) {
                            items.add(item);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CNNItem> getItems(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = start + PAGE_SIZE;
        if(items.size() < start)
            return new ArrayList<>();
        else if(items.size() < end)
            return new ArrayList<>(items.subList(start, items.size()));
        else
            return new ArrayList<>(items.subList(start, end));
    }
}

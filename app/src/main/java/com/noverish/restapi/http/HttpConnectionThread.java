package com.noverish.restapi.http;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Noverish on 2016-01-16.
 */
public class HttpConnectionThread extends Thread {
    private String htmlCode;
    private String urlStr;
    private String cookies;
    private String requestMethod;
    private boolean isItHttps;

    public HttpConnectionThread(String urlStr, String cookies, String requestMethod) {
        this.urlStr = urlStr;
        this.cookies = (cookies == null) ? "" : cookies;
        this.requestMethod = requestMethod;
        this.htmlCode = "There is no html code";

        if(urlStr.substring(0,5).matches("https")) {
            isItHttps = true;
        } else if(urlStr.substring(0,5).matches("http:")) {
            isItHttps = false;
        } else {
            isItHttps = false;
            Log.e("[Log]Error", "HttpConnectionThread.constructor - I don't know it is https or http");
        }
    }

    public void run() {
        request();
    }

    private void request() {
        try {
            BufferedReader reader = null;
            URL url = new URL(urlStr);

            if(isItHttps) {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                if (con != null) {
                    con.setConnectTimeout(10000);
                    con.setRequestMethod(requestMethod);
                    con.setRequestProperty("Cookie", cookies);

                    Log.e("[Log]Note","HttpConnectionThread.request - resCode is " + con.getResponseCode());

                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }
            } else {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                if (con != null) {
                    con.setConnectTimeout(10000);
                    con.setRequestMethod(requestMethod);
                    con.setRequestProperty("Cookie", cookies);

                    Log.e("[Log]Note","HttpConnectionThread.request - resCode is " + con.getResponseCode());

                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                }
            }

            if(reader != null) {
                String line;
                htmlCode = "";

                while ((line = reader.readLine()) != null) {
                    htmlCode += line + "\n";
                }

                reader.close();
            } else {
                Log.e("[Log]Error", "HttpConnectionThread.request - reader is null");
            }

        } catch (Exception ex) {
            Log.e("[Log]Error", "HttpConnectionThread.request - exception is occurred");
        }
    }

    public static Drawable getImageDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static String processHtml(String str) {
        StringBuilder ans = new StringBuilder();
        int tab = 0;
        int index;

        str = str.replaceAll("[>][\\s]*[<]",">\n<");

        while(0 <= (index = str.indexOf("<"))) {
            if(str.charAt(index + 1) != '/') {

                boolean comment = false;
                if(str.charAt(index + 1) == '!')
                    comment = true;

                if(str.substring(index+1,index+5).equals("link"))
                    comment = true;

                String tabs = "";
                for(int i = 0;i<tab;i++)
                    tabs += "\t";

                ans.append(str.substring(0,index));
                ans.append(tabs);
                ans.append("<");
                str = str.substring(index+1);
                if(str.charAt(str.indexOf(">")-1) != '/')
                    tab++;

                if(comment)
                    tab--;
            }
            else {
                tab--;
                if(str.charAt(index-1) == '\n') {
                    String tabs = "";
                    for (int i = 0; i < tab; i++)
                        tabs += "\t";

                    ans.append(str.substring(0, index));
                    ans.append(tabs);
                    ans.append("<");
                    str = str.substring(index + 1);
                }
                else {
                    ans.append(str.substring(0, index));
                    ans.append("<");
                    str = str.substring(index + 1);
                }
            }
        }
        ans.append(str);

        return ans.toString();
    }

    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\){1,6}u([0-9]|[A-F]|[a-f]){4}");
        Matcher matcher = pattern.matcher(str);

        while(matcher.find()) {
            String tmp = matcher.group();
            str = str.replace(tmp, (char) (Integer.parseInt(tmp.substring(tmp.length() - 4), 16)) + "");
        }

        return str;
    }


    public String getHtmlCode() {
        return htmlCode;
    }

    public static String extractHtmlTag(String htmlCode, String tag, String attrs) throws NoSuchTagException {
        htmlCode = htmlCode.replaceAll(">[\n\t ]*<", "><");
        StringBuilder ans = new StringBuilder();

        Pattern pattern = Pattern.compile("<" + tag + "[^>]*" + attrs + "[^>]*>");
        Matcher matcher = pattern.matcher(htmlCode);
        if(!matcher.find())
            throw new NoSuchTagException();
        String nowTag = matcher.group();
        String endTag = "</" + tag + ">";

        htmlCode = htmlCode.substring(htmlCode.indexOf(nowTag));

        int subTagIndex = htmlCode.substring(1).indexOf("<" + tag) + 1;
        int endTagIndex = htmlCode.indexOf(endTag);

        if (subTagIndex <= 0 || subTagIndex > endTagIndex)
            return htmlCode.substring(0, endTagIndex) + endTag;
        else {
            do {
                String inFront = htmlCode.substring(0, subTagIndex);
                ans.append(inFront);
                htmlCode = htmlCode.replace(inFront, "");

                String wholeSubTag = extractHtmlTag(htmlCode, tag, "");
                ans.append(wholeSubTag);

                htmlCode = htmlCode.substring(wholeSubTag.length());

                subTagIndex = htmlCode.indexOf("<" + tag);
                endTagIndex = htmlCode.indexOf(endTag);
                try {
                    if (subTagIndex < 0 || subTagIndex > endTagIndex) {
                        ans.append(htmlCode.substring(0, endTagIndex));
                        ans.append(endTag);
                        break;
                    }
                } catch (StringIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                    Log.e("subTagIndex",subTagIndex + "");
                    Log.e("subTag","<" + tag);
                    Log.e("endTagIndex",endTagIndex + "");
                    Log.e("endTag",endTag);
                    Log.e("htmlCode",htmlCode);
                }
            } while (true);
        }

        return ans.toString();
    }

    public static class NoSuchTagException extends Exception {

    }
}

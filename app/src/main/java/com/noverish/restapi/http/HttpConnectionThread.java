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

        if(urlStr == null || urlStr.equals("")) {
            Log.e("HttpConnectionThread", "url is null or no length - " + urlStr);
            return;
        }

        if(urlStr.length() <= 5) {
            Log.e("HttpConnectionThread", "too short url - " + urlStr);
            return;
        }


        if (urlStr.substring(0, 5).matches("https")) {
            isItHttps = true;
        } else if (urlStr.substring(0, 5).matches("http:")) {
            isItHttps = false;
        } else {
            isItHttps = false;
            Log.e("HttpConnectionThread", "I don't know it is https or http - " + urlStr);
            return;
        }

        start();
    }

    public void run() {
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
        if(str == null) {
            Log.w("unicodeToString","str is null!");
            return "";
        }

        Pattern pattern = Pattern.compile("(\\\\){1,2}u([0-9]|[A-F]|[a-f]){4}");
        Matcher matcher = pattern.matcher(str);

        while(matcher.find()) {
            String tmp = matcher.group();
            str = str.replace(tmp, (char) (Integer.parseInt(tmp.substring(tmp.length() - 4), 16)) + "");
        }

        return str;
    }


    public String getHtmlCode() {
        try {
            join();
            return htmlCode;
        } catch (Exception ex) {
            return null;
        }
    }
}

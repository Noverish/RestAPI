package com.noverish.restapi.other;

import com.noverish.restapi.http.HttpConnectionThread;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;

/**
 * Created by Noverish on 2016-05-29.
 */
public class RestAPIClient extends Thread {
    final static String openWeatherURL = "http://dilab.korea.ac.kr/sigma/sigmaService/Bilingual/ResultServlet";
    private String content;
    private JSONObject json = null;
    private String htmlCode = null;

    private final String TAG = getClass().getSimpleName();

    public RestAPIClient(String content) {
        content = content.replaceAll("[\\s]","%20");
        this.content = content;
        start();
    }

    public void run() {
        //Log.e(TAG,content);
        String urlString = openWeatherURL + "?query='" + content + "'";

        try {
            // call API by using HTTPURLConnection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            htmlCode = getStringFromInputStream(in);
            //JSONObject json = new JSONObject(htmlCode);

            // parse JSON

            this.json = json;
        }catch(MalformedURLException e){
            System.err.println("Malformed URL");
            e.printStackTrace();
            this.json = null;
        }/*catch(JSONException e) {
            System.err.println("JSON parsing error");
            e.printStackTrace();
            this.json = null;
        }*/catch(IOException e){
            System.err.println("URL Connection failed");
            e.printStackTrace();
            this.json = null;
        }
    }

    private void joining() {
        try {
            join();
        } catch (InterruptedException inter) {
            System.err.println("join method failed");
            inter.printStackTrace();
        }
    }

    private JSONObject getJSON() {
        joining();
        return json;
    }

    public String getHtmlCode() {
        joining();
        htmlCode = HttpConnectionThread.processHtml(htmlCode);
        return htmlCode;
    }

    public SemanticClassification extract() {
        joining();

        if(htmlCode == null)
            return new SemanticClassification("0", "", "0");

        if (htmlCode.contains("There is no classification result"))
            return new SemanticClassification("0", "", "0");


        Pattern pattern = Pattern.compile("<td width=[\\d]{3}>[\\s\\S]*?<[/]td>");
        Matcher matcher = pattern.matcher(htmlCode);

        String[] strs = new String[3];
        for(int i = 0;i<3 && matcher.find();i++) {
            String group = matcher.group();
            //Log.e(TAG,group);
            strs[i] = group.replaceAll("<[^>]*>", "");
        }

        return new SemanticClassification(strs[0], strs[1], strs[2]);
    }

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public class SemanticClassification {
        public long matchCase;
        public String classification;
        public BigDecimal percentage;

        public SemanticClassification(String matchCase, String classification, String percentage) {
            matchCase = matchCase.replaceAll("[^\\d]","");
            percentage = percentage.replaceAll("[^\\d.]","");

            this.matchCase = Long.parseLong(matchCase);
            this.classification = classification;
            this.percentage = new BigDecimal(percentage);
        }

        @Override
        public String toString() {
            return "SemanticClassification{" +
                    "matchCase=" + matchCase +
                    ", classification='" + classification + '\'' +
                    ", percentage=" + percentage +
                    '}';
        }
    }

    public static String statusToString(Status status) {
        String content = status.toString();

        content = content.replaceAll("[{]","{\n");
        content = content.replaceAll(", ",",\n");
        content = content.replaceAll("HashtagEntityJSONImpl[{]\n","HashtagEntityJSONImpl{");

        return content;
    }
}

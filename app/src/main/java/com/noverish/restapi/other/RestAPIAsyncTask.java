package com.noverish.restapi.other;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Noverish on 2017-01-11.
 */

public class RestAPIAsyncTask extends AsyncTask<Void, Void, String> {
    public final static String SERVER_URL = "http://dilab.korea.ac.kr/sigma/sigmaService/Bilingual/ResultServlet";

    private String content;
    private TextView textView;

    private RestAPIAsyncTask(String content, TextView textView) {
        super();
        this.content = content;
        this.textView = textView;
    }

    public static void execute(String content, TextView textView) {
        RestAPIAsyncTask restAPIAsyncTask = new RestAPIAsyncTask(content, textView);
        restAPIAsyncTask.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        content = content.replaceAll("<a[\\s\\S]*?</a>","");
        content = content.replaceAll("<[^>]*>","");
        content = content.replaceAll("[\\s]+"," ");
        content = content.trim();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect(SERVER_URL)
                    .data("query", content)
                    .post();
            return document.select("td[width=\"200\"]").first().html().replaceAll("<[^>]*>","");
        } catch (Exception ex) {
//            ex.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("<RestAPI>",content + " => " + result);
        if(result.equals(""))
            textView.setVisibility(View.GONE);
        else
            textView.setText(result);
    }

    @Override
    protected void onCancelled(String aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}

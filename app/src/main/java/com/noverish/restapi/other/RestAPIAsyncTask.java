package com.noverish.restapi.other;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.noverish.restapi.activity.MainActivity;
import com.noverish.restapi.base.ArticleItem;
import com.noverish.restapi.base.ArticleView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import static com.noverish.restapi.activity.MainActivity.subMenu;

/**
 * Created by Noverish on 2017-01-11.
 */

public class RestAPIAsyncTask extends AsyncTask<Void, Void, String> {
    public final static String SERVER_URL = "http://dilab.korea.ac.kr/sigma/sigmaService/Bilingual/ResultServlet";
    public static ArrayList<String> categoryList = new ArrayList<>();

    private String content;
    private TextView textView;
    private ArticleItem item;
    private ArticleView view;

    private RestAPIAsyncTask(String content, TextView textView, ArticleItem item, ArticleView view) {
        super();
        this.content = content;
        this.textView = textView;
        this.item = item;
        this.view = view;
    }

    public static void execute(String content, TextView textView, ArticleItem item, ArticleView view) {
        RestAPIAsyncTask restAPIAsyncTask = new RestAPIAsyncTask(content, textView, item, view);
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

        if(result.equals("")) {
            textView.setVisibility(View.GONE);
            item.setCategory("");
        } else {
            String category = result.split("/")[1];
            if(!categoryList.contains(category)) {
                categoryList.add(category);
                subMenu.add(category);
            }

            item.setCategory(category);
            textView.setText(result);

            Log.d("<RestAPI>","now : " + MainActivity.instance.getNowCategory() + ", this : " + category);
            if(MainActivity.instance.getNowCategory().equals(category)) {
                view.setVisibility(View.VISIBLE);
            }
        }
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

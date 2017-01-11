package com.noverish.restapi.webview;

import android.os.AsyncTask;

/**
 * Created by cscoi019 on 2017. 1. 11..
 */

public class WriteAsyncTask extends AsyncTask<Void, Void, Void> {
    public String content;

    private WriteAsyncTask(String content) {
        this.content = content;
    }

    public static void execute(String content) {
        WriteAsyncTask task = new WriteAsyncTask(content);
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

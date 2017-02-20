package com.noverish.restapi.rss.cnn;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Noverish on 2017-02-21.
 */

public class CNNAsyncTask extends AsyncTask<Void, Void, ArrayList<CNNItem>> {
    private LinearLayout layout;

    public CNNAsyncTask(LinearLayout layout) {
        this.layout = layout;
    }

    @Override
    protected ArrayList<CNNItem> doInBackground(Void... params) {
        return RssCNNClient.getInstance().getItems(1);
    }

    @Override
    protected void onPostExecute(ArrayList<CNNItem> items) {
        Log.e("items",items.size() + "");
        super.onPostExecute(items);
        for(CNNItem item : items) {
            CNNView view = new CNNView(layout.getContext());
            view.setItem(item);
            layout.addView(view);
        }
    }
}

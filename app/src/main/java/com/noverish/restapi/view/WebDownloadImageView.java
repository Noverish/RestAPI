package com.noverish.restapi.view;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-09-19.
 */
public class WebDownloadImageView extends ImageView {
    private android.os.Handler handler;
    private Context context;
    private String imageUrl;

    public WebDownloadImageView(Context context, String imageUrl, android.os.Handler handler) {
        super(context);
        this.imageUrl = imageUrl;
        this.handler = handler;

        init();
    }

    private void init() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(context).load(imageUrl).into(WebDownloadImageView.this);
                    }
                });
            }
        });
        thread.start();
    }
}

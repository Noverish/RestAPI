package com.noverish.restapi.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-08-28.
 */
public class ImagePicassoThread extends Thread {
    private Context context;
    private ImageView imageView;
    private String url;
    private android.os.Handler handler = new android.os.Handler();

    public ImagePicassoThread(Context context, String url) {
        this.context = context;
        this.url = url;
        this.imageView = new ImageView(context);

        start();
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context).load(url).into(imageView);
                try {
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    Log.d("imageView", image.getHeight() + " " + image.getWidth());
                } catch (Exception ex) {
                    Log.d("imageView", "null");
                }
                HttpImageThread thread = new HttpImageThread(url);
            }
        });
    }

    public ImageView getImageView() {
        try {
            join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return imageView;
    }
}

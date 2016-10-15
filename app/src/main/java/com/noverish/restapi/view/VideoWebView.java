package com.noverish.restapi.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.noverish.restapi.R;
import com.noverish.restapi.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-10-15.
 */

public class VideoWebView extends FrameLayout implements View.OnClickListener{
    private Context context;
    private String imageUrl;
    private String videoUrl;

    public VideoWebView(Context context, String imageUrl, String videoUrl) {
        super(context);
        this.context = context;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    private void init() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(imageUrl).into(imageView);
        addView(imageView);

        ImageView playButton = new ImageView(context);
        playButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.play_button));
        playButton.setLayoutParams(params);
        addView(playButton);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url",videoUrl);
        context.startActivity(intent);
    }
}

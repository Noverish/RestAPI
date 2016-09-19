package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;
import com.noverish.restapi.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-05-30.
 */
public class TwitterArticleView extends LinearLayout {
    private Context context;
    private android.os.Handler handler;

    private LinearLayout mediaLayout;

    private TwitterArticleItem item;

    public TwitterArticleView(Context context, TwitterArticleItem item, android.os.Handler handler) {
        super(context);
        this.item = item;
        this.context = context;
        this.handler = handler;

        if(!isInEditMode()) {
            init();
        }
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.twitter_article_view, this, true);


        TextView classificationTextView = (TextView) findViewById(R.id.twitter_article_view_classification);
        RestAPIClient.getInstance().process(item.getContent(), classificationTextView);

        TextView headerTextView = (TextView) findViewById(R.id.twitter_article_view_header);
        if(item.getHeader() == null || item.getHeader().equals("")) {
            headerTextView.setVisibility(GONE);
        }
        else {
            headerTextView.setText(item.getHeader());
        }

        TextView contentTextTextView = (TextView) findViewById(R.id.twitter_article_view_content);
        contentTextTextView.setText(item.getContent());

        ImageView profileImageView = (ImageView) findViewById(R.id.twitter_article_view_profile_image_view);
        Picasso.with(context).load(item.getProfileImageUrl()).into(profileImageView);

        TextView nameTextView = (TextView) findViewById(R.id.twitter_article_view_name);
        nameTextView.setText(item.getFullName());

        TextView screenNameTextView = (TextView) findViewById(R.id.twitter_article_view_screen_name);
        screenNameTextView.setText(item.getScreenName());

        TextView timeTextView = (TextView) findViewById(R.id.twitter_article_view_time);
        timeTextView.setText(item.getTime());

        /*mediaLayout = (LinearLayout) findViewById(R.id.twitter_article_view_media_layout);
        if(item.getMedia() != null && !item.getMedia().equals("")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpConnectionThread thread1 = new HttpConnectionThread(item.getMedia(), "", "GET");
                    Elements imageElements = Jsoup.parse(thread1.getHtmlCode()).select("div.media").first().select("img");

                    for (Element ele : imageElements) {
                        mediaLayout.addView(new WebDownloadImageView(context, ele.attr("src"), handler));
                    }
                }
            });
            thread.start();
        }*/

        Button replyButton = (Button) findViewById(R.id.twitter_article_view_reply);
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getReplyUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });

        Button retweetButton = (Button) findViewById(R.id.twitter_article_view_retweet);
        retweetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getRetweetUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });

        Button favoriteButton = (Button) findViewById(R.id.twitter_article_view_favorite);
        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getFavoriteUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });
    }
}

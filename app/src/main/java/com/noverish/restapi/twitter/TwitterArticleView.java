package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by Noverish on 2016-05-30.
 */
public class TwitterArticleView extends LinearLayout {
    private Context context;
    private android.os.Handler handler;

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
        inflater.inflate(R.layout.article_twitter, this, true);

        OnClickListener goToPoster = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",item.getPosterUrl());
                context.startActivity(intent);
            }
        };

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
        profileImageView.setOnClickListener(goToPoster);

        TextView nameTextView = (TextView) findViewById(R.id.twitter_article_view_name);
        nameTextView.setText(item.getFullName());
        nameTextView.setOnClickListener(goToPoster);

        TextView screenNameTextView = (TextView) findViewById(R.id.twitter_article_view_screen_name);
        screenNameTextView.setText(item.getScreenName());
        screenNameTextView.setOnClickListener(goToPoster);

        TextView timeTextView = (TextView) findViewById(R.id.twitter_article_view_time);
        timeTextView.setText(item.getTimeString());

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.twitter_article_view_media_layout);
        try {
            Long tweetId = Long.parseLong(item.getMedia().split("[/]")[5]);
            Thread thread = new Thread(new CustomRunnable(mediaLayout, tweetId));
            thread.start();
        } catch (Exception ex) {

        }

        Button replyButton = (Button) findViewById(R.id.twitter_article_view_reply);
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + item.getReplyUrl());
                context.startActivity(intent);
            }
        });

        Button retweetButton = (Button) findViewById(R.id.twitter_article_view_retweet);
        retweetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + item.getRetweetUrl());
                context.startActivity(intent);
            }
        });
        if(item.isRetweeted()) {
            retweetButton.setTextColor(ContextCompat.getColor(context, R.color.twitter_retweet));
            retweetButton.setText(R.string.twitter_article_view_retweetd);
        }

        Button favoriteButton = (Button) findViewById(R.id.twitter_article_view_favorite);
        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getFavoriteUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });
        if(item.isFavorited()) {
            favoriteButton.setTextColor(ContextCompat.getColor(context, R.color.twitter_favorite));
            favoriteButton.setText(R.string.twitter_article_view_favorited);
        }
    }

    class CustomRunnable implements Runnable {
        LinearLayout mediaLayout;
        long tweetId;

        CustomRunnable(LinearLayout mediaLayout, long tweetId) {
            this.mediaLayout = mediaLayout;
            this.tweetId = tweetId;
        }

        @Override
        public void run() {
            Twitter4jClient client = Twitter4jClient.getInstance();

            Status status = client.getStatusById(tweetId);

            MediaEntity[] entities = status.getMediaEntities();
            for(MediaEntity entity : entities) {
                final String imageUrl = entity.getMediaURL();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = new ImageView(context);
                        Picasso.with(context).load(imageUrl).into(imageView);
                        mediaLayout.addView(imageView);
                    }
                });
            }
        }
    }
}

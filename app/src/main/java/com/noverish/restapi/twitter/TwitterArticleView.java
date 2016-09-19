package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class TwitterArticleView extends LinearLayout implements View.OnClickListener{
    private Context context;

    public static Status nowSelectedStatus;
    private Status status;
    private TwitterArticleItem item;

    private final String TAG = getClass().getSimpleName();

    private ImageView profileImageView;
    private LinearLayout mediaLayout;

    public TwitterArticleView(Context context, Status nowSelectedStatus) {
        super(context);
        this.status = nowSelectedStatus;

        if(!isInEditMode()) {
            init(context);
        }
    }

    public TwitterArticleView(Context context, TwitterArticleItem item) {
        super(context);
        this.item = item;

        this.context = context;

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.twitter_article_view, this, true);


        TextView isRetweetedTextView = (TextView) findViewById(R.id.twitter_article_view_is_retweeted);
        TextView contentTextTextView = (TextView) findViewById(R.id.twitter_article_view_content);
        profileImageView = (ImageView) findViewById(R.id.twitter_article_view_profile_image_view);
        TextView nameTextView = (TextView) findViewById(R.id.twitter_article_view_name);
        TextView screenNameTextView = (TextView) findViewById(R.id.twitter_article_view_screen_name);
        TextView classificationTextView = (TextView) findViewById(R.id.twitter_article_view_classification);
        TextView timeTextView = (TextView) findViewById(R.id.twitter_article_view_time);
        mediaLayout = (LinearLayout) findViewById(R.id.twitter_article_view_media_layout);

        if(item.getHeader() == null || item.getHeader().equals(""))
            isRetweetedTextView.setVisibility(GONE);
        else
            isRetweetedTextView.setText(item.getHeader());

        contentTextTextView.setText(item.getContent());
        nameTextView.setText(item.getFullName());
        screenNameTextView.setText(item.getScreenName());

        Picasso.with(context).load(item.getProfileImageUrl()).into(profileImageView);

        RestAPIClient.getInstance().process(item.getContent(), classificationTextView);

        timeTextView.setText(item.getTime());

        Button replyButton = (Button) findViewById(R.id.twitter_article_view_reply);
        Button retweetButton = (Button) findViewById(R.id.twitter_article_view_retweet);
        Button favoriteButton = (Button) findViewById(R.id.twitter_article_view_favorite);

        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getReplyUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });

        retweetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getRetweetUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });

        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getFavoriteUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });

    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.twitter_article_view, this, true);

        setOnClickListener(this);

        setStatus(status);
    }

    public void setStatus(Status status) {
        if(status.getRetweetedStatus() != null) {
            TextView isRetweetedTextView = (TextView) findViewById(R.id.twitter_article_view_is_retweeted);
            isRetweetedTextView.setText(status.getUser().getName() + " 님이 리트윗하셨습니다");

            status = status.getRetweetedStatus();
        } else {
            TextView isRetweetedTextView = (TextView) findViewById(R.id.twitter_article_view_is_retweeted);
            isRetweetedTextView.setVisibility(GONE);
        }

        String realText = status.getText();
        realText = realText.replaceAll("https://t[.]co[\\S]*","");

        TextView contentTextTextView = (TextView) findViewById(R.id.twitter_article_view_content);
        contentTextTextView.setText(realText);

        profileImageView = (ImageView) findViewById(R.id.twitter_article_view_profile_image_view);

        TextView nameTextView = (TextView) findViewById(R.id.twitter_article_view_name);
        nameTextView.setText(status.getUser().getName());

        TextView screenNameTextView = (TextView) findViewById(R.id.twitter_article_view_screen_name);
        screenNameTextView.setText("@" + status.getUser().getScreenName());


        TextView classificationTextView = (TextView) findViewById(R.id.twitter_article_view_classification);
        RestAPIClient.getInstance().process(realText.replaceAll("#[\\S]*",""), classificationTextView);

        TextView timeTextView = (TextView) findViewById(R.id.twitter_article_view_time);
        timeTextView.setText(status.getCreatedAt().toString());

        mediaLayout = (LinearLayout) findViewById(R.id.twitter_article_view_media_layout);
    }

    public void downloadImages() {
        Picasso.with(context).load(status.getUser().getProfileImageURL()).into(profileImageView);

        for(MediaEntity m : status.getExtendedMediaEntities()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView mediaImageView = new ImageView(context);
            mediaImageView.setLayoutParams(layoutParams);
            Picasso.with(context).load(m.getMediaURL()).into(mediaImageView);

            mediaLayout.addView(mediaImageView);
        }
    }

    @Override
    public void onClick(View v) {
        nowSelectedStatus = status;
        Intent intent = new Intent(context, ReplyTwitterActivity.class);
        context.startActivity(intent);
    }
}

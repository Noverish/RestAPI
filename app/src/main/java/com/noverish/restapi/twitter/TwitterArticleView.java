package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.activity.MainActivity;
import com.noverish.restapi.base.ArticleView;
import com.noverish.restapi.other.RestAPIAsyncTask;
import com.noverish.restapi.view.VideoWebView;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageFinishedListener;
import com.noverish.restapi.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

import static com.noverish.restapi.activity.MainActivity.Status.ARTICLE;

/**
 * Created by Noverish on 2016-05-30.
 */
public class TwitterArticleView extends ArticleView implements View.OnClickListener{
    private Context context;
    private TwitterArticleItem item;

    public TwitterArticleView(Context context, TwitterArticleItem item) {
        super(context, item);
        this.item = item;
        this.context = context;
        this.setOnClickListener(this);

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
        RestAPIAsyncTask.execute(item.getContent(), classificationTextView, item, this);

        TextView headerTextView = (TextView) findViewById(R.id.twitter_article_view_header);
        if(item.getHeader() == null || item.getHeader().equals("")) {
            headerTextView.setVisibility(GONE);
        }
        else {
            headerTextView.setText(item.getHeader());
        }

        TextView contentTextTextView = (TextView) findViewById(R.id.twitter_article_view_content);
        contentTextTextView.setClickable(true);
        contentTextTextView.setMovementMethod(LinkMovementMethod.getInstance());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            contentTextTextView.setText(Html.fromHtml(item.getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            contentTextTextView.setText(Html.fromHtml(item.getContent()));
        }


        ImageView profileImageView = (ImageView) findViewById(R.id.twitter_article_view_profile_image_view);
        if(item.getProfileImageUrl() != null && !item.getProfileImageUrl().equals(""))
            Picasso.with(context).load(item.getProfileImageUrl()).into(profileImageView);
        profileImageView.setOnClickListener(goToPoster);

        TextView nameTextView = (TextView) findViewById(R.id.twitter_article_view_name);
        nameTextView.setText(item.getName());
        nameTextView.setOnClickListener(goToPoster);

        TextView screenNameTextView = (TextView) findViewById(R.id.twitter_article_view_screen_name);
        screenNameTextView.setText(item.getScreenName());
        screenNameTextView.setOnClickListener(goToPoster);

        TextView timeTextView = (TextView) findViewById(R.id.twitter_article_view_time);
        timeTextView.setText(item.getTimeString());

        LinearLayout linkLayout = (LinearLayout) findViewById(R.id.twitter_article_view_link_layout);
        if(item.getLinkUrl() != null) {
            ImageView linkImg = (ImageView) findViewById(R.id.twitter_article_view_link_img);
            TextView linkContent = (TextView) findViewById(R.id.twitter_article_view_link_content);
            TextView linkDomain = (TextView) findViewById(R.id.twitter_article_view_link_domain);

            Picasso.with(context).load(item.getImageUrls().get(0)).into(linkImg);
            linkContent.setText(item.getLinkContent());
            linkDomain.setText(item.getLinkDomain());
        } else {
            linkLayout.setVisibility(GONE);

            LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.twitter_article_view_media_layout);
            if(item.getVideoUrl() != null) {
                mediaLayout.addView(new VideoWebView(context, item.getImageUrls().get(0), item.getVideoUrl()));
            } else if(item.getImageUrls().size() != 0) {
                for(String url : item.getImageUrls()) {
                    ImageView image = new ImageView(context);
                    Picasso.with(context).load(url).into(image);
                    mediaLayout.addView(image);
                }
            } else {
                mediaLayout.setVisibility(GONE);
            }
        }

        ImageView replyButton = (ImageView) findViewById(R.id.twitter_article_view_reply);
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + item.getReplyUrl());
                context.startActivity(intent);
            }
        });

        ImageView retweetButton = (ImageView) findViewById(R.id.twitter_article_view_retweet);
        retweetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + item.getRetweetUrl());
                context.startActivity(intent);
            }
        });
        TextView retweetNum = (TextView) findViewById(R.id.article_twitter_retweet_number);
        if(item.getRetweetNumber() > 0)
            retweetNum.setText(String.valueOf(item.getRetweetNumber()));
        else
            retweetNum.setVisibility(GONE);
        if(item.isRetweeted()) {
            retweetButton.setImageResource(R.drawable.icon_twitter_arti_retweet_active);
        }

        ImageView favoriteButton = (ImageView) findViewById(R.id.twitter_article_view_favorite);
        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TwitterArticleView.this.context, WebViewActivity.class);
                intent.putExtra("url","https://mobile.twitter.com" + TwitterArticleView.this.item.getFavoriteUrl());
                TwitterArticleView.this.context.startActivity(intent);
            }
        });
        TextView favoriteNum = (TextView) findViewById(R.id.article_twitter_favorite_number);
        if(item.getFavoriteNumber() > 0)
            favoriteNum.setText(String.valueOf(item.getFavoriteNumber()));
        else
            favoriteNum.setVisibility(GONE);
        if(item.isFavorited()) {
            favoriteButton.setImageResource(R.drawable.icon_twitter_arti_favorite_active);
        }
    }

    @Override
    public void onClick(View view) {
        OnPageFinishedListener listener = new OnPageFinishedListener() {
            @Override
            public void onPageFinished(HtmlParseWebView webView, String url) {
                MainActivity.instance.setStatus(ARTICLE);
                MainActivity.instance.changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                MainActivity.instance.fab.setVisibility(INVISIBLE);
            }
        };
        MainActivity.instance.anotherWebView.loadUrl(item.getArticleUrl(), null, null, listener, HtmlParseWebView.SNSType.Twitter);
    }
}

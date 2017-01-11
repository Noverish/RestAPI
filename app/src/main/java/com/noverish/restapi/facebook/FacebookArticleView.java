package com.noverish.restapi.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIAsyncTask;
import com.noverish.restapi.view.VideoWebView;
import com.noverish.restapi.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleView extends LinearLayout {
    private Context context;
    private FacebookArticleItem article;
    private android.os.Handler handler;

    public FacebookArticleView(Context context, FacebookArticleItem article) {
        super(context);
        this.context = context;
        this.article = article;
        handler = new Handler();

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.article_facebook, this);

        OnClickListener goToPoster = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url",article.getPosterUrl());
                context.startActivity(intent);
            }
        };

        TextView classification = (TextView) findViewById(R.id.article_facebook_classification);
        RestAPIAsyncTask.execute(article.getContent(), classification, article);

        TextView header = (TextView) findViewById(R.id.facebook_article_header);
        if(article.getHeader() == null || article.getHeader().equals(""))
            header.setVisibility(GONE);
        else
            header.setText(article.getHeader());

        ImageView profileImg = (ImageView) findViewById(R.id.facebook_article_profile_img);
        Picasso.with(context).load(article.getProfileImgUrl()).into(profileImg);
        profileImg.setOnClickListener(goToPoster);

        TextView title = (TextView) findViewById(R.id.facebook_article_title);
        title.setText(article.getTitle());
        title.setOnClickListener(goToPoster);

        TextView time = (TextView) findViewById(R.id.facebook_article_time);
        time.setText(article.getTimeString());

        TextView content = (TextView) findViewById(R.id.facebook_article_content);
        content.setText(article.getContent());

        TextView sympathy = (TextView) findViewById(R.id.facebook_article_sympathy);
        sympathy.setText(article.getSympathyNum());

        TextView comment = (TextView) findViewById(R.id.facebook_article_comment);
        comment.setText(article.getCommentNum());

        TextView sharing = (TextView) findViewById(R.id.facebook_article_sharing);
        sharing.setText(article.getSharingNum());

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.facebook_article_media_layout);
        if(article.getImageUrls() != null) {
            for (String url : article.getImageUrls()) {
                ImageView image = new ImageView(context);
                Picasso.with(context).load(url).into(image);
                mediaLayout.addView(image);
            }
        }

        if(article.getVideo() != null) {
            VideoWebView videoWebView = new VideoWebView(context, article.getVideo().first, article.getVideo().second);
            mediaLayout.addView(videoWebView);
        }

        Button like = (Button) findViewById(R.id.facebook_article_like_button);
        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(article.isLiked()) {
                    Toast.makeText(context, "좋아요 취소는 아직 지원하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println(article.getLikeUrl());
                    try {
                        Document document = Jsoup.connect(article.getLikeUrl()).get();
                        Log.i("Document",document.outerHtml());
                        article.setLiked(true);
                        makeButtonLiked();
                    } catch (Exception ex) {
                        Toast.makeText(context, "연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(article.isLiked()) {
            makeButtonLiked();
        }
    }

    private void makeButtonLiked() {
        ImageView likeIcon = (ImageView) findViewById(R.id.article_facebook_like_icon);
        TextView likeText = (TextView) findViewById(R.id.article_facebook_like_text);

        likeIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_facebook_like_true));
        likeText.setTextColor(ContextCompat.getColor(context, R.color.facebook));
    }
}

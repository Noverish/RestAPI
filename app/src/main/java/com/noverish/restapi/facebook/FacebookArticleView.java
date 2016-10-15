package com.noverish.restapi.facebook;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;
import com.noverish.restapi.view.VideoWebView;
import com.squareup.picasso.Picasso;

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

        TextView classification = (TextView) findViewById(R.id.article_facebook_classification);
        RestAPIClient.getInstance().process(article.getContent(), classification);

        TextView header = (TextView) findViewById(R.id.facebook_article_header);
        if(article.getHeader() == null || article.getHeader().equals(""))
            header.setVisibility(GONE);
        else
            header.setText(article.getHeader());

        ImageView profileImg = (ImageView) findViewById(R.id.facebook_article_profile_img);
        Picasso.with(context).load(article.getProfileImgUrl()).into(profileImg);

        TextView title = (TextView) findViewById(R.id.facebook_article_title);
        title.setText(article.getTitle());

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
        if(Math.random() < 0.4)
            like.setTextColor(ContextCompat.getColor(context, R.color.facebook));
    }
}

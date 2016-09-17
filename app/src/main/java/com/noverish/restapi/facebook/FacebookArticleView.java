package com.noverish.restapi.facebook;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleView extends LinearLayout {
    private FacebookArticleItem article;
    private android.os.Handler handler;

    public FacebookArticleView(Context context,FacebookArticleItem article) {
        super(context);

        this.article = article;
        handler = new Handler();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.article_facebook, this);

        TextView classification = (TextView) findViewById(R.id.article_facebook_classification);
        RestAPIClient client = new RestAPIClient(article.content);
        RestAPIClient.SemanticClassification sc = client.extract();
        if(!sc.classification.equals(""))
            classification.setText(sc.classification);
        else
            classification.setVisibility(GONE);

        TextView header = (TextView) findViewById(R.id.facebook_article_header);
        header.setText(article.header);

        ImageView profileImg = (ImageView) findViewById(R.id.facebook_article_profile_img);
        Picasso.with(context).load(article.profileImgUrl).into(profileImg);

        TextView title = (TextView) findViewById(R.id.facebook_article_title);
        title.setText(article.title);

        TextView time = (TextView) findViewById(R.id.facebook_article_time);
        time.setText(article.time);

        TextView content = (TextView) findViewById(R.id.facebook_article_content);
        content.setText(article.content);

        TextView sympathy = (TextView) findViewById(R.id.facebook_article_sympathy);
        sympathy.setText(article.sympathyNum);

        TextView comment = (TextView) findViewById(R.id.facebook_article_comment);
        comment.setText(article.commentNum);

        TextView sharing = (TextView) findViewById(R.id.facebook_article_sharing);
        sharing.setText(article.sharingNum);

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.facebook_article_media_layout);
        if(article.media != null) {
            for (String url : article.media) {
                ImageView image = new ImageView(context);
                Picasso.with(context).load(url).into(image);
                mediaLayout.addView(image);
            }
        }
    }
}

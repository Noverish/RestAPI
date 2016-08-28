package com.noverish.restapi.kakao;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2016-08-28.
 */
public class KakaoArticleView extends LinearLayout {
    private KakaoArticleItem article;
    private android.os.Handler handler;

    public KakaoArticleView(Context context, KakaoArticleItem article) {
        super(context);

        this.article = article;
        handler = new Handler();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.article_kakao, this);

        WebView profileImg = (WebView) findViewById(R.id.kakao_article_profile_img);
        profileImg.loadUrl(article.profileImgUrl);

        TextView title = (TextView) findViewById(R.id.kakao_article_title);
        title.setText(article.title);

        TextView time = (TextView) findViewById(R.id.kakao_article_time);
        time.setText(article.time);

        TextView content = (TextView) findViewById(R.id.kakao_article_content);
        content.setText(article.content);

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.kakao_article_media_layout);
        if(article.imageUrls != null) {
            for (String url : article.imageUrls) {
                ImageView image = new ImageView(context);
                Picasso.with(context).load(url).into(image);
                mediaLayout.addView(image);
            }
        }
    }
}

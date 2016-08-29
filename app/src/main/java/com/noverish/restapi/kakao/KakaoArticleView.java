package com.noverish.restapi.kakao;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;

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

        int width1=40;
        int height1=40;
        String data1="<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+width1+", initial-scale=0.65 \" /></head>";
        data1=data1+"<body><center><img width=\""+width1+"\" height=\""+height1+"\" src=\""+article.profileImgUrl+"\" /></center></body></html>";
        profileImg.loadData(data1, "text/html", null);


        TextView title = (TextView) findViewById(R.id.kakao_article_title);
        title.setText(article.title);

        TextView time = (TextView) findViewById(R.id.kakao_article_time);
        time.setText(article.time);

        TextView content = (TextView) findViewById(R.id.kakao_article_content);
        content.setText(article.content);

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.kakao_article_media_layout);
        if(article.imageUrls != null) {
            for (String url : article.imageUrls) {

                int width=300;
                int height=300;




                WebView image = new WebView(context);

                String data="<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+width+", initial-scale=0.65 \" /></head>";
                data=data+"<body><center><img width=\""+width+"\" height=\""+height+"\" src=\""+url+"\" /></center></body></html>";
                image.loadData(data, "text/html", null);

                mediaLayout.addView(image);

                /*ImageView image = new ImageView(context);
                Picasso.with(context).load(url).into(image);
                mediaLayout.addView(image);*/
            }
        }
    }
}

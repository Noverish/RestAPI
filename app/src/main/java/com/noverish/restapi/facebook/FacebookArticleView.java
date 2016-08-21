package com.noverish.restapi.facebook;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.http.HttpImageThread;

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
        inflater.inflate(R.layout.facebook_article, this);

        TextView header = (TextView) findViewById(R.id.facebook_article_header);
        header.setText(article.header.msg);

        ImageView profileImg = (ImageView) findViewById(R.id.facebook_article_profile_img);
        new setImageThread(profileImg, article.title.profileImgUrl);

        TextView title = (TextView) findViewById(R.id.facebook_article_title);
        title.setText(article.title.title);

        TextView time = (TextView) findViewById(R.id.facebook_article_time);
        time.setText(article.title.postedTime);

        TextView content = (TextView) findViewById(R.id.facebook_article_content);
        content.setText(article.body.content);

        TextView sympathy = (TextView) findViewById(R.id.facebook_article_sympathy);
        sympathy.setText(article.footer.sympathyNum);

        TextView comment = (TextView) findViewById(R.id.facebook_article_comment);
        comment.setText(article.footer.commentNum);

        TextView sharing = (TextView) findViewById(R.id.facebook_article_sharing);
        sharing.setText(article.footer.sharingNum);

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.facebook_article_media_layout);
        for(String url : article.media.urls) {
            ImageView image = new ImageView(context);
            new setImageThread(image, url);
            mediaLayout.addView(image);
        }
    }

    private class setImageThread extends Thread {
        private ImageView view;
        private String url;
        private HttpImageThread thread;

        private setImageThread(ImageView view, String url) {
            this.view = view;
            this.url = url;
            start();
        }

        @Override
        public void run() {
             thread = new HttpImageThread(url);
            try {
                thread.start();
                thread.join();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setImageBitmap(thread.getImage());
                    }
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

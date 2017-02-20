package com.noverish.restapi.rss.cnn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.base.BaseView;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2017-02-21.
 */

public class CNNView extends BaseView {
    private CNNItem item;

    private TextView category;
    private TextView title;
    private TextView date;
    private TextView content;
    private LinearLayout media;

    public CNNView(Context context) {
        super(context);
        init();
    }

    public CNNView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.article_cnn, this, true);

        category = (TextView) findViewById(R.id.article_category);
        title = (TextView) findViewById(R.id.article_rss_title);
        date = (TextView) findViewById(R.id.article_rss_date);
        content = (TextView) findViewById(R.id.article_rss_content);
        media = (LinearLayout) findViewById(R.id.article_rss_media);
    }

    public void setItem(CNNItem item) {
        this.item = item;

        setTitle(item.getTitle());
        setDate(item.getDate());
        setContent(item.getContent());
        setImgs(item.getImg());
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDate(String date) {
        this.date.setText(date);
    }

    public void setContent(String content) {
        this.content.setText(content);
//        new RestAPIAsyncTask(content, this.category, item, this).execute();
    }

    public void setImgs(String img) {
        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(img).into(imageView);
        media.addView(imageView);
    }
}

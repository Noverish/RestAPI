package com.noverish.restapi.facebook;

import android.content.Context;
import android.content.Intent;
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
import com.noverish.restapi.activity.MainActivity;
import com.noverish.restapi.base.ArticleView;
import com.noverish.restapi.other.RestAPIAsyncTask;
import com.noverish.restapi.view.VideoWebView;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageFinishedListener;
import com.noverish.restapi.webview.WebViewActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static com.noverish.restapi.activity.MainActivity.Status.ARTICLE;

/**
 * Created by Noverish on 2016-07-17.
 */
public class FacebookArticleView extends ArticleView implements View.OnClickListener{
    private Context context;
    private FacebookArticleItem item;

    public FacebookArticleView(Context context, FacebookArticleItem item) {
        super(context, item);
        this.context = context;
        this.item = item;
        this.setOnClickListener(this);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.article_facebook, this);

        OnClickListener goToPoster = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", item.getPosterUrl());
                context.startActivity(intent);
            }
        };

        TextView classification = (TextView) findViewById(R.id.article_facebook_classification);
        RestAPIAsyncTask.execute(item.getContent(), classification, item, this);

        TextView header = (TextView) findViewById(R.id.facebook_article_header);
        if(item.getHeader() == null || item.getHeader().equals(""))
            header.setVisibility(GONE);
        else
            header.setText(item.getHeader());

        ImageView profileImg = (ImageView) findViewById(R.id.facebook_article_profile_img);
        Picasso.with(context).load(item.getProfileImgUrl()).into(profileImg);
        profileImg.setOnClickListener(goToPoster);

        TextView title = (TextView) findViewById(R.id.facebook_article_title);
        title.setText(item.getTitle());
        title.setOnClickListener(goToPoster);

        TextView time = (TextView) findViewById(R.id.facebook_article_time);
        time.setText(item.getTimeString());

        TextView content = (TextView) findViewById(R.id.facebook_article_content);
        content.setText(item.getContent());

        TextView sympathy = (TextView) findViewById(R.id.facebook_article_sympathy);
        sympathy.setText(item.getSympathyNum());

        TextView comment = (TextView) findViewById(R.id.facebook_article_comment);
        comment.setText(item.getCommentNum());

        TextView sharing = (TextView) findViewById(R.id.facebook_article_sharing);
        sharing.setText(item.getSharingNum());

        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.facebook_article_media_layout);
        if(item.getImageUrls() != null) {
            for (String url : item.getImageUrls()) {
                ImageView image = new ImageView(context);
                Picasso.with(context).load(url).into(image);
                mediaLayout.addView(image);
            }
        }

        if(item.getVideo() != null) {
            VideoWebView videoWebView = new VideoWebView(context, item.getVideo().first, item.getVideo().second);
            mediaLayout.addView(videoWebView);
        }

        Button like = (Button) findViewById(R.id.facebook_article_like_button);
        like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.isLiked()) {
                    Toast.makeText(context, "좋아요 취소는 아직 지원하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println(item.getLikeUrl());
                    try {
                        Document document = Jsoup.connect(item.getLikeUrl()).get();
                        Log.i("Document",document.outerHtml());
                        item.setLiked(true);
                        makeButtonLiked();
                    } catch (Exception ex) {
                        Toast.makeText(context, "연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(item.isLiked()) {
            makeButtonLiked();
        }
    }

    private void makeButtonLiked() {
        ImageView likeIcon = (ImageView) findViewById(R.id.article_facebook_like_icon);
        TextView likeText = (TextView) findViewById(R.id.article_facebook_like_text);

        likeIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_facebook_like_true));
        likeText.setTextColor(ContextCompat.getColor(context, R.color.facebook));
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
        MainActivity.instance.anotherWebView.loadUrl(item.getArticleUrl(), null, null, listener, HtmlParseWebView.SNSType.Facebook);
    }
}

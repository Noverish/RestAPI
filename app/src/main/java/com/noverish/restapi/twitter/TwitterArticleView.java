package com.noverish.restapi.twitter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by Noverish on 2016-05-30.
 */
public class TwitterArticleView extends LinearLayout implements View.OnClickListener{
    private Context context;

    public static Status nowSelectedStatus;
    private Status status;

    private final String TAG = getClass().getSimpleName();

    public TwitterArticleView(Context context, Status nowSelectedStatus) {
        super(context);
        this.status = nowSelectedStatus;

        if(!isInEditMode()) {
            init(context);
        }

        setStatus(nowSelectedStatus);
    }

    public TwitterArticleView(Context context) {
        super(context);

        if(!isInEditMode()) {
            init(context);
        }
    }

    public TwitterArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.status_view, this, true);

        setOnClickListener(this);
    }

    public void setStatus(Status status) {
        if(status.getRetweetedStatus() != null) {
            TextView isRetweetedTextView = (TextView) findViewById(R.id.status_view_is_retweeted);
            isRetweetedTextView.setText(status.getUser().getName() + " 님이 리트윗하셨습니다");

            status = status.getRetweetedStatus();
        } else {
            TextView isRetweetedTextView = (TextView) findViewById(R.id.status_view_is_retweeted);
            isRetweetedTextView.setVisibility(GONE);
        }

        String realText = status.getText();
        realText = realText.replaceAll("https://t[.]co[\\S]*","");

        TextView contentTextTextView = (TextView) findViewById(R.id.status_view_temporary_text_view);
        contentTextTextView.setText(realText);

        ImageView profileImageView = (ImageView) findViewById(R.id.status_view_profile_image_view);
        Picasso.with(context).load(status.getUser().getProfileImageURL()).into(profileImageView);

        TextView nameTextView = (TextView) findViewById(R.id.status_view_name_text_view);
        nameTextView.setText(status.getUser().getName());

        TextView screenNameTextView = (TextView) findViewById(R.id.status_view_screen_name_text_view);
        screenNameTextView.setText("@" + status.getUser().getScreenName());

        RestAPIClient restAPIClient = new RestAPIClient(realText.replaceAll("#[\\S]*",""));
        RestAPIClient.SemanticClassification sc = restAPIClient.extract();
        if(!sc.classification.equals("")) {
            TextView classificationTextView = (TextView) findViewById(R.id.status_view_classification);
            classificationTextView.setText(sc.classification);
        } else {
            TextView classificationTextView = (TextView) findViewById(R.id.status_view_classification);
            classificationTextView.setVisibility(GONE);
        }



/*
        Pattern pattern = Pattern.compile("http[\\S]*");
        Matcher matcher = pattern.matcher(status.getText());

        while(matcher.find()) {
            String url = matcher.group();
            Log.e(TAG,url);
        }
*/
        LinearLayout mediaLayout = (LinearLayout) findViewById(R.id.status_view_media_layout);
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


    public class ImageThread extends Thread {
        public Bitmap bitmap;
        public String src;

        public ImageThread(String url) {
            src = url;

            run();
        }

        @Override
        public void run() {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
            }
        }

        public Bitmap getImage() {
            try {
                join();
            } catch (InterruptedException inter) {
                inter.printStackTrace();
                return null;
            }

            return bitmap;
        }
    }
}

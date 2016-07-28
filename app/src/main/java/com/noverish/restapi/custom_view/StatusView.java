package com.noverish.restapi.custom_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.other.RestAPIClient;
import com.noverish.restapi.http.HttpConnectionThread;
import com.noverish.restapi.twitter.ReplyTwitterActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by Noverish on 2016-05-30.
 */
public class StatusView extends LinearLayout implements View.OnClickListener{
    private Context context;

    public static Status nowSelectedStatus;
    private Status status;

    private final String TAG = getClass().getSimpleName();

    public StatusView(Context context, Status nowSelectedStatus) {
        super(context);
        this.status = nowSelectedStatus;

        if(!isInEditMode()) {
            init(context);
        }

        setStatus(nowSelectedStatus);
    }

    public StatusView(Context context) {
        super(context);

        if(!isInEditMode()) {
            init(context);
        }
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.status_view, this, true);



        setPadding(0, 0, 0, 50);

        setOnClickListener(this);
    }

    public void setStatus(Status status) {
        if(status.getRetweetedStatus() != null) {
            TextView isRetweetedTextView = (TextView) findViewById(R.id.status_view_is_retweeted);
            isRetweetedTextView.setText(status.getUser().getName() + " 님이 리트윗하셨습니다");
            isRetweetedTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            status = status.getRetweetedStatus();
        }

        String realText = status.getText();
        realText = realText.replaceAll("https://t[.]co[\\S]*","");

        TextView contentTextTextView = (TextView) findViewById(R.id.status_view_temporary_text_view);
        contentTextTextView.setText(realText);

        ImageView profileImageView = (ImageView) findViewById(R.id.status_view_profile_image_view);
        String profileImageUrl = status.getUser().getProfileImageURL();
        profileImageView.setBackground(HttpConnectionThread.getImageDrawableFromUrl(profileImageUrl));

        TextView nameTextView = (TextView) findViewById(R.id.status_view_name_text_view);
        nameTextView.setText(status.getUser().getName());

        TextView screenNameTextView = (TextView) findViewById(R.id.status_view_screen_name_text_view);
        screenNameTextView.setText("@" + status.getUser().getScreenName());

        RestAPIClient restAPIClient = new RestAPIClient(realText.replaceAll("#[\\S]*",""));
        RestAPIClient.SemanticClassification sc = restAPIClient.extract();
        if(!sc.classification.equals("")) {
            TextView classificationTextView = (TextView) findViewById(R.id.status_view_classification);
            classificationTextView.setText(sc.classification);
            classificationTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
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
            ImageThread imageThread = new ImageThread(m.getMediaURL());
            Bitmap bitmap = imageThread.getImage();

            if(bitmap != null) {
                Log.e(TAG, "height : " + bitmap.getHeight() + ", width : " + bitmap.getWidth());
            }

            mediaImageView.setImageBitmap(bitmap);
            mediaImageView.setLayoutParams(layoutParams);

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

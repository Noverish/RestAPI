package com.noverish.restapi.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-11-21.
 */

public class TmpMessageView extends LinearLayout {
    private Context context;
    private Drawable profile;
    private String name;
    private String screenName;
    private String time;
    private String content;

    public TmpMessageView(Context context, int profileImageId, String name, String screenName, String time, String content) {
        super(context);
        this.context = context;
        this.profile = ContextCompat.getDrawable(context, profileImageId);
        this.name = name;
        this.screenName = screenName;
        this.time = time;
        this.content = content;

        init();
    }

    public TmpMessageView(Context context, AttributeSet attrs, int profileImageId, String name, String screenName, String time, String content) {
        super(context, attrs);
        this.context = context;
        this.profile = ContextCompat.getDrawable(context, profileImageId);
        this.name = name;
        this.screenName = screenName;
        this.time = time;
        this.content = content;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tmp_message_layout, this, true);

        ImageView profileImageView = (ImageView) findViewById(R.id.tmp_profile);
        profileImageView.setImageDrawable(profile);

        TextView nameTextView = (TextView) findViewById(R.id.tmp_name);
        nameTextView.setText(name);

        TextView screenNameTextView = (TextView) findViewById(R.id.tmp_screen_name);
        screenNameTextView.setText(screenName);

        TextView timeTextView = (TextView) findViewById(R.id.tmp_time);
        timeTextView.setText(time);

        TextView contentTextView = (TextView) findViewById(R.id.tmp_content);
        contentTextView.setText(content);
    }
}

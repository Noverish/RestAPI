package com.noverish.restapi.twitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2017-01-11.
 */

public class TwitterMessageView extends LinearLayout{
    private TwitterMessageItem item;
    private Context context;

    public TwitterMessageView(Context context, TwitterMessageItem item) {
        super(context);
        this.context = context;
        this.item = item;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_twitter_message, this);

        ImageView profile = (ImageView) findViewById(R.id.view_twitter_message_profile);
        Picasso.with(context).load(item.getProfileImg()).into(profile);

        TextView name = (TextView) findViewById(R.id.view_twitter_message_name);
        name.setText(item.getName());

        TextView screenName = (TextView) findViewById(R.id.view_twitter_message_screen_name);
        screenName.setText(item.getScreenName());

        TextView content = (TextView) findViewById(R.id.view_twitter_message_content);
        content.setText(item.getContent());

        TextView time = (TextView) findViewById(R.id.view_twitter_message_time);
        time.setText(item.getTimeStr());
    }
}

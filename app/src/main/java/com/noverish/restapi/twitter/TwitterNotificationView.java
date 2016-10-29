package com.noverish.restapi.twitter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;

/**
 * Created by Noverish on 2016-10-30.
 */

public class TwitterNotificationView extends LinearLayout {
    private TwitterNotificationItem item;
    private Context context;

    public TwitterNotificationView(Context context, TwitterNotificationItem item) {
        super(context);
        this.context = context;
        this.item = item;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.twitter_notification_view, this, true);

        ImageView imageView = (ImageView) findViewById(R.id.twitter_notification_view_activity_type);
        if(item.getActivityType() == TwitterNotificationItem.FOLLOW) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_follow));
        } else if(item.getActivityType() == TwitterNotificationItem.FAVORITE) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_favorite));
        } else if(item.getActivityType() == TwitterNotificationItem.RETWEET) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_retweet));
        } else {
            Log.e("ERROR","TwitterNotificationView.init() : The activityType of this TwitterNotificationItem is unknown - " + item.getActivityType());
        }

        TextView textView = (TextView) findViewById(R.id.twitter_notification_view_content);
        textView.setText(item.getContent());
    }
}

package com.noverish.restapi.twitter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.squareup.picasso.Picasso;

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
        if(item.getActivityType() == TwitterNotificationItem.ActivityType.Follow) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_follow));
        } else if(item.getActivityType() == TwitterNotificationItem.ActivityType.Like) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_favorite));
        } else if(item.getActivityType() == TwitterNotificationItem.ActivityType.Retweet) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_twitter_noti_retweet));
        } else {
            Log.e("ERROR","TwitterNotificationView.init() : The activityType of this TwitterNotificationItem is unknown - " + item.getActivityType());
        }

        TextView textView = (TextView) findViewById(R.id.twitter_notification_view_content);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(item.getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(item.getContent()));
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.twitter_notification_view_profile_layout);
        for(String url : item.getProfileImgs()) {
            ImageView profileImg = new ImageView(context);
            Picasso.with(context).load(url).into(profileImg);
            layout.addView(profileImg);

            LinearLayout.LayoutParams params = (LayoutParams) profileImg.getLayoutParams();
            params.setMargins(24, 24, 24, 24);
        }
    }
}

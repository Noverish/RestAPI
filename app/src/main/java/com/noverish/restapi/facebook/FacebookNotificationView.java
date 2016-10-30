package com.noverish.restapi.facebook;

import android.content.Context;
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

public class FacebookNotificationView extends LinearLayout {
    private FacebookNotificationItem item;
    private Context context;
    private android.os.Handler handler;

    public FacebookNotificationView(Context context, FacebookNotificationItem item, android.os.Handler handler) {
        super(context);
        this.context = context;
        this.item = item;
        this.handler = handler;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.facebook_notification_view, this, true);

        ImageView imageView = (ImageView) findViewById(R.id.facebook_notification_view_profile_image);
        Picasso.with(context).load(item.getProfileImageUrl()).into(imageView);

        TextView textView = (TextView) findViewById(R.id.facebook_notification_view_content);
        textView.setText(item.getContent());

        TextView time = (TextView) findViewById(R.id.facebook_notification_view_time);
        time.setText(item.getTimeString());

        ImageView type = (ImageView) findViewById(R.id.facebook_notification_view_type);
        if(item.getType() == FacebookNotificationItem.FACEBOOK)
            type.setImageResource(R.drawable.icon_facebook_noti_facebook);
        else if(item.getType() == FacebookNotificationItem.COMMENT)
            type.setImageResource(R.drawable.icon_facebook_noti_comment);
        else if(item.getType() == FacebookNotificationItem.DOYOUKNOW)
            type.setImageResource(R.drawable.icon_facebook_noti_doyouknow);
        else if(item.getType() == FacebookNotificationItem.LIKE)
            type.setImageResource(R.drawable.icon_facebook_noti_like);
        else if(item.getType() == FacebookNotificationItem.AMAZING)
            type.setImageResource(R.drawable.icon_facebook_noti_amazing);
        else if(item.getType() == FacebookNotificationItem.STATUS)
            type.setImageResource(R.drawable.icon_facebook_noti_status);
        else if(item.getType() == FacebookNotificationItem.PICTURE)
            type.setImageResource(R.drawable.icon_facebook_noti_picture);
        else if(item.getType() == FacebookNotificationItem.GROUP)
            type.setImageResource(R.drawable.icon_facebook_noti_group);
        else if(item.getType() == FacebookNotificationItem.LOCATION)
            type.setImageResource(R.drawable.icon_facebook_noti_location);
        else if(item.getType() == FacebookNotificationItem.LOVE)
            type.setImageResource(R.drawable.icon_facebook_noti_love);
        else
            Log.e("ERROR","FacebookNotificationView.init() : unknown type - " + item.getType());
    }
}

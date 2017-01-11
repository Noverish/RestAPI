package com.noverish.restapi.facebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.restapi.R;
import com.noverish.restapi.activity.MainActivity;
import com.noverish.restapi.webview.HtmlParseWebView;
import com.noverish.restapi.webview.OnPageFinishedListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Noverish on 2017-01-11.
 */

public class FacebookMessageView extends LinearLayout implements View.OnClickListener{
    private FacebookMessageItem item;
    private Context context;

    public FacebookMessageView(Context context, FacebookMessageItem item) {
        super(context);
        this.context = context;
        this.item = item;
        this.setOnClickListener(this);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_facebook_message, this);

        ImageView profile = (ImageView) findViewById(R.id.view_facebook_message_profile);
        Picasso.with(context).load(item.getProfileImg()).into(profile);

        TextView name = (TextView) findViewById(R.id.view_facebook_message_name);
        name.setText(item.getName());

        TextView content = (TextView) findViewById(R.id.view_facebook_message_content);
        content.setText(item.getContent());

        TextView time = (TextView) findViewById(R.id.view_facebook_message_time);
        time.setText(item.getTimeStr());
    }

    @Override
    public void onClick(View view) {
        OnPageFinishedListener listener = new OnPageFinishedListener() {
            @Override
            public void onPageFinished(HtmlParseWebView webView, String url) {
                webView.scrollBottom(null);
                MainActivity.instance.changeVisibleLevel(MainActivity.LEVEL_ANOTHER);
                MainActivity.instance.setStatus(MainActivity.Status.MESSAGE_FACEBOOK);
            }
        };
        MainActivity.instance.anotherWebView.loadUrl(item.getUrl(), null, null, listener, HtmlParseWebView.SNSType.Facebook);
    }
}

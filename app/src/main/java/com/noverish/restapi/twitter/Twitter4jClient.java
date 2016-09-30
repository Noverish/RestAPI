package com.noverish.restapi.twitter;

/**
 * Created by Noverish on 2016-05-29.
 */

import android.util.Log;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Twitter4jClient {
    private final static String CONSUMER_KEY = "9L4UhCD42oogkoAOxoeulaBTJ";
    private final static String CONSUMER_KEY_SECRET = "YPDiVCgAsUPLVHksGs8az1M1Uno1D9rqdRqseAvuAifPXo7Fvg";
    private final static String ACCESS_TOKEN = "736846365184008194-Tq9tZdcnigX4BS8TQUGKETNBHbNdSZj";
    private final static String ACCESS_TOKEN_SECRET = "PND8Ks6JtgN8CWiMMarIMPUqHqsU1t5zsPvq4IaGrgSKS";

    private Twitter twitter;
    private List<Status> statuses;

    private final String TAG = getClass().getSimpleName();

    private static Twitter4jClient twitter4jClient = null;
    public static Twitter4jClient getInstance() {
        if(twitter4jClient == null)
            twitter4jClient = new Twitter4jClient();
        return twitter4jClient;
    }

    private Twitter4jClient() {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET));
    }


    public void updateStatus(final String status) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    twitter.updateStatus(status);
                } catch (TwitterException tw) {
                    Log.e(TAG,"TwitterException occurred");
                    tw.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public List<Status> getTimeLine(int pageNum) {
        final Paging page = new Paging (pageNum, 20);//page number, number per page

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("getTimeLine","getHomeTimeline start");
                    statuses = twitter.getHomeTimeline(page);
                    Log.i("getTimeLine","getHomeTimeline end");
                } catch (TwitterException tw) {
                    Log.e(TAG, "TwitterException occurred");
                    tw.printStackTrace();
                }
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException inter) {
            Log.e(TAG,"InterruptedException occurred");
            inter.printStackTrace();
        }

        return statuses;
    }

    public void reply(final long inReplyToStatusId, final String screenName, final String status) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StatusUpdate stat = new StatusUpdate("@" + screenName + " " + status);
                    stat.setInReplyToStatusId(inReplyToStatusId);
                    twitter.updateStatus(stat);
                } catch (TwitterException tw) {
                    Log.e(TAG, "TwitterException occurred");
                    tw.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public Status getStatusById(long tweetId) {
        try {
            return twitter.showStatus(tweetId);
        } catch (TwitterException e) {
            Log.e("getStatusById", "Failed to search tweets: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

package com.rmobdick.twplant;

import android.app.ListActivity;
import android.os.Bundle;

import android.util.Log;


import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.BasicTimelineFilter;
import com.twitter.sdk.android.tweetui.FilterValues;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineFilter;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by rodrigo on 25/05/18.
 */

public class TwitterFeedActivity extends ListActivity {

    private static final String TAG = "TwitterFeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_feed);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();

        Twitter.initialize(config);

        if (getIntent().hasExtra("plant_name")){

            final String plant_name = getIntent().getStringExtra("plant_name");


            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("PLASMA93890477")
                    .build();


            //final SearchTimeline st = new SearchTimeline.Builder()
            //       .query(queryt)
            //        .build();

            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();




            setListAdapter(adapter);

        }

    }



}

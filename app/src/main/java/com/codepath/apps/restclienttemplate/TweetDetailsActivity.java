package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import com.bumptech.glide.Glide;


public class TweetDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvName;
    TextView tvRelTime;
    ImageView ivTweetPic;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);


        ivProfileImage = findViewById(R.id.ivProfile);
        tvBody = findViewById(R.id.tvTweet);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvName = findViewById(R.id.tvName);
        tvRelTime = findViewById(R.id.tvRelTime);
        ivTweetPic = findViewById(R.id.ivTweetPic);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tvBody.setText(tweet.getBody());
        tvName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvRelTime.setText(tweet.getRelTime());
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        Log.i("ADAPTER", tweet.getMediaUrl());
        Glide.with(this).load(tweet.getMediaUrl()).into(ivTweetPic);

        if(tweet.getMediaUrl().isEmpty()){
            ivTweetPic.setVisibility(View.GONE);
        }


    }
}
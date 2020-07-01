package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONException;
import org.parceler.Parcels;
import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.Headers;


public class TweetDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvName;
    TextView tvRelTime;
    ImageView ivTweetPic;
    Tweet tweet;
    FloatingActionButton favRetweet;
    FloatingActionButton favFab;
    public static final String TAG = "TweetsDetailsActivity";
    TwitterClient client;

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
        favRetweet = findViewById(R.id.fabRetweet);
        favFab = findViewById(R.id.fabFav);

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

        client = new TwitterClient(TweetDetailsActivity.this);

        setRetweetListener();
        favFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.favouriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "Favourited!");
                        Toast.makeText(TweetDetailsActivity.this, "Favourited!", Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure "+response, throwable);
                        Toast.makeText(TweetDetailsActivity.this, "Could not favourite tweet", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });



    }

    private void setRetweetListener() {
        favRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.retweet(tweet.getId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Log.i(TAG, Tweet.fromJson(json.jsonObject).getBody());
                            Toast.makeText(TweetDetailsActivity.this, "Retweeted!", Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TweetDetailsActivity.this, "Already retweeted this tweet", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure "+response, throwable);
                    }
                });
            }
        });
    }
}
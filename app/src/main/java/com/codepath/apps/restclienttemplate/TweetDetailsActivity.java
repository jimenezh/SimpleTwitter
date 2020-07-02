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
    ImageView ivRetweet;
    ImageView ivFav;
    public static final String TAG = "TweetsDetailsActivity";
    TwitterClient client;
    Boolean isFavourited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        isFavourited = false;

        ivProfileImage = findViewById(R.id.ivProfile);
        tvBody = findViewById(R.id.tvTweet);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvName = findViewById(R.id.tvName);
        tvRelTime = findViewById(R.id.tvRelTime);
        ivTweetPic = findViewById(R.id.ivTweetPic);
        ivRetweet = findViewById(R.id.ivRetweet);
        ivFav = findViewById(R.id.ivFav);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        tvBody.setText(tweet.getBody());
        tvName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvRelTime.setText(tweet.getRelTime());
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        Log.i("ADAPTER", tweet.getMediaUrl());
        Glide.with(this).load(tweet.getMediaUrl()).into(ivTweetPic);

        if (tweet.getMediaUrl().isEmpty()) {
            ivTweetPic.setVisibility(View.GONE);
        }

        client = new TwitterClient(TweetDetailsActivity.this);

        setRetweetListener();
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavourited = !isFavourited;
                Toast.makeText(TweetDetailsActivity.this,
                        isFavourited ? "Favourited!" : "Unfavourited!",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFavourited) {
            favoriteTweet();
        } else{
            unfavouriteTweet();
        }

    }

    private void unfavouriteTweet() {
        client.unfavoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Unfavourited!");
                Toast.makeText(TweetDetailsActivity.this, "Unfavourited!", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure " + response, throwable);
                Toast.makeText(TweetDetailsActivity.this, response, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void favoriteTweet() {
        client.favoriteTweet(tweet.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Favorited!");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure " + response, throwable);
                Toast.makeText(TweetDetailsActivity.this, response, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setRetweetListener() {
        ivRetweet.setOnClickListener(new View.OnClickListener() {
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
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure " + response, throwable);
                        Toast.makeText(TweetDetailsActivity.this, response, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
    }
}
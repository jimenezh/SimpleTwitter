package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONException;
import org.parceler.Parcels;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.Headers;


public class TweetDetailsActivity extends AppCompatActivity {


    Tweet tweet;

    public static final String TAG = "TweetsDetailsActivity";
    TwitterClient client;
    Boolean isFavourited;
    ActivityTweetDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isFavourited = false;

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        binding.tweetMain.tvTweet.setText(tweet.getBody());
        binding.tweetMain.tvName.setText(tweet.getUser().getName());
        binding.tweetMain.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        binding.tweetMain.tvRelTime.setText(tweet.getRelTime());
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).transform(new CircleCrop()).into(binding.tweetMain.ivProfile);
        Log.i("ADAPTER", tweet.getMediaUrl());
        Glide.with(this).load(tweet.getMediaUrl()).into(binding.tweetMain.ivTweetPic);

        if (tweet.getMediaUrl().isEmpty()) {
            binding.tweetMain.ivTweetPic.setVisibility(View.GONE);
        }

        client = new TwitterClient(TweetDetailsActivity.this);

        setRetweetListener();
        setFavoriteListener();
        setReplyListener();
    }

    private void setReplyListener() {
        binding.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TweetDetailsActivity.this, ComposeActivity.class);
                intent.putExtra("user", tweet.getUser().getScreenName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void setFavoriteListener() {
        binding.ivFav.setOnClickListener(new View.OnClickListener() {
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
        } else {
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
                Toast.makeText(TweetDetailsActivity.this, "Already done!", Toast.LENGTH_LONG)
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
                Toast.makeText(TweetDetailsActivity.this, "Already done!", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setRetweetListener() {
        binding.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.retweet(tweet.getId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {

                            Toast.makeText(TweetDetailsActivity.this, "Retweeted!", Toast.LENGTH_LONG)
                                    .show();
                            try {
                                Tweet t = Tweet.fromJson(json.jsonObject);
                                Intent intent = new Intent();
                                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(t));
                                setResult(20, intent);
                                Log.i(TAG, "Published tweet is: " + t.getBody());
                                finish(); // End activity
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure " + response, throwable);
                        Toast.makeText(TweetDetailsActivity.this, "Already retweeted", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.compose_menu);
        ImageView compose = findViewById(R.id.ivHome);

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TweetDetailsActivity.this, TimelineActivity.class);
                finish();
            }
        });
        return true;
    }
}
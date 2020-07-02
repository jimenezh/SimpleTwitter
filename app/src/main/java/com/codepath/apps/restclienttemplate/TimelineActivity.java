package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.MainMenuBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    public static TwitterClient client;
    public static final int REQUEST_CODE = 20;
    RecyclerView recyclerView;
    List<Tweet> tweets;
    TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swipeContainer;
    LinearLayoutManager linearLayoutManager;
    ActivityTimelineBinding binding;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_menu);
        ImageView compose = findViewById(R.id.ivCompose);
        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == REQUEST_CODE) {
            // Get data from tweet
            Tweet t = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            // Update rv view
            tweets.add(0, t);
            tweetAdapter.notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
        } else {
            recyclerView.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(this);

        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the recycler view
        recyclerView = binding.rvTweets;
        // Init the list of tweets and adapter
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this, tweets);
        // RV setup: layout manager and rv
        recyclerView.setAdapter(tweetAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setSwipeRefresh();

        setOnScrollListener();
        populateHomeTimeline("");


    }

    private void setOnScrollListener() {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Tweet lastTweet = tweets.get(tweets.size() - 1);
                populateHomeTimeline(getMaxTweetId(lastTweet));
            }
        });
    }

    private String getMaxTweetId(Tweet lastTweet) {
        String maxTweetId = lastTweet.getId();
        return String.valueOf(Long.parseLong(maxTweetId)-1);
    }

    private void setSwipeRefresh() {
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) binding.swipeContainer;
        // Setup refresh listener which triggers new data loading
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline("");
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void populateHomeTimeline(String maxTweetId) {
        client.getHomeTimeline(maxTweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess " + json.toString());
                try {
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    tweetAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure " + response, throwable);

//                Log.e(TAG, "onFailure", throwable);
            }
        });
    }

}
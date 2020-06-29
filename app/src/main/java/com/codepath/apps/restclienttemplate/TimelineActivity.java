package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    
    public static final String TAG ="TimelineActivity";
    public static TwitterClient client;
    RecyclerView recyclerView;
    List<Tweet> tweets;
    TweetAdapter tweetAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose){
            // Navigate to compose activity
            Toast.makeText(this, "Compose", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        client = new TwitterClient(this);

        // Find the recycler view
        recyclerView = findViewById(R.id.rvTweets);
        // Init the list of tweets and adapter
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this, tweets);
        // RV setup: layout manager and rv
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess " +json.toString());
                try {
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    tweetAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure " +response,  throwable);

//                Log.e(TAG, "onFailure", throwable);
            }
        });
    }
}
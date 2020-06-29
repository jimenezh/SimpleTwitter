package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    int MAX_TWEET_LENGTH = 140;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = new TwitterClient(this);

        etCompose = findViewById(R.id.etTweet);
        btnTweet = findViewById(R.id.btnTweet);

        publishTweet();
    }

    private void publishTweet() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.length() <= 0) {
                    Snackbar.make((View) etCompose.getParent(), R.string.empty_tweet,  Snackbar.LENGTH_LONG).show();
                    return;
                } else if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Snackbar.make((View) etCompose.getParent(), R.string.too_full_tweet,  Snackbar.LENGTH_LONG).show();
                    return;
                }
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess");
                        try {
                            Tweet t = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet is: "+t.getBody());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure " +response, throwable);
                    }
                });
            }
        });
    }
}
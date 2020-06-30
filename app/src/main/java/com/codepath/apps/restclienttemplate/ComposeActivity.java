package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    TextView charCount;
    int MAX_TWEET_LENGTH = 140;
    TwitterClient client;
    private int REQUEST_OK = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = new TwitterClient(this);

        etCompose = findViewById(R.id.etTweet);
        btnTweet = findViewById(R.id.btnTweet);
        charCount = findViewById(R.id.tvUserChar);

        charCount.setText("0 ");

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                charCount.setText(Integer.toString(len) +" ");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                            Intent intent = new Intent();
                            intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(t));
                            setResult(REQUEST_OK, intent);
                            Log.i(TAG, "Published tweet is: "+t.getBody());
                            finish(); // End activity
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
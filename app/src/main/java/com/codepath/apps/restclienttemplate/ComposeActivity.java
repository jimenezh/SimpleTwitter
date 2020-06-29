package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;
    int MAX_TWEET_LENGTH = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etTweet);
        btnTweet = findViewById(R.id.btnTweet);

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
                Toast.makeText(ComposeActivity.this, "Sending tweet!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
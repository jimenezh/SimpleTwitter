package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeDialogFragment extends DialogFragment {

    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    TextView charCount;
    int MAX_TWEET_LENGTH = 140;
    TwitterClient client;
    private int REQUEST_OK = 20;

    public ComposeDialogFragment() {
    }

    public static ComposeDialogFragment newInstance(String title){
        ComposeDialogFragment frag = new ComposeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_compose, container);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());

        etCompose = binding.etTweet;
        btnTweet = binding.btnTweet;
        charCount = binding.tvUserChar;

        client = new TwitterClient(getContext());


//         Fetch arguments from bundle and set title


        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        if(getArguments().getString("user") != null){
            etCompose.setText("@"+getArguments().getString("user")+" ");
        }

        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setEditTextListener();

        publishTweet();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        client = new TwitterClient(this);
//
//        etCompose = binding.etTweet;
//        btnTweet = binding.btnTweet;
//        charCount = binding.tvUserChar;
//
//
//        charCount.setText("0 ");
//
//        setEditTextListener();
//
//        isReply(etCompose, getIntent());
//
//        publishTweet();
//    }

    private void setEditTextListener() {
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();
                charCount.setText(Integer.toString(len) + " ");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void publishTweet() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.length() <= 0) {
                    Snackbar.make((View) etCompose.getParent(), R.string.empty_tweet, Snackbar.LENGTH_LONG).show();
                    return;
                } else if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Snackbar.make((View) etCompose.getParent(), R.string.too_full_tweet, Snackbar.LENGTH_LONG).show();
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
//                            setResult(REQUEST_OK, intent);
                            Log.i(TAG, "Published tweet is: " + t.getBody());
//                            finish(); // End activity
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure " + response, throwable);
                    }
                });
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.compose_menu);
//        ImageView compose = findViewById(R.id.ivHome);
//        compose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ComposeDialogFragment.this, TimelineActivity.class);
//                finish();
//            }
//        });
//        return true;
//    }
}
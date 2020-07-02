package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.parceler.Parcel;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String relTime;
    public String mediaUrl;
    public String id;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getRelTime() {
        return relTime;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Tweet() {
    }

    public String getId() {
        return id;
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet t = new Tweet();


        t.createdAt = json.getString("created_at");
        t.relTime = getRelativeTimIeaAgo(t.createdAt);
        t.body = json.getString("text");
        t.user = User.fromJson(json.getJSONObject("user"));
        t.id = json.getString("id_str");

        try{
            t.mediaUrl = json.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            Log.i("TWEET", t.mediaUrl);
        } catch(Exception JSONException){
            Log.e("TWEET", "error");
            t.mediaUrl = "";
        }




        return t;

    }

    private static String getRelativeTimIeaAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(Tweet.fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}

package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public String createdAt;
    public User user;

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet t = new Tweet();


        t.createdAt = json.getString("created_at");
        t.body = json.getString("text");
        t.user = User.fromJson(json.getJSONObject("user"));

        return t;

    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(  Tweet.fromJson(jsonArray.getJSONObject(i)) );
        }
        return tweets;
    }
}

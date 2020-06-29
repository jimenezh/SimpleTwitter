package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public static String name;
    public static String screenName;
    public static String profileImageUrl;

    public static User fromJson(JSONObject user) throws JSONException {
        User u = new User();
        u.name = user.getString("name");
        u.screenName = user.getString("screen_name");
        u.profileImageUrl = user.getString("profile_image_url_https");

        return u;
    }
}

package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String name;
    public String screenName;
    public String profileImageUrl;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJson(JSONObject user) throws JSONException {
        User u = new User();
        u.name = user.getString("name");
        u.screenName = user.getString("screen_name");
        u.profileImageUrl = user.getString("profile_image_url_https");
        return u;
    }
}

package com.sniffit.sniffit.REST;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit.http.Field;

/**
 * Created by andrewpang on 11/17/15.
 */
public class User implements Serializable{

    String token;
    @SerializedName("user_id")
    String userId;

    public User(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

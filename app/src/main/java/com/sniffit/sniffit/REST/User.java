package com.sniffit.sniffit.REST;

import java.io.Serializable;

import retrofit.http.Field;

/**
 * Created by andrewpang on 11/17/15.
 */
public class User implements Serializable{
    String cookie;

    public User(String cookie){
        this.cookie = cookie;
    }
}

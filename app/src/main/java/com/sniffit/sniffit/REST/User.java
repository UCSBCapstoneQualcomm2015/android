package com.sniffit.sniffit.REST;

import retrofit.http.Field;

/**
 * Created by andrewpang on 11/17/15.
 */
public class User {
    //@Field("_csrf")
    String csrf;
    //@Field("email")
    String email;
    //@Field("password")
    String password;

    public User(String csrf, String password, String email) {
        this.csrf = csrf;
        this.password = password;
        this.email = email;
    }
}

package com.sniffit.sniffit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by andrewpang on 11/2/15.
 */
public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Request request;
    Response response;
    String jsonData = "";

    public ServerRequest() {
    }

    public interface LoginInterface{
        @FormUrlEncoded
        @POST("/login")
        Call<ResponseBody> loginUser(@Field("_csrf") String csrf, @Field("email") String email, @Field("password") String password);
    }

    public void sendRequest(retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-27-212-208.us-west-2.compute.amazonaws.com/login")
                .build();

        LoginInterface login = retrofit.create(LoginInterface.class);
        try{
            Call<ResponseBody> call = login.loginUser("", "abc123@gmail.com", "abc123");
            call.enqueue(callback);


        }catch (Exception e){
            e.printStackTrace();
        }



//        OkHttpClient client = new OkHttpClient();
//
//        if (method.equalsIgnoreCase("get")) {
//            request = requestBuilder(headerMap).url(url).build();
//        } else if (method.equalsIgnoreCase("delete")) {
//            request = requestBuilder(headerMap).url(url).delete().build();
//        } else if (method.equalsIgnoreCase("post")) {
//            request = requestBuilder(headerMap).url(url).post(body).build();
//        } else if (method.equalsIgnoreCase("put")) {
//            request = requestBuilder(headerMap).url(url).put(body).build();
//        }
//
//        client.newCall(request).enqueue(callback);
    }

    protected Request.Builder requestBuilder(HashMap<String, String> params) {

//        if (params == null) {
//            params = new HashMap<String, String>();
//        }
//        params.put("Authorization", authHeader());
        Request.Builder requestBuilder = new Request.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }


}

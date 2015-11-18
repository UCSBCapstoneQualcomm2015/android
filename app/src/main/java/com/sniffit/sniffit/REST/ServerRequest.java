package com.sniffit.sniffit.REST;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;


/**
 * Created by andrewpang on 11/2/15.
 */
public class ServerRequest {

    private static final String base_url = "http://192.168.57.1:3000/";
            //"http://ec2-52-27-212-208.us-west-2.compute.amazonaws.com/";

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Request request;
    Response response;
    String jsonData = "";

    public ServerRequest() {
    }

    public interface apiInterface{
        @GET("rfidtags")
        Call<ResponseBody> getTags(@Header("cookie") String cookie);

        @FormUrlEncoded
        @POST("login")
        Call<ResponseBody> loginUser(@Field("_csrf") String csrf, @Field("email") String email, @Field("password") String password);
    }

    public void sendRequest(String requestType, User user, retrofit.Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .build();

        apiInterface apiService = retrofit.create(apiInterface.class);
        try{
            if(requestType == "login") {
                Call<ResponseBody> call = apiService.loginUser("", "a@gmail.com", "1234");
                call.enqueue(callback);
            }
            if(requestType == "getTags") {
                Call<ResponseBody> call = apiService.getTags(user.cookie);
                call.enqueue(callback);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}

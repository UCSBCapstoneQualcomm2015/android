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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


/**
 * Created by andrewpang on 11/2/15.
 */
public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public ServerRequest() {
    }

    public JSONObject getJSONFromUrl(String url, HashMap<String, String> hm){
        OkHttpClient client = new OkHttpClient();
        Request request = requestBuilder(hm)
                .url(url)
                .build();
        Response response = null;
        String jsonData = "";
        try {
            response = client.newCall(request).execute();
            jsonData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();

        }

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }

    protected Request.Builder requestBuilder(HashMap<String, String> hm) {

//        if (hm == null) {
//            hm = new HashMap<String, String>();
//        }
//        hm.put("Authorization", authHeader());
        Request.Builder requestBuilder = new Request.Builder();
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }


}

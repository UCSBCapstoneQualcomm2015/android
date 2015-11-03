package com.sniffit.sniffit.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.squareup.okhttp.Request.*;

/**
 * Created by andrewpang on 10/28/15.
 */
public class HttpCalls {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    Request request;
    OkHttpClient client = new OkHttpClient();

    public void findRFID (String id) throws Exception{
        Builder requestBuilder = new Builder();
        request = requestBuilder
                .url("http://")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, id))
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }

    public void run(Callback c) throws Exception {
        Request request = new Request.Builder()
                .url("http://")
                .build();

        client.newCall(request).enqueue(c);
    }

}

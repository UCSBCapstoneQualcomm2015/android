package com.sniffit.sniffit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sniffit.sniffit.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.Retrofit;

public class LoginActivity extends Activity {

    ServerRequest sr;
    EditText email,password,res_email,code,newpass;
    Button login, register,forgotPass, cont,cont_code,cancel,cancel1;
    String emailString, passwordString;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sr = new ServerRequest();

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        forgotPass = (Button)findViewById(R.id.forgot);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regActivity);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = email.getText().toString();
                passwordString = password.getText().toString();

                HashMap<String, String> params = new HashMap<String, String>();

                ServerRequest sr = new ServerRequest();
                //String bodyString = "_csrf=i428sMA86TyLgZieYv3mrNr501tWTgItTZZls%3D&email=abc123%40gmail.com&password=abc123";
                //RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, bodyString);

                sr.sendRequest(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit.Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            String b = response.body().string();
                            Log.d("hey2", b);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d("hey", Integer.toString(response.code()));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("he21y", t.toString());
                    }
                });


                //JSONObject json = sr.sendRequest("post", "http://ec2-52-27-212-208.us-west-2.compute.amazonaws.com/login", requestBody, params);
//                if(json != null) {
//                        /* If login returns success, start MenuActivity */
//                            Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
//
//                            startActivity(menuActivity);
//                            finish();
//                        }
                }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

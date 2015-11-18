package com.sniffit.sniffit.Activities;

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
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Retrofit;

public class LoginActivity extends Activity {

    ServerRequest sr;
    EditText email, password;
    Button login, register,forgotPass;
    String emailString, passwordString;
    User user;

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

                sr.sendRequest("login", null, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit.Response<ResponseBody> response, Retrofit retrofit) {
                        Headers h = response.headers();
                        String cookie = h.get("Set-Cookie");
                        //Creates a new User class, passes in cookie
                        user = new User(cookie);
                        Log.d("Cookie", cookie);
                        Log.d("Status code", Integer.toString(response.code()));
                        Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("Error", t.toString());
                    }
                });



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

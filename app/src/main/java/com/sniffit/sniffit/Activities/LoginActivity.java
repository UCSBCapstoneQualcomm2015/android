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

import com.google.gson.Gson;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

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

        //Implement csrf
        final String csrfString = "";
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        forgotPass = (Button)findViewById(R.id.forgot);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emailString = email.getText().toString();
                    passwordString = password.getText().toString();

                    ServerRequest sr = new ServerRequest();

                    sr.authenticate("register", emailString, passwordString, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(retrofit.Response<ResponseBody> response, Retrofit retrofit) {
                            try {
                                String jsonBody = response.body().string();

                                Gson gson = new Gson();
                                JSONObject object = new JSONObject(jsonBody);
                                String message = object.getString("message");

                                if (message.equals("Successful sign up.")) {
                                    User user = gson.fromJson(jsonBody, User.class);
                                    Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("user", user);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    //Throw error
                                    System.out.print(message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.d("Error", t.toString());
                        }
                    });

                }
            });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = email.getText().toString();
                passwordString = password.getText().toString();

                //Hardcoded for testing
//                emailString = "b@gmail.com";
//                passwordString = "abc123";

                HashMap<String, String> params = new HashMap<String, String>();

                ServerRequest sr = new ServerRequest();

                sr.authenticate("login", emailString, passwordString, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit.Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            String jsonBody = response.body().string();
                            Gson gson = new Gson();
                            User user = gson.fromJson(jsonBody, User.class);

                            Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("Error", t.toString());
                    }
                });

//                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);




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

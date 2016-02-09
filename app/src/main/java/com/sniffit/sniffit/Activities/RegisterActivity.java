package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.Retrofit;


public class RegisterActivity extends ActionBarActivity {

    EditText email, password, password2;
    Button login, register;
    String emailString, passwordString, password2String;
    android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.header);
        TextView header = (TextView) toolbar.findViewById(R.id.header_title);
        toolbar.setTitle("");
        header.setText("Register");
        setSupportActionBar(toolbar);


        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.password2);
        register = (Button)findViewById(R.id.register);
        login = (Button)findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailString = email.getText().toString();
                passwordString = password.getText().toString();
                password2String = password2.getText().toString();

                if(!(passwordString).equals(password2String)){
                    Context context = getApplicationContext();
                    CharSequence text = "Passwords don't match";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                ServerRequest sr = new ServerRequest();

                sr.authenticate("register", emailString, passwordString, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit.Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            String jsonBody = response.body().string();
                            Gson gson = new Gson();
                            JSONObject object = new JSONObject(jsonBody);
                            String message = object.getString("message");
                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_LONG;

                            if (message.equals("Successful sign up")) {
                                System.out.print(jsonBody);
                                User user = gson.fromJson(jsonBody, User.class);
                                Intent intent = new Intent(getApplicationContext(), ListDisplay.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                bundle.putSerializable("flag", -1);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                            if (message.equals("Account with that email address already exists")){
                                CharSequence text = "Account with that email address already exists";
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else if (message.equals("Email is not valid")){
                                CharSequence text = "Email is not valid";
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else {
                                JSONObject messageObject = new JSONObject(message);
                                if (messageObject.has("email")) {
                                    JSONObject emailObject = (JSONObject) messageObject.get("email");
                                    CharSequence text = emailObject.get("msg").toString();
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                } else if (messageObject.has("password")) {
                                    JSONObject passwordObject = (JSONObject) messageObject.get("password");
                                    CharSequence text = passwordObject.get("msg").toString();
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

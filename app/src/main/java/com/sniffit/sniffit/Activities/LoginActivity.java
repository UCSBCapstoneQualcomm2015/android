package com.sniffit.sniffit.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends ActionBarActivity {

    ServerRequest sr;
    EditText email, password;
    Button login, register,forgotPass;
    String emailString, passwordString;
    User user;
    SharedPreferences pref;
    android.support.v7.widget.Toolbar toolbar;


    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.header);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.header_title);
        mTitle.setText("Login");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
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
                            JSONObject object = new JSONObject(jsonBody);
                            String success = object.getString("success");

                            if(success.equals("true")) {
                                User user = gson.fromJson(jsonBody, User.class);
                                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                bundle.putSerializable("flag", -1);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtras(bundle);
                                startActivity(intent);
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                            else {
                                Context context = getApplicationContext();
                                CharSequence text = ":(";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
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

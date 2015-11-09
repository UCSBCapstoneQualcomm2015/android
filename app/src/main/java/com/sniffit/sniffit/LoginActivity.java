package com.sniffit.sniffit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sniffit.sniffit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends Activity {

    ServerRequest sr;
    EditText email,password,res_email,code,newpass;
    Button login, register,forgotPass, cont,cont_code,cancel,cancel1;
    String emailString, passwordString;


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
                JSONObject json = sr.getJSONFromUrl("http://", params);
                if(json != null) {
                        /* If login returns success, start MenuActivity */
                            Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);

                            startActivity(menuActivity);
                            finish();
                        }
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

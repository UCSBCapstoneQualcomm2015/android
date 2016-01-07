package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.gson.*;

public class FindActivity extends Activity {

    User user;
    Spinner roomSpinner, itemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        TextView header = (TextView)findViewById(R.id.header_title);
        header.setText("Find Tag");
        Button findButton = (Button) findViewById(R.id.sniff_button);
        findButton.setBackgroundColor(Color.parseColor("#293e6a"));
        roomSpinner = (Spinner)findViewById(R.id.room_spinner);
        itemSpinner = (Spinner)findViewById(R.id.item_spinner);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        final ServerRequest sr = new ServerRequest();

        //Set Room Spinner values
        sr.getIds("rooms", user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    String json = response.body().string();
                    System.out.println(json);
                    Gson gson = new Gson();
                    Room[] roomArray = gson.fromJson(json, Room[].class);
                    ArrayAdapter<Room> adapter;
                    adapter = new ArrayAdapter<Room>(getApplicationContext(),
                            R.layout.spinner_dropdown_item, roomArray);
                    roomSpinner.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        //Set item spinner value
        sr.getIds("rfid", user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    String json = response.body().string();
                    System.out.println(json);
                    Gson gson = new Gson();
                    RFIDItem[] rfidArray = gson.fromJson(json, RFIDItem[].class);
                    ArrayAdapter<RFIDItem> adapter = new ArrayAdapter<RFIDItem>(getApplicationContext(),
                            R.layout.spinner_dropdown_item, rfidArray);
                    itemSpinner.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        //Find Button click
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFIDItem rfid = (RFIDItem) itemSpinner.getSelectedItem();
                sr.getId("rfid", rfid.getTagId(), user, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            int code = response.code();
                            Headers h = response.headers();
                            ResponseBody body = response.body();
                            String bodyString = body.string();
                            Log.d("Body", bodyString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find, menu);
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
    public void goToRooms(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void goToItems(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

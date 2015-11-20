package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.sniffit.sniffit.Room;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.ResponseBody;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sohanshah on 11/19/15.
 */
public class RoomActivity extends Activity{
    Room room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hi","hi");
        setContentView(R.layout.room);
        TextView header = (TextView)findViewById(R.id.header_title);
        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("object");
        header.setText(room.getName());

    }

    public void goToRooms(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("displayFlag", 1);
        startActivity(intent);
    }
    public void goToItems(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("displayFlag", 2);
        startActivity(intent);
    }
}

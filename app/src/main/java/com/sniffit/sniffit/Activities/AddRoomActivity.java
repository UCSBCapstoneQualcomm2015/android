package com.sniffit.sniffit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sniffit.sniffit.R;

public class AddRoomActivity extends Activity {

    EditText roomName, width, height;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        TextView header = (TextView)findViewById(R.id.header_title);
        header.setText("Add Room");

        roomName = (EditText)findViewById(R.id.roomName);
        width = (EditText)findViewById(R.id.width);
        height = (EditText)findViewById(R.id.height);
        confirmButton = (Button)findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent roomActivity = new Intent(RoomActivity.this, AddRoomActivity.class);
//                startActivity(roomActivity);
//                finish();
            }
        });
    }
    
    public void goToRooms(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

package com.sniffit.sniffit.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sniffit.sniffit.R;

import java.util.ArrayList;

/**
 * Created by sohanshah on 11/16/15.
 */
public class RoomListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> roomList;     //JUST FOR NOW; SHOULD EVENTUALLY BE AN ARRAYLIST OF "ROOM" OBJECTS
    Button roomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getSupportActionBar().hide();
        TextView header = (TextView)findViewById(R.id.header_title);
        header.setText("Room List");

        roomButton = (Button) findViewById(R.id.rooms_button);
        roomButton.setBackgroundColor(Color.parseColor("#293e6a"));
        roomList = new ArrayList<String>();
        roomList.add("Bedroom");    //hardcode examples
        roomList.add("Bathroom");
        roomList.add("Bathroom2");
        roomList.add("Bathroom3");
        roomList.add("Bathroom4");
        roomList.add("Bathroom5");
        roomList.add("Bathroom6");
        roomList.add("Bathroom7");

//        for (rooms in database) {
            //roomList: populate with list of user's rooms
//        }

        if (roomList.size() > 0) {
            TextView nodata = (TextView) findViewById(R.id.nodata);
            nodata.setVisibility(View.INVISIBLE);
        }
        else {
            TextView nodata = (TextView) findViewById(R.id.nodata);
            nodata.setVisibility(View.VISIBLE);
        }
        // DATA SET CONTAINS THE STRING EACH TEXTVIEW WILL CONTAIN
//        String[] myDataSet = {"THIS IS A STRING1", "THIS IS THE STRING 2", "THIS IS THE STRING 3","THIS IS THE STRING 4"};

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(roomList);
        mRecyclerView.setAdapter(mAdapter);
    }

}

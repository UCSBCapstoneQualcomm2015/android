package com.sniffit.sniffit.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.Objects.SniffitObject;

import java.util.ArrayList;

/**
 * Created by sohanshah on 11/16/15.
 */
public class ListDisplay extends AppCompatActivity implements AddItemDialogFragment.AddItemListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<SniffitObject> sniffitList;     //JUST FOR NOW; SHOULD EVENTUALLY BE AN ARRAYLIST OF "ROOM" OBJECTS
    Button currentPage;
    FloatingActionButton fab;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().hide();
        TextView header = (TextView)findViewById(R.id.header_title);
        sniffitList = new ArrayList<SniffitObject>();
        int displayFlag = getIntent().getIntExtra("displayFlag", -1);
        this.flag = displayFlag;

        if (flag == 1) {
            header.setText("Room List");
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
            Room room1 = new Room();
            room1.setName("Bedroom");
            Room room2 = new Room();
            room2.setName("Bathroom");//hardcode examples
            Room room3 = new Room();
            room3.setName("Bathroom2");
            sniffitList.add(room1);
            sniffitList.add(room2);
            sniffitList.add(room3);
        }

        else if (flag == 2) {
            header.setText("Item List");
            currentPage = (Button) findViewById(R.id.items_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
            RFIDItem item1 = new RFIDItem();
            item1.setName("keys");
            RFIDItem item2 = new RFIDItem();
            item2.setName("wallet");
            sniffitList.add(item1);
            sniffitList.add(item2);
        }

//        for (rooms in database) {
        //roomList: populate with list of user's rooms
//        }

        if (sniffitList.size() > 0) {
            TextView nodata = (TextView) findViewById(R.id.nodata);
            nodata.setVisibility(View.INVISIBLE);
        }
        else {
            Log.d("a","why does this show up");
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
        mAdapter = new RecyclerAdapter(sniffitList, displayFlag, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        Log.d("adapter set", "adapter");
    }
    /// ADDING AN OBJECT //////
    public void addObject(View view) {
        switch (flag) {
            case 2:
                DialogFragment dialog =  AddItemDialogFragment.newInstance(1, "", "");
                dialog.show(getFragmentManager(), "addItem");
                break;
            default:
                break;
        }
    }

    @Override
    public void itemConfirm(DialogFragment dialog, String itemName, String itemId) {
        Log.d(itemName, itemId);
                //would add it to the database here and reload the intent to update list
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("displayFlag", 2);
        startActivity(intent);
    }


    /////STUFF FOR FOOTER/////

    public void goToFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

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
import com.sniffit.sniffit.Dialogs.AddRefTagDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRoomDialogFragment;
import com.sniffit.sniffit.Dialogs.AddSnapdragonDialogFragment;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.Objects.SniffitObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohanshah on 11/16/15.
 */
public class ListDisplay extends AppCompatActivity implements AddItemDialogFragment.AddItemListener,
                                                                AddRoomDialogFragment.AddRoomListener,
                                                                AddSnapdragonDialogFragment.AddSnapDragonListener,
                                                                AddRefTagDialogFragment.AddRefTagListener {
    static final int ROOM = 1;
    static final int ITEM = 2;
    static final int SNAPDRAGON = 3;
    static final int REFERENCE = 4;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<SniffitObject> sniffitList;     //JUST FOR NOW; SHOULD EVENTUALLY BE AN ARRAYLIST OF "ROOM" OBJECTS
    Button currentPage;
    FloatingActionButton fab;
    private int flag;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().hide();
        TextView header = (TextView)findViewById(R.id.header_title);
        sniffitList = new ArrayList<SniffitObject>();
        int displayFlag = getIntent().getExtras().getInt("displayFlag", -1);
        this.flag = displayFlag;

        if (flag == ROOM) {
            header.setText("Room List");
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
            Room room1 = new Room();
            room1.setName("Bedroom");
            room1.setLength("7");
            room1.setWidth("5");
            Room room2 = new Room();
            room2.setName("Bathroom");//hardcode examples
            Room room3 = new Room();
            room3.setName("Bathroom2");
            sniffitList.add(room1);
            sniffitList.add(room2);
            sniffitList.add(room3);
        }

        else if (flag == ITEM) {
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

        else if (flag == SNAPDRAGON) {         //if we are adding a snapdragon (same with ref tag), we need to figure out way to add it to that specific room in db
            header.setText("Snapdragon List");
            room = (Room) getIntent().getExtras().getSerializable("room");
            Log.d("room", room.getName());
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
            Snapdragon snap1 = new Snapdragon();
            snap1.setName("Snapple");
            snap1.setIp("12394959203939");
            Snapdragon snap2 = new Snapdragon();
            snap2.setName("SnapDragon Tales");
            snap2.setIp("129392049");
            sniffitList.add(snap1);
            sniffitList.add(snap2);

        }

        else if (flag == REFERENCE) {
            room = (Room) getIntent().getExtras().getSerializable("room");
            header.setText("Reference Tags (Bedroom)");
            header.setTextSize(25);
            ReferenceTag tag1 = new ReferenceTag();
            ReferenceTag tag2 = new ReferenceTag();
            tag1.setRoom(room);
            tag1.setId("29493929392");
            tag1.setX("7");
            tag1.setY("2");
            tag1.setName("Bed 1");
            tag2.setName("Bed 2");

            sniffitList.add(tag1);
            sniffitList.add(tag2);
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        }

//        for (rooms in database) {
        //roomList: populate with list of user's rooms
//        }

        if (sniffitList.size() > 0) {
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
        mAdapter = new RecyclerAdapter(sniffitList, displayFlag, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        Log.d("adapter set", "adapter");
    }
    /// ADDING AN OBJECT //////
    public void addObject(View view) {
        DialogFragment dialog;
        switch (flag) {     //1: room     2:item

            case ROOM:
                dialog = AddRoomDialogFragment.newInstance(1, "", "", "");
                dialog.show(getFragmentManager(), "addRoom");
                break;
            case ITEM:
                dialog =  AddItemDialogFragment.newInstance(1, "", "");
                dialog.show(getFragmentManager(), "addItem");
                break;
            case SNAPDRAGON:
                dialog = AddSnapdragonDialogFragment.newInstance(1,"","","","");
                dialog.show(getFragmentManager(), "addSnapdragon");
                break;
            case REFERENCE:
                dialog = AddRefTagDialogFragment.newInstance(1,"","","","");
                dialog.show(getFragmentManager(),"addRefTag");
            default:
                break;
        }
    }

    @Override
    public void itemConfirm(DialogFragment dialog, String itemName, String itemId) {
        Log.d(itemName, itemId);
                //would add it to the database here and reload the intent to update list
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void roomConfirm(DialogFragment dialog, String roomName, String length, String width) {
        Log.d(roomName, length);
        //would add it to the database here and reload the intent to update room
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void snapdragonConfirm(DialogFragment dialog, String snapName, String ip) {
        Log.d(snapName, ip);
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 3);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //would add it to the database here and reload the intent to update list
    }

    @Override
    public void refTagConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y)   {
        Log.d (tagName, tagId);
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 4);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //would add it to the database here and reload the intent to update list
    }


    /////STUFF FOR FOOTER/////

    public void goToFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
    }

    public void goToRooms(View view) {
        Bundle bundle = new Bundle();
        Intent intent;
        if (flag == SNAPDRAGON || flag == REFERENCE) {
            intent = new Intent(this, RoomActivity.class);
            bundle.putSerializable("room", room);
        }
        else {
            intent = new Intent(this, ListDisplay.class);
            bundle.putInt("displayFlag", 1);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
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

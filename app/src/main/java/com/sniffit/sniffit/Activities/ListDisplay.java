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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.sniffit.sniffit.REST.ServerRequest;
import com.squareup.okhttp.ResponseBody;
import com.sniffit.sniffit.REST.User;
import com.sniffit.sniffit.REST.ServerRequest;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    User user;
    final ServerRequest sr = new ServerRequest();
    Bundle bundle;

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
        user = (User) getIntent().getExtras().getSerializable("user");
        bundle = new Bundle();
        bundle.putSerializable("user", user);
        this.flag = displayFlag;

        if (flag == ROOM) {
            header.setText("Room List");
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));

            sr.getIds("rooms", user, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String json = response.body().string();
                        System.out.println(json);
                        Gson gson = new Gson();
                        Room[] roomArray = gson.fromJson(json, Room[].class);
                        for(Room room: roomArray){
                            sniffitList.add(room);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        else if (flag == ITEM) {
            header.setText("Item List");
            currentPage = (Button) findViewById(R.id.items_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));

            sr.getIds("rfid", user, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String json = response.body().string();
                        System.out.println(json);
                        Gson gson = new Gson();
                        RFIDItem[] rfidArray = gson.fromJson(json, RFIDItem[].class);
                        for(RFIDItem item: rfidArray){
                            sniffitList.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }

        else if (flag == SNAPDRAGON) {         //if we are adding a snapdragon (same with ref tag), we need to figure out way to add it to that specific room in db
            header.setText("Snapdragon List");
            room = (Room) getIntent().getExtras().getSerializable("room");
            Log.d("room", room.get_id());
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));

            sr.getRoomIds("snapdragon", user, room.get_id(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String json = response.body().string();
                        System.out.println();
                        Gson gson = new Gson();
                        Snapdragon[] snapdragonArray = gson.fromJson(json, Snapdragon[].class);
                        for(Snapdragon snapdragon: snapdragonArray){
                            sniffitList.add(snapdragon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }

        else if (flag == REFERENCE) {
            room = (Room) getIntent().getExtras().getSerializable("room");
            header.setText("Reference Tags (" + room.getName() + ")");
            header.setTextSize(25);
            currentPage = (Button) findViewById(R.id.rooms_button);
            currentPage.setBackgroundColor(Color.parseColor("#294e6a"));

            sr.getRoomIds("reference", user, room.get_id(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String json = response.body().string();
                        System.out.println(json);
                        Gson gson = new Gson();
                        ReferenceTag[] referenceTagArray = gson.fromJson(json, ReferenceTag[].class);
                        for(ReferenceTag tag: referenceTagArray){
                            sniffitList.add(tag);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
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
        mAdapter = new RecyclerAdapter(sniffitList, displayFlag,user, getFragmentManager());
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
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void roomConfirm(DialogFragment dialog, String roomName, String length, String width, int roomFlag) {
        Log.d(roomName, length);
        if (roomFlag == 1) {
            sr.postRoom(user, roomName, width, length, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void snapdragonConfirm(DialogFragment dialog, String snapName, String ip, String xCoord, String yCoord) {
        room = (Room) getIntent().getExtras().getSerializable("room");

        sr.postSnapdragon(user, snapName, room.get_id(), ip, xCoord, yCoord, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 3);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //would add it to the database here and reload the intent to update list
    }

    @Override
    public void refTagConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y, int refFlag)   {
        Log.d(tagName, tagId);
        if (refFlag == 1) {
            sr.postReferenceTag(user, tagName, room.get_id(), tagId, x, y, new Callback<ResponseBody>() {      //NEED TO THROW IN X AND Y
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

                }
                @Override
                public void onFailure(Throwable t) {

                }
            });

        }
        else {
            //PUT Reference Tag
        }

        Intent intent = new Intent(this, ListDisplay.class);
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
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToRooms(View view) {
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
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

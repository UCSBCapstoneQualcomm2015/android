package com.sniffit.sniffit.Activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;
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
                                                                AddRefTagDialogFragment.AddRefTagListener,
                                                                AddRoomDialogFragment.DeleteRoomListener,
                                                                AddSnapdragonDialogFragment.DeleteSnapDragonListener,
                                                                AddRefTagDialogFragment.DeleteRefTagListener
{
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
    int displayFlag = -1;
    Room room;
    User user;
    final ServerRequest sr = new ServerRequest();
    Bundle bundle;
    SharedPreferences pref;
    android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.header);
        TextView header = (TextView) toolbar.findViewById(R.id.header_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        sniffitList = new ArrayList<SniffitObject>();
        displayFlag = getIntent().getExtras().getInt("displayFlag", -1);
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
                        setVisibility(sniffitList);

                        setView();
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
                            setVisibility(sniffitList);

                            setView();

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
            room = (Room) getIntent().getExtras().getSerializable("room");
            header.setText("Sensors (" + room.getName().substring(0,1).toUpperCase() + room.getName().substring(1) + ")");
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
                        setVisibility(sniffitList);
                        setView();
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
            header.setText("Reference Tags (" + room.getName().substring(0,1).toUpperCase() + room.getName().substring(1) + ")");
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
                    setVisibility(sniffitList);
                    setView();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
//        TextView nodata = (TextView) findViewById(R.id.nodata);
//
//        nodata.setVisibility(View.INVISIBLE);

//        for (rooms in database) {
//        roomList: populate with list of user's rooms
//        }
//
//        if (sniffitList.size() > 0) {
//            TextView nodata = (TextView) findViewById(R.id.nodata);
//            nodata.setVisibility(View.INVISIBLE);
//        }
//        else {
//            TextView nodata = (TextView) findViewById(R.id.nodata);
//            nodata.setVisibility(View.VISIBLE);
//        }
        // DATA SET CONTAINS THE STRING EACH TEXTVIEW WILL CONTAIN
//        String[] myDataSet = {"THIS IS A STRING1", "THIS IS THE STRING 2", "THIS IS THE STRING 3","THIS IS THE STRING 4"};

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
    }

    public void setVisibility(ArrayList<SniffitObject> sniffitList) {
        if (sniffitList.size() > 0) {
            TextView nodata = (TextView) findViewById(R.id.nodata);
            nodata.setVisibility(View.INVISIBLE);
        }
        else {
            TextView nodata = (TextView) findViewById(R.id.nodata);
            nodata.setVisibility(View.VISIBLE);
        }

    }

    public void setView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(sniffitList, displayFlag,user, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

    }




    /// ADDING AN OBJECT //////
    public void addObject(View view) {
        DialogFragment dialog;
        switch (flag) {     //1: room     2:item

            case ROOM:
                dialog = AddRoomDialogFragment.newInstance(1, "", "", "", "");
                dialog.show(getFragmentManager(), "addRoom");
                break;
            case ITEM:
                dialog =  AddItemDialogFragment.newInstance(1, "", "");
                dialog.show(getFragmentManager(), "addItem");
                break;
            case SNAPDRAGON:
                dialog = AddSnapdragonDialogFragment.newInstance(1,"","","","", "");
                dialog.show(getFragmentManager(), "addSnapdragon");
                break;
            case REFERENCE:
                dialog = AddRefTagDialogFragment.newInstance(1,"","","","","");
                dialog.show(getFragmentManager(),"addRefTag");
            default:
                break;
        }
    }

    @Override
    public void itemConfirm(DialogFragment dialog, String itemName, String itemId, int itemFlag, String oldId) {
        Log.d(itemName, itemId);
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (itemFlag == 1) {
            sr.postRFIDTag(user, itemId, itemName, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else{
            sr.putRFIDTag(user, oldId, itemId, itemName, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    }

    @Override
    public void roomConfirm(DialogFragment dialog, String roomName, String length, String width, int roomFlag, String oldRoomId) {
        Log.d(roomName, length);
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (roomFlag == 1) {
            sr.postRoom(user, roomName, width, length, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
        else {
            sr.putRoom(user, roomName, width, length, oldRoomId, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }



    }

    @Override
    public void snapdragonConfirm(DialogFragment dialog, String snapName, String ip, String xCoord, String yCoord, int snapFlag, String oldIp) {
        room = (Room) getIntent().getExtras().getSerializable("room");
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 3);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (snapFlag == 1) {
            sr.postSnapdragon(user, snapName, room.get_id(), ip, xCoord, yCoord, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            sr.putSnapdragon(user, oldIp, snapName, room.get_id(), ip, xCoord, yCoord, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        //would add it to the database here and reload the intent to update list
    }

    @Override
    public void refTagConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y, int refFlag, String oldId)   {
        Log.d(tagName, tagId);
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 4);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (refFlag == 1) {
            sr.postReferenceTag(user, tagName, room.get_id(), tagId, x, y, new Callback<ResponseBody>() {      //NEED TO THROW IN X AND Y
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }
        else {
            sr.putReferenceTag(user, oldId, tagName, room.get_id(), tagId, x, y, new Callback<ResponseBody>() {      //NEED TO THROW IN X AND Y
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        //would add it to the database here and reload the intent to update list
    }

    ///////////////// DELETE LISTENERS /////////////////

    @Override
    public void roomDelete(DialogFragment dialog, String roomId) {
        Log.d("test3 ", roomId);
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sr.deleteRoom(user, roomId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                final SharedPreferences.Editor editor = pref.edit();
                editor.putInt("roomSpinnerPosition", -1);
                editor.commit();
                startActivity(intent);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void snapdragonDelete(DialogFragment dialog, String snapId) {
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 3);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sr.deleteSnapdragon(user, snapId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void refTagDelete(DialogFragment dialog, String tagId) {
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 4);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sr.deleteRefTag(user, tagId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void itemDelete(DialogFragment dialog, String id) {

        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sr.deleteItem(user, id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                final SharedPreferences.Editor editor = pref.edit();
                editor.putInt("itemSpinnerPosition", -1);
                editor.commit();
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    /// MENU STUFF /////


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged_in, menu);
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
        if (id == R.id.log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    /////STUFF FOR FOOTER/////

    public void goToFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        bundle.putSerializable("flag", -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

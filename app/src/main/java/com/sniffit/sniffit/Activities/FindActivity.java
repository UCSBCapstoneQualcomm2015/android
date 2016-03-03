package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sniffit.sniffit.Dialogs.ConfirmDialogFragment;
import com.sniffit.sniffit.Objects.Location;
import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.gson.*;

//import com.github.amlcurran.showcaseview.ShowcaseView;
//import com.github.amlcurran.showcaseview.targets.Target;
//import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class FindActivity extends ActionBarActivity implements ConfirmDialogFragment.ConfirmDialogListener{

    //passed in all activities
    User user;
    Bundle bundle;
    Intent intent;
    final ServerRequest sr = new ServerRequest();
    android.support.v7.widget.Toolbar toolbar;



    //different arrays of points
    Room[] roomArray;
    Snapdragon[] snapArray;
    ReferenceTag[] referenceTagArray;
    RFIDItem[] rfidArray;
    Location myLocation;

    //Buttons
    Button currentPage;
    Button findButton;
    Button roomsButton;
    Button itemsButton;

    //Map
    MapView roomImage;
    TextView noRooms;
    Bitmap b;
    Paint myPaint;
    int imageFlag;
    ProgressBar progressBar;
    RelativeLayout.LayoutParams imgParams;
    RelativeLayout mapLayout;
    int TOP, LEFT, RIGHT, BOTTOM;
    float width, length, scaledSize, diff, scaledXUnit, scaledYUnit;

    Bitmap rfidMap;
    Bitmap scaledRfid;
    Bitmap snapMap;
    Bitmap scaledSnap;
    Resources res;



    //Scaling
    int w;
    int h;
    int densityDpi;
    int w_px;
    int h_px;

    //for spinner
    boolean firstLoad;
    android.support.v7.widget.AppCompatSpinner roomSpinner, itemSpinner;
    SharedPreferences pref;
    int roomPosition = -1;
    int itemPosition = -1;


    //for toast
    Context context;
    Toast toast;
    int duration = Toast.LENGTH_SHORT;
    CharSequence text;

//    //ShowcaseView
//    private ShowcaseView showcaseView;
//    int counter = 0;
//
//    //confirm dialog listener methods
//
//
//    @Override
//    public void onClick(View v) {
//        switch (counter) {
//            case 0:
//                showcaseView.setShowcase(new ViewTarget(itemsButton), true);
//                showcaseView.setContentText("Register items here, with their RFIDs");
//                break;
//
//            case 1:
//                showcaseView.setShowcase(new ViewTarget(findButton), true);
//                showcaseView.setContentText("After selecting a room and item, click here to locate your item");
//                break;
////
////            case 2:
////                showcaseView.setTarget(Target.NONE);
////                showcaseView.setContentTitle("Check it out");
////                showcaseView.setContentText("You don't always need a target to showcase");
////                showcaseView.setButtonText(getString(R.string.close));
////                setAlpha(0.4f, textView1, textView2, textView3);
////                break;
////
////            case 3:
////                showcaseView.hide();
////                setAlpha(1.0f, textView1, textView2, textView3);
////                break;
//        }
//        counter++;
//    }
//
//    private void setAlpha(float alpha, View... views) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            for (View view : views) {
//                view.setAlpha(alpha);
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.header);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.header_title);
        mTitle.setText("Find");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        roomsButton = (Button)findViewById(R.id.rooms_button);
        itemsButton = (Button)findViewById(R.id.items_button);


//        showcaseView = new ShowcaseView.Builder(this)
//                //.withMaterialShowcase()
//                .setTarget(new ViewTarget(roomsButton))
//                .setOnClickListener(this)
//                .build();
//        setAlpha(.5f, showcaseView);
//        showcaseView.setButtonText("Next");
//        showcaseView.setContentTitle("Getting Started");
//        showcaseView.setContentText("Click here to register rooms as well as snapdragons and reference tags");



        findButton = (Button) findViewById(R.id.sniff_button);

        currentPage = (Button) findViewById(R.id.find_button);
        currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        roomSpinner = (android.support.v7.widget.AppCompatSpinner)findViewById(R.id.room_spinner);

        itemSpinner = (android.support.v7.widget.AppCompatSpinner)findViewById(R.id.item_spinner);

        user = (User) getIntent().getExtras().getSerializable("user");
        imageFlag = (Integer) getIntent().getExtras().getSerializable("flag");
        if (imageFlag == 1) {
            myLocation = (Location) getIntent().getExtras().getSerializable("location");
        }

        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        noRooms = (TextView) findViewById(R.id.no_rooms);

        bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent = new Intent(this, FindActivity.class);


        pref =  getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();
        firstLoad = true;

        res = getApplicationContext().getResources();

        rfidMap = BitmapFactory.decodeResource(res, R.mipmap.rfid_image);
        scaledRfid = Bitmap.createScaledBitmap(rfidMap,
                70, 70, false);
        snapMap = BitmapFactory.decodeResource(res, R.mipmap.sensor_image);
        scaledSnap = Bitmap.createScaledBitmap(snapMap, 70, 70, false);

        //Set Room Spinner values
        sr.getIds("rooms", user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    roomArray = gson.fromJson(json, Room[].class);
                    if (roomArray.length > 0)  {
                        Room allRooms = new Room();
                        allRooms.setName("All Rooms");
                        allRooms.setLength("-1");
                        allRooms.setWidth("-1");
                        ArrayList<Room> roomList = new ArrayList<Room>();
                        for(Room e : roomArray)
                            roomList.add(e);
                        roomList.add(allRooms);
                        roomArray = roomList.toArray(new Room[roomList.size()]);
                    }

                    ArrayAdapter<Room> adapter;
                    adapter = new ArrayAdapter<Room>(getApplicationContext(),
                            R.layout.spinner_dropdown_item, roomArray);
                    roomSpinner.setAdapter(adapter);

                    roomPosition = pref.getInt("roomSpinnerPosition", -1);
                    if (roomArray.length > 0 && roomPosition == -1) {
                        roomPosition = 0;
                    }
                    Log.d(":<", Integer.toString(roomPosition));
                    if (roomPosition >= roomArray.length) {
                        if (roomArray.length == 0) {
                            roomPosition = -1;
                        }
                        else {
                            roomPosition = 0;
                        }
                    }
                    if (roomPosition >= 0) {
                        roomSpinner.post(new Runnable() {
                            @Override
                            public void run() {
                                roomSpinner.setSelection(roomPosition, false);
                            }
                        });

                    }


                    roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (firstLoad == false) {

                                editor.putInt("roomSpinnerPosition", roomSpinner.getSelectedItemPosition());
                                editor.commit();
                                imageFlag = -1;
                                Intent intent = new Intent(FindActivity.this, FindActivity.class);
                                bundle.putSerializable("flag", -1);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
//                            return;
                        }

                        public void onNothingSelected(AdapterView<?> adapterView) {
//                            return;
                        }
                    });


                    roomImage = (MapView) findViewById(R.id.find_view_room);

                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    densityDpi = (int)(metrics.density * 160f);
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                    h_px = 300 * (densityDpi/160);
                    w_px = 300 * (densityDpi/160);

                    Bitmap bitmap = Bitmap.createBitmap(w_px, h_px, conf); // this creates a MUTABLE bitmap
                    roomImage.setImageBitmap(bitmap);
                    roomImage.setFlag(imageFlag);



                    //GET ROOM'S SNAPDRAGONS//
                    if (roomPosition >= 0 && roomArray[roomPosition].getName() != "All Rooms") {
                        //hi
                        sr.getRoomIds("snapdragon", user, roomArray[roomPosition].get_id(), new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                try {
                                    String json = response.body().string();
                                    Gson gson = new Gson();
                                    snapArray = gson.fromJson(json, Snapdragon[].class);

                                    //SECOND NEST

                                    sr.getRoomIds("reference", user, roomArray[roomPosition].get_id(), new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                            try {
                                                String json = response.body().string();
                                                Gson gson = new Gson();
                                                referenceTagArray = gson.fromJson(json, ReferenceTag[].class);

                                                ///THIRD NEST

                                                roomImage.setFlag(2);
                                                roomImage.setRoom(roomArray[roomPosition]);
                                                roomImage.setSnapdragonArray(snapArray);
                                                roomImage.setReferenceTags(referenceTagArray);

                                                roomImage.invalidate();
                                                setupRoom();



                                                //// ITEM STUFF ////


                                                //Set item spinner value
                                                sr.getIds("rfid", user, new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                                        try {
                                                            String json = response.body().string();
                                                            Gson gson = new Gson();
                                                            rfidArray = gson.fromJson(json, RFIDItem[].class);
//                                                            Log.d("items", Integer.toString(rfidArray.length));

                                                            ArrayAdapter<RFIDItem> adapter = new ArrayAdapter<RFIDItem>(getApplicationContext(),
                                                                    R.layout.spinner_dropdown_item, rfidArray);


                                                            itemPosition = pref.getInt("itemSpinnerPosition", -1);

                                                            if (itemPosition >= 0) {
                                                                itemSpinner.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        itemSpinner.setSelection(itemPosition);
                                                                    }
                                                                });
                                                            }

                                                            itemSpinner.setAdapter(adapter);

                                                            //fourth nest


                                                            itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    editor.putInt("itemSpinnerPosition", itemSpinner.getSelectedItemPosition());
                                                                    editor.commit();
                                                                    imageFlag = -1;
                                                                    itemPosition = itemSpinner.getSelectedItemPosition();

                                                                    return;
                                                                }

                                                                public void onNothingSelected(AdapterView<?> adapterView) {
                                                                    return;
                                                                }
                                                            });


                                                            //Find Button click
                                                            findButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    //Confirm dialog shown, not fully functional
                                                                    DialogFragment dialog = ConfirmDialogFragment.newInstance(rfidArray[itemPosition].getName(), roomArray[roomPosition].getName());
                                                                    dialog.show(getFragmentManager(), "confirm");

                                                                }
                                                            });
                                                            firstLoad = false;


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {

                                                    }
                                                });


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {

                                        }
                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });


                    }
                    else if (roomPosition >= 0) {


                                                //Set item spinner value
                        sr.getIds("rfid", user, new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                try {
                                    String json = response.body().string();
                                    Gson gson = new Gson();
                                    rfidArray = gson.fromJson(json, RFIDItem[].class);
//                                                            Log.d("items", Integer.toString(rfidArray.length));
                                    ArrayAdapter<RFIDItem> adapter = new ArrayAdapter<RFIDItem>(getApplicationContext(),
                                            R.layout.spinner_dropdown_item, rfidArray);
                                    itemPosition = pref.getInt("itemSpinnerPosition", -1);
                                    if (itemPosition >= 0) {
                                        itemSpinner.post(new Runnable() {
                                                                    @Override
                                                                    public void run() {itemSpinner.setSelection(itemPosition);}
                                                                });
                                                            }
                                    itemSpinner.setAdapter(adapter);
                                                            //fourth nest


                                    itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            editor.putInt("itemSpinnerPosition", itemSpinner.getSelectedItemPosition());
                                            editor.commit();
                                            imageFlag = -1;
                                            return;
                                        }

                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                            return;
                                        }
                                    });


                                                            //Find Button click
                                    findButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {//Confirm dialog shown, not fully functional
                                            DialogFragment dialog = ConfirmDialogFragment.newInstance(rfidArray[itemPosition].getName(), roomArray[roomPosition].getName());
                                            dialog.show(getFragmentManager(), "confirm");
                                        }
                                    });
                                    firstLoad = false;

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
                        Log.d("roomposition", Integer.toString(roomPosition));
                        noRooms.setVisibility(View.VISIBLE);
                        Log.d("visibility", Integer.toString(noRooms.getVisibility()));
                        //Set item spinner value
                        sr.getIds("rfid", user, new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                try {
                                    String json = response.body().string();
                                    Gson gson = new Gson();
                                    rfidArray = gson.fromJson(json, RFIDItem[].class);
//                                    Log.d("items", Integer.toString(rfidArray.length));
                                    ArrayAdapter<RFIDItem> adapter = new ArrayAdapter<RFIDItem>(getApplicationContext(),
                                            R.layout.spinner_dropdown_item, rfidArray);


                                    itemPosition = pref.getInt("itemSpinnerPosition", -1);

                                    if (itemPosition >= 0) {
                                        itemSpinner.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                itemSpinner.setSelection(itemPosition);
                                            }
                                        });
                                    }

                                    itemSpinner.setAdapter(adapter);

                                    //fourth nest


                                    itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            editor.putInt("itemSpinnerPosition", itemSpinner.getSelectedItemPosition());
                                            editor.commit();
                                            imageFlag = -1;

                                            return;
                                        }

                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                            return;
                                        }
                                    });


                                    //Find Button click
                                    findButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            context = getApplicationContext();
                                            text = "No rooms in database";
                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }
                                    });


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
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
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    ////////////////HELPER METHODS///////////////





    public void goToRooms(View view) {
        intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void goToItems(View view) {
        intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToFind(View view) {
        intent = new Intent(this, FindActivity.class);
        bundle.putSerializable("flag", -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        startActivity(intent);
    }









    ////CONFIRM DIALOG/////






    @Override
    public void onConfirmPositiveClick(DialogFragment dialog) {
        if (itemPosition == -1) {
            context = getApplicationContext();
            text = "No items in database";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            bundle.putSerializable("flag", 1);
            progressBar.setVisibility(View.VISIBLE);
            //depending on text, for loop if text is all rooms
            if (roomArray[roomPosition].getName() == "All Rooms") {
                final boolean breakFlag = false;
                for (int i = 0; i < roomPosition - 1; i ++) {   //MAY NEED TO CHANGE THIS
                    if (myLocation.getxCoord() != "-1") {       //break as we found the item
                        progressBar.setVisibility(View.GONE);
                        break;
                    }
                    final int currentRoom = i;
                    sr.findItem(user, roomArray[currentRoom].getName(), rfidArray[itemPosition].getName(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                            try {
                                String json = response.body().string();
                                Gson gson = new Gson();
                                myLocation = gson.fromJson(json, Location.class);
                                if (myLocation.getxCoord() != "-1") {
                                    sr.getRoomIds("snapdragon", user, roomArray[currentRoom].get_id(), new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                            try {
                                                String json = response.body().string();
                                                Gson gson = new Gson();
                                                snapArray = gson.fromJson(json, Snapdragon[].class);

                                                //SECOND NEST

                                                sr.getRoomIds("reference", user, roomArray[currentRoom].get_id(), new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                                        try {
                                                            String json = response.body().string();
                                                            Gson gson = new Gson();
                                                            referenceTagArray = gson.fromJson(json, ReferenceTag[].class);
                                                            roomImage.setFlag(1);
                                                            roomImage.setLocation(myLocation);
                                                            roomImage.setRoom(roomArray[currentRoom]);
                                                            roomImage.setSnapdragonArray(snapArray);
                                                            roomImage.setReferenceTags(referenceTagArray);
                                                            roomImage.invalidate();
                                                            setupRoom();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                    }

                                                });


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {

                                        }
                                    });
                                }
                                else if (currentRoom == roomPosition - 1) {
                                    roomImage.setFlag(1);
                                    roomImage.setLocation(myLocation);
                                    roomImage.setRoom(roomArray[currentRoom]);
                                    roomImage.setSnapdragonArray(snapArray);
                                    roomImage.setReferenceTags(referenceTagArray);
                                    roomImage.invalidate();
                                    setupRoom();
                                    progressBar.setVisibility(View.GONE);
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
            }
            else{
                Log.d("entry", "point");
                sr.findItem(user, roomArray[roomPosition].getName(), rfidArray[itemPosition].getName(), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            String json = response.body().string();
                            Gson gson = new Gson();
                            myLocation = gson.fromJson(json, Location.class);
                            if (myLocation.getxCoord() == "-1") {
                                roomImage.setFlag(1);
                            }
                            else if (myLocation.getxCoord() == "-2") {
                                roomImage.setFlag(1);
                            }
                            else {
                                roomImage.setFlag(1);
                            }
                            roomImage.setLocation(myLocation);
                            roomImage.setRoom(roomArray[roomPosition]);
                            roomImage.setSnapdragonArray(snapArray);
                            roomImage.setReferenceTags(referenceTagArray);
                            roomImage.invalidate();
                            setupRoom();
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
//                        StringWriter sw = new StringWriter();
//                        PrintWriter pw = new PrintWriter(sw);
//                        t.printStackTrace(pw);
//                        Log.d("fail", sw.toString());
                        Log.d("failure", "fail", t);
                    }

                });
            }
        }


    }


   //////////ADDING STUFF TO MAP/////////////////////

    public void setupRoom() {
        roomImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence text = roomArray[roomPosition].getName() +
                        " dimensions: " + roomArray[roomPosition].getWidth() +
                        " x " + roomArray[roomPosition].getLength() ;

                Toast roomToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                roomToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                roomToast.show();
            }
        });

        mapLayout = (RelativeLayout) findViewById(R.id.layout2);

        TOP = 50;
        LEFT = 50;
        RIGHT = mapLayout.getRight() - 110;
        BOTTOM = mapLayout.getBottom() - 85 - 192;


        width = Float.parseFloat(roomArray[roomPosition].getWidth());
        length = Float.parseFloat(roomArray[roomPosition].getLength());

        if (length > width) {       //height > width: draw rect->top,
            scaledSize = BOTTOM * width / length;
            diff = (BOTTOM - scaledSize) / 2;
            LEFT += diff;
            RIGHT -= diff;
        }
        else {
            scaledSize = RIGHT * length/ width;
            diff = (RIGHT - scaledSize)/2;
            TOP += diff;
            BOTTOM -= diff;
        }
        scaledXUnit = (RIGHT - LEFT)/width;
        scaledYUnit = (BOTTOM - TOP)/length;
        imgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        addRefTags(scaledRfid, LEFT, BOTTOM, scaledXUnit, scaledYUnit);
        addSensors(scaledSnap, LEFT, BOTTOM, scaledXUnit, scaledYUnit);
    }

    public void addRefTags(Bitmap scaledRfid,float LEFT, float BOTTOM, float scaledXUnit, float scaledYUnit) {
        for (int i = 0; i < referenceTagArray.length; i++) {
            ImageView v = new ImageView(getApplicationContext());
            v.setImageBitmap(scaledRfid);
            v.setX(LEFT + Float.parseFloat(referenceTagArray[i].getX()) * scaledXUnit - 35);
            v.setY(BOTTOM - Float.parseFloat(referenceTagArray[i].getY()) * scaledYUnit - 35);
            v.setClickable(true);
            v.setVisibility(View.VISIBLE);
            final int curr = i;

            mapLayout.addView(v, imgParams);
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CharSequence text = referenceTagArray[curr].getName() +
                            " (X: " + referenceTagArray[curr].getX() +
                            ", Y: " + referenceTagArray[curr].getY() + ")";
                    Toast refTagToast = Toast.makeText(getApplicationContext(),
                            text, Toast.LENGTH_SHORT);
                    refTagToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    refTagToast.show();
                }
            });
        }
    }

    public void addSensors(Bitmap scaledSnap,float LEFT, float BOTTOM, float scaledXUnit, float scaledYUnit) {
        for (int i = 0; i < snapArray.length; i++) {
            ImageView v = new ImageView(getApplicationContext());
            v.setImageBitmap(scaledSnap);
            v.setX(LEFT + Float.parseFloat(snapArray[i].getxCoord()) * scaledXUnit - 35);
            v.setY(BOTTOM - Float.parseFloat(snapArray[i].getyCoord()) * scaledYUnit - 35);
            v.setClickable(true);
            v.setVisibility(View.VISIBLE);
            final int curr = i;

            mapLayout.addView(v, imgParams);
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CharSequence text = snapArray[curr].getName() +
                            " (X: " + snapArray[curr].getxCoord() +
                            ", Y: " + snapArray[curr].getyCoord() + ")";
                    Toast refTagToast = Toast.makeText(getApplicationContext(),
                            text, Toast.LENGTH_SHORT);
                    refTagToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    refTagToast.show();
                }
            });
        }
    }


}

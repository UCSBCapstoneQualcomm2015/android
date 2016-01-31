package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.gson.*;

public class FindActivity extends Activity {

    User user;
    Spinner roomSpinner, itemSpinner;
    Bundle bundle;
    MapView roomImage;
    Bitmap b;
    Paint myPaint;
    int imageFlag;
    Intent findIntent;
    SharedPreferences pref;
    int roomPosition = -1;
    int itemPosition = -1;
    Button currentPage;
    Room[] roomArray;
    Snapdragon[] snapArray;
    ReferenceTag[] referenceTagArray;

    int w;
    int h;
    int densityDpi;
    int w_px;
    int h_px;

    final ServerRequest sr = new ServerRequest();


    boolean firstLoad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        TextView header = (TextView)findViewById(R.id.header_title);
        header.setText("Find Tag");
        final Button findButton = (Button) findViewById(R.id.sniff_button);
        findButton.setBackgroundColor(Color.parseColor("#293e6a"));

        currentPage = (Button) findViewById(R.id.find_button);
        currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        roomSpinner = (Spinner)findViewById(R.id.room_spinner);

        itemSpinner = (Spinner)findViewById(R.id.item_spinner);

        user = (User) getIntent().getExtras().getSerializable("user");
        imageFlag = (Integer) getIntent().getExtras().getSerializable("flag");

        bundle = new Bundle();
        bundle.putSerializable("user", user);
        findIntent = new Intent(this, FindActivity.class);


        pref =  getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();
        firstLoad = true;


//        final int drawableResourceId = this.getResources().getIdentifier("rectangle", "drawable", this.getPackageName());

        Log.d("room position", Integer.toString(roomPosition));

        //Set Room Spinner values
        sr.getIds("rooms", user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    roomArray = gson.fromJson(json, Room[].class);
                    ArrayAdapter<Room> adapter;
                    adapter = new ArrayAdapter<Room>(getApplicationContext(),
                            R.layout.spinner_dropdown_item, roomArray);

                    roomSpinner.setAdapter(adapter);

                    roomPosition = pref.getInt("roomSpinnerPosition", -1);
                    if (roomPosition >= roomArray.length) {
                        if (roomArray.length == 0) {
                            roomPosition = -1;
                        }
                        else {
                            roomPosition = 0;
                        }
                    }

//        Log.d("roomPosition", Integer.toString(roomPosition));
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
                                Log.d("roomPosition", "hi");

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
//                    Drawable d = getResources().getDrawable(R.drawable.square);
//                    roomImage.setBackground(d);
//                    Drawable roomDrawable = getResources().getDrawable(R.drawable.rectangle);

//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableResourceId);

//        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
//        scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
//                    Bitmap bitmap = new Bitmap();

                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    densityDpi = (int)(metrics.density * 160f);
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                    h_px = 300 * (densityDpi/160);
                    w_px = 300 * (densityDpi/160);
                    Log.d("heightpx: ", Integer.toString(h_px));

                    Bitmap bitmap = Bitmap.createBitmap(w_px, h_px, conf); // this creates a MUTABLE bitmap
                    roomImage.setImageBitmap(bitmap);
                    roomImage.setFlag(imageFlag);

                    //GET ROOM'S SNAPDRAGONS//
                    if (roomPosition >= 0) {

                        sr.getRoomIds("snapdragon", user, roomArray[roomPosition].get_id(), new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                try {
                                    String json = response.body().string();
                                    System.out.println();
                                    Gson gson = new Gson();
                                    snapArray = gson.fromJson(json, Snapdragon[].class);



                                    //SECOND NEST

                                    sr.getRoomIds("reference", user, roomArray[roomPosition].get_id(), new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                            try {
                                                String json = response.body().string();
                                                System.out.println(json);
                                                Gson gson = new Gson();
                                                referenceTagArray = gson.fromJson(json, ReferenceTag[].class);

                                                ///THIRD NEST


                                                roomImage.setFlag(2);
                                                roomImage.setRoom(roomArray[roomPosition]);
                                                roomImage.setSnapdragonArray(snapArray);
                                                roomImage.setReferenceTags(referenceTagArray);
                                                roomImage.invalidate();

                                                //Set item spinner value
                                                sr.getIds("rfid", user, new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                                        try {
                                                            String json = response.body().string();
                                                            System.out.println(json);
                                                            Gson gson = new Gson();
                                                            RFIDItem[] rfidArray = gson.fromJson(json, RFIDItem[].class);
                                                            Log.d("items", Integer.toString(rfidArray.length));

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
                                                                    bundle.putSerializable("flag", 1);
                                                                    findIntent.putExtras(bundle);
                                                                    findIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(findIntent);
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

                    else {
                        //Set item spinner value
                        sr.getIds("rfid", user, new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                                try {
                                    String json = response.body().string();
                                    System.out.println(json);
                                    Gson gson = new Gson();
                                    RFIDItem[] rfidArray = gson.fromJson(json, RFIDItem[].class);
                                    Log.d("items", Integer.toString(rfidArray.length));
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
                                        public void onClick(View view) {        //should just give toast that room doesn't exist
                                            bundle.putSerializable("flag", 1);
                                            findIntent.putExtras(bundle);
                                            findIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(findIntent);
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
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void goToItems(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToFind(View view) {
        Intent intent = new Intent(this, FindActivity.class);
        bundle.putSerializable("flag", -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

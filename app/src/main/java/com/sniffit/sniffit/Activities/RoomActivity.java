package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRoomDialogFragment;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sohanshah on 11/19/15.
 */
public class RoomActivity extends ActionBarActivity implements AddRoomDialogFragment.AddRoomListener, AddRoomDialogFragment.DeleteRoomListener {
    Room room;
    Button currentPage;
    User user;
    Bundle bundle;
    final ServerRequest sr = new ServerRequest();
    android.support.v7.widget.Toolbar toolbar;
    MapView roomImage;
    Bitmap bitmap;
    Bitmap scaled;
    Paint myPaint;
    Context context;
    RelativeLayout mapLayout;
    RelativeLayout.LayoutParams imgParams;

    int densityDpi;
    int h_px;
    int w_px;

    Snapdragon[] snapdragonArray;
    ReferenceTag[] referenceTagArray;



    int TOP, LEFT, RIGHT, BOTTOM;
    float width, length, scaledSize, diff, scaledXUnit, scaledYUnit;

    Bitmap rfidMap;
    Bitmap scaledRfid;
    Bitmap snapMap;
    Bitmap scaledSnap;
    Resources res;


    //    import android.graphics.Bitmap;
//    import android.graphics.Canvas;
//    import android.graphics.Paint;
//    import android.graphics.RectF;
//    import android.graphics.drawable.BitmapDrawable;
//
//    ImageView myImageView = ...
//    Bitmap myBitmap = ...
//    Paint myRectPaint = ...
//    int x1 = ...
//    int y1 = ...
//    int x2 = ...
//    int y2 = ...
//
//    //Create a new image bitmap and attach a brand new canvas to it
//    Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
//    Canvas tempCanvas = new Canvas(tempBitmap);
//
////Draw the image bitmap into the cavas
//    tempCanvas.drawBitmap(myBitmap, 0, 0, null);
//
////Draw everything else you want into the canvas, in this example a rectangle with rounded edges
//    tempCanvas.drawRoundRect(new RectF(x1,y1,x2,y2), 2, 2, myPaint);
//
////Attach the canvas to the ImageView
//    myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.header);
        TextView header = (TextView) toolbar.findViewById(R.id.header_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView headerTitle = (TextView)findViewById(R.id.header_title);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("room");
        user = (User) intent.getExtras().getSerializable("user");
        currentPage = (Button) findViewById(R.id.rooms_button);
        currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        headerTitle.setText(room.getName().substring(0,1).toUpperCase() + room.getName().substring(1));
        bundle = new Bundle();
        bundle.putSerializable("user", user);
        //SET UP THE ROOM IMAGE
        roomImage = (MapView) findViewById(R.id.room_image);
        //Drawable roomDrawable = getResources().getDrawable(R.drawable.rectangle);

//        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
//        scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityDpi = (int) (metrics.density * 160f);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        h_px = 300 * (densityDpi/160);
        w_px = 300 * (densityDpi/160);

        final Bitmap bitmap = Bitmap.createBitmap(w_px, h_px, conf); // this creates a MUTABLE bitmap

        res = getApplicationContext().getResources();

        rfidMap = BitmapFactory.decodeResource(res, R.mipmap.rfid_image);
        scaledRfid = Bitmap.createScaledBitmap(rfidMap,
                70, 70, false);
        snapMap = BitmapFactory.decodeResource(res, R.mipmap.sensor_image);
        scaledSnap = Bitmap.createScaledBitmap(snapMap, 70, 70, false);

        sr.getRoomIds("snapdragon", user, room.get_id(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    String json = response.body().string();
                    System.out.println();
                    Gson gson = new Gson();
                    snapdragonArray = gson.fromJson(json, Snapdragon[].class);


                    //nest

                    sr.getRoomIds("reference", user, room.get_id(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                            try {
                                String json = response.body().string();
                                System.out.println(json);
                                Gson gson = new Gson();
                                referenceTagArray = gson.fromJson(json, ReferenceTag[].class);
                                roomImage.setImageBitmap(bitmap);
                                roomImage.setFlag(2);
                                roomImage.setRoom(room);
                                roomImage.setSnapdragonArray(snapdragonArray);
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

    // when edit room button is clicked

    public void editRoom(View view) {
        DialogFragment editItem = AddRoomDialogFragment.newInstance(2, room.getName(), room.getLength(), room.getWidth(), room.get_id());       //SHOULD PASS IN NAME AND ID OF SNIFFITLIST.GET(POSITION)
        editItem.show(getFragmentManager(), "editItem");
    }

    public void getSnapList(View view) {        //need to pass in room object to snapdragons list (same with ref tags) - necessary for when we add snapdragon
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //POSSIBILITY MAKE ALL INTENTS TO LISTDISPLAY BUNDLES AND GRAB THINGS OUT ACCORDINGLY
        bundle.putInt("displayFlag", 3);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void getReferenceTagList(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //POSSIBILITY MAKE ALL INTENTS TO LISTDISPLAY BUNDLES AND GRAB THINGS OUT ACCORDINGLY
        bundle.putInt("displayFlag", 4);
        bundle.putSerializable("room", room);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void roomConfirm(DialogFragment dialog, String roomName, String length, String width, int roomFlag, String oldRoomId) {
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
        else {
            sr.putRoom(user, oldRoomId, roomName, width, length, new Callback<ResponseBody> () {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    public void roomDelete(DialogFragment dialog, String roomId) {
        Log.d("test1 ", roomId);

        sr.deleteRoom(user, roomId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                Log.d("delete: ", "success");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("delete:  ", "failed");
            }
        });
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    ///////// MENU STUFF /////////////

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
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
//        if (id == android.R.id.home) {
//            Intent intent = new Intent(this, ListDisplay.class);
//            bundle.putInt("displayFlag", 1);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//
//        }
        return super.onOptionsItemSelected(item);
    }




    ////////// FOOTER STUFF /////////////

    public void goToFind(View view){
        Intent intent = new Intent(this, FindActivity.class);
        bundle.putSerializable("flag", -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        startActivity(intent);

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

    /////////// POPULATE MAP ///////////

    public void setupRoom() {
        roomImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence text = room.getName() +
                        " dimensions: " + room.getWidth() +
                        " x " + room.getLength() ;

                Toast roomToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                roomToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                roomToast.show();
            }
        });

        mapLayout = (RelativeLayout) findViewById(R.id.room_map);

        int TOP = 50;
        int LEFT = 50;
        int RIGHT = mapLayout.getRight() - 95;
        int BOTTOM = mapLayout.getBottom() - 85 - (1711 - 930);

        width = Float.parseFloat(room.getWidth());
        length = Float.parseFloat(room.getLength());

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
        for (int i = 0; i < snapdragonArray.length; i++) {
            ImageView v = new ImageView(getApplicationContext());
            v.setImageBitmap(scaledSnap);
            v.setX(LEFT + Float.parseFloat(snapdragonArray[i].getxCoord()) * scaledXUnit - 35);
            v.setY(BOTTOM - Float.parseFloat(snapdragonArray[i].getyCoord()) * scaledYUnit - 35);
            v.setClickable(true);
            v.setVisibility(View.VISIBLE);
            final int curr = i;

            mapLayout.addView(v, imgParams);
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CharSequence text = snapdragonArray[curr].getName() +
                            " (X: " + snapdragonArray[curr].getxCoord() +
                            ", Y: " + snapdragonArray[curr].getyCoord() + ")";
                    Toast refTagToast = Toast.makeText(getApplicationContext(),
                            text, Toast.LENGTH_SHORT);
                    refTagToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    refTagToast.show();
                }
            });
        }
    }


}

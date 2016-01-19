package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRoomDialogFragment;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.ResponseBody;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sohanshah on 11/19/15.
 */
public class RoomActivity extends Activity implements AddRoomDialogFragment.AddRoomListener, AddRoomDialogFragment.DeleteRoomListener {
    Room room;
    Button currentPage;
    User user;
    Bundle bundle;
    final ServerRequest sr = new ServerRequest();
    ImageView roomImage;
    Bitmap bitmap;
    Paint myPaint;

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
        TextView header = (TextView)findViewById(R.id.header_title);
        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("room");
        user = (User) intent.getExtras().getSerializable("user");
        currentPage = (Button) findViewById(R.id.rooms_button);
        currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        header.setText(room.getName());
        bundle = new Bundle();
        bundle.putSerializable("user", user);
        //SET UP THE ROOM IMAGE
        roomImage = (ImageView) findViewById(R.id.room_image);
        Log.d("height", Integer.toString(roomImage.getHeight()));

        Bitmap viewBitmap = Bitmap.createBitmap(roomImage.getWidth(),roomImage.getHeight(),Bitmap.Config.ARGB_8888);//i is imageview whch u want to convert in bitmap
        Canvas canvas = new Canvas(viewBitmap);
        myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setColor(Color.BLACK);
        canvas.drawCircle(50, 50, 10, myPaint);
        roomImage.draw(canvas);


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
        Log.d("hiiii", "test");
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    public void goToFind(View view){
        Intent intent = new Intent(this, FindActivity.class);
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


    //////ROOM DISPLAY /////////
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

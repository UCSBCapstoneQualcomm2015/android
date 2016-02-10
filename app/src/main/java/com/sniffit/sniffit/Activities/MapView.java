package com.sniffit.sniffit.Activities;

/**
 * Created by sohanshah on 1/19/16.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sniffit.sniffit.Objects.Location;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sohanshah on 5/16/15.
 */
public class MapView extends ImageView {

    Paint paint;
    Paint paintSnaps;
    Paint paintRefTags;
    Paint border;
    Paint textPaint;
    Paint locationPaint;

    Context context;
    float length;
    float width;
    float scaledSize;
    float diff;
    float TOP;
    float BOTTOM;
    float LEFT;
    float RIGHT;
    float scaledXUnit;
    float scaledYUnit;

    float d,newRot = 0f;
    ArrayList<PointF> points;
    int myFlag = -1;
    Room room;
    User user;
    ServerRequest sr = new ServerRequest();
    RectF roomLayout;

    Snapdragon[] snapdragons;
    ReferenceTag[] referenceTags;
    Location location;

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context) {
        this(context, null, 0);

    }
    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);

        border = new Paint();
        border.setColor(Color.BLACK);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(5);

        paintSnaps = new Paint();
        paintSnaps.setColor(Color.BLUE);
        paintSnaps.setStyle(Paint.Style.FILL);

        paintRefTags = new Paint();
        paintRefTags.setColor(Color.GREEN);
        paintRefTags.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);

        locationPaint = new Paint();
        locationPaint.setColor(Color.RED);
        locationPaint.setStyle(Paint.Style.STROKE);
        locationPaint.setStrokeWidth(5);



        points = new ArrayList<PointF>();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("flag:", Integer.toString(myFlag));


        TOP = 50;
        LEFT = 50;
        RIGHT = this.getRight() - 80;
        BOTTOM = this.getBottom() - 85;


        if (myFlag == 2 || myFlag == 1) {

            //ADD LEGEND
            canvas.drawText("SENSORS: ",  ((LEFT + RIGHT)/2) - 400, TOP - 15, textPaint);
            canvas.drawCircle(((LEFT + RIGHT) / 2) - 50, TOP - 30, 15, paintSnaps);

            canvas.drawText("REFERENCE TAGS: ", ((LEFT + RIGHT) / 2) + 40, TOP - 15, textPaint);
            canvas.drawCircle(((LEFT + RIGHT) / 2) + 430, TOP - 30, 15, paintRefTags);




            //draw map
            width = Float.parseFloat(room.getWidth());
            length = Float.parseFloat(room.getLength());

            if (length > width) {       //height > width: draw rect->top,
                scaledSize = BOTTOM * width / length;
                diff =  (BOTTOM - scaledSize)/2;
                LEFT += diff;
                RIGHT -= diff;
                roomLayout = new RectF(LEFT, TOP, RIGHT, BOTTOM);
                canvas.drawRect(roomLayout, paint);
                canvas.drawRect(roomLayout, border);
            }
            else {
                scaledSize = RIGHT * length/ width;
                diff = (RIGHT - scaledSize)/2;
                TOP += diff;
                BOTTOM -= diff;
                roomLayout = new RectF(LEFT, TOP, RIGHT, BOTTOM);
                canvas.drawRect(roomLayout, paint);
                canvas.drawRect(roomLayout, border);
            }

            scaledXUnit = (RIGHT - LEFT)/width;
            scaledYUnit = (BOTTOM - TOP)/length;

                ////LOAD SNAPDRAGONS/////
            for (int i = 0; i < snapdragons.length; i++) {
                canvas.drawCircle(LEFT + Float.parseFloat(snapdragons[i].getxCoord()) * scaledXUnit, BOTTOM - Float.parseFloat(snapdragons[i].getyCoord()) * scaledYUnit, 30, paintSnaps);
            }

            /////LOAD REFERENCE TAGS///////

            for (int i = 0; i < referenceTags.length; i++) {
                canvas.drawCircle(LEFT + Float.parseFloat(referenceTags[i].getX()) * scaledXUnit, BOTTOM - Float.parseFloat(referenceTags[i].getY()) * scaledYUnit, 30, paintRefTags);
            }

            if (myFlag == 1) {
                if (Float.parseFloat(location.getxCoord()) != -1) {
                    canvas.drawCircle(LEFT + Float.parseFloat(location.getxCoord()) * scaledXUnit, BOTTOM - Float.parseFloat(location.getyCoord()) * scaledYUnit, 50, locationPaint);
                    canvas.drawCircle(LEFT + Float.parseFloat(location.getxCoord()) * scaledXUnit, BOTTOM - Float.parseFloat(location.getyCoord()) * scaledYUnit, 75, locationPaint);
                    canvas.drawCircle(LEFT + Float.parseFloat(location.getxCoord()) * scaledXUnit, BOTTOM - Float.parseFloat(location.getyCoord()) * scaledYUnit, 100, locationPaint);

                }
                else {
                    CharSequence text = "Item not found in " + room.getName() ;
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();

                }
            }



        }




    }

    ////////////NECESSARY SETTERS/////
    public void setFlag(int flag) {
        myFlag = flag;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setSnapdragonArray(Snapdragon[] snaps) {
        this.snapdragons = snaps;
    }

    public void setReferenceTags(ReferenceTag[] refTags) {
        this.referenceTags = refTags;
    }

    public void setLocation(Location location) {this.location = location;}

}
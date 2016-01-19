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
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.REST.User;

import java.util.ArrayList;

/**
 * Created by sohanshah on 5/16/15.
 */
public class MapView extends ImageView {

    Paint paint;
    Context context;

    float d,newRot = 0f;
    ArrayList<PointF> points;
    int myFlag = -1;
    Room room;
    User user;
    private float[] lastEvent = null;


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
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        points = new ArrayList<PointF>();
//        matrix = new Matrix();
//        copyMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // matrix.setScale(mScaleFactor, mScaleFactor);
        if (myFlag == 1)
        canvas.drawCircle(300,300,30,paint);


//        canvas.save();



        //   canvas.drawBitmap(MainActivity.scaled, matrix, null);
//        canvas.restore();

        for (int i = 0; i < points.size(); i++) {
            canvas.drawCircle(points.get(i).x, points.get(i).y, 12, paint);
//            }
//        }


        }

    }

    ////////////SET FLAG/////
    public void setFlag(int flag) {
        myFlag = flag;
    }


}
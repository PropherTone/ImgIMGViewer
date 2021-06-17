package com.example.imgviewer.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import org.jetbrains.annotations.NotNull;

public class MyScaleScrollView extends NestedScrollView {

    private float Finger1DownX;
    private float Finger1DownY;
    private float Finger2DownX;
    private float Finger2DownY;
    private double oldDistance;
    private double newDistance;

    public static final float SCALE_MAX = 5.0f;
    private static final float SCALE_MIN = 1.0f;


    public MyScaleScrollView(@NonNull @NotNull Context context) {
        super(context);
    }

    public MyScaleScrollView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        int fingerCounts = ev.getPointerCount();
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (fingerCounts==2){
                    Finger1DownX= 0;
                    Finger1DownY= 0;
                    Finger2DownX= 0;
                    Finger2DownY= 0;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (fingerCounts==2){
                    float movex1 = ev.getX(0);
                    float movey1 = ev.getY(0);
                    float movex2 = ev.getX(1);
                    float movey2 = ev.getY(1);

                    double changeX1 = movex1 - Finger1DownX;
                    double changeY1 = movey1 - Finger1DownY;
                    double changeX2 = movex2 - Finger2DownX;
                    double changeY2 = movey2 - Finger2DownY;
                    if (getScaleX() > 1) { //滑动
                        Log.e("getScaleX",""+getScaleX());
                        float lessX = (float) ((changeX1) / 2 + (changeX2) / 2);
                        float lessY = (float) ((changeY1) / 2 + (changeY2) / 2);
//                        setPivot(-lessX,-lessY);
                    }
                    newDistance = spacing(ev);
                    double space = newDistance - oldDistance;
                    float scale = (float) (getScaleX() + space / getWidth());
                    Log.e("getWidth",""+getWidth());
                    Log.e("getScaleX",""+getScaleX());
                    Log.e("space",""+space);
                    Log.e("scale",""+scale);
                    if (scale > SCALE_MAX) {
                        setScale(SCALE_MAX);
                    } else {
                        setScale(scale);
                    }
                    return true;
                }
            case MotionEvent.ACTION_POINTER_DOWN:
                if (fingerCounts == 2){
                    Finger1DownX = ev.getX(0);
                    Finger1DownY =  ev.getY(0);
                    Finger2DownX=  ev.getX(1);
                    Finger2DownY=  ev.getY(1);
                    oldDistance = spacing(ev);
                    return true;
                }
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setPivot(float x,float y){
        float PivotX = 0;
        float PivotY = 0;
        PivotX = getPivotX() + x;
        PivotY = getPivotY() + y;
        if (PivotX < 0 && PivotY < 0) {
            PivotX = 0;
            PivotY = 0;
        } else if (PivotX > 0 && PivotY < 0) {
            PivotY = 0;
            if (PivotX > getWidth()) {
                PivotX = getWidth();
            }
        } else if (PivotX < 0 && PivotY > 0) {
            PivotX = 0;
            if (PivotY > getHeight()) {
                PivotY = getHeight();
            }
        } else {
            if (PivotX > getWidth()) {
                PivotX = getWidth();
            }
            if (PivotY > getHeight()) {
                PivotY = getHeight();
            }
        }
        Log.e("PivotX",""+PivotX);
        Log.e("PivotY",""+PivotY);
        setPivotX(PivotX);
        setPivotY(PivotY);

    }

    private void setScale(float scale) {
        Log.e("scale11",""+scale);
        setScaleX(scale);
        setScaleY(scale);
    }

    private double spacing(MotionEvent event) {

        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }

    }
}

package com.example.imgviewer.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


//自定义View解决滑动冲突问题
public class MyViewPager extends androidx.viewpager.widget.ViewPager {

    private float Finger1DownX;
    private float Finger1DownY;
    private float Finger2DownX;
    private float Finger2DownY;
    private double oldDistance;
    private float mDownPosX = 0;
    private float mDownPosY = 0;
    private double newDistance;

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
               if ((deltaX-deltaY)>10) {
                    //拦截左右滑动
                    return true;
                }
               if (ev.getPointerCount() == 2){
                   return true;
               }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int fingerCounts = ev.getPointerCount();
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (fingerCounts==2){
                    Finger1DownX= 0;
                    Finger1DownY= 0;
                    Finger2DownX= 0;
                    Finger2DownY= 0;
                }
            case MotionEvent.ACTION_MOVE:
                if (fingerCounts == 2){
                    float moveX1 = ev.getX(0);
                    float moveY1 = ev.getY(0);
                    float moveX2 = ev.getX(1);
                    float moveY2 = ev.getY(1);

                    double changeX1 = moveX1 - Finger1DownX;
                    double changeY1 = moveY1 - Finger1DownY;
                    double changeX2 = moveX2 - Finger2DownX;
                    double changeY2 = moveY2 - Finger2DownY;

                    double changeSize = Math.abs(changeX1) +Math.abs(changeX2)+Math.abs(changeY1)+Math.abs(changeY2);
//                    Log.e("changeSize",""+changeSize);
                    if (changeSize<200){
//                        Log.e("moveX1",""+moveX1);
//                        Log.e("moveY1",""+moveY1);
//                        Log.e("moveX2",""+moveX2);
//                        Log.e("moveY2",""+moveY2);
//                        Log.e("changeX1",""+changeX1);
//                        Log.e("changeY1",""+changeY1);
//                        Log.e("changeX2",""+changeX2);
//                        Log.e("changeY2",""+changeY2);
                        if (getScaleX() > 1) { //滑动
//                            Log.e("getScaleX",""+getScaleX());
                            float lessX = (float) ((changeX1) / 2 + (changeX2) / 2);
                            float lessY = (float) ((changeY1) / 2 + (changeY2) / 2);
                            setPivot(-lessX,-lessY);
                        }
                        newDistance = MathPointDistance(ev);
                        double MoveDistance = newDistance - oldDistance;
                        float scale = (float) (getScaleX() + MoveDistance / getWidth());
//                        Log.e("getWidth",""+getWidth());
//                        Log.e("getScaleX",""+getScaleX());
//                        Log.e("space",""+space);
//                        Log.e("scale",""+scale);
                        if (scale > 5.0f) {
                            setScale(5.0f);
                        } else setScale(Math.max(scale, 1.0f));
                    }
                }
            case MotionEvent.ACTION_POINTER_DOWN:
                if (fingerCounts == 2){
                    Finger1DownX = ev.getX(0);
                    Finger1DownY =  ev.getY(0);
                    Finger2DownX=  ev.getX(1);
                    Finger2DownY=  ev.getY(1);
                    oldDistance = MathPointDistance(ev);
                    newDistance = oldDistance;
                }
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setPivot(float x, float y){
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
//        Log.e("PivotX",""+PivotX);
//        Log.e("PivotY",""+PivotY);
        setPivotX(PivotX);
        setPivotY(PivotY);

    }

    private void setScale(float scale) {
//        Log.e("scale11",""+scale);
        setScaleX(scale);
        setScaleY(scale);
    }

    private double MathPointDistance(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }
    
}

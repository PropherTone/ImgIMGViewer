package com.example.imgviewer.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenPXMath {

    public static int getHeight(Activity activity, String choose){
        DisplayMetrics metrics2 = activity.getResources().getDisplayMetrics();
        int height = metrics2.heightPixels;
        int wight = metrics2.widthPixels;

        WindowManager windowManager =
                (WindowManager) activity.getApplication().getSystemService(Context.
                        WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);

        int height2 = outPoint.y;
        int wight2 = outPoint.x;

//        Log.e("hight",""+height);
//        Log.e("hight",""+height2);
        switch (choose){
            case "height":
                return height;
            case "wight":
                return wight;
            case "currentHeight":
                return height2;
            case "currentWight":
                return wight2;
            default:
                return 0;
        }
    }

    public static boolean HasBottomButton(Activity activity){
        return getHeight(activity, "currentHeight") > getHeight(activity, "height");
    }

    public static void HideBottomUIMenu(View v,Activity activity) {
        if (HasBottomButton(activity)){
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setSystemUiVisibility(uiOptions);
                }
            });

        }else {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setSystemUiVisibility(uiOptions);
                }
            });
        }
    }

    public static void ShowBottomUIMenu(View v,Activity activity) {
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                v.setSystemUiVisibility(uiOptions);
            }
        });
    }

}

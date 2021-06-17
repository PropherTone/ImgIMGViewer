package com.example.imgviewer.Utils;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * The type Img viewer.
 */
public class ImgViewer {

    private  int viewCount = 0;

    /**
     * 显示图片.
     *
     * @param activity     the activity
     * @param imageViews   the image views
     * @param linearlayout the linearlayout
     * @param ImgUri       the img uri
     */
    public  void ShowImgThread(Activity activity, List<ImageView> imageViews, LinearLayout linearlayout, List<String> ImgUri) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int displayNumber = 0;
                if (ImgUri != null){
                    if (ImgUri.size() > 0 && ImgUri.size() < 5) {
                        displayNumber = ImgUri.size();
                    }else{displayNumber = 5;}
                    if (ImgUri.size() != 0){

                        for (int i = 0; i < displayNumber; i++) {
                            imageViews.add(new ImageView(activity));
                            Glide.with(activity).load(ImgUri.get(i)).override(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViews.get(i));
//                            Log.e("path",ImgUri.get(i));
                            linearlayout.removeView(imageViews.get(i));
                            linearlayout.addView(imageViews.get(i));
                            viewCount++;
//                            Log.e("count",""+viewCount);
                        }
                    }else {
                        Toast.makeText(activity,"没有相片!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 刷新出新的图片.
     *
     * @param activity     the activity
     * @param imageViews   the image views
     * @param linearlayout the linearlayout
     * @param imgUri       the img uri
     * @return the boolean
     */
    public  boolean CallBackView(Activity activity, List<ImageView> imageViews, LinearLayout linearlayout, List<String> imgUri){
        if(viewCount+1 >= imgUri.size())
        {
            return true;
        }else{
            imageViews.add(new ImageView(activity));
            Glide.with(activity).load(imgUri.get(viewCount)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViews.get(viewCount));
            linearlayout.removeView(imageViews.get(viewCount));
            linearlayout.addView(imageViews.get(viewCount));
            viewCount++;
            if (viewCount>10){
                ReCallView(activity,imageViews);
            }
            return false;
        }
    }

    /**
     * 将不在屏幕显示范围内的图片设置为不可见.
     *
     * @param activity   the activity
     * @param imageViews the image views
     */
    public  void ReCallView(Activity activity,List<ImageView> imageViews){
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(point);
            Rect rect = new Rect(0, 0, point.x, point.y);
            for (int i = 0;i < viewCount;i++){
//                Log.e("viewcount",""+viewCount);
//                Log.e("i",""+i);

                if (!imageViews.get(i).getLocalVisibleRect(rect))
                {
                    if (i>1 && i<viewCount-2){
                        imageViews.get(i).setVisibility(View.INVISIBLE);
                        imageViews.get(i+1).setVisibility(View.INVISIBLE);
                        imageViews.get(i+2).setVisibility(View.INVISIBLE);
                    }else {
                        imageViews.get(i).setVisibility(View.INVISIBLE);
                    }

                }else {
                    if (i<viewCount-2){
                        imageViews.get(i).setVisibility(View.VISIBLE);
                        imageViews.get(i+1).setVisibility(View.VISIBLE);
                        imageViews.get(i+2).setVisibility(View.VISIBLE);
                    }else {
                        imageViews.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }

    }
}

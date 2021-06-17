package com.example.imgviewer.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imgviewer.Utils.ImgViewer;
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.ScreenPXMath;

import java.util.ArrayList;
import java.util.List;

public class ScrollingFragment extends Fragment {

    View view;
    public LinearLayout linearLayout;
    public List<String> imgUri;
    List<ImageView> imageViews = new ArrayList<>();
    private NestedScrollView scrollView;
    private int lastScrollY = 0;
    private ImgViewer imgViewer;
    public Activity activity;

    public ScrollingFragment(Activity activity,List<String> imgUri) {
        this.activity = activity;
        this.imgUri = imgUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_scrolling, container, false);
        }
        InitView();
        return view;
    }


    private void InitView() {
        imgViewer = new ImgViewer();
        linearLayout = view.findViewById(R.id.listLinearLayout);
        imgViewer.ShowImgThread(activity, imageViews, linearLayout, imgUri);
        scrollView = view.findViewById(R.id.nestedsv);
        ScrollViewListener();
    }

    /*
     * 监听scrollview滑动位置
     *
     * */
    @SuppressLint("ClickableViewAccessibility")
    private void ScrollViewListener() {

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int scrollY = scrollView.getScrollY();
                        int height = scrollView.getHeight();
                        int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();
//                        Log.e("l",""+lastScrollY);
//                        Log.e("y",""+scrollY);
//                        if(scrollY==0){
//                                Toast.makeText(MainActivity.this,"到头了",Toast.LENGTH_SHORT).show();
//                        }
                        if ((scrollY + height) == scrollViewMeasuredHeight) {

                            if ( imgViewer.CallBackView(getActivity(), imageViews, linearLayout, imgUri)) {
                                Toast.makeText(getActivity(), "没有相片了", Toast.LENGTH_SHORT).show();
                            }

                        }
                        if (scrollY > lastScrollY) {
                            imgViewer.ReCallView(getActivity(), imageViews);
                            lastScrollY = scrollY;
                        } else if (scrollY < lastScrollY && (scrollY + height) != scrollViewMeasuredHeight) {
                            imgViewer.ReCallView(getActivity(), imageViews);
                            lastScrollY = scrollY;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
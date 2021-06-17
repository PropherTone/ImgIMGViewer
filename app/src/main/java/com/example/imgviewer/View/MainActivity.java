package com.example.imgviewer.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.imgviewer.Adapter.IMGVP2_Adapter;
import com.example.imgviewer.Utils.GetFilePath;
import com.example.imgviewer.CustomView.MyViewPager;
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.ScreenPXMath;
import com.example.imgviewer.test.InternetSurfing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.INCREMENTAL;
import static android.os.Build.VERSION.SDK_INT;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private List<String> ImgUri;
    private Uri videoUri;
    public String[] path;

    private ImageView button;
    private ImageView textButton;
    private CoordinatorLayout coordinatorLayout;
    private MyViewPager viewPager;
    private TabLayout viewTab;
    private Button fileChoose;
    private Button imgLibChoose,videoLibChoose;
    private ImageButton showFAB;
    private ImageView imageView,imgbackiv;

    public  ArrayList<Fragment> fragments = new ArrayList<>();
    private int pagerNum = 0;
    private IMGVP2_Adapter imgvp2_adapter;
    private boolean tabLayoutIsShow = false;
    private int displayHight;
    private int disPlayWight;
    public String DCIMLocation = "";
    private boolean listIsOpen = false;
    private boolean isChoosen = false;
    private boolean buttonIsMid = true;

    private AnimatorSet animatorSet;
    private AnimatorSet animatorSetone;

    private SharedPreferences userSetting;
    private SharedPreferences.Editor userSettingEditor;

    private static final String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private boolean isFromVideoLib = false;
    private View views;
    private ImageButton reset;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SDK_INT < Build.VERSION_CODES.P) {
            return;
        }
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            Object sVmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
        } catch (Throwable e) {
            Log.e("[error]", "reflect bootstrap failed:", e);
        }

        setContentView(R.layout.activity_main);

        userSetting = getSharedPreferences("usersetting",MODE_PRIVATE);
        userSettingEditor = userSetting.edit();
        userSettingEditor.apply();

        requestPermissions(PERMISSIONS_CAMERA_AND_STORAGE, 123);

        InitView();
    }

    private void InitView() {
        textButton = findViewById(R.id.textbtn);
        viewPager = findViewById(R.id.imgVP);
        button = findViewById(R.id.btn);
        coordinatorLayout = findViewById(R.id.cons);
        imageView = findViewById(R.id.background);
        fileChoose = findViewById(R.id.choosefile);
        imgLibChoose = findViewById(R.id.ImgLibChoose);
        imgbackiv = findViewById(R.id.backiv);
        viewTab = findViewById(R.id.viewList);
        videoLibChoose = findViewById(R.id.videoLibChoose);

        reset = findViewById(R.id.restPage);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagerNum>0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("清空所有页面");
                    alert.setNegativeButton("不",null);
                    alert.setPositiveButton("好", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pagerNum = 0;
                            fragments.clear();
                            viewPager.removeAllViews();
                            imgvp2_adapter.notifyDataSetChanged();
                            imgvp2_adapter = null;
                            viewTab.removeAllTabs();
                            reset.setVisibility(View.INVISIBLE);
                        }
                    }).create();
                    alert.show();
                    InitViewPager();
                }
            }
        });

        showFAB = findViewById(R.id.showFAB);
        showFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonInMid();
                ScreenPXMath.ShowBottomUIMenu(views,MainActivity.this);
            }
        });
        showFAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowTabLayout();
                return true;
            }
        });

        viewTab.setupWithViewPager(viewPager,false);
        viewTab.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyTextBook.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(this::clk);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowTabLayout();
                return true;
            }
        });
        imageView.setVisibility(View.INVISIBLE);
        fileChoose.setVisibility(View.INVISIBLE);
        imgLibChoose.setVisibility(View.INVISIBLE);
        videoLibChoose.setVisibility(View.INVISIBLE);
        //打开文件夹选择页面
        fileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChoosen = false;
                Intent intent= new Intent(MainActivity.this, FileChooseActivity.class);
                intent.putExtra("getStorageDirectory",path);
                startActivityForResult(intent,4);
            }
        });
        //打开相册选择页面
        imgLibChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatorSet!=null){
                    animatorSet.end();
                }
                Intent intent = new Intent(MainActivity.this,ImgLibFolderChoose.class);
                startActivityForResult(intent,3);
            }
        });

        //打开视频相册选择界面
        videoLibChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatorSet!=null){
                    animatorSet.end();
                }
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,6);
                isFromVideoLib = true;
            }
        });

        viewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("viewpager","clk");
                if (animatorSet!=null){
                    animatorSet.end();
                }
                ObjectAnimator objectAnimatortrans = ObjectAnimator.ofFloat(button,"Alpha",0f,1f);
                objectAnimatortrans.start();
                return true;
            }
        });
    }

    private void ShowTabLayout() {
        if(!tabLayoutIsShow){
            viewTab.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewTab,"alpha",0f,1f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(viewTab,"translationX",400,0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimator,objectAnimator1);
            animatorSet.setDuration(200);
            animatorSet.start();
            tabLayoutIsShow = true;
        }else{
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewTab,"alpha",1f,0f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(viewTab,"translationX",0,400);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(objectAnimator,objectAnimator1);
            animatorSet.setDuration(200);
            animatorSet.start();
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    viewTab.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.INVISIBLE);
                }
            });
            tabLayoutIsShow = false;
        }
    }

    private void AppCourse() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (userSetting.getString("userchoosen","null").equals("null")){
            dialog.setView(R.layout.appcourse);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            dialog.setNegativeButton("明白（不再提示）", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userSettingEditor.putString("userchoosen","不再提醒").apply();
                }
            }).create();
            dialog.show();
        }
    }

    private void InitViewPager() {
        if(ImgUri != null || videoUri != null){
            if(pagerNum == 0){
                if (isFromVideoLib){
                    fragments.add(new ScrollingVideoFragment(MainActivity.this,videoUri));
                    isFromVideoLib = false;
                }else{
                    fragments.add(new ScrollingFragment(MainActivity.this,ImgUri));
                }
                imgvp2_adapter = new IMGVP2_Adapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments);
                viewPager.setAdapter(imgvp2_adapter);
                pagerNum++;
            }else if(!isFromVideoLib){
                AddNewPage();
            }else {
                AddVideoPlayPage();
                isFromVideoLib = false;
            }
            ImgUri = null;
            videoUri = null;
        }
    }

    private void AddNewPage(){
//        fragments.add(new ScrollingFragment(MainActivity.this,ImgUri));
        imgvp2_adapter.addPage(new ScrollingFragment(MainActivity.this,ImgUri));
        viewTab.addTab(viewTab.newTab());
    }

    private void AddVideoPlayPage(){
//        fragments.add(new ScrollingVideoFragment(MainActivity.this,videoUri));
//        viewPager.removeViewAt(0);
//        imgvp2_adapter.notifyDataSetChanged();
        imgvp2_adapter.addPage(new ScrollingVideoFragment(MainActivity.this,videoUri));
        viewTab.addTab(viewTab.newTab());
    }



    public void clk(View view) {
        //检查按钮是否在屏幕中间，否则现将按钮置于屏幕中间
        if (buttonIsMid){
            //检测权限，没有则申请
            if (!GetFilePath.CheckStoragePermission(this,123)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("请开启文件读取权限");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GetFilePath.OpenAppSetting(MainActivity.this);
                    }
                }).create();
                dialog.show();
                //判断选择菜单是否打开
            } else if (!listIsOpen) {
                path = GetFilePath.getAllSdPaths(this);
                listIsOpen = true;
                OpenMenuAnimator();
            } else {
                CloseMenuAnimator();
                listIsOpen = false;
                if(!DCIMLocation.equals("")){
                    isChoosen = false;
                    buttonIsMid = false;
                    animatorSetone.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imgbackiv,"scaleX",1f,0f);
                            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imgbackiv,"scaleY",1f,0f);
                            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imgbackiv,"alpha",1f,0f);
                            AnimatorSet animatorSet1 = new AnimatorSet();
                            animatorSet1.playTogether(objectAnimator,objectAnimator1,objectAnimator2);
                            animatorSet1.setDuration(100);
                            animatorSet1.start();
                            animatorSet1.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    imgbackiv.setVisibility(View.INVISIBLE);
                                    coordinatorLayout.setBackground(getDrawable(R.color.white));
                                }
                            });
                            SetPrepareView();
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppCourse();
                            InitViewPager();
                            ScreenPXMath.HideBottomUIMenu(view,MainActivity.this);
                            views = view;
//                            ImgViewer.ShowImgThread(MainActivity.this, imageViews, linearLayout, ImgUri);
                        }
                    });
                }
            }
        }
    }
    //按钮置于中间的动画效果
    private void SetButtonInMid(){
        buttonIsMid = true;
        isFromVideoLib = false;
        ObjectAnimator objectAnimatortrans = ObjectAnimator.ofFloat(button,"translationX",disPlayWight,0);
        ObjectAnimator objectAnimatortrans1 = ObjectAnimator.ofFloat(button,"translationY",displayHight,0);
        ObjectAnimator objectAnimatortrans2 = ObjectAnimator.ofFloat(button,"Alpha",0f,1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatortrans,objectAnimatortrans1,objectAnimatortrans2);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgbackiv.setVisibility(View.VISIBLE);
                showFAB.setVisibility(View.INVISIBLE);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imgbackiv,"scaleX",0.5f,1.0f);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imgbackiv,"scaleY",0.5f,1.0f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imgbackiv,"alpha",0f,1f);
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.playTogether(objectAnimator,objectAnimator1,objectAnimator2);
                animatorSet1.setDuration(200);
                animatorSet1.start();
            }
        });
    }
    //开启图片显示后启动按钮置于右下角的动画效果
    private void SetPrepareView(){
        animatorSet.end();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] location = new int[2];
        button.getLocationInWindow(location); int viewX = location[0]; int viewY = location[1];
        displayHight = displayMetrics.heightPixels - viewY- (displayMetrics.heightPixels/8);
        disPlayWight = displayMetrics.widthPixels - viewX - (displayMetrics.widthPixels/5);
        ObjectAnimator objectAnimatortrans = ObjectAnimator.ofFloat(button,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimatortrans1 = ObjectAnimator.ofFloat(button,"translationY",0,displayHight);
        ObjectAnimator objectAnimatortrans2 = ObjectAnimator.ofFloat(button,"Alpha",1f,0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatortrans,objectAnimatortrans1,objectAnimatortrans2);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showFAB.setVisibility(View.VISIBLE);
            }
        });
    }
    //关闭菜单的动画
    private void CloseMenuAnimator() {
        if (animatorSet!=null){
            animatorSet.end();
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"scaleX",1.0f,0f);
        ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(imageView,"scaleY",1.0f,0f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(fileChoose,"translationX",0,300);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imgLibChoose,"translationX",0,-300);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(videoLibChoose,"translationY",0,-350);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(textButton,"translationY",-310,0);
        animatorSetone = new AnimatorSet();
        animatorSetone.playTogether(objectAnimator,objectAnimator1,objectAnimator2,objectAnimator3,objectAnimator4,objectAnimator5);
        animatorSetone.setDuration(250);
        animatorSetone.start();
        animatorSetone.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fileChoose.setVisibility(View.INVISIBLE);
                imgLibChoose.setVisibility(View.INVISIBLE);
                videoLibChoose.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                textButton.setVisibility(View.INVISIBLE);
            }
        });

    }
    //打开菜单的动画
    private void OpenMenuAnimator() {

        fileChoose.setVisibility(View.VISIBLE);
        imgLibChoose.setVisibility(View.VISIBLE);
        videoLibChoose.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        textButton.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(textButton,"translationY",0,-310);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(textButton,"scaleX",1,1.2f);
        ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(textButton,"scaleY",1,1.2f);
        objectAnimator3.setInterpolator(new OvershootInterpolator());
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"scaleX",0.0f,1.0f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageView,"scaleY",0.0f,1.0f);
        ObjectAnimator objectAnimatortrans1 = ObjectAnimator.ofFloat(fileChoose,"translationX",300,0);
        ObjectAnimator objectAnimatortrans2 = ObjectAnimator.ofFloat(imgLibChoose,"translationX",-300,0);
        ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(videoLibChoose,"translationY",-350,0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator,objectAnimatortrans1,objectAnimatortrans2,objectAnimator3,objectAnimator4,objectAnimator5,objectAnimator6,objectAnimator1);
        animatorSet.setDuration(250);
        animatorSet.start();
    }
    //选择路径之后按钮处于准备阶段的动态效果
    private void ReadyToGO(){
        animatorSet = new AnimatorSet();
        if(isChoosen){
            ImageView imageView = findViewById(R.id.wave);
            imageView.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"scaleX",0.5f,1.0f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageView,"scaleY",0.5f,1.0f);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageView,"alpha",1f,0f);
            objectAnimator.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator.setRepeatCount(-1);
            objectAnimator1.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator1.setRepeatCount(-1);
            objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator2.setRepeatCount(-1);
            animatorSet.playTogether(objectAnimator,objectAnimator1,objectAnimator2);
            animatorSet.setDuration(900);
            animatorSet.start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4){
            if(data != null){
                DCIMLocation = data.getStringExtra("FolderSelected");
                isChoosen = true;
                isFromVideoLib = false;
                ImgUri = GetFilePath.SearchData(MainActivity.this, DCIMLocation);
                ReadyToGO();
            }
        }else if(resultCode == 5){
            if(data!= null){
                DCIMLocation = data.getStringExtra("LibSelected");
                ImgUri = GetFilePath.SearchData(MainActivity.this, DCIMLocation);
                isFromVideoLib = false;
                isChoosen = true;
                ReadyToGO();
            }
        }else if(requestCode== 6){
            if (data != null){
                videoUri = data.getData();
                DCIMLocation = "1";
                isFromVideoLib = true;
                isChoosen = true;
                ReadyToGO();
            }
        }
    }

}

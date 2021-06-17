package com.example.imgviewer.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.imgviewer.Adapter.MyTextAdapter;
import com.example.imgviewer.Model.TextViewModel;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDataBase;
import com.example.imgviewer.Utils.IMG;
import com.example.imgviewer.test.MySurfing;
import com.example.imgviewer.test.MyText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.imgviewer.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class MyTextBook extends AppCompatActivity {

    private List<Text> mtext;
    private int disPlayWight;
    private boolean isOpen = false;
    private RelativeLayout addTextPopWindows;
    private FragmentContainerView addText;
    private FragmentManager fragmentManager;
    private ImageButton goBack;
    private ImageView textBack;
    private ArrayList<Fragment> fragmens = new ArrayList<>();
    private TabLayout tabLayout;

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

        setContentView(R.layout.activity_my_text_book);

        if (SDK_INT>=Build.VERSION_CODES.R){
            DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
            disPlayWight = displayMetrics1.widthPixels-150;
        }else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            disPlayWight = displayMetrics.widthPixels-150;
        }

        InitView();
        InitViewPager();


        InitRecyclerView();
    }

    private void InitView() {
        addTextPopWindows = findViewById(R.id.addTextPopWindows);
        addText = findViewById(R.id.addTextFragment);
        ImageButton callPopWindows = findViewById(R.id.callPopWindows);
        goBack = findViewById(R.id.goBack);
        tabLayout = findViewById(R.id.viewpager_tab);
        textBack = findViewById(R.id.textBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addTextPopWindows,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(addText,"translationX",0,disPlayWight+100);
        objectAnimator.setDuration(0);
        objectAnimator1.setDuration(0);
        objectAnimator1.start();
        objectAnimator.start();

        callPopWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopWindow();
            }
        });

//        addText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyTextBook.this,EditTextActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void InitViewPager() {
        ViewPager2 viewPager = findViewById(R.id.myText_viewPager);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new MyText(MyTextBook.this, mtext);
                }
                return new MySurfing(MyTextBook.this);

            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position==0){
                    tab.setText("记事本");
                }else {
                    tab.setText("网页");
                }
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1){
                    addTextPopWindows.setVisibility(View.INVISIBLE);
                    goBack.setVisibility(View.INVISIBLE);
                }else {
                    addTextPopWindows.setVisibility(View.VISIBLE);
                    goBack.setVisibility(View.VISIBLE);
                }
                tabLayout.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tabLayout,"alpha",0f,1f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(tabLayout,"alpha",1f,0f);
                objectAnimator.setDuration(400); objectAnimator2.setDuration(800);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(objectAnimator,objectAnimator2);
                animatorSet.start();;
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tabLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void InitRecyclerView() {
//        RecyclerView textRCV = findViewById(R.id.mytextRCV);
//        textRCV.setLayoutManager(new LinearLayoutManager(MyTextBook.this));
//        mtext = textDataBase.getTextDAO().QueryAll();
        TextViewModel textViewModel = new ViewModelProvider(this).get(TextViewModel.class);
        LiveData<List<Text>> user = textViewModel.getUser(this);
        user.observe(MyTextBook.this, new Observer<List<Text>>() {
            @Override
            public void onChanged(List<Text> texts) {
                mtext = texts;
//                fragmens.add(new MyText(MyTextBook.this,mtext));
//                myTextPagerAdapter.notifyDataSetChanged();
//                MyTextAdapter myTextAdapter = new MyTextAdapter(MyTextBook.this, mtext);
//                textRCV.setAdapter(myTextAdapter);
//                textRCV.setItemAnimator(null);
                if (isOpen){
                    ClosePopWindows();
                    isOpen = false;
                }
                if (fragmentManager==null){
                    InitFragment();
                }
            }
        });

    }

    private void ShowPopWindow(){
        if (isOpen){
            isOpen = false;
            goBack.setVisibility(View.VISIBLE);
            ClosePopWindows();
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(textBack,"alpha",0.8f,0f);
            objectAnimator.setDuration(100).start();
        }else {
            isOpen = true;
            goBack.setVisibility(View.INVISIBLE);
            textBack.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(textBack,"alpha",0f,0.8f);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addTextPopWindows,"translationX",disPlayWight,0);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(addText,"translationX",disPlayWight+100,0);
            objectAnimator1.start();
            objectAnimator.start();
            objectAnimator2.setDuration(100).start();
        }
    }

    private void ClosePopWindows(){
        goBack.setVisibility(View.VISIBLE);
        textBack.setVisibility(View.INVISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addTextPopWindows,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(addText,"translationX",0,disPlayWight+100);
        objectAnimator1.start();
        objectAnimator.start();
    }

    private void InitFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.addTextFragment,AddTextFragment.newInstance(MyTextBook.this,MyTextBook.this))
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
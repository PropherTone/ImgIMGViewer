package com.example.imgviewer.View;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.imgviewer.Adapter.IMGPathRecyclerView_Adapter;
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.FileViewer;
import com.example.imgviewer.Utils.IMGdealler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ImgLibFolderChoose extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> images = new ArrayList<>();
    RecyclerView rvFolderView;
    ArrayList<String> imgFolderName = new ArrayList<>();
    Button back,confirm;
    private Intent intent;
    private IMGPathRecyclerView_Adapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_img_lib_folder_choose);

        LinearLayout bar = findViewById(R.id.filebar);
        bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(getResources()
                        .getIdentifier("status_bar_height", "dimen", "android"))));
        bar.setBackground(getDrawable(R.drawable.colorful_bar));

        InitView();
        LiveData<ArrayList<String>> liveData  = IMGdealler.LoadImg(ImgLibFolderChoose.this);
        liveData.observe(ImgLibFolderChoose.this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                images = strings;
//                HashMap<String,ArrayList<String>> map = FileViewer.SortIMGFolder(images);
//                Set<String> sss = map.keySet();
//                for (String i : sss) {
//                    Log.e("getmap",i);
//                    ArrayList<String> ss = map.get(i);
//                    Log.e("getmapinsize","xxx"+ss.size());
//                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rvFolderView == null){
                            InitImgFolder();
                        }else {
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

    }

    private void InitView() {
        back = findViewById(R.id.ImgFilelList_backbtn);
        confirm = findViewById(R.id.ImgFilelList_confirmbtn);

        back.setOnClickListener(this);
    }



    private void InitImgFolder() {
        rvFolderView = findViewById(R.id.ImgFilelList_rcview);
        rvFolderView.setLayoutManager(new LinearLayoutManager(ImgLibFolderChoose.this));
        recyclerViewAdapter =
                new IMGPathRecyclerView_Adapter(ImgLibFolderChoose.this,images);
        recyclerViewAdapter.LibSelected(new IMGPathRecyclerView_Adapter.onLibSelected() {
            @Override
            public void Libselect(String string,boolean ischecked) {
                getLibSelected(string,ischecked);
            }
        });

        rvFolderView.setAdapter(recyclerViewAdapter);
        ((SimpleItemAnimator)rvFolderView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void getLibSelected(String string,boolean ischecked){
        if (ischecked){
            confirm.setBackground(getDrawable(R.drawable.curclestyle));
            confirm.setOnClickListener(this);
        }else {
            confirm.setBackground(getDrawable(R.drawable.uncheckbtn));
        }
        intent = new Intent();
        intent.putExtra("LibSelected",string);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ImgFilelList_backbtn:
                finish();
                break;
            case R.id.ImgFilelList_confirmbtn:
                    setResult(5,intent);
                    finish();
                break;
        }
    }
}
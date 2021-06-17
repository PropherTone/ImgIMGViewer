//package com.example.imgviewer.View;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.imgviewer.Adapter.IMGPathRecyclerView_Adapter;
//import com.example.imgviewer.R;
//import com.example.imgviewer.Utils.IMGdealler;
//
//import java.util.ArrayList;
//
//public class VideoLibFolderChoose extends AppCompatActivity implements View.OnClickListener {
//
//    ArrayList<String> images = new ArrayList<>();
//    RecyclerView rvFolderView;
//    ArrayList<String> imgFolderName = new ArrayList<>();
//    Button back,confirm;
//    private Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        setContentView(R.layout.activity_img_lib_folder_choose);
//
//        LinearLayout bar = findViewById(R.id.filebar);
//        bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getResources().getDimensionPixelSize(getResources()
//                        .getIdentifier("status_bar_height", "dimen", "android"))));
//        bar.setBackground(getDrawable(R.drawable.colorful_bar));
//
//        InitView();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                images = IMGdealler.LoadVideo(VideoLibFolderChoose.this);
//                InitImgFolder();
//            }
//        });
//
//    }
//
//    private void InitView() {
//        back = findViewById(R.id.ImgFilelList_backbtn);
//        confirm = findViewById(R.id.ImgFilelList_confirmbtn);
//
//        back.setOnClickListener(this);
//    }
//
//
//
//    private void InitImgFolder() {
//        rvFolderView = findViewById(R.id.ImgFilelList_rcview);
//        rvFolderView.setLayoutManager(new LinearLayoutManager(VideoLibFolderChoose.this));
//        IMGPathRecyclerView_Adapter recyclerViewAdapter =
//                new IMGPathRecyclerView_Adapter(VideoLibFolderChoose.this,images);
//        recyclerViewAdapter.LibSelected(new IMGPathRecyclerView_Adapter.onLibSelected() {
//            @Override
//            public void Libselect(String string,boolean ischecked) {
//                getLibSelected(string,ischecked);
//            }
//        });
//
//        rvFolderView.setAdapter(recyclerViewAdapter);
//    }
//
//    private void getLibSelected(String string,boolean ischecked){
//        if (ischecked){
//            confirm.setBackground(getDrawable(R.drawable.curclestyle));
//            confirm.setOnClickListener(this);
//        }else {
//            confirm.setBackground(getDrawable(R.drawable.uncheckbtn));
//        }
//        intent = new Intent();
//        intent.putExtra("VideoLibSelected",string);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ImgFilelList_backbtn:
//                finish();
//                break;
//            case R.id.ImgFilelList_confirmbtn:
//                    setResult(6,intent);
//                    finish();
//                break;
//        }
//    }
//}
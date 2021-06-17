package com.example.imgviewer.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.imgviewer.Adapter.FileChoosenRVAtapter;
import com.example.imgviewer.Adapter.FileViewRecyclerView_Adapter;
import com.example.imgviewer.R;

public class FileChooseActivity extends AppCompatActivity  {

    Button file_Back,file_Choosen;
    private String[] path;
    public RecyclerView recyclerView,folderRecycler;
    private Intent intent;
    public FileViewRecyclerView_Adapter fileViewRecyclerView_adapter;
    public String folderName;
    private FileChoosenRVAtapter fileChoosenRVAtapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);

        //设置状态栏颜色
        LinearLayout bar = findViewById(R.id.bar);
        bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(getResources()
                        .getIdentifier("status_bar_height", "dimen", "android"))));
        bar.setBackground(getDrawable(R.drawable.colorful_bar));

        Intent intent = getIntent();
        path = intent.getStringArrayExtra("getStorageDirectory");
        InitView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (folderRecycler==null&&recyclerView==null){
                    InitFolderChoosenView();
                    InitFolderCyclerView();
                }else {
                    fileChoosenRVAtapter.notifyDataSetChanged();
                    fileViewRecyclerView_adapter.notifyDataSetChanged();
                }
            }
        }).start();
    }

    //显示选择路径的Recyclerview初始化
    private void InitFolderChoosenView() {
        folderRecycler = findViewById(R.id.folderChoosenRV);
        folderRecycler.setLayoutManager(new LinearLayoutManager(FileChooseActivity.this,RecyclerView.HORIZONTAL,false));
        fileChoosenRVAtapter = new FileChoosenRVAtapter();
        fileChoosenRVAtapter.mFileChoosenRVAtapter(this,folderName);
        fileChoosenRVAtapter.ChooseFolderListener(new FileChoosenRVAtapter.ChooseFolder() {
            @Override
            public void Folder(String folder) {
                fileViewRecyclerView_adapter.Back(folder);
            }
        });
        folderRecycler.setAdapter(fileChoosenRVAtapter);
        ((SimpleItemAnimator)folderRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    //文件夹列表的Recyclerview初始化
    private void InitFolderCyclerView() {
        recyclerView = findViewById(R.id.folderview);
        recyclerView.setLayoutManager(new LinearLayoutManager(FileChooseActivity.this));
        fileViewRecyclerView_adapter = new FileViewRecyclerView_Adapter();
        fileViewRecyclerView_adapter.FileViewRecyclerViewAdapter(FileChooseActivity.this,path);
        fileViewRecyclerView_adapter.FolderSelectedListener(new FileViewRecyclerView_Adapter.OnFolderSelected() {
            @Override
            public void Folderselect(String string, boolean ischecked) {
                getLibSelected(string,ischecked);
            }
        });
        fileViewRecyclerView_adapter.CallBackFolderNameListener(new FileViewRecyclerView_Adapter.CallBackFolderName() {
            @Override
            public void FolderName(String string) {
                fileChoosenRVAtapter.test(string);
            }
            @Override
            public void IsChecked(String string,Boolean check){
                fileChoosenRVAtapter.IsCheck(string,check);
            }
        });
        recyclerView.setAdapter(fileViewRecyclerView_adapter);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void InitView() {
//        ImageView folderIMG = findViewById(R.id.folderChoosen);
        file_Back = findViewById(R.id.file_back);
        file_Choosen = findViewById(R.id.file_choosen);

        file_Back.setOnClickListener(this::BackCLK);

    }

    private void ChoosenCLK(View view) {
        setResult(4,intent);
        finish();
    }

    private void BackCLK(View view) {
        finish();
    }

    private void getLibSelected(String string,boolean ischecked){
        if (ischecked){
            file_Choosen.setBackground(getDrawable(R.drawable.curclestyle));
            file_Choosen.setOnClickListener(this::ChoosenCLK);
        }else {
            file_Choosen.setBackground(getDrawable(R.drawable.uncheckbtn));
        }
        intent = new Intent();
        intent.putExtra("FolderSelected",string);
//        Log.e("foldername",folderName);
    }

}
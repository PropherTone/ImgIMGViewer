package com.example.imgviewer.View;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.example.imgviewer.Adapter.GridViewAdapter;
import com.example.imgviewer.Adapter.IMGFolderList_Adapter;
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.FileViewer;
import com.example.imgviewer.Utils.GetFilePath;
import com.example.imgviewer.Utils.IMG;
import com.example.imgviewer.Utils.IMGdealler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class IMGLibChoose extends AppCompatActivity {

    public String choosenIMG;
    private final IMG img = new IMG();
    public ArrayList<String> list_selectedIMGList = new ArrayList<>();
    public String selectedIMGList;
    private GridViewAdapter gridViewAdapter;
    private RecyclerView recyclerView;

    private LinearLayout choose_IMGLib;
    private RecyclerView iMGFolderList;
    private boolean isOpen;
    private int disPlayWight;
    public HashMap<String,ArrayList<String>> iMGLibMap;
    public  ArrayList<String> iMGFolder = new ArrayList<>();
    private IMGFolderList_Adapter imgFolderList_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_m_g_lib_choose);
        //设置状态栏颜色
        LinearLayout bar = findViewById(R.id.barimg);
        bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(getResources()
                        .getIdentifier("status_bar_height", "dimen", "android"))));
        bar.setBackground(getDrawable(R.drawable.colorful_bar));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        disPlayWight = displayMetrics.widthPixels/2 +30;
        LiveData<HashMap<String, ArrayList<String>>> liveDta = IMGdealler.LoadImgMap(this);
        liveDta.observe(IMGLibChoose.this, new Observer<HashMap<String, ArrayList<String>>>() {
            @Override
            public void onChanged(HashMap<String, ArrayList<String>> stringArrayListHashMap) {
                iMGLibMap = stringArrayListHashMap;
                iMGFolder.addAll(stringArrayListHashMap.keySet());
                Log.e("SelectLibPath", "1"+iMGFolder);
                if (stringArrayListHashMap.get("全部相片") !=null){
                    list_selectedIMGList.addAll(stringArrayListHashMap.get("全部相片"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (iMGFolderList == null && recyclerView == null){
                            InitGriView();
                            InitIMGFolderList();
                        }else {
                            imgFolderList_adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        InitView();
    }

    private void InitIMGFolderList() {
        iMGFolderList = findViewById(R.id.iMGFolderSelect);
        iMGFolderList.setLayoutManager(new LinearLayoutManager(this));
        ImageView isSelected = findViewById(R.id.isSelected_IMGFolderView);
        TextView  isSelectedName = findViewById(R.id.isSelected_IMGFolderName);

        isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(choose_IMGLib,"translationX",disPlayWight+200,0);
                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(iMGFolderList,"translationX",disPlayWight+300,0);
                    objectAnimator1.start();
                    objectAnimator.start();
                    isOpen = true;
                }else {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(choose_IMGLib,"translationX",0,disPlayWight+200);
                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(iMGFolderList,"translationX",0,disPlayWight+300);
                    objectAnimator1.start();
                    objectAnimator.start();
                    isOpen = false;
                }
            }
        });

        imgFolderList_adapter = new IMGFolderList_Adapter(this,iMGFolder);
        imgFolderList_adapter.LibSelected(new IMGFolderList_Adapter.onLibSelected() {
            @Override
            public void LibSelect(String string,String s) {
                if (iMGLibMap.get(string) != null){
                    list_selectedIMGList.clear();
                    list_selectedIMGList.addAll(iMGLibMap.get(string));
                    gridViewAdapter.notifyDataSetChanged();
                }
                if (string.equals("全部相片")){
                    isSelectedName.setText(string);
                }else{
                    isSelectedName.setText(FileViewer.getFolderName(string,"name"));
                }
                if (s!=null){
                    Glide.with(IMGLibChoose.this).asBitmap().load(s).centerCrop().into(isSelected);
                }else {
                    isSelected.setBackground(getDrawable(R.drawable.colorful_bar));
                }
            }
        });
        iMGFolderList.setAdapter(imgFolderList_adapter);
        ((SimpleItemAnimator)iMGFolderList.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void InitView() {
        choose_IMGLib = findViewById(R.id.choose_IMGLib);
        Button back = findViewById(R.id.imglib_back);
        Button confirm = findViewById(R.id.img_choosen);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("chooseIMG",choosenIMG);
                setResult(0x20,intent);
//                Log.e("uri","1"+choosenIMG);
                finish();
                if (img.getImgUri() != null){
                    choosenIMG = img.getImgUri();
                }
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(choose_IMGLib,"translationX",0,disPlayWight+200);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(iMGFolderList,"translationX",0,disPlayWight+300);
        objectAnimator1.start();
        objectAnimator.start();
        isOpen = false;
    }

    private void InitGriView() {
        recyclerView = findViewById(R.id.gridview);
        recyclerView.setLayoutManager(new GridLayoutManager(IMGLibChoose.this,4));
        gridViewAdapter = new GridViewAdapter(this, list_selectedIMGList);
        gridViewAdapter.GetChooseListener(new GridViewAdapter.GetChoose() {
            @Override
            public void GetimgChoose(String uri) {
                if (uri != null){
                    choosenIMG = uri;
                }
            }
        });
        recyclerView.setAdapter(gridViewAdapter);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        recyclerView.getItemAnimator().setChangeDuration(0);
    }
}
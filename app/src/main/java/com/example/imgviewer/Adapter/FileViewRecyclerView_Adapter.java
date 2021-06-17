package com.example.imgviewer.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.Utils.FileViewer;
import com.example.imgviewer.Utils.GetFilePath;
import com.example.imgviewer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FileViewRecyclerView_Adapter extends RecyclerView.Adapter<FileViewRecyclerView_Adapter.ViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<String> folderPath = new ArrayList<>();
    private Context mcontext;
    public ArrayList<String> viewSelected = new ArrayList<>();
    private boolean isChecked;
    private OnFolderSelected onFolderSelectedListner;
    private CallBackFolderName callBackFolderNameListener;


    public void FileViewRecyclerViewAdapter(Context context,String[] path) {
        Log.e("fvra","1");
        this.mcontext = context;
        Collections.addAll(folderPath,path);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.file_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetItemChecked(holder,viewSelected.contains(FileViewer.getFolderName(folderPath.get(position),"finnalfoldername")),position);
        Log.e("onBindViewHolder","onBindViewHolder");
        holder.folderLibName.setText(FileViewer.getFolderName(folderPath.get(position),"finnalfoldername"));
            holder.folderListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                GetFilePath.IntoFolder()
                    IntoFolders(position);
//                    Log.e("ischecked",""+isChecked);

                }
            });


        holder.foldercheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItem(holder,folderPath.get(position),position);
//                Log.e("ischecked   1",""+isChecked);

                onFolderSelectedListner.Folderselect(folderPath.get(position),isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return folderPath==null?0:folderPath.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderLibName;
        RelativeLayout folderListItem;
        CheckBox foldercheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderLibName = itemView.findViewById(R.id.folderlList_name);
            folderListItem = itemView.findViewById(R.id.folderListItem);
            foldercheckBox = itemView.findViewById(R.id.foldercheck);
        }
    }

    private void IntoFolders(int position){
        //提前载入文件夹的内容
        ArrayList<String> foreFolder = new ArrayList<>();
        //缓存文件夹列表
        ArrayList<String> cacheFolder = new ArrayList<>(folderPath);
//        for (int i = 0;i<cacheFolder.size();i++) {
//            Log.e("cacheFolder1",""+cacheFolder.get(i));
//        }
        if (GetFilePath.IntoFolder(cacheFolder.get(position))!=null){
            foreFolder.addAll(GetFilePath.IntoFolder(folderPath.get(position)));
        }
//        Log.e("folderPath",""+foreFolder.size());
        if (folderPath.size()!=0 & foreFolder.size()!=0 & !isChecked){

            callBackFolderNameListener.IsChecked(folderPath.get(position),false);
            callBackFolderNameListener.FolderName(folderPath.get(position));
            folderPath.clear();
            folderPath.addAll(GetFilePath.IntoFolder(cacheFolder.get(position)));
            cacheFolder.clear();
            notifyDataSetChanged();
        }else if (!isChecked){
            Toast.makeText(mcontext,"里面没有东西啦",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mcontext,"取消选择",Toast.LENGTH_SHORT).show();
        }
        foreFolder.clear();
//        for (int i = 0;i<folderPath.size();i++) {
//            Log.e("folderPath5",""+folderPath.get(i));
//        }
    }

    private void CheckItem(ViewHolder holder, String path,int position){
        if (viewSelected.contains(path)){
            unChecked(path);
            SetItemChecked(holder,false,position);
        }else{
            ClearCheckItem();
            SetChecked(path);
            SetItemChecked(holder,true,position);
        }
    }

    private void SetChecked(String path){
        viewSelected.add(path);
    }

    private void unChecked(String path){
        viewSelected.remove(path);
    }

    private void SetItemChecked(ViewHolder holder, boolean isSelected,int position){
        if (isSelected)
        {
            isChecked = true;
            callBackFolderNameListener.IsChecked(folderPath.get(position),isChecked);
            holder.foldercheckBox.setChecked(true);
        }else {
            isChecked = false;
            callBackFolderNameListener.IsChecked(folderPath.get(position),isChecked);
            holder.foldercheckBox.setChecked(false);
        }
    }

    private void ClearCheckItem() {
        if(folderPath != null && viewSelected.size() == 1){
            int index = folderPath.indexOf(viewSelected.get(0));
            viewSelected.clear();
            if (index != -1 ){
                notifyItemChanged(index);
            }
        }
    }

    public interface OnFolderSelected{
        void Folderselect(String string,boolean ischecked);
    }

    public void FolderSelectedListener(OnFolderSelected onFolderSelected){
        this.onFolderSelectedListner = onFolderSelected;
    }

    public interface CallBackFolderName{
        void FolderName(String string);
        void IsChecked(String string,Boolean check);
    }

    public  void CallBackFolderNameListener(CallBackFolderName callBackFolderName){
        this.callBackFolderNameListener = callBackFolderName;
    }

    public void Back(String s){
        Log.e("tag","back"+s);
        folderPath.clear();
        folderPath.addAll(GetFilePath.IntoFolder(s));
        for(String paths : folderPath){
            Log.e("path",paths);
        }
        notifyDataSetChanged();
    }
}

package com.example.imgviewer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.R;
import com.example.imgviewer.Utils.FileViewer;

import java.util.ArrayList;
import java.util.List;

public class FileChoosenRVAtapter extends RecyclerView.Adapter<FileChoosenRVAtapter.ViewHolder> {

    private LayoutInflater inflater;
    public Context mcontext;
    public ArrayList<String> folderName = new ArrayList<>();
    private ChooseFolder chooseFolderListener;


    public void mFileChoosenRVAtapter(Context context,String name) {
        this.mcontext = context;        Log.e("fcrva","1");
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { Log.e("fcrva","1");
        View view = inflater.inflate(R.layout.filechoosen_rvitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.folderText.setText(FileViewer.getFolderName(folderName.get(position),"finnalfoldername"));

        holder.folderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFolderListener.Folder(folderName.get(position));
                List<String> sublist = folderName.subList(position+1, folderName.size());
                folderName.removeAll(sublist);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderName==null?0:folderName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderText = itemView.findViewById(R.id.choosenFolderName);
        }
    }

    public  void test(String name){
        if (folderName.size()>0){
            if (!folderName.get(folderName.size()-1).equals(name)){
                this.folderName.add(name);
            }
        }else {
            this.folderName.add(name);
        }
        notifyDataSetChanged();
    }

    public  void IsCheck(String name,Boolean ischecked){
        if (ischecked){
            if (folderName.size()>0){
                if (!folderName.get(folderName.size()-1).equals(name)){
                    this.folderName.add(name);
                }
            }else {
                this.folderName.add(name);
            }
        }else {
            this.folderName.remove(name);
        }
        notifyDataSetChanged();
    }

    public interface ChooseFolder{
        void Folder(String folder);
    }

    public void ChooseFolderListener(ChooseFolder chooseFolder){
        this.chooseFolderListener = chooseFolder;
    }
}

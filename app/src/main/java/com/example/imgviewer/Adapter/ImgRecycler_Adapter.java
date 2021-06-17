package com.example.imgviewer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;

import java.util.ArrayList;
import java.util.List;

public class ImgRecycler_Adapter extends RecyclerView.Adapter<ImgRecycler_Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<String> imgPath = new ArrayList<>();
    Context context;

    public ImgRecycler_Adapter(Context context,List<String> imgpaths) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imgPath = imgpaths;
        Log.e("adapter","in1"+imgpaths.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("adapter","in1");
        View view = inflater.inflate(R.layout.imgview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("adapter","in");
        Glide.with(context).load(imgPath.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgPath ==null?0:imgPath.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imview);
        }
    }
}

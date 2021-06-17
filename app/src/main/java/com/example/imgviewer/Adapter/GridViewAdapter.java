package com.example.imgviewer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;
import com.example.imgviewer.View.IMGLibChoose;

import java.util.ArrayList;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    private final ArrayList<String> Uri;
    private final Context context;
    private GetChoose getChooseListener;
    private final ArrayList<String> isChoosen = new ArrayList<>();
    private final LayoutInflater inflater;
    private ArrayList<String> viewSelected = new ArrayList<>();

    public GridViewAdapter(Context context, ArrayList<String> Uri) {
        this.Uri = Uri;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.imglib_gridview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetItemChecked(holder,viewSelected.contains(Uri.get(position)));
        Glide.with(context).asBitmap().load(Uri.get(position)).into(holder.imgPreview);
        holder.imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChooseListener.GetimgChoose(Uri.get(position));
                CheckItem(holder,Uri.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return Uri==null?0:Uri.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview,forceGround,selectIMG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.IMGpreview);
            forceGround = itemView.findViewById(R.id.forceground);
            selectIMG = itemView.findViewById(R.id.selectimg);
        }
    }

    private void CheckItem(ViewHolder holder, String path){
        if (viewSelected.contains(path)){
            unChecked(path);
            SetItemChecked(holder,false);
        }else{
            ClearCheckItem();
            SetChecked(path);
            SetItemChecked(holder,true);
        }
    }

    private void SetChecked(String path){
        viewSelected.add(path);
    }

    private void unChecked(String path){
        viewSelected.remove(path);
    }

    private void SetItemChecked(ViewHolder holder, boolean isSelected){
        if (isSelected)
        {
            holder.selectIMG.setVisibility(View.VISIBLE);
            holder.forceGround.setVisibility(View.VISIBLE);
        }else {
            holder.selectIMG.setVisibility(View.INVISIBLE);
            holder.forceGround.setVisibility(View.INVISIBLE);
        }
    }

    private void ClearCheckItem() {
        if(Uri != null && viewSelected.size() == 1){
            int index = Uri.indexOf(viewSelected.get(0));
            viewSelected.clear();
            if (index != -1 ){
                notifyItemChanged(index);
            }
        }
    }

    public interface GetChoose{
        void GetimgChoose(String uri);
    }

    public void GetChooseListener(GetChoose getChoose){
        this.getChooseListener = getChoose;
    }


}

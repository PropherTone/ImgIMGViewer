package com.example.imgviewer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imgviewer.Utils.FileViewer;
import com.example.imgviewer.R;

import java.util.ArrayList;

public class IMGPathRecyclerView_Adapter extends RecyclerView.Adapter<IMGPathRecyclerView_Adapter.ViewHolder> {

    public LayoutInflater inflater;
    public ArrayList<String> ImgLibPath = new ArrayList<>();
    public ArrayList<String> SelectLibPath = new ArrayList<>();
    public Context context;
    private onLibSelected onLibSelected;
    private boolean isChecked;
    private final ArrayList<String> viewSelected = new ArrayList<>();
    public IMGPathRecyclerView_Adapter(Context context, ArrayList<String> imgLibPath) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.ImgLibPath = imgLibPath;
        this.SelectLibPath = FileViewer.fileterImgLib(ImgLibPath,"");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.imglib_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetItemChecked(holder,viewSelected.contains(SelectLibPath.get(position)));

        holder.imgLibName.setText(FileViewer.getFolderName(SelectLibPath.get(position),"finnalfoldername"));
//        holder.imgLibName.setText(imags.getImgFolderName().get(position));
        Glide.with(context).asBitmap().load(CheckPreviewImg(SelectLibPath.get(position))).circleCrop().into(holder.imgLibIV);
        holder.imgListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItem(holder,SelectLibPath.get(position));
                onLibSelected.Libselect(SelectLibPath.get(position),isChecked);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItem(holder,SelectLibPath.get(position));
                onLibSelected.Libselect(SelectLibPath.get(position),isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return SelectLibPath==null ? 0:SelectLibPath.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView imgLibName;
        RelativeLayout imgListItem;
        ImageView imgLibIV;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLibName = itemView.findViewById(R.id.ImgFilelList_name);
            imgListItem = itemView.findViewById(R.id.ImgListItem);
            imgLibIV = itemView.findViewById(R.id.imgpreview);
            checkBox = itemView.findViewById(R.id.check);
        }
    }

    public interface onLibSelected{
        void Libselect(String string,boolean ischecked);
    }

    public void LibSelected(onLibSelected onLibSelected){
        this.onLibSelected = onLibSelected;
    }

    private String CheckPreviewImg(String s){
        String preview = null;
        for(int i = 0;i<ImgLibPath.size();i++)
        {
            if(ImgLibPath.get(i).contains(s)){
                preview=ImgLibPath.get(i);
                break;
            }
        }
        return preview;
    }

    private void CheckItem(ViewHolder holder,String path){
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

    private void SetItemChecked(ViewHolder holder,boolean isSelected){
        if (isSelected)
        {
            isChecked = true;
            holder.checkBox.setChecked(true);
        }else {
            isChecked = false;
            holder.checkBox.setChecked(false);
        }
    }

    private void ClearCheckItem() {
        if(SelectLibPath != null && viewSelected.size() == 1){
            int index = SelectLibPath.indexOf(viewSelected.get(0));
            viewSelected.clear();
            if (index != -1 ){
                notifyItemChanged(index);
            }
        }
    }
}

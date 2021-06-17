package com.example.imgviewer.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.FileViewer;
import com.example.imgviewer.Utils.GetFilePath;
import com.example.imgviewer.View.MainActivity;

import java.util.ArrayList;

public class IMGFolderList_Adapter extends RecyclerView.Adapter<IMGFolderList_Adapter.ViewHolder> {

    private Context mContext;
    private final LayoutInflater inflater;
    public ArrayList<String> ImgLibPath = new ArrayList<>();
    public ArrayList<String> SelectLibPath = new ArrayList<>();
    private onLibSelected onLibSelected;
    private final ArrayList<String> viewSelected = new ArrayList<>();
    private String path;

    public IMGFolderList_Adapter(Context context, ArrayList<String> imgLibPath) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.SelectLibPath = imgLibPath;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.imgfolderlist_view,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetItemChecked(holder,viewSelected.contains(SelectLibPath.get(position)));
        if (SelectLibPath.get(position).equals("全部相片")){
            holder.imgLibName.setText(SelectLibPath.get(position));
        }else{
            holder.imgLibName.setText(FileViewer.getFolderName(SelectLibPath.get(position),"name"));
        }
//        holder.imgLibName.setText(imags.getImgFolderName().get(position));
        if (!SelectLibPath.get(position).equals("全部相片")){
            Glide.with(mContext)
                    .asBitmap()
                    .load(SelectLibPath.get(position))
                    .centerCrop()
                    .into(holder.imgLibIV);
        }else if(SelectLibPath.get(position).equals("全部相片")){
            holder.imgLibIV.setBackground(mContext.getDrawable(R.drawable.colorful_bar));
            path = null;
        }
        holder.imgListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItem(holder,SelectLibPath.get(position));
                onLibSelected.LibSelect(SelectLibPath.get(position),SelectLibPath.get(position));
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItem(holder,SelectLibPath.get(position));
                onLibSelected.LibSelect(SelectLibPath.get(position),SelectLibPath.get(position));

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
            imgLibName = itemView.findViewById(R.id.isSelected_IMGFolderName);
            imgListItem = itemView.findViewById(R.id.iMGFolderRL);
            imgLibIV = itemView.findViewById(R.id.isSelected_IMGFolderView);
            checkBox = itemView.findViewById(R.id.iMGFolderCheckBox);
        }
    }

    public interface onLibSelected{
        void LibSelect(String string,String s);
    }

    public void LibSelected(onLibSelected onLibSelected){
        this.onLibSelected = onLibSelected;
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
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
    }

    private void ClearCheckItem() {
        if(SelectLibPath != null && viewSelected.size() == 1){
            int index = SelectLibPath.indexOf(viewSelected.get(0));
            viewSelected.clear();
            if (index != -1 ){
                notifyDataSetChanged();
            }
        }
    }
}

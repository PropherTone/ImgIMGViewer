package com.example.imgviewer.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imgviewer.Adapter.IMGFolderList_Adapter;
import com.example.imgviewer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    public int resourceId;
    private ArrayList<String> type = new ArrayList<>();
    public ArrayList<String> viewSelected = new ArrayList<>();

    public ListViewAdapter(@NonNull @NotNull Context context, int resource) {
        super(context, resource);
    }

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.type = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.typeName =(TextView) view.findViewById(R.id.typeName);
            view.setTag(viewHolder);
        }else
        {
            view = convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.typeName.setText(type.get(position));
        return view;
    }

    static class ViewHolder{
        TextView typeName;
    }

}

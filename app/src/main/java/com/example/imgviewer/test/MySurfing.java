package com.example.imgviewer.test;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.R;
import com.example.imgviewer.Web.Web;
import com.example.imgviewer.Web.WebDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class MySurfing extends Fragment implements View.OnClickListener {

    private View view;
    public Context context;
    public HashMap<String, ArrayList<String>> web_star = new HashMap<String, ArrayList<String>>();
    public ArrayList<String> types = new ArrayList<>();
    public ArrayList<String> uris = new ArrayList<>();
    EditText enter_uri,enter_type;
    private List<Web> mWebs;
    private MyWebRVAdatper myWebRVAdatper;
    LinearLayout addWebLL;
    private boolean isOpen;
    private ListView typeList;
    private int disPlayWight;
    private ListViewAdapter listViewAdapter;

    public MySurfing(Context context) {
        this.context = context;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater
            , @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.mytextbook_mysurfing,container,false);
        }

        if (SDK_INT>= Build.VERSION_CODES.R){
            DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
            disPlayWight = displayMetrics1.widthPixels/2-1;
        }else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            disPlayWight = displayMetrics.widthPixels/2-1;
        }

        InitView();
        types.add("ALL");
        InitData();
        InitRV();
        ClosePopWindows();
        return view;
    }

    private void InitRV() {

        RecyclerView recyclerView = view.findViewById(R.id.star_HTTP);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        myWebRVAdatper = new MyWebRVAdatper(context,uris,this){
            @Override
            public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.my_webView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent().setClass(context,InternetSurfing.class);
                        intent.putExtra("WebUri",uris.get(position));
                        startActivity(intent);
                        onStop();
                    }
                });
            }
        };
        if (types.get(0).equals("ALL")){
            QueryAll();
        }
        recyclerView.setAdapter(myWebRVAdatper);
    }

    private void InitView() {
        enter_uri = view.findViewById(R.id.enter_uri);
        enter_type = view.findViewById(R.id.enter_type);
        typeList = view.findViewById(R.id.type_list);
        addWebLL = view.findViewById(R.id.web_confirm);
        ImageView webConfirmView = view.findViewById(R.id.web_confirmView);
        TextView webConfirm = view.findViewById(R.id.web_confirmUri);
        
        webConfirm.setOnClickListener(this);
        webConfirmView.setOnClickListener(this);

        listViewAdapter = new ListViewAdapter(context,R.layout.listview_layout,types);
        typeList.setAdapter(listViewAdapter);
        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (types.get(position).equals("ALL")){
                    QueryAll();
                }else{
                   QueryList(types.get(position));
                }
            }
        });
    }

    private void InitData() {
        LiveData<List<Web>> web = WebDB.getInstance(context).getWebDAO().QueryAll();
        web.observe(getViewLifecycleOwner(), new Observer<List<Web>>() {
            @Override
            public void onChanged(List<Web> webs) {
                    mWebs = webs;
                    types.clear();
                    types.add("ALL");
                for (int i =0;i<webs.size();i++){
                    String type = webs.get(i).getType();
                    if (!types.contains(type)){
                        types.add(type);
                    }
                }
                listViewAdapter.notifyDataSetChanged();
//                Log.e("size", String.valueOf(types.size()));
//                web_star.clear();
//                for (int i = 0;i<types.size();i++){
//                    if(types.get(i).equals("ALL")){
//                        ArrayList<String> urlList = new ArrayList<>(WebDB.getInstance(context).getWebDAO().QueryAllUrlNotLive());
//                        web_star.put("ALL",urlList);
//                    }else {
//                        ArrayList<String> urlList = new ArrayList<>(WebDB.getInstance(context).getWebDAO().QueryWebListNotLive(types.get(i)));
//                        web_star.put(types.get(i),urlList);
//                    }
//                } Log.e("list", String.valueOf(web_star.keySet()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.web_confirmUri){
            String webUri = enter_uri.getText().toString();
            String webType = enter_type.getText().toString();
            if (webUri.startsWith("https://")||webUri.startsWith("http://")){
                WebDB.getInstance(context).getWebDAO().InsertMyWebData(new Web(webUri,webType));
            }else if (webType.contains(" ")){
                Toast.makeText(context,"类别不能有空格",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"输入正确的网络地址（https://或http://开头)",Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.web_confirmView){

            ShowPopWindow();
            InitData();
        }
    }

    private void ShowPopWindow(){
        if (isOpen){
            isOpen = false;
            ClosePopWindows();
        }else {
            isOpen = true;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addWebLL,"translationX",disPlayWight,0);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(typeList,"translationX",disPlayWight+100,0);
            objectAnimator1.start();
            objectAnimator.start();
        }
    }

    private void ClosePopWindows(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(addWebLL,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(typeList,"translationX",0,disPlayWight+100);
        objectAnimator1.start();
        objectAnimator.start();
    }

   private void QueryAll(){
       LiveData<List<String>> listLiveData = WebDB.getInstance(context).getWebDAO().QueryAllUrl();
       listLiveData.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
           @Override
           public void onChanged(List<String> strings) {
               uris.clear();
               uris.addAll(strings);
               myWebRVAdatper.notifyDataSetChanged();
           }
       });
   }

   private void QueryList(String type){
       LiveData<List<String>> listLiveData = WebDB.getInstance(context).getWebDAO().QueryWebList(type);
       listLiveData.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
           @Override
           public void onChanged(List<String> strings) {
               uris.clear();
               uris.addAll(strings);
               myWebRVAdatper.notifyDataSetChanged();
           }
       });
   }
}

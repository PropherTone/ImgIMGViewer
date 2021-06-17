package com.example.imgviewer.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.R;
import com.example.imgviewer.TextROOM.TextDataBase;
import com.example.imgviewer.Web.Web;
import com.example.imgviewer.Web.WebDB;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyWebRVAdatper extends RecyclerView.Adapter<MyWebRVAdatper.ViewHolder> {

    public LayoutInflater inflater;
    public Context context;
    public ArrayList<String> uris = new ArrayList<>();
    public Fragment fragment;

    public MyWebRVAdatper(Context context, ArrayList<String> uris, Fragment fragment) {
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.context = context;
        this.uris = uris;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.myweb_rvadapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyWebRVAdatper.ViewHolder holder, int position) {
        holder.my_webName.setText(uris.get(position));
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CheckConnected(uris.get(position))){
                    holder.cant_con.setVisibility(View.INVISIBLE);
                }
            }
        }).start();

        holder.my_webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.my_webDelete.setVisibility(View.VISIBLE);
                holder.my_webDeleteSrc.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(holder.my_webDeleteSrc,"alpha",0f,1f);
                objectAnimator1.setDuration(50);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.my_webDelete,"scaleX",0f,1f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(holder.my_webDelete,"alpha",0f,1f);
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.playTogether(objectAnimator,objectAnimator1,objectAnimator2);
                animatorSet1.setDuration(200);
                animatorSet1.start();
                holder.my_webDelete.setClickable(true);
                holder.my_webDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebDB.getInstance(context).getWebDAO().DeleteWeb(uris.get(position));
                        uris.remove(position);
                        notifyDataSetChanged();
                        holder.my_webDelete.setVisibility(View.INVISIBLE);
                        holder.my_webDeleteSrc.setVisibility(View.INVISIBLE);
                    }
                });
                holder.my_webDelete.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ObjectAnimator objectAnimator3 = ObjectAnimator.
                                ofFloat(holder.my_webDelete,"scaleX",1f,0f).
                                setDuration(200);objectAnimator3.start();
                        objectAnimator3.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                    holder.my_webDelete.setClickable(false);
                                    holder.my_webDelete.setVisibility(View.INVISIBLE);
                                    holder.my_webDeleteSrc.setVisibility(View.INVISIBLE);
                            }
                        });
                        return true;
                    }
                });
                return true;
            }
        });

        holder.openBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(uris.get(position));
                intent.setData(uri);
                context.startActivity(intent);
            }
        });

        holder.NET_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Url", uris.get(position));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context,"已复制到剪切板",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return uris!=null?uris.size():0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView my_webView,my_webDelete,my_webDeleteSrc,cant_con,openBrowser,NET_paste;
        TextView my_webName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            my_webName = itemView.findViewById(R.id.my_webName);
            my_webView = itemView.findViewById(R.id.my_webView);
            my_webDelete = itemView.findViewById(R.id.delete_web);
            my_webDeleteSrc = itemView.findViewById(R.id.deletesrc_web);
            cant_con = itemView.findViewById(R.id.CantConnect);
            openBrowser = itemView.findViewById(R.id.net_openBrowser);
            NET_paste = itemView.findViewById(R.id.NET_paste);
        }
    }

    public boolean CheckConnected(String url){
        try{
            URL path = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)path.openConnection();
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            //HTTP connect
            try {
                connection.connect();
            } catch(Exception ignored) {
                return false;
            }

            int code = connection.getResponseCode();
            return (code >= 100) && (code < 400);
        }catch (Exception ignored){
            return false;
        }
    }

}

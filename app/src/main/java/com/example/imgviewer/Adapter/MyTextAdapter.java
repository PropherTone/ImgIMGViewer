package com.example.imgviewer.Adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDataBase;
import com.example.imgviewer.View.TextViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyTextAdapter extends RecyclerView.Adapter<MyTextAdapter.ViewHolder> {
    LayoutInflater inflater;
    public List<Text> mtext =  new ArrayList<>();
    private final Context context;

    public MyTextAdapter(Context context,List<Text> text) {
        this.context = context;
        this.mtext = text;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mytextrcv_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myText.setText(mtext.get(position).getDisplaytitle());
        holder.date.setText(mtext.get(position).getTime());
        if (!mtext.get(position).getAlarmTime().equals("null")){
            holder.isAlarmIcon.setVisibility(View.VISIBLE);
            holder.alarmDate.setText(mtext.get(position).getAlarmTime());
        }
        if (mtext.get(position).getiMGUri() != null){
            Glide.with(context).load(mtext.get(position).getiMGUri()).into(holder.myIMG);
        }
//        InitColorfulLine(holder);
        holder.myIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TextViewer.class);
                int id = mtext.get(position).getId();
                intent.putExtra("ID",id);
                context.startActivity(intent);
            }
        });
        holder.myIMG.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.deleteSrc.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(holder.deleteSrc,"alpha",0f,1f);
                objectAnimator1.setDuration(50);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.delete,"scaleX",0f,1f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(holder.delete,"alpha",0f,1f);
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.playTogether(objectAnimator,objectAnimator1,objectAnimator2);
                animatorSet1.setDuration(200);
                animatorSet1.start();
                holder.delete.setClickable(true);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextDataBase.getInstance(context).getTextDAO().DeleteText(mtext.get(position));
                        mtext.remove(position);
                        notifyDataSetChanged();
                        holder.delete.setVisibility(View.INVISIBLE);
                        holder.deleteSrc.setVisibility(View.INVISIBLE);
                    }
                });
                holder.delete.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ObjectAnimator objectAnimator3 = ObjectAnimator.
                                ofFloat(holder.delete,"scaleX",1f,0f).
                                setDuration(200);objectAnimator3.start();
                                objectAnimator3.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        holder.delete.setClickable(false);
                                        holder.delete.setVisibility(View.INVISIBLE);
                                        holder.deleteSrc.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                        return true;
                    }
                });
                return true;
            }
        });

    }

//    private void InitColorfulLine(ViewHolder holder) {
//        holder.myIMG.buildDrawingCache();
//        Bitmap bitmap = holder.myIMG.getDrawingCache();
//        if(bitmap != null){
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(@Nullable Palette palette) {
//                    lightMuted = palette.getLightVibrantColor(Color.WHITE);
//                }
//            });
//            holder.colorLine.setBackgroundColor(lightMuted);
//        }
//    }

    @Override
    public int getItemCount() {
        return mtext ==null?0:mtext.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

//        LinearLayout colorLine;
        ImageView myIMG,delete,deleteSrc,isAlarmIcon;
        TextView myText,alarmDate,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myIMG = itemView.findViewById(R.id.myTimg);
            myText = itemView.findViewById(R.id.myTextview);
            delete = itemView.findViewById(R.id.delete_text);
            deleteSrc = itemView.findViewById(R.id.deletesrc);
            isAlarmIcon = itemView.findViewById(R.id.isAlarmIcon);
            alarmDate = itemView.findViewById(R.id.alarmDATE);
            date = itemView.findViewById(R.id.createDate);
//            colorLine = itemView.findViewById(R.id.botton_colorline);
        }
    }
}

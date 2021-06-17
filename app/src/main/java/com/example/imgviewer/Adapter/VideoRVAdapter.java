//package com.example.imgviewer.Adapter;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.drawable.Drawable;
//import android.media.MediaPlayer;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.imgviewer.R;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.IOException;
//import java.util.List;
//
//public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.ViewHolder> implements MediaPlayer.OnPreparedListener {
//
//    public Context mContext;
//    public LayoutInflater inflater;
//    private MediaPlayer mediaPlayer;
//    private List<String> videoPath;
//    private boolean isVisible;
//
//    public VideoRVAdapter(Context context,List<String> videoPath) {
//        this.videoPath = videoPath;
//        this.mContext = context;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.video_layout,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull VideoRVAdapter.ViewHolder holder, int position) {
//
//        holder.start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer = new MediaPlayer();
//                holder.starter.setVisibility(View.INVISIBLE);
//                holder.controller.setVisibility(View.VISIBLE);
//                mediaPlayer.setOnPreparedListener(VideoRVAdapter.this);
////                mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mp.stop();
//                        mp.release();
//                        mediaPlayer = null;
//                        holder.starter.setVisibility(View.VISIBLE);
//                        holder.controller.setVisibility(View.INVISIBLE);
//                    }
//                });
//                try {
//                    mediaPlayer.setDataSource(videoPath.get(position));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.setSurface(new Surface(holder.textureView.getSurfaceTexture()));
//                mediaPlayer.prepareAsync();
//            }
//        });
//
//        holder.control.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                    holder.control.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_pause_24));
//                }else {
//                    mediaPlayer.start();
//                    holder.control.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_arrow_right_24));
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return videoPath==null?0:videoPath.size();
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        mp.start();
//    }
//
//    static class ViewHolder extends MyTextAdapter.ViewHolder {
//        RelativeLayout starter,video,controller;
//        TextureView textureView;
//        ImageView start,control;
//        public ViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//            textureView = itemView.findViewById(R.id.videoPlayer);
//            start = itemView.findViewById(R.id.videoStart);
//            starter = itemView.findViewById(R.id.videoStarter);
//            control = itemView.findViewById(R.id.videoControl);
//            controller = itemView.findViewById(R.id.videoController);
//            video = itemView.findViewById(R.id.video);
//        }
//    }
//
//
//}

package com.example.imgviewer.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.CustomView.MyTextSureView;
import com.example.imgviewer.R;
import com.example.imgviewer.Utils.ImgViewer;
import com.example.imgviewer.Utils.ScreenPXMath;
import com.example.imgviewer.Video.VideoController;
import com.example.imgviewer.Video.VideoPlayer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollingVideoFragment extends Fragment {

    View view;
    public RelativeLayout relativeLayout,controller;
    public Uri videoUri;
    public Activity activity;
    private MyTextSureView textureView;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView start;
    private boolean isPrepared = false;
    private int videoProgress;
    private Timer timer,timer1;
    private boolean isFullScreen;

    private VideoPlayer videoPlayer;

    public ScrollingVideoFragment(Activity activity, Uri videoUri) {
        this.activity = activity;
        this.videoUri = videoUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_video_player, container, false);
        }
        InitView();
        return view;
    }

    private void InitView() {
        videoPlayer = view.findViewById(R.id.videoPlayer);
        videoPlayer.SetVideoPath(videoUri);
        videoPlayer.InitPlayer();


//        controller = view.findViewById(R.id.controller);
//        start = view.findViewById(R.id.startVideo);
//        seekBar = view.findViewById(R.id.seekBar);
//        relativeLayout = view.findViewById(R.id.videoLL);
//        textureView = view.findViewById(R.id.videoView);
//        ImageView fullScreen = view.findViewById(R.id.fullScreen);
//        fullScreen.setOnClickListener(this);
//        start.setOnClickListener(this);
//        controller.setOnClickListener(this);
//        seekBar.setEnabled(true);
//        seekBar.setOnTouchListener(this);
//        seekBar.setOnSeekBarChangeListener(this);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                Gravity.CENTER);
//        textureView.setSurfaceTextureListener(this);
//        textureView.setOnClickListener(this);
//        mediaPlayer = new MediaPlayer();
//        videoController = new VideoController(activity.getApplicationContext());

    }

//    private void openMediaPlayer() {
//
//        try {
//            mediaPlayer.setDataSource(getContext(),videoUri);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (surface == null) {
//            surface = new Surface(surfaceTexture);
//        }
//        mediaPlayer.setSurface(surface);
//        mediaPlayer.prepareAsync();
//        mediaPlayer.setOnVideoSizeChangedListener(this);
//        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnPreparedListener(this);
//
//    }
//
//    private void ListenVideoProgress(){
//            timer = new Timer();
//        //                    Log.e("progress",""+videoProgress);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    videoProgress = mediaPlayer.getCurrentPosition();
////                    Log.e("progress",""+videoProgress);
//                    seekBar.setProgress(videoProgress);
//                }catch (IllegalStateException ignored){
//
//                }
//            }
//        };
//            timer.schedule(timerTask,0,1000);
//    }
//
//    private void HideController(){
//        timer1 = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    controller.setVisibility(View.INVISIBLE);
//                }catch (IllegalStateException ignored){
//                }
//            }
//        };
//        timer1.schedule(timerTask,5000);
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
//
//        if (surfaceTexture == null) {
//            surfaceTexture = surface;
//            openMediaPlayer();
//        } else {
//            textureView.setSurfaceTexture(surfaceTexture);
//        }
//    }
//    @Override public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) { }
//    @Override public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) { return false; }
//    @Override public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) { }
//
//    @Override
//    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//        textureView.adaptVideoSize(width,height);
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        controller.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//        videoController.SetMediaPlayer(mp);
//        videoController.SetDuration(mp.getDuration());
//
//        seekBar.setProgress(0);
//        seekBar.setMax(mp.getDuration());
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.startVideo){
//            mediaPlayer.start();
//            videoController.ShowController();
//                ListenVideoProgress();
//                start.setVisibility(View.INVISIBLE);
//                isPrepared = true;
////                controller.setVisibility(View.VISIBLE);
//                HideController();
//        }
//        if (v.getId() == R.id.controller){
//            if (mediaPlayer.isPlaying()){
//                mediaPlayer.pause();
//            }else{
//                mediaPlayer.start();
//                ListenVideoProgress();
//            }
//        }
//        if (v.getId() == R.id.videoView){
//            controller.setVisibility(View.VISIBLE);
//            HideController();
//        }
//        if (v.getId() == R.id.fullScreen){
//            if (!isFullScreen){
//                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                isFullScreen = true;
//            }else{
//                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                isFullScreen = false;
//            }
//        }
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (fromUser){
//            mediaPlayer.seekTo(progress);
//        }
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        if (timer!=null){
//            timer.cancel();
//        }
//    }
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        videoProgress = seekBar.getProgress();
//        mediaPlayer.seekTo(videoProgress);
//        ListenVideoProgress();
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
//            seekBar.getParent().requestDisallowInterceptTouchEvent(false);
//        } else {
//            seekBar.getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return false;
//    }
//
    @Override
    public void onResume() {
        super.onResume();
//        Log.e("onResume","onResume");
        videoPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.e("onPause","onPause");
        videoPlayer.pause();
    }

    @Override
    public void onStop() {
//        Log.e("onStop","onStop");
        super.onStop();
        videoPlayer.pause();
    }

    @Override
    public void onDestroy() {
//        Log.e("onDestroy","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStart() {
//        Log.e("onStart","onStart");
        super.onStart();
    }
}
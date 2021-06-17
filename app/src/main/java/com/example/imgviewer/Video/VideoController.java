package com.example.imgviewer.Video;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.example.imgviewer.R;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public  class VideoController extends Controller implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, View.OnTouchListener {

    public Context context;
    private SeekBar seekBar;
    private ImageView control,fullScreen;
    private RelativeLayout controller;
    public Timer timer,timer1;
    protected MediaPlayer mediaPlayer;
    private int videoProgress;
    private boolean isFullScreen;

    public VideoController(@NonNull @NotNull Context context) {
        super(context);
        this.context = context;
        InitView();
    }

    private void InitView() {
        LayoutInflater.from(context).inflate(R.layout.videocontroller,this,true);

        controller = findViewById(R.id.video_controller);
        seekBar = findViewById(R.id.video_seekBar);
        control = findViewById(R.id.video_videoControl);
        fullScreen = findViewById(R.id.video_fullScreen);

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setOnTouchListener(this);
        control.setOnClickListener(this);
        fullScreen.setOnClickListener(this);
        controller.setOnClickListener(this);

    }

    @Override
    public void SetDuration(int duration) {
        seekBar.setMax(duration);
    }

    @Override
    public void ListenVideoProgress() {
        timer = new Timer();
        //                    Log.e("progress",""+videoProgress);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    videoProgress = mediaPlayer.getCurrentPosition();
//                    Log.e("progress",""+videoProgress);
                    seekBar.setProgress(videoProgress);
                }catch (IllegalStateException ignored){

                }
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    @Override
    public void HideController() { timer1 = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    controller.setVisibility(View.INVISIBLE);
                }catch (IllegalStateException ignored){
                }
            }
        };
        timer1.schedule(timerTask,5000);
    }

    @Override
    public void SetMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void SetProgress(int progress) {
        seekBar.setProgress(progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoProgress = seekBar.getProgress();
        mediaPlayer.seekTo(videoProgress);
        ListenVideoProgress();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.video_videoControl){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                ProgressTimerCancle();
                HideTimerCancle();
                control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24,null));
            }else{
                mediaPlayer.start();
                ListenVideoProgress();
                HideController();
                control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_right_24,null));
            }
        }
        if (v.getId() == R.id.video_fullScreen){
            if (!isFullScreen){
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        isFullScreen = true;
                    }
                });
            }else{
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        isFullScreen = false;
                    }
                });
            }
        }
    }

    public void ShowController(){
        controller.setVisibility(VISIBLE);
        HideController();
    }

    public void ShowControllerWhileVideoEnd(){
        controller.setVisibility(VISIBLE);
        control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24,null));
        ProgressTimerCancle();
        HideTimerCancle();
    }

    public void HideControllerIMMEDIATELY(){
        controller.setVisibility(INVISIBLE);
        if (timer1 != null){
            timer1.cancel();
        }
    }

    public void ProgressTimerCancle(){
        if (timer!=null){
            timer.cancel();
        }
    }

    public void HideTimerCancle(){
        if (timer1!=null){
            timer1.cancel();
        }
    }

    public void resetState(){
        ListenVideoProgress();
        HideController();
        control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_right_24,null));
    }

    public void setPauseState(){
        ProgressTimerCancle();
        HideTimerCancle();
        control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_24,null));
    }

    public void release(){
        ProgressTimerCancle();
        HideTimerCancle();
        controller = null;
    }


    public void ResetController(){
        ShowController();
        control.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_right_24,null));
        isFullScreen = false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
            seekBar.getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            seekBar.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }


}

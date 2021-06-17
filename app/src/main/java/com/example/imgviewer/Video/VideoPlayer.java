package com.example.imgviewer.Video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imgviewer.CustomView.MyTextSureView;
import com.example.imgviewer.Utils.ScreenPXMath;

import java.io.IOException;

public class VideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private Context context;
    private FrameLayout container;
    private VideoController videoController;
    private MediaPlayer mediaPlayer;
    private MyTextSureView myTextSureView;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private String videoPath;
    private Uri videoUriPath;
    private boolean isUri;
    private boolean contorllerIsShow = true;
    private LayoutParams params;
    private boolean isPrepared;

    public VideoPlayer(@NonNull Context context) {
        super(context);

    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Init();
    }

    private void Init() {
        container = new FrameLayout(context);
        container.setBackgroundColor(Color.BLACK);
//        if (ScreenPXMath.HasBottomButton((Activity) context)){
//            params = new LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ScreenPXMath.getHeight((Activity) context,"height"));
//        }else {
            params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//        }
        this.addView(container, params);
    }

    public void SetVideoPath(Uri path){
        isUri = true;
        this.videoUriPath = path;
    }

    public void SetVideoPath(String path){
        isUri = false;
        this.videoPath = path;
    }

    public void SetController(){
        if(videoController ==null){
            container.removeView(videoController);
            videoController = new VideoController(context);
            videoController.ResetController();
            videoController.SetMediaPlayer(mediaPlayer);
            container.addView(videoController);
        }
    }

    public void InitPlayer(){
        InitVideoPlayer();
        InitTextSureView();
        addTextureView();
//        Log.e("InitPlayer","InitPlayer");
        SetController();
    }

    private void InitVideoPlayer() {
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
    }

    private void InitTextSureView() {
        if (myTextSureView == null){
            myTextSureView = new MyTextSureView(context);
            myTextSureView.setSurfaceTextureListener(this);
            myTextSureView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contorllerIsShow){
                        videoController.HideControllerIMMEDIATELY();
                        contorllerIsShow = false;
                    }else{
                        videoController.ShowController();
                        contorllerIsShow = true;
                    }
                }
            });
        }
    }

    private void addTextureView() {
        container.removeView(myTextSureView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        container.addView(myTextSureView, 0, params);
    }


    private void openMediaPlayer() {

        try {
            if (isUri){
                mediaPlayer.setDataSource(context,videoUriPath);
                isUri = false;
            }else {
                mediaPlayer.setDataSource(context, Uri.parse(videoPath));
                isUri = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (surface == null) {
            surface = new Surface(surfaceTexture);
        }
        mediaPlayer.setSurface(surface);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);

    }

    public boolean IsPrepared(){
        return isPrepared;
    }

    public boolean IsPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void start(){
        try {
            if (!mediaPlayer.isPlaying()&&isPrepared){
                mediaPlayer.start();
                videoController.resetState();
            }
        }catch (IllegalStateException ignored){

        }

    }

    public void pause(){
        try {
            if (mediaPlayer.isPlaying()&&isPrepared){
                mediaPlayer.pause();
                videoController.setPauseState();
            }
        }catch (IllegalStateException ignored){

        }

    }

    public void release(){
        if (mediaPlayer!=null){
            videoController.release();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {

        if (surfaceTexture == null) {
            surfaceTexture = surface;
            openMediaPlayer();
        } else {
            myTextSureView.setSurfaceTexture(surfaceTexture);
        }
    }
    @Override public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) { }
    @Override public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) { return myTextSureView==null; }
    @Override public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) { }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        myTextSureView.adaptVideoSize(width,height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Log.e("onCompletion","onCompletion");
        videoController.ShowControllerWhileVideoEnd();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
//        Log.e("onPrepared","onPrepared");
        videoController.SetProgress(0);
        videoController.SetDuration(mp.getDuration());
    }
}

package com.example.imgviewer.Video;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public abstract class Controller extends FrameLayout {

    public Controller(@NonNull Context context) {
        super(context);
    }

    public abstract void SetDuration(int duration);

    public abstract void ListenVideoProgress();

    public abstract void HideController();

    public abstract void SetMediaPlayer(MediaPlayer mediaPlayer);

    public abstract void SetProgress(int progress);
}

package com.example.imgviewer.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.imgviewer.R;
import com.example.imgviewer.View.MainActivity;

public class CallingService extends Service {

    private Vibrator vibrator;

    public CallingService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String NotificationTitle = intent.getStringExtra("NotificationTitle");
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
                    intent.setClass(CallingService.this,AlarmView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    PendingIntent pendingIntent = PendingIntent.getActivity(CallingService.this,223,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationChannel notificationChannel = new NotificationChannel("1","notifiction",NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                    Notification notification = new NotificationCompat.Builder(CallingService.this,"1")
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                            .setContentTitle("提醒")
                            .setContentIntent(pendingIntent)
                            .setContentText(NotificationTitle).build();

                        notificationManager.notify(1,notification);

                    vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    VibrationEffect vibrationEffect = VibrationEffect.createWaveform(new long[]{0, 100, 1000, 300, 200, 100, 500, 200, 100},1);
                    vibrator.vibrate(vibrationEffect);
                    startActivity(intent);
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }
}

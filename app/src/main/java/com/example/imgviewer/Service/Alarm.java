package com.example.imgviewer.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.imgviewer.View.IMGLibChoose;

import java.util.Calendar;

public class Alarm {

    private final Context context;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static Calendar calendar;
    @SuppressLint("StaticFieldLeak")
    public static Alarm instance = null;

    private Alarm(Context context) {
        this.context = context;
    }

    public static Alarm getInstance(Context mContext){
        if (instance == null){
            synchronized (Alarm.class){
                if (instance == null){
                    instance = new Alarm(mContext);
                }
            }
        }
        return instance;
    }

    public void CreateAlarm(String s,String path){
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CallingService.class);
        intent.putExtra("AlarmTitles",s);
        intent.putExtra("AlarmViewPath",path);
        pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void SetUpAlarm(int year,int month,int day,int hours,int min,int sec){

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hours);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,sec);
        calendar.set(Calendar.DATE,day);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);
        Log.e("setted",year+"|"+month+"|"+day+"|"+hours+"|"+min+"|"+sec+".."+calendar.getTimeInMillis());
    }

}

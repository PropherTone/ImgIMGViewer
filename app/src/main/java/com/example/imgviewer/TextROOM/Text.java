package com.example.imgviewer.TextROOM;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Entity
public class Text {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int id;

    @ColumnInfo(name = "TITLE")
    public String title;

    @ColumnInfo(name = "TEXT")
    public String text;

    @ColumnInfo(name = "IMGUri")
    public String iMGUri;
    
    @ColumnInfo(name = "DISTITLE")
    public String displaytitle;

    @ColumnInfo(name = "HasAlarm")
    public Boolean hasAlarm;

    @ColumnInfo(name = "Time")
    public String time;

    @ColumnInfo(name = "AlarmTime")
    public String alarmTime;

    @ColumnInfo(name = "SpanBytes")
    public byte[] spanBytes;

    public Text(String title, String displaytitle, String text, String iMGUri, Boolean hasAlarm, String time, String alarmTime, byte[] spanBytes) {
        this.title = title;
        this.text = text;
        this.iMGUri = iMGUri;
        this.displaytitle = displaytitle;
        this.hasAlarm = hasAlarm;
        this.time = time;
        this.alarmTime = alarmTime;
        this.spanBytes = spanBytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getiMGUri() {
        return iMGUri;
    }

    public void setiMGUri(String iMGUri) {
        this.iMGUri = iMGUri;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public Boolean getHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(Boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public byte[] getSpanBytes() {
        return spanBytes;
    }

    public void setSpanBytes(byte[] spanBytes) {
        this.spanBytes = spanBytes;
    }
}

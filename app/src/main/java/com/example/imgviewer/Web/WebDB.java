package com.example.imgviewer.Web;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDAO;
import com.example.imgviewer.TextROOM.TextDataBase;

@Database(entities = {Web.class},version = 1,exportSchema = false)
public abstract class WebDB extends RoomDatabase {

    private static WebDB webDB;

    public static synchronized WebDB getInstance(Context context){
        if (webDB == null){
            webDB = Room
                    .databaseBuilder(context.getApplicationContext(),WebDB.class,"WebDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return webDB;
    }

    public abstract WebDAO getWebDAO();

}

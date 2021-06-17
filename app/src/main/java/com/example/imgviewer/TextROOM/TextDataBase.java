package com.example.imgviewer.TextROOM;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Text.class},version = 1,exportSchema = false)
public abstract class TextDataBase extends RoomDatabase {

    private static TextDataBase textDataBase;

    public static synchronized TextDataBase getInstance(Context context){
        if (textDataBase == null){
            textDataBase = Room
                    .databaseBuilder(context.getApplicationContext(),TextDataBase.class,"TextDataBase")
                    .allowMainThreadQueries()
                    .build();
        }
        return textDataBase;
    }

    public abstract TextDAO getTextDAO();
}

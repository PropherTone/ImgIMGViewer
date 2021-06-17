package com.example.imgviewer.TextROOM;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TextDAO {

    @Insert
    void InsertText(Text texts);

    @Query("delete from Text where ID like :id")
    int DeleteText(int id);

    @Query("select TITLE from Text where TITLE like :title")
    String QueryTitle(String title);

    @Query("select TITLE from Text")
    List<String> QueryALLTitle();

    @Query("select ID from Text where DISTITLE like :distitle")
    int QueryID(String distitle);

    @Query("select * from Text")
    LiveData<List<Text>> QueryAll();

    @Query("select * from Text where ID like :id")
    Text QueryText(int id);

    @Query("update Text set DISTITLE = :disTitle where ID like :id")
    void UpdateDisTitle(int id,String disTitle);

    @Query("update Text set IMGUri = :iMGUri where ID like :id")
    void UpdateIMGUri(int id,String iMGUri);

    @Query("update Text set TEXT = :text where ID like :id")
    void UpdateText(int id,String text);

    @Query("update Text set SpanBytes = :SpanBytes where ID like :id")
    void UpdateSpanBytes(int id,byte[] SpanBytes);

    @Query("update Text set TITLE = :title where ID like :id")
    void UpdateTitle(int id,String title);

    @Update
    void UpdateText(Text text);

    @Delete
    void DeleteText(Text text);
}
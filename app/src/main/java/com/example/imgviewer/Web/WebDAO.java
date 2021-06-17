package com.example.imgviewer.Web;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WebDAO {

    @Insert
    void InsertMyWebData(Web web);

    @Query("select WebUri from Web")
    LiveData<String> QueryData();

    @Query("select * from Web")
    LiveData<List<Web>> QueryAll();

    @Query("select WebUri from Web")
    LiveData<List<String>> QueryAllUrl();

    @Query("select WebUri from Web")
    List<String> QueryAllUrlNotLive();

    @Query(("select WebUri from Web where type like:type"))
    LiveData<List<String>> QueryWebList(String type);

    @Query(("select WebUri from Web where type like:type"))
    List<String> QueryWebListNotLive(String type);


    @Query("delete from Web where WebUri like:uri")
    void DeleteWeb(String uri);
}

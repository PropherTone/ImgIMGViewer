package com.example.imgviewer.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class IMGdealler {

    public static LiveData<ArrayList<String>> LoadImg(Context context){
        MutableLiveData<ArrayList<String>> liveImages = new MutableLiveData<>();
        ArrayList<String> images = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media.SIZE},
                        MediaStore.MediaColumns.SIZE + ">0",
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC");

                //读取扫描到的图片
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(
                                mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        images.add(path);
                    }
                    mCursor.close();
                    liveImages.postValue(images);
                }
            }
        }).start();
        return liveImages;
    }


    public static LiveData<HashMap<String,ArrayList<String>>> LoadImgMap(Context context){
        MutableLiveData<HashMap<String,ArrayList<String>>> liveImages = new MutableLiveData<>();
        ArrayList<String> images = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media.SIZE},
                        MediaStore.MediaColumns.SIZE + ">0",
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC");

                //读取扫描到的图片
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(
                                mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        images.add(path);
                    }
                    mCursor.close();
                    liveImages.postValue(SortIMGFolderMap(images));
                }
            }
        }).start();
        return liveImages;
    }

    public static HashMap<String,ArrayList<String>> SortIMGFolderMap(ArrayList<String> imgLib){
        HashMap<String,ArrayList<String>> IMGLibMap = new HashMap<String,ArrayList<String>>();
        ArrayList<String> filtratedIMGLib = FileViewer.FilleterImgLib(imgLib,"path");
        IMGLibMap.put("全部相片",imgLib);
        for (int i = 0;i<filtratedIMGLib.size();i++){
            ArrayList<String> arrayList = new ArrayList<>();
            for (int j = 0;j<imgLib.size();j++){
                if (FileViewer.getFolderName(filtratedIMGLib.get(i),"").equals(FileViewer.getFolderName(imgLib.get(j),""))){
                    arrayList.add(imgLib.get(j));
                }
            }
            IMGLibMap.put(filtratedIMGLib.get(i),arrayList);
        }
        return IMGLibMap;
    }

    public static ArrayList<String> LoadVideo(Context context){
        ArrayList<String> Videos = new ArrayList<>();

        Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        Cursor mCursor = mContentResolver.query(mVideoUri, new String[]{
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DATE_ADDED,
                        MediaStore.Video.Media.SIZE},
                MediaStore.MediaColumns.SIZE + ">0",
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC");

        //读取扫描到的图片
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(
                        mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Videos.add(path);
            }
            mCursor.close();
        }
        return Videos;
    }

}

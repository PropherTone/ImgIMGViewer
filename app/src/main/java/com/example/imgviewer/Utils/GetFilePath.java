package com.example.imgviewer.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.STORAGE_SERVICE;

public class GetFilePath {

    /*
    * 读取储存卡和SD卡路径
    *
    * */
    public static String[] getAllSdPaths(Context context) {
        Method mMethodGetPaths = null;
        String[] paths = null;
        //通过调用类的实例mStorageManager的getClass()获取StorageManager类对应的Class对象
        //getMethod("getVolumePaths")返回StorageManager类对应的Class对象的getVolumePaths方法，这里不带参数
        StorageManager mStorageManager = (StorageManager) context
                .getSystemService(STORAGE_SERVICE);//storage
            try {
                mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
                paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return paths;
    }


    /**
     * 检查系统权限.
     *
     * @param activity    the activity
     * @param requestcode the requestcode
     * @return the boolean
     */
    public static boolean CheckStoragePermission(Activity activity,int requestcode){
        int storagePermissionRead = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return storagePermission == PackageManager.PERMISSION_GRANTED &&
                storagePermissionRead == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 打开APP设置.
     *
     * @param context the context
     */
    public static void OpenAppSetting(Context context){
        Uri packageURI = Uri.parse("package:" + "com.example.imgviewer");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    /**
     * 搜索文件.
     *
     * @param context     the context
     * @param userSetting the user setting
     * @return the list
     */
    public static List<String> SearchData(Context context,String userSetting) {
        if (userSetting != null){
            List<String>  ImgUri = new ArrayList<>();
            File folder = new File(userSetting);
            if(folder.list() != null){
                File[] file = folder.listFiles();
                Arrays.sort(file, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        long date = o1.lastModified()-o2.lastModified();
                        if (date>1){
                            return -1;
                        }else if(date == 0){
                            return  0;
                        }else{
                            return 1;
                        }
                    }
                });
                String[] allImg = folder.list();
                for (int i = 0; i < file.length; i++) {
                    allImg[i] = file[i].getName();
                    String ImgPath = folder + "/" + allImg[i];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(ImgPath, options);
            if (options.outWidth != -1) {
                    ImgUri.add(ImgPath);
            }
                }
                return ImgUri;
            }else {
                Toast.makeText(context,"没有这个文件夹哦",Toast.LENGTH_SHORT).show();
                return null;
            }
        }else {
            return null;
        }
    }

    /**
     * Into folder array list.
     *
     * @param path the path
     * @return the array list
     */
    public static ArrayList<String> IntoFolder(String path){
//        Log.e("path",path);
        String[] isDireTory;
        ArrayList<String> readpath = new ArrayList<>();
        File file = new File(path);
        File filefore;
        if (file.list() != null){
            isDireTory = file.list();
//            Log.e("path",isDireTory[0]);
            if (isDireTory.length>0){
                filefore = new File(path+"/"+isDireTory[0]);
            }else{
                filefore = new File(path);
            }
        }else{
            filefore = new File(path);
        }
        String[] getPath = new String[0];
        if (file.list() != null && filefore.isDirectory()){
            getPath = file.list();
            for (int i = 0;i<getPath.length;i++) {
                readpath.add(path+"/"+getPath[i]);
//                Log.e("readpath",""+readpath.get(i));
            }
        }else {
            readpath = null;
            return readpath;
        }
        return readpath;
    }
}

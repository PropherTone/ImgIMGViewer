package com.example.imgviewer.Utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FileViewer {

    /*
    * 过滤相册名称
    *
    * */
    public static ArrayList<String> fileterImgLib(ArrayList<String> imgpaths, String choose) {
        ArrayList<String> imgFolders = new ArrayList<>();
        if (imgpaths != null){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int isSameNmae = 0;
                for (int i = 0;i<imgpaths.size();i++){
                    String name;
                    if (!choose.equals("path")){
                        name = getFolderName(imgpaths.get(i),choose);
                        for (int j = 0;j < imgFolders.size();j++)
                        {
                            if(imgFolders.get(j).equals(name)){
                                isSameNmae = 1;
                            }
                        }if (isSameNmae == 0){
                            imgFolders.add(name);
                        }isSameNmae = 0;
                    }else {
                        name = imgpaths.get(i);
                        for (int j = 0;j < imgFolders.size();j++)
                        {
                            if(getFolderName(imgFolders.get(j),"name").equals(getFolderName(name,"name"))){
                                isSameNmae = 1;
                            }
                        }if (isSameNmae == 0){
                            imgFolders.add(name);
                        }isSameNmae = 0;
                    }

                }
            }
        }).start();
        }
        return imgFolders;
    }

    /*
     * 提取相册名称
     * */
    public static String getFolderName(String path,String choose) {
        String[] strings = path.split(File.separator);
        String imgLibpath;
        if (strings.length >= 2) {
            if(choose.equals("name")){
                imgLibpath = strings[strings.length - 2];}
            else if(choose.equals("finnalfoldername")){
                imgLibpath = strings[strings.length - 1];
            }else{
                imgLibpath = path.substring(0,path.indexOf(strings[strings.length-1]));
            }return  imgLibpath;
        }
        return "";
    }

    public static ArrayList<String> FilleterImgLib(ArrayList<String> imgpaths, String choose) {
        ArrayList<String> imgFolders = new ArrayList<>();
        if (imgpaths != null){
            int isSameNmae = 0;
            for (int i = 0;i<imgpaths.size();i++){
                String name;
                if (!choose.equals("path")){
                    name = getFolderName(imgpaths.get(i),choose);
                    for (int j = 0;j < imgFolders.size();j++)
                    {
                        if(imgFolders.get(j).equals(name)){
                            isSameNmae = 1;
                        }
                    }if (isSameNmae == 0){
                        imgFolders.add(name);
                    }isSameNmae = 0;
                }else {
                    name = imgpaths.get(i);
                    for (int j = 0;j < imgFolders.size();j++)
                    {
                        if(getFolderName(imgFolders.get(j),"name").equals(getFolderName(name,"name"))){
                            isSameNmae = 1;
                        }
                    }if (isSameNmae == 0){
                        imgFolders.add(name);
                    }isSameNmae = 0;
                }

            }
        }
        return imgFolders;
    }

}

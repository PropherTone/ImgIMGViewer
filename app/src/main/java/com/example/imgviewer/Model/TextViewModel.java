package com.example.imgviewer.Model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDAO;
import com.example.imgviewer.TextROOM.TextDataBase;

import java.util.List;

public class TextViewModel extends ViewModel {

    LiveData<List<Text>> texts;

    public LiveData<List<Text>> getUser(Context context) {
        TextDataBase textDataBase = TextDataBase.getInstance(context.getApplicationContext());
        TextDAO textDAO = textDataBase.getTextDAO();
        texts = textDAO.QueryAll();
        return texts;
    }

}

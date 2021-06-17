package com.example.imgviewer.test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imgviewer.Adapter.MyTextAdapter;
import com.example.imgviewer.Model.TextViewModel;
import com.example.imgviewer.R;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.View.MyTextBook;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyText extends Fragment {

    private Context context;
    public View view;
    private List<Text> mText;

    public MyText(Context context, List<Text> mText) {
        this.context = context;
        this.mText = mText;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.mytextbook_mytext,container,false);
        }

        InitRecyclerView();

        return view;
    }

    private void InitRecyclerView() {
        RecyclerView textRCV = view.findViewById(R.id.myText_RV);
        textRCV.setLayoutManager(new LinearLayoutManager(context));
//        mtext = textDataBase.getTextDAO().QueryAll();
        TextViewModel textViewModel = new ViewModelProvider(this).get(TextViewModel.class);
        LiveData<List<Text>> user = textViewModel.getUser(context);
        user.observe(getViewLifecycleOwner(), new Observer<List<Text>>() {
            @Override
            public void onChanged(List<Text> texts) {
                mText = texts;
                MyTextAdapter myTextAdapter = new MyTextAdapter(context, mText);
                textRCV.setAdapter(myTextAdapter);
                textRCV.setItemAnimator(null);
//                if (isOpen){
//                    ClosePopWindows();
//                    isOpen = false;
//                }
//                if (fragmentManager==null){
//                    InitFragment();
//                }
            }
        });

    }


}

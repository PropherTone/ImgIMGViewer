package com.example.imgviewer.Service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;
import com.example.imgviewer.View.MyTextBook;

public class AlarmView extends AppCompatActivity {

    private String titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmview);

        InitView();
    }


    private void InitView() {
        //    private FloatingActionButton floatingActionButton;
        //    private View view;
        ImageView background = findViewById(R.id.alarmBackGround);
        ImageView close = findViewById(R.id.close);
        TextView title = findViewById(R.id.alarmTitle);

        Intent intent = getIntent();
        titles = intent.getStringExtra("AlarmTitles");
        String path = intent.getStringExtra("AlarmViewPath");
        title.setText(titles);
        if (path != null){
            Glide.with(this).load(path).into(background);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
                finish();
            }
        });

    }

    private void StartService() {
        Intent intent = new Intent(this,CallingService.class);
        intent.putExtra("NotificationTitle",titles);
        startService(intent);
    }

    private void stopService() {
        stopService(new Intent(this,CallingService.class));
    }

}

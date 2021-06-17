package com.example.imgviewer.View;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.imgviewer.Model.TextViewModel;
import com.example.imgviewer.R;
import com.example.imgviewer.Service.Alarm;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDataBase;
import com.example.imgviewer.Text.TextSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;


public class EditTextActivity extends AppCompatActivity implements View.OnClickListener {

    public String IMGPath;
    private ImageView addimg;
    private boolean isOpen;
    private int disPlayWight;
    private EditText title,text;
    public Boolean isAlarm = false;
    private int hours;
    private int mins;
    private int days;
    private int month;
    private int year;

    public ArrayList<String> textColorSet = new ArrayList<>();
    public ArrayList<Integer> textColorSetLocation = new ArrayList<>();

    public TextSetting textSetting= new TextSetting();
    private byte[] spanBytes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_popwindows);

        if (SDK_INT>= Build.VERSION_CODES.R){
            DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
            disPlayWight = displayMetrics1.widthPixels/2 +30;
        }else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            disPlayWight = displayMetrics.widthPixels/2 +30;
        }

        InitPopwindows();
        InitAlarm();
        InitTextSetting();
    }

    private void InitTextSetting() {
        EditText textSize = findViewById(R.id.textSize);
        Button blue,red,green,yellow,setTextSize;

        blue = findViewById(R.id.textBlue);
        red = findViewById(R.id.textRed);
        green = findViewById(R.id.textGreen);
        yellow = findViewById(R.id.textYellow);
        setTextSize = findViewById(R.id.setTextSize);

        blue.setOnClickListener(this);
        red.setOnClickListener(this);
        green.setOnClickListener(this);
        yellow.setOnClickListener(this);

        textSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTextSize.setText(textSize.getText().toString());

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textSize.getText().toString().equals("")){
                    textSetting.SetBounds(Integer.parseInt(textSize.getText().toString())*10,text,text.getSelectionStart(),text.getSelectionEnd());
                }else {
                    Toast.makeText(EditTextActivity.this,"输入大小",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void InitAlarm() {
        DatePicker datePicker = findViewById(R.id.datePicker);
        LinearLayout setAlarm = findViewById(R.id.alarm);
        LinearLayout dateTimePicker = findViewById(R.id.setAlarm);
        ImageView timerView = findViewById(R.id.confirmView);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        TextView confirmAlarm = findViewById(R.id.confirmAlarm);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",0,disPlayWight+100);
        objectAnimator.setDuration(0);
        objectAnimator1.setDuration(0);
        objectAnimator1.start();
        objectAnimator.start();

        timerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen){
                    isOpen = false;
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",0,disPlayWight);
                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",0,disPlayWight+100);
                    objectAnimator1.start();
                    objectAnimator.start();
                }else {
                    isOpen = true;
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",disPlayWight,0);
                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",disPlayWight+100,0);
                    objectAnimator1.start();
                    objectAnimator.start();
                }
            }
        });

        confirmAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAlarm = true;
                hours = timePicker.getHour();
                mins = timePicker.getMinute();
                days = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                if (InsertText()){
                    Alarm alarm = Alarm.getInstance(EditTextActivity.this);
                    String s = title.getText().toString();
                    alarm.CreateAlarm(s,IMGPath);
                   Log.e("title", s);
                   Intent intent = new Intent();
                   intent.putExtra("AlarmTitle",s);
                   alarm.SetUpAlarm(year,month,days,hours,mins,0);
                   finish();
                }else {
                    Toast.makeText(EditTextActivity.this,"输入标题",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void InitPopwindows() {

        ImageView back;

        back = findViewById(R.id.popback);
        ImageView confirm = findViewById(R.id.popconfirm);
        title = findViewById(R.id.titleEdit);
        text = findViewById(R.id.textEdit);
        addimg = findViewById(R.id.addtitleimg);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InsertText()) {
                    finish();
                }else {
                    Toast.makeText(EditTextActivity.this,"输入标题",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTextActivity.this,IMGLibChoose.class);
                startActivityForResult(intent,6);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6){
            if (data!= null){
                IMGPath = data.getStringExtra("chooseIMG");
                if(IMGPath != null){
                    Glide.with(EditTextActivity.this).load(IMGPath).into(addimg);
                    Log.e("uri","1"+IMGPath);
                }
            }
        }
    }

    private Boolean InsertText() {
        int sameCount = 0;
        Log.e("title", "1" + title.getText().toString());
        if (!title.getText().toString().equals("")) {
            String titles = title.getText().toString();
            String texts = text.getText().toString();
            List<String> alltitles = TextDataBase.getInstance(this).getTextDAO().QueryALLTitle();
            if (alltitles.contains(titles)) {
                for (String i : alltitles) {
                    if (i.equals(titles)) {
                        sameCount++;
                    }
                }
                titles = titles + "(" + sameCount + ")";
            }
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String nowTime = simpleDateFormat.format(date);
            String alarmTime;
            if (isAlarm){
                alarmTime = year+"年"+month+"月"+days+"日"+hours+":"+mins;
            }else {
                alarmTime = "null";
            }
            spanBytes = textSetting.GetSpan(text);
            TextDataBase.getInstance(this).getTextDAO().InsertText(new Text(title.getText().toString(), titles, texts, IMGPath,isAlarm,nowTime,alarmTime,spanBytes));
            return  true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.textBlue){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("BLUE");
            textSetting.SetColor("BLUE",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        } else if (id == R.id.textGreen){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("GREEN");
            textSetting.SetColor("GREEN",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }else if (id == R.id.textYellow){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("YELLOW");
            textSetting.SetColor("YELLOW",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }else if (id == R.id.textRed){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("RED");
            textSetting.SetColor("RED",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }

    }
}

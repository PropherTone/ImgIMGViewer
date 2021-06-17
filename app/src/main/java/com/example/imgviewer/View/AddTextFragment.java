package com.example.imgviewer.View;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;
import com.example.imgviewer.Service.Alarm;
import com.example.imgviewer.Text.TextSetting;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDataBase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class AddTextFragment extends Fragment implements View.OnClickListener {

    public Context context;
    public Activity activity;
    private View view;

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


    public AddTextFragment() {

    }

    public static AddTextFragment newInstance(Context context, Activity activity) {
        return new AddTextFragment(context,activity);
    }


    public AddTextFragment(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_text, container, false);
        }

        if (SDK_INT>= Build.VERSION_CODES.R){
            DisplayMetrics displayMetrics1 = getResources().getDisplayMetrics();
            disPlayWight = displayMetrics1.widthPixels/2 +30;
        }else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            disPlayWight = displayMetrics.widthPixels/2 +30;
        }

        InitPopwindows();
        InitAlarm();
        InitTextSetting();

        return view;
    }

    private void InitTextSetting() {
        EditText textSize = view.findViewById(R.id.frag_textSize);
        Button blue,red,green,yellow,setTextSize;

        blue = view.findViewById(R.id.frag_textBlue);
        red = view.findViewById(R.id.frag_textRed);
        green = view.findViewById(R.id.frag_textGreen);
        yellow = view.findViewById(R.id.frag_textYellow);
        setTextSize = view.findViewById(R.id.frag_setTextSize);

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
                    Toast.makeText(context,"输入大小",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void InitAlarm() {
        DatePicker datePicker = view.findViewById(R.id.frag_datePicker);
        LinearLayout setAlarm = view.findViewById(R.id.frag_alarm);
        LinearLayout dateTimePicker = view.findViewById(R.id.frag_setAlarm);
        ImageView timerView = view.findViewById(R.id.frag_confirmView);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.frag_timePicker);
        timePicker.setIs24HourView(true);
        TextView confirmAlarm = view.findViewById(R.id.frag_confirmAlarm);

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
                    Alarm alarm = Alarm.getInstance(context);
                    String s = title.getText().toString();
                    alarm.CreateAlarm(s,IMGPath);
                    Log.e("title", s);
                    Intent intent = new Intent();
                    intent.putExtra("AlarmTitle",s);
                    alarm.SetUpAlarm(year,month,days,hours,mins,0);
//                    finish();
                }else {
                    Toast.makeText(context,"输入标题",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void InitPopwindows() {


        title = view.findViewById(R.id.frag_titleEdit);
        text = view.findViewById(R.id.frag_textEdit);
        addimg = view.findViewById(R.id.frag_addtitleimg);
        TextView confirm = view.findViewById(R.id.frag_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InsertText()) {
                    Toast.makeText(context,"^.^",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"输入标题",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,IMGLibChoose.class);
                startActivityForResult(intent,6);
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6){
            if (data!= null){
                IMGPath = data.getStringExtra("chooseIMG");
                if(IMGPath != null){
                    Glide.with(AddTextFragment.this).load(IMGPath).into(addimg);
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
            List<String> alltitles = TextDataBase.getInstance(context).getTextDAO().QueryALLTitle();
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
            TextDataBase.getInstance(context).getTextDAO().InsertText(new Text(title.getText().toString(), titles, texts, IMGPath,isAlarm,nowTime,alarmTime,spanBytes));
            return  true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.frag_textBlue){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("BLUE");
            textSetting.SetColor("BLUE",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        } else if (id == R.id.frag_textGreen){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("GREEN");
            textSetting.SetColor("GREEN",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }else if (id == R.id.frag_textYellow){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("YELLOW");
            textSetting.SetColor("YELLOW",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }else if (id == R.id.frag_textRed){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("RED");
            textSetting.SetColor("RED",text,text.getSelectionStart(),text.getSelectionEnd());
//            textSetting.ResetText(text,textColorSet,textSetBound,textColorSetLocation);
        }

    }

    public interface RecallPopWindow{
        void Recall();
    }

    public void RecallPopWindowsListener(RecallPopWindow recallPopWindow){
    }

}
package com.example.imgviewer.View;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.imgviewer.R;
import com.example.imgviewer.Service.Alarm;
import com.example.imgviewer.TextROOM.Text;
import com.example.imgviewer.TextROOM.TextDataBase;
import com.example.imgviewer.Text.TextSetting;
import com.example.imgviewer.test.InternetSurfing;

import java.util.ArrayList;
import java.util.List;

public class TextViewer extends AppCompatActivity implements View.OnClickListener {

    public String IMGPath = null;
    private EditText title,text;
    private ImageView img;
    private String disTitle;
    private Text texts;
    private int disPlayWight;
    private boolean isOpen;
    private LinearLayout setAlarm,dateTimePicker;
    private final TextDataBase textDataBase = TextDataBase.getInstance(TextViewer.this);

    public ArrayList<String> textColorSet = new ArrayList<>();
    public ArrayList<Integer> textColorSetLocation = new ArrayList<>();
    public TextSetting textSetting= new TextSetting();
    private byte[] spanBytes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textviewer);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        disPlayWight = displayMetrics.widthPixels/2 +30;

        InitView();
        InitMyText();
        InitAlarm();
        InitTextSetting();
    }

    private void InitMyText() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID",-1);
        texts = textDataBase.getTextDAO().QueryText(id);
        disTitle = texts.getDisplaytitle();
        String maintext = texts.getText();
        String iMGUri = texts.getiMGUri();
        IMGPath = texts.getiMGUri();

        title.setText(disTitle);
        text.setText(maintext);
        TextSetting textSetting = new TextSetting();

        textSetting.ReSetSpan(text,texts.getSpanBytes());
        if (iMGUri != null){
            Glide.with(this).asBitmap().load(iMGUri).into(img);
        }
    }


    private void InitView() {
        ImageView back,confirm;

        confirm = findViewById(R.id.viewer_popconfirm);
        back = findViewById(R.id.viewer_popback);
        title = findViewById(R.id.viewer_titleEdit);
        text = findViewById(R.id.viewer_textEdit);
        img =  findViewById(R.id.viewer_imgviewer);
        setAlarm = findViewById(R.id.text_alarm);
        dateTimePicker = findViewById(R.id.text_setAlarm);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (img.getDrawable()==null){
                        Glide.with(TextViewer.this).load(getDrawable(R.drawable.ic_baseline_add_24)).into(img);
                    }
                    img.setOnClickListener(TextViewer.this::clk);
                    confirm.setVisibility(View.VISIBLE);
                }
            }
        });

        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (img.getDrawable()==null){
                        Glide.with(TextViewer.this).load(getDrawable(R.drawable.ic_baseline_add_24)).into(img);
                    }
                    img.setOnClickListener(TextViewer.this::clk);
                    confirm.setVisibility(View.VISIBLE);
                    RichTextMenu();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sameCount = 0;
                String newTitle = title.getText().toString();
                String newText  = text.getText().toString();
                String newIMGUri = IMGPath;
                if (!newTitle.equals(disTitle)){
                    List<String> alltitles = textDataBase.getTextDAO().QueryALLTitle();
                    if(alltitles.contains(newTitle)){
                        for (String i:alltitles){
                            if(i.equals(newTitle)){
                                sameCount++;
                            }
                        }
                        newTitle = newTitle+"("+sameCount+")";
                    }
                    textDataBase.getTextDAO().UpdateDisTitle(texts.getId(),newTitle);
                }
                textDataBase.getTextDAO().UpdateTitle(texts.getId(),newTitle);
                textDataBase.getTextDAO().UpdateText(texts.getId(),newText);
                if (null != newIMGUri){
                    textDataBase.getTextDAO().UpdateIMGUri(texts.getId(),newIMGUri);
                }
                confirm.setVisibility(View.INVISIBLE);
                title.clearFocus();
                text.clearFocus();
                img.setClickable(false);
                InputMethodManager inputMethodManager= (InputMethodManager) getApplicationContext().getSystemService(InputMethodManager.class);
                inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(),0);
                InitMyText();
                InitAlarm();
                if (IMGPath == null){
                    Glide.with(TextViewer.this).asBitmap().load(IMGPath).into(img);
                }
                Toast.makeText(TextViewer.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void clk(View view){
        Intent intent = new Intent(TextViewer.this,IMGLibChoose.class);
        startActivityForResult(intent,0x20);
    }

    private void InitAlarm() {
        setAlarm.setVisibility(View.INVISIBLE);
        dateTimePicker.setVisibility(View.INVISIBLE);
        isOpen = false;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",0,disPlayWight);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",0,disPlayWight+100);
        objectAnimator.setDuration(0);
        objectAnimator1.setDuration(0);
        objectAnimator1.start();
        objectAnimator.start();
    }

    private void RichTextMenu(){
        if (!isOpen){
            isOpen = true;
            setAlarm.setVisibility(View.VISIBLE);
            dateTimePicker.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",disPlayWight,0);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",disPlayWight+100,0);
            objectAnimator1.start();
            objectAnimator.start();
        }else {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(setAlarm,"translationX",0,disPlayWight);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(dateTimePicker,"translationX",0,disPlayWight+100);
            objectAnimator1.start();
            objectAnimator.start();
            isOpen = false;
        }
    }

    private void InitTextSetting() {
        EditText textSize = findViewById(R.id.text_textSize);
        Button blue,red,green,yellow,setTextSize;
        TextView richTextConfirm = findViewById(R.id.text_confirmAlarm);
        ImageView textConfirm = findViewById(R.id.text_confirmView);

        blue = findViewById(R.id.text_textBlue);
        red = findViewById(R.id.text_textRed);
        green = findViewById(R.id.text_textGreen);
        yellow = findViewById(R.id.text_textYellow);
        setTextSize = findViewById(R.id.text_setTextSize);

        blue.setOnClickListener(this);
        red.setOnClickListener(this);
        green.setOnClickListener(this);
        yellow.setOnClickListener(this);

        textConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichTextMenu();
            }
        });

        richTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextSetting textSetting = new TextSetting();
                spanBytes = textSetting.GetSpan(text);
                textDataBase.getTextDAO().UpdateSpanBytes(texts.getId(),spanBytes);
                Toast.makeText(TextViewer.this,"设置成功",Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toast.makeText(TextViewer.this,"输入大小",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x20 && resultCode == 0x20){
            if (data != null){
                IMGPath = data.getStringExtra("chooseIMG");
                if (IMGPath != null){
                    Glide.with(this).asBitmap().load(IMGPath).into(img);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_textBlue){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("BLUE");
            textSetting.SetColor("BLUE",text,text.getSelectionStart(),text.getSelectionEnd());
        } else if (id == R.id.text_textGreen){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("GREEN");
            textSetting.SetColor("GREEN",text,text.getSelectionStart(),text.getSelectionEnd());
        }else if (id == R.id.text_textYellow){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("YELLOW");
            textSetting.SetColor("YELLOW",text,text.getSelectionStart(),text.getSelectionEnd());
        }else if (id == R.id.text_textRed){
            textColorSetLocation.add(text.getSelectionStart());
            textColorSetLocation.add(text.getSelectionEnd());
            textColorSet.add("RED");
            textSetting.SetColor("RED",text,text.getSelectionStart(),text.getSelectionEnd());
        }

    }

}

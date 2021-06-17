package com.example.imgviewer.Text;

import android.graphics.Color;
import android.os.Parcel;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

public class TextSetting {

    public TextSetting() {

    }

    public void SetColor(String color, EditText editText,int start,int end){
        if (start!=end){
            if (end<start){
                int tag;
                tag = end;
                end = start;
                start = tag;
            }
            String s= editText.getText().toString().substring(start,end);
            Editable editable = editText.getText();
            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            ForegroundColorSpan colorSpan;
            switch (color) {
                case "BLUE": {
                    colorSpan = new ForegroundColorSpan(Color.BLUE);
                    break;
                }
                case "YELLOW": {
                    colorSpan = new ForegroundColorSpan(Color.YELLOW);
                    break;
                }
                case "GREEN": {
                    colorSpan = new ForegroundColorSpan(Color.GREEN);
                    break;
                }
                case "RED":{
                    colorSpan = new ForegroundColorSpan(Color.RED);
                    break;
                }
                default:{
                    colorSpan = new ForegroundColorSpan(Color.BLACK);
                }
            }
            builder.setSpan(colorSpan,0,s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editable.replace(start,end,builder);
            editText.setText(editable);
            editText.setSelection(end);
            editText.setFocusable(true);
        }
    }

    public void SetBounds(int size, EditText editText,int start,int end){
        if (start!=end) {
            if (end < start) {
                int tag;
                tag = end;
                end = start;
                start = tag;
            }
            String s = editText.getText().toString().substring(start, end);
            Log.e("s", s);
            Editable editable = editText.getText();
            SpannableStringBuilder builder = new SpannableStringBuilder(s);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size);
            builder.setSpan(absoluteSizeSpan, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editable.replace(start, end, builder);
            editText.setText(editable);
            editText.setSelection(end);
            editText.setFocusable(true);
        }
    }

    public byte[] GetSpan(EditText editText){
        Editable editable = editText.getText();
        ForegroundColorSpan[] foregroundColorSpans = editText.getText().getSpans(0,editText.length(), ForegroundColorSpan.class);
        AbsoluteSizeSpan[] absoluteSizeSpans = editText.getText().getSpans(0,editable.length(),AbsoluteSizeSpan.class);
        ArrayList<byte[]> colorSpans = new ArrayList<>();
        ArrayList<byte[]> sizeSpans = new ArrayList<>();
        ArrayList<Integer> colorSpansStart = new ArrayList<>();
        ArrayList<Integer> colorSpansEnd = new ArrayList<>();
        ArrayList<Integer> sizeSpansStart = new ArrayList<>();
        ArrayList<Integer> sizeSpansEnd = new ArrayList<>();

        for (int i = 0;i<foregroundColorSpans.length;i++){
            Parcel parcel = Parcel.obtain();
            foregroundColorSpans[i].writeToParcel(parcel,0);
            colorSpans.add(i,parcel.marshall());
            colorSpansStart.add(i,editable.getSpanStart(foregroundColorSpans[i]));
            colorSpansEnd.add(i,editable.getSpanEnd(foregroundColorSpans[i]));
            parcel.recycle();
        }
        for (int i = 0;i<absoluteSizeSpans.length;i++){
            Parcel parcel = Parcel.obtain();
            absoluteSizeSpans[i].writeToParcel(parcel,0);
            sizeSpans.add(i,parcel.marshall());
            sizeSpansStart.add(i,editable.getSpanStart(absoluteSizeSpans[i]));
            sizeSpansEnd.add(i,editable.getSpanEnd(absoluteSizeSpans[i]));
            parcel.recycle();
        }

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(new TextSpans(colorSpans,colorSpansStart,colorSpansEnd,sizeSpans,sizeSpansStart,sizeSpansEnd),0);
        byte[] bytes= parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public void ReSetSpan(EditText editText,byte[] bytes) {
        if (bytes != null){
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes,0,bytes.length);
            parcel.setDataPosition(0);
            TextSpans textSpans = parcel.readParcelable(TextSpans.class.getClassLoader());
            ArrayList<byte[]> colorSpans = textSpans.getColorSpans();
            ArrayList<byte[]> sizeSpans = textSpans.getSizeSpans();
            parcel.recycle();
            for (int i = 0;i<colorSpans.size();i++){
                String s= editText.getText().toString().substring(textSpans.getColorSpansStart().get(i),textSpans.getColorSpansEnd().get(i));
                Parcel parcel1 = Parcel.obtain();
                parcel1.unmarshall(colorSpans.get(i),0,colorSpans.get(i).length);
                parcel1.setDataPosition(0);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(parcel1);
                parcel1.recycle();
                Editable editable = editText.getText();
                spannableStringBuilder.setSpan(foregroundColorSpan,0,s.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.replace(textSpans.getColorSpansStart().get(i),textSpans.getColorSpansEnd().get(i),spannableStringBuilder);
                editText.setText(editable);
            }
            for (int i = 0;i<sizeSpans.size();i++){
                String s= editText.getText().toString().substring(textSpans.getSizeSpansStart().get(i),textSpans.getSizeSpansEnd().get(i));
                Parcel parcel2 = Parcel.obtain();
                parcel2.unmarshall(sizeSpans.get(i),0,sizeSpans.get(i).length);
                parcel2.setDataPosition(0);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(parcel2);
                parcel2.recycle();
                Editable editable = editText.getText();
                spannableStringBuilder.setSpan(absoluteSizeSpan,0,s.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editable.replace(textSpans.getSizeSpansStart().get(i),textSpans.getSizeSpansEnd().get(i),spannableStringBuilder);
                editText.setText(editable);
            }
        }
    }
}

package com.example.imgviewer.Text;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class TextSpans implements Parcelable {
    private ArrayList<byte[]> colorSpans;
    private ArrayList<byte[]> sizeSpans;
    private ArrayList<Integer> colorSpansStart = new ArrayList<>();
    private ArrayList<Integer> colorSpansEnd = new ArrayList<>();
    private ArrayList<Integer> sizeSpansStart = new ArrayList<>();
    private ArrayList<Integer> sizeSpansEnd = new ArrayList<>();

    public TextSpans() {
    }

    public TextSpans(ArrayList<byte[]> colorSpans,  ArrayList<Integer> colorSpansStart, ArrayList<Integer> colorSpansEnd, ArrayList<byte[]> sizeSpans,ArrayList<Integer> sizeSpansStart, ArrayList<Integer> sizeSpansEnd) {
        this.colorSpans = colorSpans;
        this.colorSpansStart = colorSpansStart;
        this.colorSpansEnd = colorSpansEnd;
        this.sizeSpans = sizeSpans;
        this.sizeSpansStart = sizeSpansStart;
        this.sizeSpansEnd = sizeSpansEnd;
    }

    protected TextSpans(Parcel in) {
    }

    public ArrayList<byte[]> getColorSpans() {
        return colorSpans;
    }

    public void setColorSpans(ArrayList<byte[]> colorSpans) {
        this.colorSpans = colorSpans;
    }

    public ArrayList<byte[]> getSizeSpans() {
        return sizeSpans;
    }

    public void setSizeSpans(ArrayList<byte[]> sizeSpans) {
        this.sizeSpans = sizeSpans;
    }

    public ArrayList<Integer> getColorSpansStart() {
        return colorSpansStart;
    }

    public void setColorSpansStart(ArrayList<Integer> colorSpansStart) {
        this.colorSpansStart = colorSpansStart;
    }

    public ArrayList<Integer> getColorSpansEnd() {
        return colorSpansEnd;
    }

    public void setColorSpansEnd(ArrayList<Integer> colorSpansEnd) {
        this.colorSpansEnd = colorSpansEnd;
    }

    public ArrayList<Integer> getSizeSpansStart() {
        return sizeSpansStart;
    }

    public void setSizeSpansStart(ArrayList<Integer> sizeSpansStart) {
        this.sizeSpansStart = sizeSpansStart;
    }

    public ArrayList<Integer> getSizeSpansEnd() {
        return sizeSpansEnd;
    }

    public void setSizeSpansEnd(ArrayList<Integer> sizeSpansEnd) {
        this.sizeSpansEnd = sizeSpansEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(colorSpans);
        dest.writeList(colorSpansStart);
        dest.writeList(colorSpansEnd);
        dest.writeList(sizeSpans);
        dest.writeList(sizeSpansStart);
        dest.writeList(sizeSpansEnd);
    }

    public static final Creator<TextSpans> CREATOR = new Creator<TextSpans>() {
        @Override
        public TextSpans createFromParcel(Parcel in) {
            TextSpans textSpans = new TextSpans();
            textSpans.colorSpans = new ArrayList<>();
            textSpans.colorSpansStart = new ArrayList<>();
            textSpans.colorSpansEnd = new ArrayList<>();
            textSpans.sizeSpans = new ArrayList<>();
            textSpans.sizeSpansStart = new ArrayList<>();
            textSpans.sizeSpansEnd = new ArrayList<>();
            in.readList(textSpans.colorSpans,getClass().getClassLoader());
            in.readList(textSpans.colorSpansStart,getClass().getClassLoader());
            in.readList(textSpans.colorSpansEnd,getClass().getClassLoader());
            in.readList(textSpans.sizeSpans,getClass().getClassLoader());
            in.readList(textSpans.sizeSpansStart,getClass().getClassLoader());
            in.readList(textSpans.sizeSpansEnd,getClass().getClassLoader());
            return textSpans;
        }

        @Override
        public TextSpans[] newArray(int size) {
            return new TextSpans[size];
        }
    };
}

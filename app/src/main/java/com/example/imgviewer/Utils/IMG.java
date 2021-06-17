package com.example.imgviewer.Utils;

public class IMG {

    public String imgname;
    public String imgUri;

    public IMG() {
    }

    public IMG(String imgname, String imgUri) {
        this.imgname = imgname;
        this.imgUri = imgUri;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

}

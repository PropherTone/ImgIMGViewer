package com.example.imgviewer.Web;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Web {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    int _id;

    @ColumnInfo(name = "WebUri")
    String WebUri;

    @ColumnInfo(name = "type")
    String type;

    public Web() {
    }

    public Web(String webUri, String type) {
        WebUri = webUri;
        this.type = type;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getWebUri() {
        return WebUri;
    }

    public void setWebUri(String webUri) {
        WebUri = webUri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

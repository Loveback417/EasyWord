package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_motto")
public class MenuMotto {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "motto",   typeAffinity = ColumnInfo.TEXT )
    private String motto;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getMotto() {
        return motto;
    }

    public void setMotto(@NonNull String motto) {
        this.motto = motto;
    }
}

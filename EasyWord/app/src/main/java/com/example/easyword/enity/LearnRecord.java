package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "learn_record")
public class LearnRecord {
    @Ignore
    public LearnRecord(@NonNull String uesr, @NonNull String date, int learn_words) {
        this.uesr = uesr;
        this.date = date;
        this.learn_words = learn_words;
    }

    public LearnRecord() {
    }

    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "user",   typeAffinity = ColumnInfo.TEXT )
    private String uesr;
    @NonNull
    @ColumnInfo(name = "date", typeAffinity = ColumnInfo.TEXT)
    private String date;
    @NonNull
    @ColumnInfo(name = "learn_words")
    private int learn_words;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getUesr() {
        return uesr;
    }

    public void setUesr(@NonNull String uesr) {
        this.uesr = uesr;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getLearn_words() {
        return learn_words;
    }

    public void setLearn_words(int learn_words) {
        this.learn_words = learn_words;
    }
}

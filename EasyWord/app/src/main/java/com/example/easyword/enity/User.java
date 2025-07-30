package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @Ignore
    public User(@NonNull String account, @NonNull String password, @NonNull String nickname,@NonNull String email,
                int learn_words, int learn_books, int learn_minutes, int learn_days,@NonNull String user_photo) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.learn_words = learn_words;
        this.learn_books = learn_books;
        this.learn_minutes = learn_minutes;
        this.learn_days = learn_days;
        this.user_photo = user_photo;
    }

    public User() {
    }

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @NonNull
    @ColumnInfo(name = "account")
    private String account;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    @ColumnInfo(name = "nickname")
    private String nickname;
    @NonNull
    @ColumnInfo(name = "email",defaultValue = "''")
    private String email;
    @NonNull
    @ColumnInfo(name = "learn_words")
    private int learn_words;
    @NonNull
    @ColumnInfo(name = "learn_books")
    private int learn_books;
    @NonNull
    @ColumnInfo(name = "learn_minutes")
    private int learn_minutes;
    @NonNull
    @ColumnInfo(name = "learn_days")
    private int learn_days;

    @ColumnInfo(name = "Capacity",typeAffinity = ColumnInfo.REAL)
    private Float Capacity;

    @NonNull
    @ColumnInfo(name = "user_photo")
    private String user_photo;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getAccount() {
        return account;
    }

    public void setAccount(@NonNull String account) {
        this.account = account;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@NonNull String nickname) {
        this.nickname = nickname;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public int getLearn_words() {
        return learn_words;
    }

    public void setLearn_words(int learn_words) {
        this.learn_words = learn_words;
    }

    public int getLearn_books() {
        return learn_books;
    }

    public void setLearn_books(int learn_books) {
        this.learn_books = learn_books;
    }

    public int getLearn_minutes() {
        return learn_minutes;
    }

    public void setLearn_minutes(int learn_minutes) {
        this.learn_minutes = learn_minutes;
    }

    public int getLearn_days() {
        return learn_days;
    }

    public void setLearn_days(int learn_days) {
        this.learn_days = learn_days;
    }

    public Float getCapacity() {
        return Capacity;
    }

    public void setCapacity(Float capacity) {
        Capacity = capacity;
    }

    @NonNull
    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(@NonNull String user_photo) {
        this.user_photo = user_photo;
    }
}

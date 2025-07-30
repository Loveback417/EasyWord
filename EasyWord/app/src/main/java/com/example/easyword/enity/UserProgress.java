package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
@Entity(tableName = "user_progress")
public class UserProgress {
    @Ignore
    public UserProgress(@NonNull String account,int current_learn,
                        int learn_count, @NonNull String learn_situation,int total_count) {
        this.account = account;
        this.current_learn = current_learn;
        this.learn_count = learn_count;
        this.learn_situation = learn_situation;
        this.total_count = total_count;
    }
    public UserProgress() {
    }

    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "account")
    private String account;
    @NonNull
    @ColumnInfo(name = "current_learn")
    private int current_learn;
    @NonNull
    @ColumnInfo(name = "learn_count")
    private int learn_count;

    @ColumnInfo(name = "learn_situation")
    private String learn_situation;

    @NonNull
    @ColumnInfo(name = "total_count")
    private int total_count;

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

    public int getCurrent_learn() {
        return current_learn;
    }

    public void setCurrent_learn(int current_learn) {
        this.current_learn = current_learn;
    }

    public int getLearn_count() {
        return learn_count;
    }

    public void setLearn_count(int learn_count) {
        this.learn_count = learn_count;
    }

    @NonNull
    public String getLearn_situation() {
        return learn_situation;
    }

    public void setLearn_situation(@NonNull String learn_situation) {
        this.learn_situation = learn_situation;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}

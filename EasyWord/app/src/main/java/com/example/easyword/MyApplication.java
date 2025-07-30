package com.example.easyword;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.BookImage;
import com.example.easyword.enity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MyApplication extends Application {
    private LearnWordDatabase learnWordDatabase;//声明一个数据库对象
    private static MyApplication mApp;
    public User login_user;

    public static MyApplication getInstance(){
        return mApp;
    }//单例模式
    SharedPreferences login_preferences;
    int  activityCount = 0;
    @Override
    public void onCreate() {
        mApp = this;
        super.onCreate();
        learnWordDatabase = LearnWordDatabase.getInstance(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {
                activityCount++;
            }
            @Override
            public void onActivityResumed(Activity activity) {}

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {
                activityCount--;
                if (activityCount == 0) { // 所有 Activity 都进入后台
                    saveLoginState();
                }
            }
            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
    public LearnWordDatabase getLearnWordDatabase(){
        return learnWordDatabase;
    }

    public void saveLoginState() {
        login_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = login_preferences.edit();
        editor.putString("last_account",login_user.getAccount());
        editor.commit();
    }
}

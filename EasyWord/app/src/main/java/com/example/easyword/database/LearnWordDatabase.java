package com.example.easyword.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.easyword.dao.BookImageDao;
import com.example.easyword.dao.FavouriteWordDao;
import com.example.easyword.dao.LearnRecordDao;
import com.example.easyword.dao.LearnWordDao;
import com.example.easyword.dao.MenuMottoDao;
import com.example.easyword.dao.ReviewWordDao;
import com.example.easyword.dao.TotalWordDao;
import com.example.easyword.dao.UserDao;
import com.example.easyword.dao.UserProgressDao;
import com.example.easyword.dao.WordDao;
import com.example.easyword.enity.BookImage;
import com.example.easyword.enity.FavouriteWord;
import com.example.easyword.enity.LearnRecord;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.MenuMotto;
import com.example.easyword.enity.ReviewWord;
import com.example.easyword.enity.TotalWord;
import com.example.easyword.enity.User;
import com.example.easyword.enity.UserProgress;


@Database(entities = {LearnWord.class, ReviewWord.class, MenuMotto.class, TotalWord.class,
        User.class, BookImage.class, LearnRecord.class, FavouriteWord.class, UserProgress.class},
        version = 1,exportSchema = false)
public abstract class LearnWordDatabase extends RoomDatabase {
    public abstract LearnWordDao learnWordDao();
    public abstract ReviewWordDao reviewWordDao();
    public abstract MenuMottoDao menuMottoDao();
    public abstract TotalWordDao totalWordDao();
    public abstract UserDao userDao();
    public abstract FavouriteWordDao favouriteWordDao();
    public abstract LearnRecordDao learnRecordDao();
    public abstract UserProgressDao userProgressDao();
    private static LearnWordDatabase instance;

    public static synchronized LearnWordDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LearnWordDatabase.class, "word_database")
                    .createFromAsset("WordDatabase.db") // 使用 asset 中的现成数据库
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}

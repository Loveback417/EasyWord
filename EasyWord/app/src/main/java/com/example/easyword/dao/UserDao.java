package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE account = :account")
    User LoginCheck(String account);
    @Query("SELECT learn_words FROM user WHERE account = :account")
    int getLearnWords(String account);
    @Query("SELECT learn_books FROM user WHERE account = :account")
    int getLearnBooks(String account);
    @Query("SELECT learn_minutes FROM user WHERE account = :account")
    int getLearnMinutes(String account);
    @Query("SELECT * FROM user WHERE account = :account")
    User SelectUser(String account);
    @Query("SELECT learn_days FROM user WHERE account = :account")
    int getLearnDays(String account);

    @Insert
    void SignUp(User user);
    @Query("UPDATE user SET learn_words = :words, learn_books= :books, learn_minutes = :minutes ,learn_days=:day , capacity= :capacity WHERE account = :account")
    void UpdateDetail(String account, int words, int books,int minutes,int day,float capacity);
    @Query("UPDATE user SET learn_books = :books WHERE account = :account")
    void UpdateBooks(String account, int books);
    @Query("UPDATE user SET learn_days = :days WHERE account = :account")
    void UpdateDays(String account, int days);
}

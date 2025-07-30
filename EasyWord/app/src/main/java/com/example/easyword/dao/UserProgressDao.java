package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.UserProgress;

@Dao
public interface UserProgressDao {
   @Insert
   void Insert(UserProgress progress);
   @Query("UPDATE user_progress SET current_learn = :book ,learn_count = :learn_count,total_count = :words WHERE account = :account")
   void UpdateBook(String account, int learn_count, int book, int words);
   @Query("UPDATE user_progress SET  learn_count =:words WHERE account = :account")
   void UpdateCount(String account, int words);
   @Query("UPDATE user_progress SET  learn_situation = :situation WHERE account = :account")
   void UpdateSituation(String account, String situation);
   @Query("SELECT current_learn  FROM user_progress WHERE account = :account")
   int QueryBook(String account);
   @Query("SELECT learn_situation  FROM user_progress WHERE account = :account")
   String QuerySituation(String account);
   @Query("SELECT total_count FROM user_progress WHERE account = :account")
   int QueryTotalCount(String account);
   @Query("SELECT learn_count FROM user_progress WHERE account = :account")
   int QueryReviewCount(String account);
   @Query("SELECT current_learn FROM user_progress WHERE account = :account")
   int QueryCurrent(String account);
}

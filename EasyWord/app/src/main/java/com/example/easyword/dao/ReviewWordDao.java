package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;

import java.util.List;

@Dao
public interface ReviewWordDao {
    @Insert
    void Insert(ReviewWord...reviewWords);
    @Query("SELECT * FROM review_word  WHERE next_review_time<=:now_time ORDER BY next_review_time ASC, review_time ASC ;")
    List<ReviewWord> SelectWords(String now_time);//3.10<3.18
    @Query("SELECT * FROM review_word ORDER BY next_review_time ASC, review_time ASC LIMIT 1;")

    ReviewWord SelectTop();//3.10<3.18

    @Query("UPDATE review_word SET review_time = :review_time, last_review_time = :last_review_time," +
            "next_review_time = :next_review_time ,last_review_interval=:last_review_interval WHERE _id = :id")
    void ReviewUpdate(int id, int review_time, String last_review_time, String next_review_time,int last_review_interval);

    @Query("DELETE FROM review_word")
    void DeleteAll();
    @Query("DELETE FROM review_word WHERE word=:word")
    void ReviewFinish(String word);

    @Query("Select Count(*) from review_word")
    int Count();
    @Query("SELECT * FROM review_word WHERE word = :word")
    ReviewWord QueryExist(String word);
}

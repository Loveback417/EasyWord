package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;
;

@Dao
public interface WordDao {
    @Insert
    void Insert(LearnWord...learnWords);

    @Insert
    void Insert(ReviewWord...reviewWords);

    @Delete
    void Delete(LearnWord...word);

    @Query("DELETE FROM reviewWord") // 替换为你表的实际名称
    void DeleteAll();

    @Query("SELECT * FROM LearnWord limit 1")
    LearnWord QueryTop();
}

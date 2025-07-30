package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.LearnWord;

import java.util.List;

@Dao
public interface LearnWordDao {
    @Insert
    void Insert(LearnWord...learnWords);

    @Query("SELECT * FROM learn_word")
    List<LearnWord> getAll();

    @Query("SELECT * FROM learn_word limit 1")
    LearnWord QueryTop();

    @Query("SELECT * FROM learn_word limit 20")
    List<LearnWord> Query20();

    @Delete
    void Delete(LearnWord...word);
    @Query("DELETE FROM learn_word")
    void DeleteAll();
    @Query("Select Count(*) from learn_word")
    int Count();
}

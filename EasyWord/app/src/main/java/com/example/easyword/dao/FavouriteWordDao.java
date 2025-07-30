package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.FavouriteWord;
import com.example.easyword.enity.TotalWord;

import java.util.List;

@Dao
public interface FavouriteWordDao {
    @Query("SELECT * FROM favourite_word")
    List<TotalWord> SearchAllWord();

    @Query("SELECT * FROM favourite_word WHERE word = :word")
    TotalWord SearchByWord(String word);
    @Insert
    void Insert(FavouriteWord word);

    @Query("DELETE  FROM favourite_word WHERE word=:word")
    void DeleteByWord(String word);
}
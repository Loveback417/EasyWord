package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.easyword.enity.FavouriteWord;
import com.example.easyword.enity.TotalWord;

import java.util.List;

@Dao
public interface TotalWordDao {
    @Query("SELECT * FROM total_word WHERE word LIKE :searchPattern")
    List<TotalWord> SearchAllWord(String searchPattern);
    @Query("SELECT * FROM total_word WHERE word LIKE :word")
    FavouriteWord SearchWord(String word);
    @Query("SELECT * FROM total_word WHERE kind LIKE :searchPattern Order BY word ASC")
    List<TotalWord> SearchBookkind(String searchPattern);
}

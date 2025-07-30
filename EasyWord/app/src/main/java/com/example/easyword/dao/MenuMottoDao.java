package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.easyword.enity.MenuMotto;

@Dao
public interface MenuMottoDao {
    @Query("SELECT COUNT(*) FROM menu_motto;")
    int getTotalCount();

    @Query("SELECT * FROM menu_motto WHERE _id = :id;")
    MenuMotto searchMotto(int id);
}

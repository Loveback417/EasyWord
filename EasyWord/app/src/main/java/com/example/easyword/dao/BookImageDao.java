package com.example.easyword.dao;

import android.media.Image;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.BookImage;

import java.util.List;

@Dao
public interface BookImageDao {

    @Insert
    void Insert(BookImage bookImage);

    @Query("SELECT * FROM bookimages limit 1")
    BookImage getAllImages();
}

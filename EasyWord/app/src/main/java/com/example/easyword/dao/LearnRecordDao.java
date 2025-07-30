package com.example.easyword.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.easyword.enity.LearnRecord;

import java.util.List;

@Dao
public interface LearnRecordDao {
    @Query("SELECT * FROM learn_record WHERE user = :useraccount ORDER BY date ASC")
    List<LearnRecord> SelectRecord(String useraccount);
    @Query("SELECT * FROM learn_record WHERE user = :useraccount AND date = :date")
    LearnRecord SelectOneRecord(String useraccount,String date);
    @Insert
    void Insert(LearnRecord record);
    @Query("UPDATE learn_record SET learn_words = :words WHERE user = :account AND date = :date")
    void UpdateRecord(String account, String date,int words);
}

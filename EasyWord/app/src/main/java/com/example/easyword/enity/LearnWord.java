package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "learn_word")
public class LearnWord {
    public LearnWord(@NonNull String word, @NonNull String meaning, @NonNull String sentence, @NonNull String sentence_meaning,Float difficulty) {
        this.word = word;
        this.meaning = meaning;
        this.sentence = sentence;
        this.sentence_meaning = sentence_meaning;
        this.difficulty = difficulty;
    }

    @Ignore


    public LearnWord() {}

    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "word",   typeAffinity = ColumnInfo.TEXT )
    private String word;
    @NonNull
    @ColumnInfo(name = "meaning", typeAffinity = ColumnInfo.TEXT)
    private String meaning;
    @NonNull
    @ColumnInfo(name = "sentence", typeAffinity = ColumnInfo.TEXT)
    private String sentence;

    @NonNull
    @ColumnInfo(name = "sentence_meaning", typeAffinity = ColumnInfo.TEXT)
    private String sentence_meaning;

    @ColumnInfo(name = "difficulty")
    private Float difficulty;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    @NonNull
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(@NonNull String meaning) {
        this.meaning = meaning;
    }

    @NonNull
    public String getSentence() {
        return sentence;
    }

    public void setSentence(@NonNull String sentence) {
        this.sentence = sentence;
    }

    @NonNull
    public String getSentence_meaning() {
        return sentence_meaning;
    }

    public void setSentence_meaning(@NonNull String sentence_meaning) {
        this.sentence_meaning = sentence_meaning;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }
}

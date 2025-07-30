package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "review_word")
public class ReviewWord {
    @Ignore
    public ReviewWord(@NonNull String word, @NonNull String meaning, @NonNull String sentence,
                      @NonNull String sentence_meaning, int review_time, @NonNull String next_review_time,
                      float difficulty,String last_review_time,int last_review_interval) {
        this.word = word;
        this.meaning = meaning;
        this.sentence = sentence;
        this.sentence_meaning = sentence_meaning;
        this.review_time = review_time;
        this.next_review_time = next_review_time;
        this.difficulty = difficulty;
        this.last_review_time = last_review_time;
        this.last_review_interval = last_review_interval;
    }

    public ReviewWord() {}

    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "word",   typeAffinity = ColumnInfo.TEXT )
    private String word;
    @NonNull
    @ColumnInfo(name = "meaning",   typeAffinity = ColumnInfo.TEXT )
    private String meaning;
    @NonNull
    @ColumnInfo(name = "sentence",   typeAffinity = ColumnInfo.TEXT )
    private String sentence;
    @NonNull
    @ColumnInfo(name = "sentence_meaning",   typeAffinity = ColumnInfo.TEXT )
    private String sentence_meaning;
    @NonNull
    @ColumnInfo(name = "review_time")
    private int review_time;

    @NonNull
    @ColumnInfo(name = "next_review_time",   typeAffinity = ColumnInfo.TEXT )
    private String next_review_time;

    @ColumnInfo(name = "difficulty")
    private Float difficulty;

    @NonNull
    @ColumnInfo(name = "last_review_time",   typeAffinity = ColumnInfo.TEXT )
    private String last_review_time;
    @NonNull
    @ColumnInfo(name = "last_review_interval")
    private int last_review_interval;

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

    public int getReview_time() {
        return review_time;
    }

    public void setReview_time(int review_time) {
        this.review_time = review_time;
    }

    @NonNull
    public String getNext_review_time() {
        return next_review_time;
    }

    public void setNext_review_time(@NonNull String next_review_time) {
        this.next_review_time = next_review_time;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }

    public String getLast_review_time() {
        return last_review_time;
    }

    public void setLast_review_time(String last_review_time) {
        this.last_review_time = last_review_time;
    }

    public int getLast_review_interval() {
        return last_review_interval;
    }

    public void setLast_review_interval(int last_review_interval) {
        this.last_review_interval = last_review_interval;
    }

    @Override
    public String toString() {
        return "ReviewWord{" +
                "_id=" + _id +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", sentence='" + sentence + '\'' +
                ", sentence_meaning='" + sentence_meaning + '\'' +
                ", review_time=" + review_time +
                ", next_review_time='" + next_review_time + '\'' +
                '}';
    }
}

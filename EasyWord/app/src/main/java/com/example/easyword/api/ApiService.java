package com.example.easyword.api;

import com.example.easyword.enity.TotalWord;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/api/words/by-word/{word}")
    Call<TotalWord> getWordByWord(@Path("word") String word);

    @POST("/api/words")
    Call<TotalWord> createWord(@Body TotalWord word);
}
package com.example.easyword;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.easyword.api.ApiService;
import com.example.easyword.enity.TotalWord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestApiActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_word;
    private TextView tv_result;
    private Button btn_check;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        et_word = findViewById(R.id.et_word);
        tv_result = findViewById(R.id.tv_res);
        btn_check = findViewById(R.id.btn_test_ok);

        btn_check.setOnClickListener(this);

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // 模拟器访问本机使用10.0.2.2
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void searchWord() {
        String word = et_word.getText().toString().trim();
        if (word.isEmpty()) {
            Toast.makeText(this, "请输入单词", Toast.LENGTH_SHORT).show();
            return;
        }
        apiService.getWordByWord(word).enqueue(new Callback<TotalWord>() {
            @Override
            public void onResponse(Call<TotalWord> call, Response<TotalWord> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TotalWord result = response.body();
                    String displayText = String.format(
                            "单词: %s\n词义: %s\n例句: %s\n难度: %.1f",
                            result.getWord(),
                            result.getMeaning(),
                            result.getSentence(),
                            result.getDifficulty()
                    );
                    tv_result.setText(displayText);
                } else {
                    tv_result.setText("未找到该单词");
                }
            }

            @Override
            public void onFailure(Call<TotalWord> call, Throwable t) {
                tv_result.setText("查询失败: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_test_ok){
            searchWord();
        }
    }
}
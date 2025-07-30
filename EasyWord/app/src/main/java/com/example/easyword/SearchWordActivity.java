package com.example.easyword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyword.adapter.SearchWordAdapter;
import com.example.easyword.dao.TotalWordDao;
import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.TotalWord;

import java.util.List;

public class SearchWordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_back;
    private EditText et_search;
    TotalWordDao totalWordDao;
    ListView lv_showWord;
    List<TotalWord> search_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_word);

        btn_back = findViewById(R.id.btn_back);
        et_search = findViewById(R.id.et_search);
        lv_showWord = findViewById(R.id.lv_search_word);

        btn_back.setOnClickListener(this);
        et_search.addTextChangedListener(new SearchTextWatcher());//检测文本变化监听器
        totalWordDao = LearnWordDatabase.getInstance(this).totalWordDao();

        lv_showWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchWordActivity.this, WordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("selectedWord",search_result.get(position).getWord());
                bundle.putString("selectedWordMeaning",search_result.get(position).getMeaning());
                bundle.putString("selectedWordSentence",search_result.get(position).getSentence());
                bundle.putString("selectedWordSentenceMeaning",search_result.get(position).getSentence_meaning());
                intent.putExtras(bundle);
                startActivity(intent);// 启动目标Activity
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back){
            finish();
        }
    }
    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String now_word = et_search.getText().toString();
            if(now_word.isEmpty()){
                lv_showWord.setVisibility(View.GONE);

            }else{
                lv_showWord.setVisibility(View.VISIBLE);
                String searchPattern = now_word + "%"; // 添加通配符
                search_result = totalWordDao.SearchAllWord(searchPattern);//模糊搜索
                SearchWordAdapter adapter = new SearchWordAdapter(SearchWordActivity.this,search_result);
                lv_showWord.setAdapter(adapter);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
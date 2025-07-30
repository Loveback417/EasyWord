package com.example.easyword;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyword.dao.FavouriteWordDao;
import com.example.easyword.dao.TotalWordDao;
import com.example.easyword.enity.FavouriteWord;
import com.example.easyword.enity.TotalWord;

public class WordDetailActivity extends AppCompatActivity {
    private TextView tv_word,tv_meaning,tv_sentence,tv_sentence_meaning;
    private ImageView iv_star;
    public GestureDetector gestureDetector;
    private View rootView;
    Drawable starBorderDrawable;
    Drawable starFillDrawable;
    boolean isFilled = false; // 标记是否填充
    Bundle bundle;
    Intent intent;
    FavouriteWordDao fav_dao;
    TotalWordDao dao;
    TotalWord word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word_detail);
        tv_word =findViewById(R.id.tv_word_detail_word);
        tv_meaning =findViewById(R.id.tv_word_detail_meaning);
        tv_sentence =findViewById(R.id.tv_word_detail_sentence);
        tv_sentence_meaning =findViewById(R.id.tv_word_detail_sentence_meaning);
        iv_star = findViewById(R.id.iv_star);
        rootView = findViewById(R.id.rv_main);

        starBorderDrawable = getDrawable(R.drawable.ic_star_border);
        starFillDrawable = getDrawable(R.drawable.ic_star_fill);
        iv_star.setImageDrawable(starBorderDrawable);

        fav_dao = MyApplication.getInstance().getLearnWordDatabase().favouriteWordDao();
        dao = MyApplication.getInstance().getLearnWordDatabase().totalWordDao();

        intent = getIntent();
        bundle = intent.getExtras();

        iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilled) {
                    iv_star.setImageDrawable(starBorderDrawable);
                    try {
                        fav_dao.DeleteByWord(bundle.getString("selectedWord",""));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    iv_star.setImageDrawable(starFillDrawable);
                    try {
                        FavouriteWord insert_word = dao.SearchWord(bundle.getString("selectedWord",""));
                        fav_dao.Insert(insert_word);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                isFilled =!isFilled;
            }
        });

        show();
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getY()-e1.getY()  > 50) { // 下滑的阈值
                    onSwipeDown();
                    return true;
                }
                return false;
            }
        });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void onSwipeDown() {
        Intent intent = new Intent();
        intent.setClass(WordDetailActivity.this, SearchWordActivity.class);//class为跳转对象
        startActivity(intent);
        finish();
    }

    private void show(){
        word = fav_dao.SearchByWord(bundle.getString("selectedWord",""));
        if(word == null){
            iv_star.setImageDrawable(starBorderDrawable);
            isFilled = false;
        }else{
            iv_star.setImageDrawable(starFillDrawable);
            isFilled = true;
        }
        if (bundle!=null){
            tv_word.setText(bundle.getString("selectedWord",""));
            tv_meaning.setText(bundle.getString("selectedWordMeaning",""));
            tv_sentence.setText(bundle.getString("selectedWordSentence",""));
            tv_sentence_meaning.setText(bundle.getString("selectedWordSentenceMeaning",""));
        }
    }
}
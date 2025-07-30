package com.example.easyword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.easyword.adapter.WordCardAdapter;
import com.example.easyword.dao.LearnRecordDao;
import com.example.easyword.dao.LearnWordDao;
import com.example.easyword.dao.ReviewWordDao;
import com.example.easyword.dao.UserDao;
import com.example.easyword.dao.UserProgressDao;
import com.example.easyword.enity.LearnRecord;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;
import com.example.easyword.enity.User;
import com.example.easyword.util.ToastUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LearnMenuActivity extends AppCompatActivity implements View.OnClickListener, WordCardAdapter.OnLearnWordClickListener {

    private ViewPager2 vp_learn;
    private LearnWord showWord;
    private ReviewWordDao review_dao;
    private LearnWordDao learn_dao;
    private UserDao user_dao;
    private LearnRecordDao record_dao;
    MyApplication application;
    LocalDateTime StartTime;
    private GestureDetector gestureDetector;
    User login_user;
    User current_user;
    int start_num;
    int this_time_num;
    int current_minute;
    int current_day;
    int current_book;
    int current_words;
    WordCardAdapter adapter;
    UserProgressDao progressDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_menu);
        vp_learn = findViewById(R.id.viewPager_learn_word);
        application = MyApplication.getInstance();

        review_dao = application.getLearnWordDatabase().reviewWordDao();
        learn_dao = application.getLearnWordDatabase().learnWordDao();
        user_dao = application.getLearnWordDatabase().userDao();
        record_dao = application.getLearnWordDatabase().learnRecordDao();
        progressDao = application.getLearnWordDatabase().userProgressDao();

        this_time_num=0;//本次背诵单词数

        login_user = application.login_user;

        current_user = user_dao.SelectUser(login_user.getAccount());
        current_day = current_user.getLearn_days();//获取用户记录
        current_book = current_user.getLearn_books();
        current_minute = current_user.getLearn_minutes();
        current_words  = current_user.getLearn_words();

        View rootView = findViewById(R.id.root_view_learn_menu); // 根布局的 ID
        //下滑进入搜索页面
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
        show();
    }


    private void onSwipeDown() {
        Intent intent = new Intent();
        intent.setClass(LearnMenuActivity.this, SearchWordActivity.class);//class为跳转对象
        startActivity(intent);
    }


    private void show(){
        List<LearnWord> list;
        list = learn_dao.Query20();
        if(list.size()==0){
            //当前词书已背完
            current_book++;
            //提示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("恭喜你，当前词书已背完!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
            int position = progressDao.QueryBook(login_user.getAccount());
            String situation = progressDao.QuerySituation(login_user.getAccount());
            StringBuilder sb = new StringBuilder(situation);
            sb.setCharAt(position-1, '1'); // 直接修改指定位置
            progressDao.UpdateSituation(login_user.getAccount(),sb.toString());
        }
        // 创建适配器
        adapter = new WordCardAdapter(this, list);
        adapter.setListener(this);
        // 设置适配器
        vp_learn.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        start_num = learn_dao.Count();
        StartTime = LocalDateTime.now();
    }

    @Override
    public void onClick(View v) {

    }
    private void WordLearned(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String next_review_time= now.plusDays(0).format(formatter);//一天后进行第一次复习
        ReviewWord word = new ReviewWord(showWord.getWord(),
                showWord.getMeaning(),
                showWord.getSentence(),
                showWord.getSentence_meaning(),
                1,
                next_review_time,
                showWord.getDifficulty(),
                now.format(formatter),
                1);
        review_dao.Insert(word);
        learn_dao.Delete(showWord);
        this_time_num++;//此次背诵单词数++
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int total_word_count = start_num-learn_dao.Count();

        String account = login_user.getAccount();

        LocalDateTime EndTime = LocalDateTime.now();
        Duration duration = Duration.between(StartTime, EndTime);
        current_minute = current_minute + (int) duration.toMinutes();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// 定义格式化器，包含年、月和日期
        String formattedDate = EndTime.format(formatter);//当前日期
        LearnRecord LiveRecord = record_dao.SelectOneRecord(account,formattedDate);//更新学习天数
        if (LiveRecord == null){
            LiveRecord = new LearnRecord(account,formattedDate,total_word_count);
            record_dao.Insert(LiveRecord);
            current_day++;
        }else{
            //今天的其他时候已经学习过了，则在今天的学习记录上加单词数
            record_dao.UpdateRecord(account,formattedDate,current_words+total_word_count);
        }
        user_dao.UpdateDetail(account,current_words+total_word_count,current_book,
                current_minute,current_day,current_user.getCapacity());//更新记录

        int learn_count = learn_dao.Count();
        int total_count = progressDao.QueryTotalCount(account);
        progressDao.UpdateCount(account,total_count-learn_count);
    }

    @Override
    public void onLearnWordClick(LearnWord word,int position) {
        if(review_dao.QueryExist(word.getWord())==null){//第一次学习
            showWord = word;
            WordLearned();
            int nextPosition = position + 1;
            if (nextPosition < adapter.getItemCount()) {
                vp_learn.setCurrentItem(nextPosition, true); // true 表示启用平滑滚动
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("你已背"+this_time_num+"词，是否继续？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                show();//展示新的单词
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();//结束背诵。返回主界面
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
        else {//已经学习过
            ToastUtil.show(this, "您已学习过该单词");
        }
    }
}
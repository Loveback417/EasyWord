package com.example.easyword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.easyword.adapter.ReviewWordCardAdapter;
import com.example.easyword.adapter.WordCardAdapter;
import com.example.easyword.dao.LearnRecordDao;
import com.example.easyword.dao.ReviewWordDao;
import com.example.easyword.dao.UserDao;
import com.example.easyword.dao.WordDao;
import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.LearnRecord;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;
import com.example.easyword.enity.User;
import com.example.easyword.util.ToastUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReviewMenuActivity extends AppCompatActivity implements View.OnClickListener,ReviewWordCardAdapter.OnReviewWordClickListener {
    private ViewPager2 vp_review;
    private ReviewWordDao review_dao;
    private UserDao user_dao;
    private LearnRecordDao record_dao;
    private ReviewWord reviewWord;
    double[] next_intervals = {0.56, 1.0, 2.0, 4.36, 8.81};//下一次复习间隔
    double forgetting_base = 1.84;//遗忘系数
    int[] interval = {0, 1, 2, 5, 7, 9};//下一次复习间隔
    int[] click;
    int flag=0;
    float current_capacity;//目前能力;
    int current_review_times;//目前记忆次数
    int next_review_interval;//下次复习间隔
    List<ReviewWord> list = new ArrayList<>();//接收单词
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//时间格式
    private GestureDetector gestureDetector;
    MyApplication application;
    LocalDateTime StartTime;//开始学习时间
    int this_time_num;
    int current_minute;
    int current_day;
    int current_words;
    int Start_num;
    ReviewWordCardAdapter adapter;
    User user;
    User current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_menu);
        vp_review = findViewById(R.id.viewPager_review_word);

        application = MyApplication.getInstance();

        review_dao = application.getLearnWordDatabase().reviewWordDao();
        user_dao = application.getLearnWordDatabase().userDao();
        record_dao = application.getLearnWordDatabase().learnRecordDao();

        user = application.login_user;

        current_capacity = user.getCapacity();
        current_user = user_dao.SelectUser(user.getAccount());
        current_day = current_user.getLearn_days();
        current_words = current_user.getLearn_words();
        current_minute = current_user.getLearn_minutes();

        this_time_num = 0;//此次已背单词数目

        View rootView = findViewById(R.id.root_view_review_menu); // 根布局的 ID
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
        show();//展示单词
    }
    private void onSwipeDown() {
        Intent intent = new Intent();
        intent.setClass(ReviewMenuActivity.this, SearchWordActivity.class);//class为跳转对象
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        StartTime = LocalDateTime.now();
        Start_num = review_dao.Count();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int total_word_count = Start_num-review_dao.Count();

        String account = current_user.getAccount();

        LocalDateTime EndTime = LocalDateTime.now();
        Duration duration = Duration.between(StartTime, EndTime);
        current_minute = current_minute+(int) duration.toMinutes();//总学习时间

        current_words = current_words+total_word_count;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// 定义格式化器，包含年、月和日期
        String formattedDate = EndTime.format(formatter);//当前日期
        LearnRecord LiveRecord = record_dao.SelectOneRecord(account,formattedDate);//更新学习天数
        if (LiveRecord == null){
            LiveRecord = new LearnRecord(account,formattedDate,total_word_count);
            record_dao.Insert(LiveRecord);
            current_day++;
        }else{
            //今天的其他时候已经学习过了，则在今天的学习记录上加单词数
            record_dao.UpdateRecord(account,formattedDate,current_words);
        }
        user_dao.UpdateDetail(current_user.getAccount(),current_words,current_user.getLearn_books(),
                current_minute,current_day,current_capacity);
    }

    private void show(){

        LocalDateTime now = LocalDateTime.now();//获取当前时间
        String partten = now.format(formatter);//转为字符型
        list = review_dao.SelectWords(partten);//搜索
        if(list.size()==0){
            //提示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("当前没有需要复习的单词!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        // 创建适配器
        adapter = new ReviewWordCardAdapter(this,list);
        adapter.setListener(this);
        // 设置适配器
        vp_review.setAdapter(adapter);
        click = new int[list.size()];
    }

    @Override
    public void onClick(View v) {

    }
    private void Remember(){
        /* 随机波动因子 */
        // 加入±15%随机波动（防止机械化记忆）
        double randomFactor = 0.85 + (Math.random() * 0.3);

        //单词难度
        double difficulty = reviewWord.getDifficulty();

        //从上次到现在的时间间隔
        int last_review_interval_time = getDaysSinceLastReview();

        //上次复习的间隔
        int last_interval = reviewWord.getLast_review_interval();
        // 使用指数衰减模型计算记忆保留率 R = e^(-t/S)
        // t: 距离上次复习天数，S: 上次复习的间隔*0.9（加入衰减缓冲）
        double memoryRetention = Math.exp(-last_review_interval_time / ( last_interval* 0.9));

        /* 复习进度加成 */
        // 每完成一次复习增加20%基础系数
        // 第5次复习时达到1.8倍加成
        double progressFactor = 1.0 + (current_review_times * 0.2);

        /* 能力系数处理 */
        // 对数处理用户能力（限制增长速率）
        // 结果范围0-2.3（当capacity=10时）
        double capacityFactor = 1 + Math.log(current_capacity);

        //增加用户的能力
        current_capacity = current_capacity +reviewWord.getDifficulty()/100;

        /* 难度适应系数 */
        // 对数处理难度系数（降低极端值影响）
        // +1防止log(0)，结果范围约0.3-1.6
        double difficultyFactor = 1.0 / (1 + Math.log(difficulty + 1));


        current_review_times = reviewWord.getReview_time();

        double newInterval = interval[current_review_times] * capacityFactor * difficultyFactor
                * memoryRetention * progressFactor * randomFactor;

        current_review_times++;//当前复习次数++;

        next_review_interval = (int)Math.ceil(newInterval);//向上取整

        if(current_review_times>5){
            //该单词复习完成
            review_dao.ReviewFinish(reviewWord.getWord());
        }else{
            LocalDateTime now = LocalDateTime.now();
            //下一次复习时间
            String next_review_time= now.plusDays(next_review_interval).format(formatter);
            String last_review_time= now.format(formatter);
            review_dao.ReviewUpdate(reviewWord.get_id(),current_review_times,
                    last_review_time,next_review_time,next_review_interval);
        }
    }
    private void Little(){
        //用户能力不变

        //当前复习次数不变

        //下一次复习时间

        LocalDateTime now = LocalDateTime.now();

        String last_review_time= now.format(formatter);

        String next_review_time= now.plusDays(2).format(formatter);//2天后安排再次复习

        review_dao.ReviewUpdate(reviewWord.get_id(),reviewWord.getReview_time(),last_review_time,next_review_time,2);
    }
    private void Forget(){

        //用户能力变少
        current_capacity = current_capacity - reviewWord.getDifficulty()/100;//增加用户的能力

        //当前复习次数减少
        if(reviewWord.getReview_time()>1){
            current_review_times=reviewWord.getReview_time()-1;
        }else {
            current_review_times=reviewWord.getReview_time();
        }
        LocalDateTime now = LocalDateTime.now();
        String last_review_time= now.format(formatter);
        String next_review_time;
        int review_intervals;
        if(reviewWord.getDifficulty()>5){
            next_review_time= now.plusDays(1).format(formatter);//几天后安排再次复习
            review_intervals=1;
        }else {
            next_review_time= now.plusDays(2).format(formatter);
            review_intervals=2;
        }
        review_dao.ReviewUpdate(reviewWord.get_id(),current_review_times,
                last_review_time,next_review_time,review_intervals);
    }

    private int getDaysSinceLastReview() {
        LocalDate now = LocalDate.now();
        String last_review_time  = reviewWord.getLast_review_time();
        LocalDate last_time  = LocalDate.parse(last_review_time,formatter);
        return (int) ChronoUnit.DAYS.between(last_time, now);//返回时间间隔
    }

    private void AlertEnd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("你已背"+this_time_num+"词，是否退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void AlertNoWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("你已复习完所有单词，请学习新的单词吧！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }


    @Override
    public void onReviewWordClickRemenber(ReviewWord word, int position) {
        reviewWord = word;
        int nextPosition = position + 1;
        if(click[position] == 0) {
            if (nextPosition < adapter.getItemCount()) {
                Remember();//进入对应的处理函数
                vp_review.setCurrentItem(nextPosition, true); // true 表示启用平滑滚动
                this_time_num++;
            } else {
                AlertNoWord();
            }
        }
        else {//已经学习过
            ToastUtil.show(this, "您已复习过该单词");
        }
        click[position]=1;
    }

    @Override
    public void onReviewWordClickLittle(ReviewWord word, int position) {
        reviewWord = word;
        int nextPosition = position + 1;
        if(click[position] == 0) {
            if (nextPosition < adapter.getItemCount()) {
                Little();
                vp_review.setCurrentItem(nextPosition, true); // true 表示启用平滑滚动
                this_time_num++;
            } else {
                AlertNoWord();
            }
        }
        else {//已经学习过
            ToastUtil.show(this, "您已复习过该单词");
        }
        click[position]=1;
    }

    @Override
    public void onReviewWordClickForget(ReviewWord word, int position) {
        reviewWord = word;
        int nextPosition = position + 1;
        if(click[position] == 0) {
            if (nextPosition < adapter.getItemCount()) {
                Forget();
                vp_review.setCurrentItem(nextPosition, true); // true 表示启用平滑滚动
                this_time_num++;
            } else {
                AlertNoWord();
            }
        }
        else {//已经学习过
            ToastUtil.show(this, "您已复习过该单词");
        }
        click[position]=1;
    }

    @Override
    public void onBackPressed() {
        AlertEnd();
    }
}
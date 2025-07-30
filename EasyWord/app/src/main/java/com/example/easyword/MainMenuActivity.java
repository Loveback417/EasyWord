package com.example.easyword;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.easyword.dao.LearnWordDao;
import com.example.easyword.dao.ReviewWordDao;
import com.example.easyword.dao.TotalWordDao;
import com.example.easyword.dao.UserProgressDao;
import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.ReviewWord;
import com.example.easyword.enity.TotalWord;
import com.example.easyword.util.ToastUtil;
import com.example.easyword.util.ViewUtil;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView cardView_learn;
    private CardView cardView_review;
    private TextView main_menu_motto,tv_learn_count,tv_review_count,tv_nav_header_name;
    private ImageView iv_jump_homepage,iv_jump_statistic,iv_jump_bookshell,iv_menu_user_photo;
    LearnWordDatabase learnWordDatabase;
    private GestureDetector gestureDetector;//下滑
    private long backPressedTime = 0; // 记录第一次点击返回键的时间
    private static final int TIME_INTERVAL = 2000; // 两次点击的时间间隔（2秒）

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//时间格式
    LearnWordDao learn_dao;
    ReviewWordDao review_dao;
    UserProgressDao progressDao;
    TotalWordDao totalWordDao;
    int learn_word_count;
    int review_word_count;
    private DrawerLayout drawerLayout;//导航栏
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        cardView_learn = findViewById(R.id.cardview_learn);
        cardView_review = findViewById(R.id.cardview_review);
        main_menu_motto = findViewById(R.id.tv_main_menu_motto);
        iv_jump_homepage = findViewById(R.id.iv_jump_homepage);
        iv_jump_bookshell= findViewById(R.id.iv_jump_bookshell);
        iv_jump_statistic = findViewById(R.id.iv_jump_statistic);
        tv_learn_count = findViewById(R.id.tv_main_menu_learn_count);
        tv_review_count = findViewById(R.id.tv_main_menu_review_count);
        tv_nav_header_name = findViewById(R.id.tv_nav_header_name);

        drawerLayout = findViewById(R.id.drawer_layout); // 初始化 DrawerLayout 和 NavigationView
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);// 获取头部视图
        // 从头部视图中查找 tv_nav_header_name
        tv_nav_header_name = headerView.findViewById(R.id.tv_nav_header_name);
        //tv_nav_header_name.setText(MyApplication.getInstance().login_user.getNickname());


        cardView_review.setOnClickListener(this);
        cardView_learn.setOnClickListener(this);
        iv_jump_statistic.setOnClickListener(this);
        iv_jump_bookshell.setOnClickListener(this);

        learnWordDatabase = LearnWordDatabase.getInstance(this);
        learn_dao = MyApplication.getInstance().getLearnWordDatabase().learnWordDao();
        review_dao = MyApplication.getInstance().getLearnWordDatabase().reviewWordDao();
        progressDao = MyApplication.getInstance().getLearnWordDatabase().userProgressDao();
        totalWordDao = MyApplication.getInstance().getLearnWordDatabase().totalWordDao();
        learn_word_count = learn_dao.Count();
        review_word_count = review_dao.Count();

        //导航栏显示名字
//        tv_nav_header_name.setText(MyApplication.getInstance().login_user.getNickname());

        View rootView = findViewById(R.id.root_view); // 根布局的 ID
//        rootView.setOnTouchListener(new View.OnTouchListener() {//点击非EditText的其他部分隐藏键盘
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ViewUtil.HideOneInputMethod(MainMenuActivity.this,et_search);//Util内函数
//                return false;
//            }
//        });

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

        //侧边栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_arrow); // 设置菜单图标
        }

        iv_menu_user_photo = headerView.findViewById(R.id.iv_menu_user_photo);
        //侧边栏展示头像
        String base64Photo = MyApplication.getInstance().login_user.getUser_photo();
        try {
            byte[] decodedBytes = Base64.decode(base64Photo, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            iv_menu_user_photo.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("AvatarError", "Failed to decode: " + e.getMessage());
            iv_menu_user_photo.setImageResource(R.drawable.image_default);
        }

        tv_nav_header_name.setText(MyApplication.getInstance().login_user.getNickname());

        // 设置侧边栏菜单项的点击事件
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
             if (id == R.id.nav_update_info) {
                 Intent intent = new Intent();
                 intent.setClass(MainMenuActivity.this, UpdateActivity.class);//class为跳转对象
                 startActivity(intent);
                // 处理个人资料点击
            } else if (id == R.id.nav_log_out) {
                // 点击退出登录
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, LoginActivity.class);//class为跳转对象
                startActivity(intent);
                finish();
            }
            // 关闭侧边栏
            drawerLayout.closeDrawers();
            return true;
        });

        SharedPreferences login_preferences;
        login_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        String last_account = login_preferences.getString("last_account","");
        if(!last_account.equals(MyApplication.getInstance().login_user.getAccount())){
           Init();
        }
        MainMenuShow();
        MainMenuCount();
    }
    public void Init(){
        int review_count = progressDao.QueryReviewCount(MyApplication.getInstance().login_user.getAccount());
        int position = progressDao.QueryCurrent(MyApplication.getInstance().login_user.getAccount());
        StringBuilder search = new StringBuilder();//拼接searchPartten
        if(position!=0){
            for(int i=0;i<6;i++){//拼接searchPartten
                if(i==position-1){
                    search.append("1");
                }else{
                    search.append("_");
                }
            }
        }
        learn_dao.DeleteAll();
        review_dao.DeleteAll();
        List<TotalWord> list = totalWordDao.SearchBookkind(search.toString());//将search转化为字符型
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");// 定义格式化器，包含年、月和日期
        for (int i=0;i<review_count;i++){
            ReviewWord word = new ReviewWord(list.get(i).getWord(),list.get(i).getMeaning(),
                    list.get(i).getSentence(),list.get(i).getSentence_meaning(),
                    1,time2.format(formatter),list.get(i).getDifficulty(),
                    time1.format(formatter), 1);
            review_dao.Insert(word);
        }
        for (int i=review_count;i< list.size();i++){
            LearnWord word = new LearnWord(list.get(i).getWord(),list.get(i).getMeaning(),
                    list.get(i).getSentence(),list.get(i).getSentence_meaning(),list.get(i).getDifficulty());
            learn_dao.Insert(word);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainMenuCount();
    }

    private void onSwipeDown() {
        Intent intent = new Intent();
        intent.setClass(MainMenuActivity.this, SearchWordActivity.class);//class为跳转对象
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {//点击事件
        if (v.getId() == R.id.cardview_learn) {
            if (learn_word_count == 0 && review_word_count == 0 ){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("请选择一本你想背的词书")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainMenuActivity.this, BookShellActivity.class);//class为跳转对象
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            } else if (learn_word_count == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("请先复习所学单词")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            } else{
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, LearnMenuActivity.class);//class为跳转对象
                startActivity(intent);
            }

        }else if(v.getId() == R.id.cardview_review){
            if(review_word_count==0){//如果review_word为空则拒绝进入
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("你目前还没有要背的图书")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            } else {//若review_word不为空但还没到单词记忆时间则拒绝进入
                ReviewWord reviewWord = review_dao.SelectTop();
                LocalDate LatestTime = LocalDate.parse(reviewWord.getNext_review_time(), formatter);
                LocalDate NowTime = LocalDate.now();//获取当前时间
                if(LatestTime.isAfter(NowTime)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示")
                            .setMessage("你目前还没有要背的单词")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else{//满足复习条件，进入review_activity
                    Intent intent = new Intent();
                    intent.setClass(MainMenuActivity.this, ReviewMenuActivity.class);//class为跳转对象
                    startActivity(intent);
                }
            }
        } else if (v.getId() == R.id.iv_jump_statistic) {
            Intent intent = new Intent();
            intent.setClass(MainMenuActivity.this, StatisticsActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        }else if (v.getId()==R.id.iv_jump_bookshell) {
            Intent intent = new Intent();
            intent.setClass(MainMenuActivity.this, BookShellActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START); // 打开侧边栏
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void MainMenuShow(){//展示主页名言
        int count = learnWordDatabase.menuMottoDao().getTotalCount();
        int selectnum = new Random().nextInt(count)+1;
        String motto = learnWordDatabase.menuMottoDao().searchMotto(selectnum).getMotto();
        main_menu_motto.setText(motto);
    }
    private void MainMenuCount(){
        review_word_count = review_dao.Count();
        learn_word_count = learn_dao.Count();
        tv_review_count.setText(String.valueOf(review_word_count));
        tv_learn_count.setText(String.valueOf(learn_word_count));
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
            // 如果两次点击的时间间隔小于 2 秒，退出应用
            super.onBackPressed();
        } else {
            // 第一次点击，显示提示
           ToastUtil.show(this,"再按一次退出");
        }
        // 更新第一次点击的时间
        backPressedTime = System.currentTimeMillis();
    }
}
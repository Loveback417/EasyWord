package com.example.easyword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyword.dao.LearnRecordDao;
import com.example.easyword.dao.UserDao;
import com.example.easyword.enity.LearnRecord;
import com.example.easyword.enity.User;
import com.example.easyword.util.ToastUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_jump_homepage,iv_jump_statistic,iv_jump_bookshell;
    private TextView tv_remember_words,tv_remember_books,tv_remember_minutes,tv_remember_days;
    private long backPressedTime = 0; // 记录第一次点击返回键的时间
    private static final int TIME_INTERVAL = 2000; // 两次点击的时间间隔（2秒）
    BarChart barChart;
    MyApplication application;
    private LearnRecordDao dao;
    private UserDao user_dao;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);

        iv_jump_homepage = findViewById(R.id.iv_jump_homepage);
        iv_jump_bookshell= findViewById(R.id.iv_jump_bookshell);
        iv_jump_statistic = findViewById(R.id.iv_jump_statistic);
        tv_remember_words = findViewById(R.id.tv_remember_words);
        tv_remember_books = findViewById(R.id.tv_remember_books);
        tv_remember_minutes = findViewById(R.id.tv_remember_minutes);
        tv_remember_days = findViewById(R.id.tv_remember_days);

        barChart = findViewById(R.id.bar_chart);

        iv_jump_homepage.setOnClickListener(this);
        iv_jump_bookshell.setOnClickListener(this);

        application = MyApplication.getInstance();
        user = application.login_user;
        dao = application.getLearnWordDatabase().learnRecordDao();
        user_dao = application.getLearnWordDatabase().userDao();  

        ShowChart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowStatistics();
    }
    public void ShowStatistics(){
        tv_remember_words.setText("已背单词:"+String.valueOf(user_dao.getLearnWords(user.getAccount()))+"个");
        tv_remember_books.setText("已背词书:"+String.valueOf(user_dao.getLearnBooks(user.getAccount()))+"本");
        tv_remember_minutes.setText("已学习:"+String.valueOf(user_dao.getLearnMinutes(user.getAccount()))+"分钟");
        tv_remember_days.setText("已学习:"+String.valueOf(user_dao.getLearnDays(user.getAccount()))+"天");
    }
    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.iv_jump_homepage){
            Intent intent = new Intent();
            intent.setClass(StatisticsActivity.this, MainMenuActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        } else if (v.getId()==R.id.iv_jump_bookshell) {
            Intent intent = new Intent();
            intent.setClass(StatisticsActivity.this, BookShellActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        }
    }
    private void ShowChart(){

        List<LearnRecord> list_record = dao.SelectRecord(user.getAccount());
        String[] labels = new String[7];

        List<BarEntry> entries = new ArrayList<>(); // 准备数据
        for(int i = 0; i<list_record.size()&&i<7; i++){
            entries.add(new BarEntry(i, list_record.get(i).getLearn_words()));
            labels[i] = list_record.get(i).getDate();// 自定义 X 轴标签
        }

        BarDataSet dataSet = new BarDataSet(entries, "每日背单词统计");// 创建数据集
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);


        BarData barData = new BarData(dataSet);// 创建 BarData 对象
        barData.setBarWidth(0.5f); // 设置柱子宽度


        barChart.setData(barData);// 将数据设置到图表

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置 X 轴在底部
        xAxis.setGranularity(1f); // 设置最小间隔为 1
        xAxis.setLabelCount(labels.length); // 设置标签数量


        barChart.getAxisRight().setEnabled(false);// 禁用右侧 Y 轴

        barChart.invalidate();// 刷新图表
        barChart.setTouchEnabled(false);
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
package com.example.easyword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyword.dao.BookImageDao;
import com.example.easyword.enity.BookImage;
import com.example.easyword.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

public class BookShellActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_jump_homepage,iv_jump_statistic,iv_jump_bookshell;
    private RecyclerView recyclerView;
    private com.example.myapp.ImageAdapter imageAdapter;
    private long backPressedTime = 0; // 记录第一次点击返回键的时间
    private static final int TIME_INTERVAL = 2000; // 两次点击的时间间隔（2秒）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_shell);

        iv_jump_homepage = findViewById(R.id.iv_jump_homepage);
        iv_jump_bookshell= findViewById(R.id.iv_jump_bookshell);
        iv_jump_statistic = findViewById(R.id.iv_jump_statistic);

        iv_jump_homepage.setOnClickListener(this);
        iv_jump_statistic.setOnClickListener(this);

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView_show_book);

        // 设置 GridLayoutManager，每行显示 3 张图片
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // 准备图片数据
        List<Integer> imageList = Arrays.asList(
                R.drawable.book_cet_4,
                R.drawable.book_cet_6,
                R.drawable.book_post_graduate,
                R.drawable.book_senior,
                R.drawable.book_favourite
        );

        // 设置适配器
        imageAdapter = new com.example.myapp.ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new com.example.myapp.ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(BookShellActivity.this, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image_resource_id", imageList.get(position));
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.iv_jump_homepage){
            Intent intent = new Intent();
            intent.setClass(BookShellActivity.this, MainMenuActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        } else if (v.getId()==R.id.iv_jump_statistic) {
            Intent intent = new Intent();
            intent.setClass( BookShellActivity.this, StatisticsActivity.class);//class为跳转对象
            startActivity(intent);
            finish();
        }
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
package com.example.easyword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyword.adapter.SearchWordAdapter;
import com.example.easyword.dao.FavouriteWordDao;
import com.example.easyword.dao.LearnWordDao;
import com.example.easyword.dao.ReviewWordDao;
import com.example.easyword.dao.TotalWordDao;
import com.example.easyword.dao.UserDao;
import com.example.easyword.dao.UserProgressDao;
import com.example.easyword.enity.LearnWord;
import com.example.easyword.enity.TotalWord;
import com.example.easyword.enity.UserProgress;
import com.example.easyword.util.ToastUtil;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_book_detail_image;
    Button btn_choose_book;
    ListView list_word;
    TotalWordDao dao;
    UserDao user_dao;
    FavouriteWordDao fav_dao;
    UserProgressDao progressDao;
    private TextView tv_book_detail,tv_book_detail_num;
    String []book_name = {"CET-4","CET-6","考研单词","高中单词","我的喜欢"};
    List<TotalWord> list;//对应词书的单词合集
    MyApplication application;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_book_detail);
        iv_book_detail_image = findViewById(R.id.iv_book_detail_image);
        btn_choose_book = findViewById(R.id.btn_choose_book);
        list_word = findViewById(R.id.list_word);
        tv_book_detail = findViewById(R.id.tv_book_detail);
        tv_book_detail_num = findViewById(R.id.tv_book_detail_num);

        application = MyApplication.getInstance();
        dao = application.getLearnWordDatabase().totalWordDao();
        user_dao = application.getLearnWordDatabase().userDao();
        fav_dao = application.getLearnWordDatabase().favouriteWordDao();
        progressDao = application.getLearnWordDatabase().userProgressDao();

        btn_choose_book.setOnClickListener(this);

        BookDetailshow();

        list_word.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(BookDetailActivity.this, WordDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedWord",list.get(position).getWord());
            bundle.putString("selectedWordMeaning",list.get(position).getMeaning());
            bundle.putString("selectedWordSentence",list.get(position).getSentence());
            bundle.putString("selectedWordSentenceMeaning",list.get(position).getSentence_meaning());
            intent.putExtras(bundle);
            startActivity(intent);// 启动目标Activity
        });
    }
    private void BookDetailshow(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        position = bundle.getInt("position");//获取名字

        //Log.d("kun","posotion"+position);

        StringBuilder search = new StringBuilder();//拼接searchPartten

        int imageResourceId = getIntent().getIntExtra("image_resource_id", -1);
        tv_book_detail.setText(book_name[position]);
        if (imageResourceId != -1) {
            iv_book_detail_image.setImageResource(imageResourceId);
        } else {
            ToastUtil.show(this,"No image resource ID received");
        }
        for(int i=0;i<6;i++){//拼接searchPartten
            if(i==position){
                search.append("1");
            }else{
                search.append("_");
            }
        }
        if(position<4){
           // Log.d("kun123","p="+position+" s="+search.toString());
            list = dao.SearchBookkind(search.toString());//将search转化为字符型
        }else {
            list = fav_dao.SearchAllWord();
        }
        tv_book_detail_num.setText("共有"+list.size()+"词");//展示当前词书共有多少单词
        SearchWordAdapter adapter = new SearchWordAdapter(this,list);
        list_word.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_choose_book){
            ChooseBook();
        }
    }
    private void ChooseBook(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("确定要选择此词书进行背诵吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reload();//对learn_word review_word进行重新加载
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private void Reload(){
        LearnWordDao learn_dao = MyApplication.getInstance().getLearnWordDatabase().learnWordDao();
        ReviewWordDao review_dao = MyApplication.getInstance().getLearnWordDatabase().reviewWordDao();
        learn_dao.DeleteAll();//清空两个数据库
        review_dao.DeleteAll();
        int i ;
        for(i=0;i<list.size();i++){
            Log.d("kun123","i="+i+list.get(i).getWord());
            LearnWord word = new LearnWord(list.get(i).getWord(),list.get(i).getMeaning(),
                    list.get(i).getSentence(),list.get(i).getSentence_meaning(),list.get(i).getDifficulty());
            learn_dao.Insert(word);
        }
        progressDao.UpdateBook(MyApplication.getInstance().login_user.getAccount(),0,position+1, list.size());
        ToastUtil.show(this,"添加成功，快来学习吧");
        finish();
    }
}
package com.example.easyword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyword.dao.UserDao;
import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.User;
import com.example.easyword.util.ToastUtil;
import com.example.easyword.util.ViewUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDao userDao;
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private CheckBox ck_save_password;
    private TextView tv_sign_up;
    private SharedPreferences login_preferences;//是否保存密码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        ck_save_password = findViewById(R.id.ck_save_password);
        tv_sign_up = findViewById(R.id.tv_sign_up);

        userDao = MyApplication.getInstance().getLearnWordDatabase().userDao();
        btn_login.setOnClickListener(this);
        tv_sign_up.setOnClickListener(this);

        View rootView = findViewById(R.id.root_login_view); // 根布局的 ID
        rootView.setOnTouchListener(new View.OnTouchListener() {//点击非EditText的其他部分隐藏键盘
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewUtil.HideOneInputMethod(LoginActivity.this);//Util内函数
                return false;
            }
        });

        login_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        roload();

    }
    private void Login(){
        String login_accout;
        String login_password;
        login_accout = et_account.getText().toString();
        login_password = et_password.getText().toString();
        if(login_accout.equals("")||login_password.equals("")){
            ToastUtil.show(this,"请输入账号和密码");
        }else{
            User user = userDao.LoginCheck(login_accout);
            if(user == null){
                ToastUtil.show(this,"输入账号有错误");
            }else if(!user.getPassword().equals(login_password)){
                ToastUtil.show(this,"输入密码有错误");
            }else {
                Login_success(user);
            }
        }
    }
    private void Login_success(User login_user){
        MyApplication.getInstance().login_user = login_user;//Myapplication获取当前登录用户
        SavePassword();
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,MainMenuActivity.class);//class为跳转对象
        startActivity(intent);
        ToastUtil.show(this,"欢迎"+login_user.getNickname());
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_login){
            Login();
        } else if (v.getId()== R.id.tv_sign_up) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,SignUpActivity.class);//class为跳转对象
            startActivity(intent);
        }
    }

    private void roload(){//根据SharedPreferences确定是否显示密码
        String account = login_preferences.getString("user_account","");
        et_account.setText(account);
        boolean is_saved = login_preferences.getBoolean("save_password",false);
        if(is_saved){
            String password = login_preferences.getString("user_password","");
            et_password.setText(password);
        }
        ck_save_password.setChecked(is_saved);
    }
    private void SavePassword(){//使用SharedPreferences保存密码
        SharedPreferences.Editor editor = login_preferences.edit();
        editor.putString("user_account",et_account.getText().toString());
        editor.putString("user_password",et_password.getText().toString());
        editor.putBoolean("save_password",ck_save_password.isChecked());
        editor.commit();
    }
}

package com.example.easyword;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easyword.enity.User;
import com.example.easyword.util.ToastUtil;

public class UpdateActivity extends AppCompatActivity {

    private ImageView iv_photo;
    private EditText et_user_account;
    private EditText et_user_nickname;
    private EditText et_user_old_password;
    private EditText et_user_new_password;
    private EditText et_email;
    private Button btn_check;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        iv_photo = findViewById(R.id.iv_update_user_photo);
        et_user_account = findViewById(R.id.et_update_account);
        et_user_nickname = findViewById(R.id.et_update_nickname);
        et_user_old_password = findViewById(R.id.et_update_old_password);
        et_user_new_password = findViewById(R.id.et_update_new_password);
        et_email = findViewById(R.id.et_update_email);
        btn_check = findViewById(R.id.btn_update);
        user = MyApplication.getInstance().login_user;
        String base64Photo = user.getUser_photo();
        byte[] decodedBytes = Base64.decode(base64Photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        iv_photo.setImageBitmap(bitmap);
        et_user_old_password.setText(user.getPassword());
        et_email.setText(user.getEmail());
        et_user_nickname.setText(user.getNickname());
        et_user_account.setText(user.getAccount());
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(UpdateActivity.this,"修改成功");
                finish();
            }
        });
    }
}
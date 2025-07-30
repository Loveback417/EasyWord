package com.example.easyword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.easyword.dao.UserDao;
import com.example.easyword.dao.UserProgressDao;
import com.example.easyword.database.LearnWordDatabase;
import com.example.easyword.enity.User;
import com.example.easyword.enity.UserProgress;
import com.example.easyword.util.ToastUtil;
import com.example.easyword.util.ViewUtil;
import android.Manifest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    private EditText et_sign_up_useraccount;
    private EditText et_sign_up_userpassword;
    private EditText et_sign_up_nickname;
    private EditText et_sign_up_email;
    private ImageView iv_user_photo;
    private Button btn_sign_up;
    private UserDao userDao;
    private UserProgressDao progressDao;
    private Bitmap selectedAvatar;
    private static final int REQUEST_CODE_PERMISSIONS = 100; // 自定义权限请求码
    private static final int REQUEST_CODE_GALLERY = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private Uri photoUri;
    private File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        et_sign_up_useraccount = findViewById(R.id.et_sign_up_account);
        et_sign_up_userpassword = findViewById(R.id.et_sign_up_password);
        et_sign_up_nickname = findViewById(R.id.et_sign_up_nickname);
        et_sign_up_email = findViewById(R.id.et_sign_up_email);
        iv_user_photo = findViewById(R.id.iv_user_photo);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        btn_sign_up.setOnClickListener(this);
        iv_user_photo.setOnClickListener(this);
        userDao = LearnWordDatabase.getInstance(this).userDao();
        progressDao = LearnWordDatabase.getInstance(this).userProgressDao();

        View rootView = findViewById(R.id.root_sign_up_view); // 根布局的 ID
        rootView.setOnTouchListener(new View.OnTouchListener() {//点击非EditText的其他部分隐藏键盘
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewUtil.HideOneInputMethod(SignUpActivity.this);//Util内函数
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btn_sign_up){
            SignUp();
        }else{
            handleAvatarSelection();
        }
    }
    private boolean hasRealPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * 处理头像选择逻辑
     */
    private void handleAvatarSelection() {
        // 再次检查权限，确保在用户交互时权限是最新的
        if (!hasRealPermissions() || !EasyPermissions.hasPermissions(this,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 请求权限
            EasyPermissions.requestPermissions(
                    this,
                    "需要相机和存储权限来选择头像",
                    REQUEST_CODE_PERMISSIONS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
            return;
        }
        showAvatarSelectionDialog();
    }

    private void showAvatarSelectionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("选择头像来源")
                .setItems(new String[]{"从相册选择"}, (dialog, which) -> {
                    if (which == 0) openGallery();
                })
                .show();
    }
    /**
     * 打开相册
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        } else {
            ToastUtil.show(this, "无法打开相册");
        }
    }
    /**
     * 处理从相册选择的结果
     */
    private void handleGalleryResult(Intent data) {
        try {
            Uri uri = data.getData();
            selectedAvatar = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            iv_user_photo.setImageBitmap(selectedAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(this, "无法加载图片");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("kun123", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                handleGalleryResult(data);
            }
        }
    }
    private void SignUp(){
        Log.d("kun1111","68");
        User user = new User();
        UserProgress progress;
        Log.d("kun1111","70");
        user = userDao.LoginCheck(et_sign_up_useraccount.getText().toString());
        Log.d("kun1111","71");
        if(user != null){
            ToastUtil.show(this,"该账号以存在，请更换账号");
        }else{
            Log.d("kun1111","69");
            user = new User();
            user.setAccount(et_sign_up_useraccount.getText().toString());
            user.setPassword(et_sign_up_userpassword.getText().toString());
            user.setNickname(et_sign_up_nickname.getText().toString());
            user.setEmail(et_sign_up_email.getText().toString());
            user.setLearn_days(0);
            user.setLearn_books(0);
            user.setLearn_minutes(0);
            user.setLearn_days(0);
            user.setCapacity((float)1.0);
            Log.d("kun1111","7");
            // 将Bitmap转换为Base64字符串
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedAvatar.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            // 将字节数组转换为 Base64 字符串
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            user.setUser_photo(base64Image);
            Log.d("kun1111","88");
            progress = new UserProgress();
            progress.setAccount(et_sign_up_useraccount.getText().toString());
            progress.setCurrent_learn(0);
            progress.setLearn_count(0);
            progress.setLearn_situation("000000");
            progress.setTotal_count(0);
            progressDao.Insert(progress);
            userDao.SignUp(user);
            ToastUtil.show(this,"注册成功");
            finish();
            Log.d("kun1111","888");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            showAvatarSelectionDialog();
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // 检查是否有权限被永久拒绝
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                // 用户永久拒绝了权限，引导去设置
//                new AppSettingsDialog.Builder(this)
//                        .setTitle("需要权限")
//                        .setRationale("我们需要相机和存储权限来选择头像")
//                        .setPositiveButton("去设置")
//                        .setNegativeButton("取消")
//                        .build()
//                        .show();
            } else {
                // 用户拒绝了权限，但没有永久拒绝，可以提示用户
                ToastUtil.show(this, "需要权限才能选择头像");
            }
        }
    }
}
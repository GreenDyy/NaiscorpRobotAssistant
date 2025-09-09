package com.naiscorp.robotapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.ui.home.HomeActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Thiết lập header với nút back
        setHeaderTitle("Cài đặt");
        showBackButton(true);
        
        // Thiết lập click listeners cho các card
        setupCardClickListeners();
    }
    
    private void setupCardClickListeners() {
        // Card Tài khoản
        findViewById(R.id.cardAccount).setOnClickListener(v -> {
            Toast.makeText(this, "Mở cài đặt tài khoản", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình cài đặt tài khoản
        });
        
        // Card Cài đặt Robot
        findViewById(R.id.cardRobot).setOnClickListener(v -> {
            Toast.makeText(this, "Mở cài đặt Robot", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình cài đặt Robot
        });
        
        // Card Thông báo
        findViewById(R.id.cardNotification).setOnClickListener(v -> {
            Toast.makeText(this, "Mở cài đặt thông báo", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình cài đặt thông báo
        });
        
        // Card Ngôn ngữ
        findViewById(R.id.cardLanguage).setOnClickListener(v -> {
            Toast.makeText(this, "Mở cài đặt ngôn ngữ", Toast.LENGTH_SHORT).show();
            // TODO: Mở dialog chọn ngôn ngữ
        });
        
        // Card Chủ đề
        findViewById(R.id.cardTheme).setOnClickListener(v -> {
            Toast.makeText(this, "Mở cài đặt chủ đề", Toast.LENGTH_SHORT).show();
            // TODO: Mở dialog chọn chủ đề
        });
        
        // Card Giới thiệu
        findViewById(R.id.cardAbout).setOnClickListener(v -> {
            Toast.makeText(this, "Mở thông tin ứng dụng", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình giới thiệu
        });
        
        // Nút đăng xuất
        findViewById(R.id.cardLogout).setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
            // TODO: Xử lý đăng xuất
            // Intent intent = new Intent(this, LoginActivity.class);
            // startActivity(intent);
            // finish();
        });
    }
}

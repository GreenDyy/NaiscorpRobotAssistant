package com.naiscorp.robotapp.ui.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.ui.checkin.CheckInActivity;
import com.naiscorp.robotapp.ui.map.MapActivity;
import com.naiscorp.robotapp.ui.settings.SettingsActivity;

/**
 * Activity test để kiểm tra breadcrumb functionality
 */
public class BreadcrumbTestActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadcrumb_test);
        
        setHeaderTitle("Breadcrumb Test");
        setSubTitle("Test Navigation");
        showLeftButton();
        
        // Nhận breadcrumb từ Intent
        setBreadcrumbFromIntent(getIntent());
        
        setupButtons();
    }
    
    private void setupButtons() {
        Button btnGoToMap = findViewById(R.id.btnGoToMap);
        Button btnGoToCheckIn = findViewById(R.id.btnGoToCheckIn);
        Button btnGoToSettings = findViewById(R.id.btnGoToSettings);
        Button btnAddBreadcrumb = findViewById(R.id.btnAddBreadcrumb);
        Button btnResetBreadcrumb = findViewById(R.id.btnResetBreadcrumb);
        
        btnGoToMap.setOnClickListener(v -> {
            goToScreen("Bản đồ");
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
            startActivity(intent);
        });
        
        btnGoToCheckIn.setOnClickListener(v -> {
            goToScreen("Check-in");
            Intent intent = new Intent(this, CheckInActivity.class);
            intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
            startActivity(intent);
        });
        
        btnGoToSettings.setOnClickListener(v -> {
            goToScreen("Cài đặt");
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
            startActivity(intent);
        });
        
        btnAddBreadcrumb.setOnClickListener(v -> {
            goToScreen("Test Page");
            Toast.makeText(this, "Đã thêm breadcrumb", Toast.LENGTH_SHORT).show();
        });
        
        btnResetBreadcrumb.setOnClickListener(v -> {
            resetBreadcrumb();
            Toast.makeText(this, "Đã reset breadcrumb", Toast.LENGTH_SHORT).show();
        });
    }
}

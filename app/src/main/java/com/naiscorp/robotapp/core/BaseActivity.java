package com.naiscorp.robotapp.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.ui.settings.SettingsActivity;

public class BaseActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView btnBack;
    private ImageView btnSettings;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutHeader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);
        btnSettings = findViewById(R.id.btnSettings);
        container = findViewById(R.id.container);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, container, true);
    }

    // Các hàm tiện ích cho header
    protected void setHeaderTitle(String title) {
        tvTitle.setText(title);
    }

    protected void showBackButton(boolean show) {
        btnBack.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}

package com.naiscorp.robotapp.core;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.ui.settings.SettingsActivity;

import java.io.InputStream;

public class BaseActivity extends AppCompatActivity {

    private TextView tvTitle;
    private FrameLayout container;

    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutHeader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleClick();
            }
        });
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        container = findViewById(R.id.container);
        imgLogo = findViewById(R.id.imgLogo);
        try {
            InputStream is = getAssets().open("icons/icon_plane.png");
            Drawable drawable = Drawable.createFromStream(is, null);
            imgLogo.setImageDrawable(drawable);
        } catch (Exception e) {

        }
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, container, true);
    }

    // Các hàm tiện ích cho header
    protected void setHeaderTitle(String title) {
        tvTitle.setText(title);
    }
    
    // Method để các Activity con có thể override
    protected  void onTitleClick() {
//        Toast.makeText(this, "đây là Default", Toast.LENGTH_SHORT).show();
    }
}


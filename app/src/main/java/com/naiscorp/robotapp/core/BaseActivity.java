package com.naiscorp.robotapp.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private TextView tvSubTitle, tvTitle, tvTime, tvTemperature, tvPin;
    private Button btnLeft, btnRight;
    private FrameLayout container;

    private ImageView imgLogo;

    private Handler timeHandler;
    private Runnable timeRunnable;
    private SimpleDateFormat timeFormat;

    private BroadcastReceiver batteryReceiver;


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
        initListeners();
        initTimeDisplay();
        initBatteryDisplay();

    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        container = findViewById(R.id.container);
        imgLogo = findViewById(R.id.imgLogo);
        tvTime = findViewById(R.id.tvTime);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvPin = findViewById(R.id.tvPin);
        //button
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        // mặc định ẩn 2 nút này đi nhe
        hideLeftButton();
        hideRightButton();

        try {
            InputStream is = getAssets().open("icons/icon_plane.png");
            Drawable drawable = Drawable.createFromStream(is, null);
            imgLogo.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        } try {
            InputStream is = getAssets().open("icons/icon_plane.png");
            Drawable drawable = Drawable.createFromStream(is, null);
            imgLogo.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private void initListeners() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleClick();
            }
        });

        btnLeft.setOnClickListener(v->{
            onBtnLeftClick();
        });

        btnRight.setOnClickListener(v->{
            onBtnRightClick();
        });

    }

    private void initTimeDisplay() {
        timeHandler = new Handler(Looper.getMainLooper());
        timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                timeHandler.postDelayed(this, 1000); // Cập nhậtl mỗi giây
            }
        };

        // Bắt đầu cập nhật thời gian
        timeHandler.post(timeRunnable);
    }

    private void updateTime() {
        if (tvTime != null) {
            String currentTime = timeFormat.format(new Date());
            tvTime.setText(currentTime);
        }
    }

    private void initBatteryDisplay() {
        // Lấy thông tin pin ban đầu
        updateBatteryLevel();

        // Tạo BroadcastReceiver để theo dõi thay đổi pin
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    updateBatteryLevel();
                }
            }
        };

        // Đăng ký BroadcastReceiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void updateBatteryLevel() {
        if (tvPin != null) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            if (batteryManager != null) {
                int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                tvPin.setText(batteryLevel + "%");
            }
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

    protected void setSubTitle(String text) {
        tvSubTitle.setText(text);
    }

    // Method để các Activity con có thể override
    protected void onTitleClick() {
//        Toast.makeText(this, "đây là Default", Toast.LENGTH_SHORT).show();
    }

    protected void onBtnLeftClick() {
        finish();
    }

    protected void onBtnRightClick() {
        Toast.makeText(this, "đây là nút Right Default", Toast.LENGTH_SHORT).show();
    }

    protected void showLeftButton() {
        btnLeft.setVisibility(View.VISIBLE);
    }

    protected void hideLeftButton() {
        btnLeft.setVisibility(View.GONE);
    }

    protected void showRightButton() {
        btnRight.setVisibility(View.VISIBLE);
    }

    protected void hideRightButton() {
        btnRight.setVisibility(View.GONE);
    }

    protected void setRightButtonText(String text) {
        btnRight.setText(text);
    }

    protected void setLeftButtonText(String text) {
        btnLeft.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng cập nhật thời gian khi Activity bị destroy
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
        // Hủy đăng ký BroadcastReceiver
        if (batteryReceiver != null) {
            try {
                unregisterReceiver(batteryReceiver);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error unregistering battery receiver: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tạm dừng cập nhật thời gian khi Activity bị pause
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tiếp tục cập nhật thời gian khi Activity resume
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.post(timeRunnable);
        }
    }
}


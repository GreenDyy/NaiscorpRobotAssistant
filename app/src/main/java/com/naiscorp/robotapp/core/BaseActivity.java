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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.ui.checkin.CheckInActivity;
import com.naiscorp.robotapp.ui.home.HomeActivity;
import com.naiscorp.robotapp.ui.map.MapActivity;
import com.naiscorp.robotapp.ui.playground.PlayGroundActivity;
import com.naiscorp.robotapp.ui.settings.SettingsActivity;
import com.naiscorp.robotapp.ui.demo.BreadcrumbTestActivity;
import com.naiscorp.robotapp.utils.AssetUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    //menu
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ImageView imgMenu;

    // Avatar assistant components
    protected ImageView imgAvatar;
    protected TextView tvName, tvStatus;

    private TextView tvBreadcrumb;
    private List<String> breadcrumbList = new ArrayList<>();
    private OnBackPressedCallback onBackPressedCallback;


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
        initDrawer();

        //breadcrumb
        tvBreadcrumb = findViewById(R.id.tvBreadcrumb);
        
        // Thiết lập click listener cho breadcrumb
        if (tvBreadcrumb != null) {
            tvBreadcrumb.setOnClickListener(v -> {
                onBreadcrumbClick();
            });
        }

        // Bắt đầu với màn Home
        if (breadcrumbList.isEmpty()) {
            breadcrumbList.add("Home");
        }
        updateBreadcrumb();
        
        // Thiết lập OnBackPressedCallback
        setupOnBackPressedCallback();

    }

    private void updateBreadcrumb() {
        if (tvBreadcrumb != null) {
            String breadcrumb = TextUtils.join(" > ", breadcrumbList);
            tvBreadcrumb.setText(breadcrumb);
        }
    }

    // Thêm màn hình mới vào breadcrumb
    public void goToScreen(String screenName) {
        logBreadcrumb("goToScreen - Before adding: " + screenName);
        // Không thêm duplicate
        if (breadcrumbList.isEmpty() || !breadcrumbList.get(breadcrumbList.size() - 1).equals(screenName)) {
            breadcrumbList.add(screenName);
            updateBreadcrumb();
            logBreadcrumb("goToScreen - After adding: " + screenName);
        } else {
            Log.d(TAG, "goToScreen - Duplicate screen, not adding: " + screenName);
        }
    }

    // Lấy breadcrumb list để truyền qua Intent
    public List<String> getBreadcrumbList() {
        return new ArrayList<>(breadcrumbList);
    }

    // Thiết lập breadcrumb từ Intent
    public void setBreadcrumbFromIntent(Intent intent) {
        if (intent != null && intent.hasExtra("breadcrumb")) {
            String[] breadcrumbArray = intent.getStringArrayExtra("breadcrumb");
            if (breadcrumbArray != null) {
                breadcrumbList.clear();
                for (String item : breadcrumbArray) {
                    breadcrumbList.add(item);
                }
                updateBreadcrumb();
            }
        }
    }

    // Reset breadcrumb về Home
    public void resetBreadcrumb() {
        breadcrumbList.clear();
        breadcrumbList.add("Home");
        updateBreadcrumb();
    }

    // Xử lý click vào breadcrumb
    protected void onBreadcrumbClick() {
        // Mặc định: quay về Home
        if (breadcrumbList.size() > 1) {
            resetBreadcrumb();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    // Method tiện ích để debug breadcrumb
    protected void logBreadcrumb(String action) {
        Log.d(TAG, action + " - Breadcrumb: " + TextUtils.join(" > ", breadcrumbList));
    }

    // Thiết lập OnBackPressedCallback để thay thế onBackPressed() deprecated
    private void setupOnBackPressedCallback() {
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logBreadcrumb("OnBackPressedCallback - Before");
                if (breadcrumbList.size() > 1) {
                    // Xóa màn hình hiện tại khỏi breadcrumb
                    breadcrumbList.remove(breadcrumbList.size() - 1);
                    updateBreadcrumb();
                    logBreadcrumb("OnBackPressedCallback - After remove");
                    
                    // Navigate về màn hình trước đó
                    navigateToPreviousScreen();
                } else {
                    // Nếu chỉ có Home, thoát app
                    finish();
                }
            }
        };
        
        // Đăng ký callback
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    // Navigate về màn hình trước đó dựa trên breadcrumb
    private void navigateToPreviousScreen() {
        if (breadcrumbList.size() > 0) {
            String previousScreen = breadcrumbList.get(breadcrumbList.size() - 1);
            Intent intent = null;
            
            switch (previousScreen) {
                case "Home":
                    intent = new Intent(this, HomeActivity.class);
                    break;
                case "Cài đặt":
                    intent = new Intent(this, SettingsActivity.class);
                    break;
                case "Vùng test":
                    intent = new Intent(this, PlayGroundActivity.class);
                    break;
                case "Bản đồ":
                    intent = new Intent(this, MapActivity.class);
                    break;
                case "Check-in":
                    intent = new Intent(this, CheckInActivity.class);
                    break;
                case "Breadcrumb Test":
                    intent = new Intent(this, BreadcrumbTestActivity.class);
                    break;
                default:
                    // Nếu không biết màn hình nào, quay về Home
                    intent = new Intent(this, HomeActivity.class);
                    break;
            }
            
            if (intent != null) {
                intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                startActivity(intent);
                finish(); // Đóng màn hình hiện tại
            }
        }
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

        // Drawer components
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMemu);

        // Avatar assistant components
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imgAvatarRobot);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvStatus = navigationView.getHeaderView(0).findViewById(R.id.tvStatus);

        AssetUtils.loadImageFromAssets(BaseActivity.this, imgLogo, "icons/icon_plane.png");
        AssetUtils.loadImageFromAssets(BaseActivity.this, imgAvatar, "icons/icon_plane.png");
    }

    private void initListeners() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleClick();
            }
        });

        btnLeft.setOnClickListener(v -> {
            onBtnLeftClick();
        });

        btnRight.setOnClickListener(v -> {
            onBtnRightClick();
        });

        // Drawer menu click listener
        imgMenu.setOnClickListener(v -> {
            toggleDrawer();
        });

        // Avatar assistant click listener
        if (imgAvatar != null) {
            imgAvatar.setOnClickListener(v -> {
                onAvatarClick();
            });
        }

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

    private void initDrawer() {
        if (drawerLayout != null && navigationView != null) {
            // Thiết lập NavigationView listener
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    Intent intent;
                    switch (id) {
                        case R.id.nav_home:
                            closeDrawer();
                            // Kiểm tra xem có đang ở HomeActivity không
                            if (!(BaseActivity.this instanceof HomeActivity)) {
                                // Reset breadcrumb về Home
                                resetBreadcrumb();
                                intent = new Intent(BaseActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case R.id.nav_setting:
                            closeDrawer();
                            // Kiểm tra xem có đang ở SettingsActivity không
                            if (!(BaseActivity.this instanceof SettingsActivity)) {
                                goToScreen("Cài đặt");
                                intent = new Intent(BaseActivity.this, SettingsActivity.class);
                                intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                                startActivity(intent);
                            }
                            break;
                        case R.id.nav_playground:
                            closeDrawer();
                            // Kiểm tra xem có đang ở PlayGroundActivity không
                            if (!(BaseActivity.this instanceof PlayGroundActivity)) {
                                goToScreen("Vùng test");
                                intent = new Intent(BaseActivity.this, PlayGroundActivity.class);
                                intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                                startActivity(intent);
                            }
                            break;
                        case R.id.nav_breadcrumb_test:
                            closeDrawer();
                            // Kiểm tra xem có đang ở BreadcrumbTestActivity không
                            if (!(BaseActivity.this instanceof BreadcrumbTestActivity)) {
                                goToScreen("Breadcrumb Test");
                                intent = new Intent(BaseActivity.this, BreadcrumbTestActivity.class);
                                intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                                startActivity(intent);
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
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
        logBreadcrumb("onBtnLeftClick - Before");
        // Sử dụng breadcrumb navigation thay vì finish()
        if (breadcrumbList.size() > 1) {
            // Xóa màn hình hiện tại khỏi breadcrumb
            breadcrumbList.remove(breadcrumbList.size() - 1);
            updateBreadcrumb();
            logBreadcrumb("onBtnLeftClick - After remove");
            
            // Navigate về màn hình trước đó
            navigateToPreviousScreen();
        } else {
            // Nếu chỉ có Home, thì finish
            finish();
        }
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

    // Avatar assistant methods
    protected void setAssistantName(String name) {
        if (tvName != null) {
            tvName.setText(name);
        }
    }

    protected void setAssistantStatus(String status) {
        if (tvStatus != null) {
            tvStatus.setText(status);
        }
    }

    protected void setAssistantAvatar(Drawable drawable) {
        if (imgAvatar != null) {
            imgAvatar.setImageDrawable(drawable);
        }
    }

    protected void onAvatarClick() {
        // Mặc định: hiển thị thông tin assistant
        Toast.makeText(this, "Assistant - Sẵn sàng hỗ trợ!", Toast.LENGTH_SHORT).show();
    }

    // Drawer methods
    protected void toggleDrawer() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

    protected void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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
        // Cleanup OnBackPressedCallback
        if (onBackPressedCallback != null) {
            onBackPressedCallback.remove();
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
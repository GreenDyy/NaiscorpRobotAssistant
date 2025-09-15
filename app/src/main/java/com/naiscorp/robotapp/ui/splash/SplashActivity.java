package com.naiscorp.robotapp.ui.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.naiscorp.robotapp.MyApplication;
import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 100; //số định danh của request per
    private static final long SPLASH_DELAY = 2500; // 2.5s để có thời gian cho animation
    private boolean permissionsGranted = false;
    private boolean splashTimeFinished = false;
    
    // UI Components
    private ImageView ivLogo;
    private TextView tvAppName;
    private TextView tvAppSubtitle;
    private TextView tvLoading;
    private ProgressBar pbLoading;
    private LinearLayout llLogoSection;

    // Danh sách quyền nguy hiểm cần xin
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize UI components
        initViews();
        
        // Start animations
        startSplashAnimations();

        // Countdown splash time
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashTimeFinished = true;
            tryStartApp();
        }, SPLASH_DELAY);

        // Check quyền
        if (hasAllPermissions()) {
            permissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }
    
    private void initViews() {
        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tvAppSubtitle = findViewById(R.id.tvAppSubtitle);
        tvLoading = findViewById(R.id.tvLoading);
        pbLoading = findViewById(R.id.pbLoading);
        llLogoSection = findViewById(R.id.llLogoSection);
        
        // Initially hide elements for animation
        ivLogo.setAlpha(0f);
        tvAppName.setAlpha(0f);
        tvAppSubtitle.setAlpha(0f);
        tvLoading.setAlpha(0f);
        pbLoading.setAlpha(0f);
    }
    
    private void startSplashAnimations() {
        // Logo animation - scale and fade in
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.5f, 1f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.5f, 1f);
        ObjectAnimator logoAlpha = ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f);
        
        AnimatorSet logoAnimator = new AnimatorSet();
        logoAnimator.playTogether(logoScaleX, logoScaleY, logoAlpha);
        logoAnimator.setDuration(800);
        logoAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        
        // App name animation - slide up and fade in
        ObjectAnimator nameTranslationY = ObjectAnimator.ofFloat(tvAppName, "translationY", 50f, 0f);
        ObjectAnimator nameAlpha = ObjectAnimator.ofFloat(tvAppName, "alpha", 0f, 1f);
        
        AnimatorSet nameAnimator = new AnimatorSet();
        nameAnimator.playTogether(nameTranslationY, nameAlpha);
        nameAnimator.setDuration(600);
        nameAnimator.setStartDelay(300);
        
        // Subtitle animation - slide up and fade in
        ObjectAnimator subtitleTranslationY = ObjectAnimator.ofFloat(tvAppSubtitle, "translationY", 30f, 0f);
        ObjectAnimator subtitleAlpha = ObjectAnimator.ofFloat(tvAppSubtitle, "alpha", 0f, 1f);
        
        AnimatorSet subtitleAnimator = new AnimatorSet();
        subtitleAnimator.playTogether(subtitleTranslationY, subtitleAlpha);
        subtitleAnimator.setDuration(500);
        subtitleAnimator.setStartDelay(600);
        
        // Loading elements animation
        ObjectAnimator loadingAlpha = ObjectAnimator.ofFloat(tvLoading, "alpha", 0f, 1f);
        ObjectAnimator progressAlpha = ObjectAnimator.ofFloat(pbLoading, "alpha", 0f, 1f);
        
        AnimatorSet loadingAnimator = new AnimatorSet();
        loadingAnimator.playTogether(loadingAlpha, progressAlpha);
        loadingAnimator.setDuration(400);
        loadingAnimator.setStartDelay(1000);
        
        // Start all animations
        logoAnimator.start();
        nameAnimator.start();
        subtitleAnimator.start();
        loadingAnimator.start();
        
        // Add pulsing effect to logo
        startLogoPulseAnimation();
    }
    
    private void startLogoPulseAnimation() {
        ObjectAnimator pulseScaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 1.05f, 1f);
        ObjectAnimator pulseScaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 1.05f, 1f);
        
        pulseScaleX.setRepeatCount(ObjectAnimator.INFINITE);
        pulseScaleY.setRepeatCount(ObjectAnimator.INFINITE);
        pulseScaleX.setRepeatMode(ObjectAnimator.REVERSE);
        pulseScaleY.setRepeatMode(ObjectAnimator.REVERSE);
        
        AnimatorSet pulseAnimator = new AnimatorSet();
        pulseAnimator.playTogether(pulseScaleX, pulseScaleY);
        pulseAnimator.setDuration(2000);
        pulseAnimator.setStartDelay(1500);
        
        pulseAnimator.start();
    }

    private boolean hasAllPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void tryStartApp() {
        if (permissionsGranted && splashTimeFinished) {
            Log.d(TAG, "✅ Toàn bộ quyền cần thiết đã được cấp thành công, tiến hành initSDK và các thứ khác, ... ================================");
            // Init SDK
            MyApplication app = (MyApplication) getApplication();
            app.initSDK();
            app.initMqtt();

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (hasAllPermissions()) {
                permissionsGranted = true;
                tryStartApp();
            } else {
                Toast.makeText(this, "Ứng dụng cần cấp đủ quyền để hoạt động!", Toast.LENGTH_LONG).show();
                finish(); // Đóng app nếu thiếu quyền
            }
        }
    }
}

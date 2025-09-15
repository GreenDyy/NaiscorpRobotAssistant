package com.naiscorp.robotapp.ui.playground;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.keenon.sdk.component.runtime.PeanutRuntime;
import com.keenon.sdk.external.PeanutSDK;
import com.keenon.sdk.hedera.model.ApiError;
import com.keenon.sdk.robot.ApiCallback;
import com.keenon.sdk.robot.base.BaseResp;
import com.keenon.sdk.robot.model.bean.battery.BatteryInfoBean;
import com.keenon.sdk.robot.model.bean.navigation.NavigationStatusBean;
import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;

public class PlayGroundActivity extends BaseActivity {
    private static final String TAG = PlayGroundActivity.class.getSimpleName();
    private EditText etTargetId, etApproximateTime;
    private Button btnBatteryStatus;
    private Button btnStartNavigation, btnStopNavigation, btnPauseNavigation, btnResumeNavigation, btnGetNavigationStatus;
    private Button btnClearLog;
    private TextView tvLogOutput;
    private ScrollView scrollViewLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ground);

        // Nhận breadcrumb từ Intent
        setBreadcrumbFromIntent(getIntent());

        //setUpBase
        setHeaderTitle("Vùng test");
        setSubTitle("Testt");
        showLeftButton();

        initView();
        initListener();
    }

    private void initView() {
        etTargetId = findViewById(R.id.etTargetId);
        etApproximateTime = findViewById(R.id.etApproximateTime);
        //default value approximateTimeStr = 1
        etApproximateTime.setText("1");
        
        // Khởi tạo các nút navigation
        btnStartNavigation = findViewById(R.id.btnNavStart);
        btnStopNavigation = findViewById(R.id.btnNavStop);
        btnPauseNavigation = findViewById(R.id.btnNavPause);
        btnResumeNavigation = findViewById(R.id.btnNavResume);
        btnGetNavigationStatus = findViewById(R.id.btnStatus2); // Sử dụng btnStatus2 cho get navigation status
        
        // Khởi tạo nút battery
        btnBatteryStatus = findViewById(R.id.btnBatteryStatus);
        
        // Khởi tạo log components
        btnClearLog = findViewById(R.id.btnClearLog);
        tvLogOutput = findViewById(R.id.tvLogOutput);
        scrollViewLog = findViewById(R.id.scrollViewLog);
    }

    private void initListener() {
        // Navigation buttons
        btnStartNavigation.setOnClickListener(v -> startNavigation());
        btnStopNavigation.setOnClickListener(v -> stopNavigation());
        btnPauseNavigation.setOnClickListener(v -> pauseNavigation());
        btnResumeNavigation.setOnClickListener(v -> resumeNavigation());
        btnGetNavigationStatus.setOnClickListener(v -> getNavigationStatus());
        
        // Battery button
        btnBatteryStatus.setOnClickListener(v -> getBattery());
        
        // Log button
        btnClearLog.setOnClickListener(v -> clearLog());
    }

    private void startNavigation() {
        String targetIdStr = etTargetId.getText().toString().trim();
        String approximateTimeStr = etApproximateTime.getText().toString().trim();

        if (targetIdStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Target ID", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int targetId = Integer.parseInt(targetIdStr);
            int approximateTime = approximateTimeStr.isEmpty() ? 1 : Integer.parseInt(approximateTimeStr);

            Log.d(TAG, "=== BẮT ĐẦU NAVIGATION ===");
            Log.d(TAG, "Target ID: " + targetId);
            Log.d(TAG, "Approximate Time: " + approximateTime);
            
            addLog("=== BẮT ĐẦU NAVIGATION ===");
            addLog("Target ID: " + targetId);
            addLog("Approximate Time: " + approximateTime);

            if (PeanutSDK.getInstance() != null && PeanutSDK.getInstance().navigation() != null) {
                // Sử dụng PeanutSDK API để navigation
                PeanutSDK.getInstance().navigation().setTarget(targetId, approximateTime, new ApiCallback<BaseResp<String>>() {
                    @Override
                    public void onSuccess(BaseResp<String> response) {
                        Log.d(TAG, "Navigation đã bắt đầu thành công, res đây: " + response.getData());
                        addLog("✅ Navigation đã bắt đầu thành công: " + response.getData());
                    }

                    @Override
                    public void onSuccess(String requestId, BaseResp<String> result) {
                        onSuccess(result);
                    }

                    @Override
                    public void onFail(ApiError error) {
                        Log.e(TAG, "Lỗi navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                        addLog("❌ Lỗi navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                    }
                });
            } else {
                Log.e(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
                addLog("❌ PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Target ID phải là số nguyên", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Lỗi format số: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception khi start navigation: " + e.getMessage());
            Log.e(TAG, "Error starting navigation", e);
        }
    }

    private void stopNavigation() {
        Log.d(TAG, "=== DỪNG NAVIGATION ===");
        addLog("=== DỪNG NAVIGATION ===");

        try {
            if (PeanutSDK.getInstance() != null && PeanutSDK.getInstance().navigation() != null) {
                PeanutSDK.getInstance().navigation().stop(new ApiCallback<BaseResp<String>>() {
                    @Override
                    public void onSuccess(BaseResp<String> response) {
                        if (response != null && response.getCode() == 0) {
                            Log.d(TAG, "Navigation đã dừng thành công!");
                            Log.d(TAG, "Response: " + response.getData());
                        } else {
                            Log.e(TAG, "Dừng navigation thất bại: " + (response != null ? response.getMsg() : "Response null"));
                        }
                    }

                    @Override
                    public void onSuccess(String requestId, BaseResp<String> result) {
                        onSuccess(result);
                    }

                    @Override
                    public void onFail(ApiError error) {
                        Log.e(TAG, "Lỗi dừng navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                    }
                });
            } else {
                Log.e(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception khi stop navigation: " + e.getMessage());
            Log.e(TAG, "Error stopping navigation", e);
        }
    }

    private void pauseNavigation() {
        Log.d(TAG, "=== TẠM DỪNG NAVIGATION ===");

        try {
            if (PeanutSDK.getInstance() != null && PeanutSDK.getInstance().navigation() != null) {
                PeanutSDK.getInstance().navigation().pause(new ApiCallback<BaseResp<String>>() {
                    @Override
                    public void onSuccess(BaseResp<String> response) {
                        if (response != null && response.getCode() == 0) {
                            Log.d(TAG, "Navigation đã tạm dừng thành công!");
                            Log.d(TAG, "Response: " + response.getData());
                        } else {
                            Log.e(TAG, "Tạm dừng navigation thất bại: " + (response != null ? response.getMsg() : "Response null"));
                        }
                    }

                    @Override
                    public void onSuccess(String requestId, BaseResp<String> result) {
                        onSuccess(result);
                    }

                    @Override
                    public void onFail(ApiError error) {
                        Log.e(TAG, "Lỗi tạm dừng navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                    }
                });
            } else {
                Log.e(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception khi pause navigation: " + e.getMessage());
            Log.e(TAG, "Error pausing navigation", e);
        }
    }

    private void resumeNavigation() {
        Log.d(TAG, "=== TIẾP TỤC NAVIGATION ===");

        try {
            if (PeanutSDK.getInstance() != null && PeanutSDK.getInstance().navigation() != null) {
                PeanutSDK.getInstance().navigation().resume(new ApiCallback<BaseResp<String>>() {
                    @Override
                    public void onSuccess(BaseResp<String> response) {
                        if (response != null && response.getCode() == 0) {
                            Log.d(TAG, "Navigation đã tiếp tục thành công!");
                            Log.d(TAG, "Response: " + response.getData());
                        } else {
                            Log.e(TAG, "Tiếp tục navigation thất bại: " + (response != null ? response.getMsg() : "Response null"));
                        }
                    }

                    @Override
                    public void onSuccess(String requestId, BaseResp<String> result) {
                        onSuccess(result);
                    }

                    @Override
                    public void onFail(ApiError error) {
                        Log.e(TAG, "Lỗi tiếp tục navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                    }
                });
            } else {
                Log.e(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception khi resume navigation: " + e.getMessage());
            Log.e(TAG, "Error resuming navigation", e);
        }
    }

    private void getNavigationStatus() {
        Log.d(TAG, "=== LẤY TRẠNG THÁI NAVIGATION ===");

        try {
            if (PeanutSDK.getInstance() != null && PeanutSDK.getInstance().navigation() != null) {
                PeanutSDK.getInstance().navigation().getStatus(new ApiCallback<BaseResp<NavigationStatusBean>>() {
                    @Override
                    public void onSuccess(BaseResp<NavigationStatusBean> response) {
                        if (response != null && response.getCode() == 0 && response.getData() != null) {
                            NavigationStatusBean status = response.getData();
                            Log.d(TAG, "Lấy trạng thái navigation thành công!");
                            Log.d(TAG, "Status: " + status.toString());
                        } else {
                            Log.e(TAG, "Lấy trạng thái navigation thất bại: " + (response != null ? response.getMsg() : "Response null"));
                        }
                    }

                    @Override
                    public void onSuccess(String requestId, BaseResp<NavigationStatusBean> result) {
                        onSuccess(result);
                    }

                    @Override
                    public void onFail(ApiError error) {
                        Log.e(TAG, "Lỗi lấy trạng thái navigation - Code: " + error.getCode() + ", Message: " + error.getMsg());
                    }
                });
            } else {
                Log.e(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception khi get navigation status: " + e.getMessage());
            Log.e(TAG, "Error getting navigation status", e);
        }
    }

    //==BATTERY
    public void getBattery() {
        addLog("=== KIỂM TRA BATTERY ===");
        if(PeanutSDK.getInstance() != null && PeanutSDK.getInstance().battery() != null) {
        PeanutSDK.getInstance().battery().batteryInfo(new ApiCallback<BaseResp<BatteryInfoBean>>() {
            @Override
            public void onSuccess(BaseResp<BatteryInfoBean> result) {
                if (result != null && result.getData() != null) {
                    BatteryInfoBean battery = result.getData();
                    Log.d(TAG, "✅ Battery: " + battery.toString());
                    addLog("✅ Battery: " + battery.toString());
                    // Thêm thông tin chi tiết nếu có
                    try {
                        Log.d(TAG, "🔋 Battery class: " + battery.getClass().getSimpleName());
                        addLog("🔋 Battery class: " + battery.getClass().getSimpleName());
                    } catch (Exception e) {
                        Log.d(TAG, "🔋 Battery data received");
                        addLog("🔋 Battery data received");
                    }
                } else {
                    Log.d(TAG, "⚠️ No battery data received");
                    addLog("⚠️ No battery data received");
                }
            }

            @Override
            public void onSuccess(String requestId, BaseResp<BatteryInfoBean> result) {
                onSuccess(result);
            }

            @Override
            public void onFail(ApiError error) {
                Log.e(TAG, "❌ Battery info failed: " + error.toString());
                addLog("❌ Battery info failed: " + error.toString());
            }
        });

        int powerLevel = PeanutRuntime.getInstance().getRuntimeInfo().getPower();
        Log.d(TAG, "🔋 Runtime Power: " + powerLevel + "%");
            addLog("🔋 Runtime Power: " + powerLevel + "%");
        } else {
            Log.e(TAG, "PeanutSDK hoặc Battery component chưa được khởi tạo");
            addLog("❌ PeanutSDK hoặc Battery component chưa được khởi tạo");
        }
    }

    //=== LOG METHODS ===
    private void addLog(String message) {
        runOnUiThread(() -> {
            String timestamp = java.text.DateFormat.getTimeInstance().format(new java.util.Date());
            String logMessage = "[" + timestamp + "] " + message + "\n";
            tvLogOutput.append(logMessage);
            
            // Auto scroll to bottom
            scrollViewLog.post(() -> scrollViewLog.fullScroll(View.FOCUS_DOWN));
        });
    }

    private void clearLog() {
        tvLogOutput.setText("Log đã được xóa...\n");
    }
}
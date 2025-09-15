package com.naiscorp.robotapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.keenon.sdk.external.PeanutSDK;
import com.keenon.sdk.hedera.model.ApiError;
import com.keenon.sdk.robot.ApiCallback;
import com.keenon.sdk.robot.base.ApiTopic;
import com.keenon.sdk.robot.base.BaseResp;
import com.keenon.sdk.robot.model.bean.sensor.ObjectPerceptionBean;
import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.adapter.HomeCardRecyclerAdapter;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.model.HomeCard;
import com.naiscorp.robotapp.ui.checkin.CheckInActivity;
import com.naiscorp.robotapp.ui.map.MapActivity;
import com.naiscorp.robotapp.utils.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private Gson gson = new Gson();
    private RecyclerView recyclerViewCards;
    private HomeCardRecyclerAdapter cardAdapter;
    private List<HomeCard> cardList;
    private boolean isGrid = true;

    // type show
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Thiết lập header
        setHeaderTitle(getResources().getString(R.string.home_screen_name));

        // Override onClick cho title từ HomeActivity (sẽ dùng method override thay vì
        // setupTitleClickListener)

        // Khởi tạo RecyclerView và data
        initRecyclerView();
        setupCardData();
        setupCardClickListeners();

        // Test API
        testApiCall();
    }

    private void initRecyclerView() {
        recyclerViewCards = findViewById(R.id.recyclerViewCards);
        cardList = new ArrayList<>();
        cardAdapter = new HomeCardRecyclerAdapter(cardList);

        recyclerViewCards.setLayoutManager(gridLayoutManager);
        recyclerViewCards.setAdapter(cardAdapter);
    }

    private void setupCardData() {
        // Thêm các card vào list
        cardList.add(new HomeCard("Bản đồ đến quầy Check-in", "", android.R.drawable.ic_menu_mapmode));
        cardList.add(new HomeCard("Hướng dẫn Check-in", "Tra cứu thông tin", android.R.drawable.ic_menu_search));
        cardList.add(new HomeCard("Check-in trực tuyến", "Bản đồ hướng dẫn", android.R.drawable.ic_menu_mapmode));
        cardList.add(new HomeCard("Tra cứu thông tin chuyến bay", "Kết nối Robot", android.R.drawable.ic_menu_search));
        cardList.add(new HomeCard("Những câu hỏi thường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));
        // Cập nhật adapter
        cardAdapter.notifyDataSetChanged();
    }

    private void setupCardClickListeners() {
        cardAdapter.setOnItemClickListener(new HomeCardRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, HomeCard card) {
                String cardTitle = card.getTitle().replace("\n", " ");
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(HomeActivity.this, MapActivity.class);
                        intent.putExtra("title", cardTitle);
                        goToScreen("Bản đồ");
                        intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this, CheckInActivity.class);
                        intent.putExtra("title", cardTitle);
                        goToScreen("Check-in");
                        intent.putExtra("breadcrumb", getBreadcrumbList().toArray(new String[0]));
                        startActivity(intent);
                        break;
                    case 2: // Bản đồ hướng dẫn
                        Toast.makeText(HomeActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                        // TODO: Mở màn hình bản đồ
                        break;
                    case 3: // Kết nối Robot
                        Toast.makeText(HomeActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                        // TODO: Mở màn hình kết nối Robot
                        break;
                    case 4: // Câu hỏi thường gặp
                        Toast.makeText(HomeActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                        // TODO: Mở màn hình FAQ
                        break;
                    default:
                        break;
                }
            }
        });

        // QR Code section
        findViewById(R.id.qrSection).setOnClickListener(v -> {
            Toast.makeText(this, "Quét QR code để kết nối Robot", Toast.LENGTH_SHORT).show();
            // TODO: Mở camera để quét QR
        });
    }

    @Override
    protected void onTitleClick() {
        if (isGrid) {
            recyclerViewCards.setLayoutManager(new LinearLayoutManager(this));
            isGrid = false;
        } else {
            recyclerViewCards.setLayoutManager(gridLayoutManager);
            isGrid = true;
        }
    }

    @Override
    protected void onBtnLeftClick() {
        //Không làm gì cả
    }

    private void testApiCall() {
        Log.d(TAG, "Bắt đầu test API call...");

        // Sử dụng JSONPlaceholder API mẫu để test
        String testUrl = "https://jsonplaceholder.typicode.com/posts/1";

        ApiHelper.get(testUrl, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "API Success - Response: " + response);
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivity.this, "API call thành công! Xem LogCat", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "API Error: " + e.getMessage(), e);
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivity.this, "API call thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setWelcomeSwitch(boolean isOpen) {
        PeanutSDK.getInstance().runtime().setWelcomeSwitch(new ApiCallback<BaseResp<String>>() {
            @Override
            public void onFail(ApiError apiError) {
                Log.e(TAG, "Lỗi khi thay đổi chế độ chào mừng: " + apiError.toString());
            }

            @Override
            public void onSuccess(BaseResp<String> stringBaseResp) {
                Log.d(TAG, isOpen ? "Mở " : "Tắt " + "Raw Response: " + gson.toJson(stringBaseResp));
                if (isOpen) {
                    subscribePerceptionEvent();
                } else {
                    unsubscribePerceptionEvent();
                }
            }

            @Override
            public void onSuccess(String topic, BaseResp<String> result) {
                Log.d(TAG, (isOpen ? "BẬT" : "TẮT") + " chế độ chào mừng thành công! topic=" + topic);
                if (isOpen) {
                    subscribePerceptionEvent();
                } else {
                    unsubscribePerceptionEvent();
                }
            }

            public void onFail(String topic, ApiError error) {
                Log.d(TAG, "Topic=" + topic + " | Error: " + error.toString());
            }
        }, isOpen);
    }

    private void handleObjectPerception(String data) {
        try {
            Log.d(TAG, "Nhận dữ liệu từ sensor: " + data);
            ObjectPerceptionBean bean = gson.fromJson(data, ObjectPerceptionBean.class);
            if (bean != null && bean.getObjects() != null && !bean.getObjects().isEmpty()) {
                Log.d(TAG, "Phát hiện " + bean.getObjects().size() + " đối tượng.");

                for (int i = 0; i < bean.getObjects().size(); i++) {
                    ObjectPerceptionBean.ObjectsBean obj = bean.getObjects().get(i);
                    Log.d(TAG, "OBJECT_" + (i + 1) + "Khoảng cách=" + obj.getDistance() + ", X=" + obj.getX() + ", Y=" + obj.getY());
                }
            } else {
                Log.d(TAG, "Không có đối tượng nào được phát hiện.");
            }
        } catch (Exception e) {
            Log.d(TAG, "Không thể parse dữ liệu: " + e.getMessage());
        }
    }

    private void subscribePerceptionEvent() {
        Log.d(TAG, "Đang đăng ký lắng nghe sự kiện Object Perception...");

        ApiCallback<String> commonCallback = new ApiCallback<String>() {

            @Override
            public void onSuccess(String requestId, String result) {
                // Handle the case when onSuccess is called with requestId and result
                if (result != null) {
                    handleObjectPerception(result);
                }
            }

            @Override
            public void onFail(ApiError error) {
                Log.d(TAG, "Error: " + error.toString());
            }

            @Override
            public void onSuccess(String s) {

            }
        };

//        PeanutSDK.getInstance().subscribe(ApiTopic.OBJECT_PERCEPTION, 1000, commonCallback);
        PeanutSDK.getInstance().subscribe(ApiTopic.DYNAMIC_OBJECT_PERCEPTION, 1000, commonCallback);
//        PeanutSDK.getInstance().subscribe(ApiTopic.NAVI_HUMAN_DETECTION_STATUS, 1000, commonCallback);
    }

    private void unsubscribePerceptionEvent() {
        Log.d(TAG, "Đang hủy đăng ký lắng nghe sự kiện Object Perception...");
//        PeanutSDK.getInstance().unSubscribe(ApiTopic.OBJECT_PERCEPTION, null);
        PeanutSDK.getInstance().unSubscribe(ApiTopic.DYNAMIC_OBJECT_PERCEPTION, null);
//        PeanutSDK.getInstance().unSubscribe(ApiTopic.NAVI_HUMAN_DETECTION_STATUS, null);
    }

}
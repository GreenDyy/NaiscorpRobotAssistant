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
        setHeaderTitle("Trang chủ");

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
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this, CheckInActivity.class);
                        intent.putExtra("title", cardTitle);
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
}
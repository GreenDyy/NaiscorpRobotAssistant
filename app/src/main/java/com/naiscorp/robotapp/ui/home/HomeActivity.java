package com.naiscorp.robotapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerViewCards;
    private HomeCardRecyclerAdapter cardAdapter;
    private List<HomeCard> cardList;
    private boolean isGrid = true;


    //type show
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Thiết lập header
        setHeaderTitle("Trang chủ");
        
        // Override onClick cho title từ HomeActivity (sẽ dùng method override thay vì setupTitleClickListener)
        
        // Khởi tạo RecyclerView và data
        initRecyclerView();
        setupCardData();
        setupCardClickListeners();
    }
    
    private void initRecyclerView() {
        recyclerViewCards = findViewById(R.id.recyclerViewCards);
        cardList = new ArrayList<>();
        cardAdapter = new HomeCardRecyclerAdapter(this, cardList);

        recyclerViewCards.setLayoutManager(gridLayoutManager);
        recyclerViewCards.setAdapter(cardAdapter);
    }
    
    private void setupCardData() {
        // Thêm các card vào list
        cardList.add(new HomeCard("Hướng dẫn\nsử dụng", "Hướng dẫn sử dụng", android.R.drawable.ic_menu_help));
        cardList.add(new HomeCard("Tra cứu\nthông tin", "Tra cứu thông tin", android.R.drawable.ic_menu_search));
        cardList.add(new HomeCard("Bản đồ\nhướng dẫn", "Bản đồ hướng dẫn", android.R.drawable.ic_menu_mapmode));
        cardList.add(new HomeCard("Kết nối\nRobot", "Kết nối Robot", android.R.drawable.ic_menu_share));
        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));
        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));

        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));

        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));
        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));


        // Cập nhật adapter
        cardAdapter.notifyDataSetChanged();
    }
    
    private void setupCardClickListeners() {
        cardAdapter.setOnItemClickListener(new HomeCardRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, HomeCard card) {
                String cardTitle = card.getTitle().replace("\n", " ");
                
                switch (position) {
                    case 0: // Hướng dẫn sử dụng
                        Toast.makeText(HomeActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                        // TODO: Mở màn hình hướng dẫn
                        break;
                    case 1: // Tra cứu thông tin
                        Toast.makeText(HomeActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                        // TODO: Mở màn hình tra cứu
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
            Toast.makeText(this, "Chuyển sang dạng danh sách", Toast.LENGTH_SHORT).show();
        } else {
            recyclerViewCards.setLayoutManager(gridLayoutManager);
            isGrid = true;
            Toast.makeText(this, "Chuyển sang dạng lưới", Toast.LENGTH_SHORT).show();
        }
        // TODO: Mở SettingsActivity
        // Intent intent = new Intent(this, SettingsActivity.class);
        // startActivity(intent);
    }
}
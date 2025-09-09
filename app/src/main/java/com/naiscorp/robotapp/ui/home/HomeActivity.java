package com.naiscorp.robotapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.adapter.HomeCardAdapter;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.model.HomeCard;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private GridView gridViewCards;
    private HomeCardAdapter cardAdapter;
    private List<HomeCard> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Thiết lập header
        setHeaderTitle("Trang chủ");
        showBackButton(false);
        
        // Khởi tạo GridView và data
        initGridView();
        setupCardData();
        setupCardClickListeners();
    }
    
    private void initGridView() {
        gridViewCards = findViewById(R.id.gridViewCards);
        cardList = new ArrayList<>();
        cardAdapter = new HomeCardAdapter(this, cardList);
        gridViewCards.setAdapter(cardAdapter);
    }
    
    private void setupCardData() {
        // Thêm các card vào list
        cardList.add(new HomeCard("Hướng dẫn\nsử dụng", "Hướng dẫn sử dụng", android.R.drawable.ic_menu_help));
        cardList.add(new HomeCard("Tra cứu\nthông tin", "Tra cứu thông tin", android.R.drawable.ic_menu_search));
        cardList.add(new HomeCard("Bản đồ\nhướng dẫn", "Bản đồ hướng dẫn", android.R.drawable.ic_menu_mapmode, "DT"));
        cardList.add(new HomeCard("Kết nối\nRobot", "Kết nối Robot", android.R.drawable.ic_menu_share));
        cardList.add(new HomeCard("Câu hỏi\nthường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));
        
        // Cập nhật adapter
        cardAdapter.notifyDataSetChanged();
    }
    
    private void setupCardClickListeners() {
        gridViewCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeCard selectedCard = cardList.get(position);
                String cardTitle = selectedCard.getTitle().replace("\n", " ");
                
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
}
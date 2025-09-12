package com.naiscorp.robotapp.ui.checkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.adapter.CheckInRecycleAdapter;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.model.CheckInCard;
import com.naiscorp.robotapp.ui.dialog.ConfirmDialogCallback;
import com.naiscorp.robotapp.ui.dialog.ConfirmDialogHelper;
import com.naiscorp.robotapp.ui.map.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends BaseActivity {
    private RecyclerView recyclerViewCards;
    private CheckInRecycleAdapter cardAdapter;
    private List<CheckInCard> cardList;
    private boolean isGrid = true;
    // type show
    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        
        // Nhận breadcrumb từ Intent
        setBreadcrumbFromIntent(getIntent());
        
        initView();
        initRecyclerView();
        setupRecycleViewData();
    }

    private void initView() {
        setHeaderTitle("Hướng dẫn Check-in");
        setSubTitle("Check-in instructions");
        showLeftButton();
        showRightButton();
    }

    private void initRecyclerView() {
        //chuẩn bị data
        cardList = new ArrayList<>();
        //cb adapter
        cardAdapter = new CheckInRecycleAdapter(cardList);
        cardAdapter.setOnItemClickListener((position, card) -> {
            String cardTitle = card.getTitle().replace("\n", " ");
            Intent intent;
            switch (position) {
                case 0:
//                        intent = new Intent(HomeActivity.this, MapActivity.class);
//                        intent.putExtra("title", cardTitle);
//                        startActivity(intent);
                    Toast.makeText(CheckInActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                Toast.makeText(CheckInActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                    break;
                case 2: // Check-in trực tuyến
                    Toast.makeText(CheckInActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                    break;
                case 3: // Tra cứu thông tin chuyến bay
                    Toast.makeText(CheckInActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                    break;
                case 4: // Câu hỏi thường gặp
                    Toast.makeText(CheckInActivity.this, "Mở " + cardTitle, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
        
        //cb recyclerView
        recyclerViewCards = findViewById(R.id.recyclerViewCards);
        recyclerViewCards.setLayoutManager(gridLayoutManager);
        recyclerViewCards.setAdapter(cardAdapter);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setupRecycleViewData() {
        // Thêm các card vào list
        cardList.add(new CheckInCard("Hihi", "", android.R.drawable.ic_menu_mapmode));
        cardList.add(new CheckInCard("Hướng dẫn Check-in", "Tra cứu thông tin", android.R.drawable.ic_menu_search));
        cardList.add(new CheckInCard("Check-in trực tuyến", "Bản đồ hướng dẫn", android.R.drawable.ic_menu_mapmode));
        cardList.add(new CheckInCard("Tra cứu thông tin chuyến bay", "Kết nối Robot", android.R.drawable.ic_menu_search));
        cardList.add(new CheckInCard("Những câu hỏi thường gặp", "Câu hỏi thường gặp", android.R.drawable.ic_menu_info_details));
        // Cập nhật adapter
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onBtnRightClick() {
        ConfirmDialogHelper.show(this, "Thông báo", "Bạn có muốn check-in không?", new ConfirmDialogCallback() {
            public void onConfirmed() {
                Toast.makeText(CheckInActivity.this, "Đồng ý", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {
                Toast.makeText(CheckInActivity.this, "Hủy bỏ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

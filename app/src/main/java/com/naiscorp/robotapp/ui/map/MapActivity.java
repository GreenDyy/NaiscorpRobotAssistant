package com.naiscorp.robotapp.ui.map;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.utils.AssetUtils;

import java.io.InputStream;
import java.util.Objects;

public class MapActivity extends BaseActivity {
    private static final String TAG = "MapActivity";
    private TextView tvDes;
    private ImageView imgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        // Nhận breadcrumb từ Intent
        setBreadcrumbFromIntent(getIntent());
        
        initView();
    }

    private void initView() {
        tvDes = findViewById(R.id.tvDes);
        tvDes.setText("Bạn có thể tìm thấy quầy thủ tục của Vietjet tại sảnh A1-A5 ở tầng 1F");
        imgMap = findViewById(R.id.imgMap);
        AssetUtils.loadImageFromAssets(this, imgMap, "images/map_plane.jpg");
        
        // Thêm click listener cho ảnh map
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog();
            }
        });

        setHeaderTitle("Bản đồ đến quầy Check-in");
        setSubTitle("Guiding Map");
        showRightButton();
        showLeftButton();
    }

    @Override
    protected void onBtnRightClick() {
        Toast.makeText(this, "map ne ku", Toast.LENGTH_SHORT).show();
    }
    
    private void showZoomDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image_zoom);
        
        ZoomableImageView zoomImageView = dialog.findViewById(R.id.zoomImageView);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        TextView tvZoomHint = dialog.findViewById(R.id.tvZoomHint);
        
        // Load ảnh vào ZoomableImageView
        AssetUtils.loadImageFromAssets(this, zoomImageView, "images/map_plane.jpg");
        
        // Xử lý nút đóng
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        // Ẩn hint sau 3 giây
        tvZoomHint.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvZoomHint.setVisibility(View.GONE);
            }
        }, 3000);
        
        dialog.show();
    }
}
package com.naiscorp.robotapp.ui.map;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;

public class MapActivity extends BaseActivity {
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tvTest = new TextView(this);
        tvTest.setText("Duy dep trai");
        showRightButton();
        showLeftButton();
    }

    @Override
    protected void onBtnRightClick() {
        Toast.makeText(this, "đây là nút Right", Toast.LENGTH_SHORT).show();
    }
}
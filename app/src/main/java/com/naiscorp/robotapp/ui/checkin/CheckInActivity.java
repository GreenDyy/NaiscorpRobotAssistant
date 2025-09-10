package com.naiscorp.robotapp.ui.checkin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;

public class CheckInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        initView();
    }

    private void initView() {
        setHeaderTitle("Hướng dẫn Check-in");
        setSubTitle("Check-in instructions");
        showLeftButton();
        showRightButton();
    }
}

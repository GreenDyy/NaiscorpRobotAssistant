package com.naiscorp.robotapp.ui.playground;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;

public class PlayGroundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ground);

        //setUpBase
        setHeaderTitle("Vùng test");
        setSubTitle("Testt");
        showLeftButton();
        showRightButton();
    }
}
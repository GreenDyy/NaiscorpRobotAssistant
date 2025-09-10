package com.naiscorp.robotapp.ui.checkin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.core.BaseActivity;
import com.naiscorp.robotapp.ui.dialog.ConfirmDialogCallback;
import com.naiscorp.robotapp.ui.dialog.ConfirmDialogHelper;

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

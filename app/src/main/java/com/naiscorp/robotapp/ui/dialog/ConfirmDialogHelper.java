package com.naiscorp.robotapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naiscorp.robotapp.R;

public class ConfirmDialogHelper {
    public static void show(Context context,
                            String title,
                            String message,
                            ConfirmDialogCallback callback) {

        Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_confirm);

        // Nếu muốn full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setDimAmount(0.5f); // nền mờ mờ
        }
        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText(title);
         TextView tvDialogMessage = dialog.findViewById(R.id.tvDialogMessage);
        tvDialogMessage.setText(message);

        dialog.findViewById(R.id.btnPositive).setOnClickListener(v -> {
            if (callback != null) callback.onConfirmed();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btnNegative).setOnClickListener(v -> {
            if (callback != null) callback.onCancelled();
            dialog.dismiss();
        });

        dialog.show();
    }
}

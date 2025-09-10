package com.naiscorp.robotapp.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.naiscorp.robotapp.R;

public class ConfirmDialog extends DialogFragment {

    private String title;
    private String message;
    private int iconRes;
    private ConfirmListener listener;

    // Interface callback cho Activity/Fragment gọi dialog
    public interface ConfirmListener {
        void onConfirmed();
        void onCancelled();
    }

    public ConfirmDialog(String title, String message, int iconRes, ConfirmListener listener) {
        this.title = title;
        this.message = message;
        this.iconRes = iconRes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Tạo dialog không có title mặc định
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm, container, false);

        ImageView icon = view.findViewById(R.id.dialog_icon);
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        TextView tvMessage = view.findViewById(R.id.dialog_message);
        Button btnPositive = view.findViewById(R.id.btn_positive);
        Button btnNegative = view.findViewById(R.id.btn_negative);

        // Gán dữ liệu
        icon.setImageResource(iconRes);
        tvTitle.setText(title);
        tvMessage.setText(message);

        btnPositive.setOnClickListener(v -> {
            if (listener != null) listener.onConfirmed();
            dismiss();
        });

        btnNegative.setOnClickListener(v -> {
            if (listener != null) listener.onCancelled();
            dismiss();
        });

        return view;
    }
}

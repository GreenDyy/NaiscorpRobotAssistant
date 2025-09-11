package com.naiscorp.robotapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.model.CheckInCard;

import java.util.List;

public class CheckInRecycleAdapter extends RecyclerView.Adapter<CheckInRecycleAdapter.MyViewHolder> {
    private List<CheckInCard> listData;
    private OnItemClickListener callback;
    public interface OnItemClickListener {
        void onItemClick(int position, CheckInCard card);
    }

     public CheckInRecycleAdapter(List<CheckInCard> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //tạo view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_card, parent, false);
        //trả về view đó cho viewholder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CheckInCard card = listData.get(position);

        // Set icon
        holder.ivIcon.setImageResource(card.getIconResId());

        // Set title
        holder.tvTitle.setText(card.getTitle());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (callback != null) {
                callback.onItemClick(position, card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    //tạo viewholder để giữ Item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

}

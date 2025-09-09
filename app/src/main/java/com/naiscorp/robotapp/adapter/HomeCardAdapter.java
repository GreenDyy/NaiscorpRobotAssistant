package com.naiscorp.robotapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naiscorp.robotapp.R;
import com.naiscorp.robotapp.model.HomeCard;

import java.util.List;

public class HomeCardAdapter extends BaseAdapter {
    private Context context;
    private List<HomeCard> cardList;
    private LayoutInflater inflater;

    public HomeCardAdapter(Context context, List<HomeCard> cardList) {
        this.context = context;
        this.cardList = cardList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public HomeCard getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_home_card, parent, false);
            holder = new ViewHolder();
            holder.ivIcon = convertView.findViewById(R.id.ivIcon);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvBadge = convertView.findViewById(R.id.tvBadge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomeCard card = cardList.get(position);

        // Set icon
        holder.ivIcon.setImageResource(card.getIconResId());

        // Set title
        holder.tvTitle.setText(card.getTitle());

        // Set badge
        if (card.hasBadge()) {
            holder.tvBadge.setText(card.getBadge());
            holder.tvBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvBadge;
    }
}

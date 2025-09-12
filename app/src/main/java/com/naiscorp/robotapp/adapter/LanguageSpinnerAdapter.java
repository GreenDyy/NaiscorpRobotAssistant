package com.naiscorp.robotapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naiscorp.robotapp.R;

import java.util.List;

/**
 * Adapter cho Language Spinner
 */
public class LanguageSpinnerAdapter extends BaseAdapter {
    
    private Context context;
    private List<LanguageItem> languageItems;
    private LayoutInflater inflater;
    
    public LanguageSpinnerAdapter(Context context, List<LanguageItem> languageItems) {
        this.context = context;
        this.languageItems = languageItems;
        this.inflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        return languageItems.size();
    }
    
    @Override
    public Object getItem(int position) {
        return languageItems.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.language_spinner_item, parent, false);
        }
        
        LanguageItem item = languageItems.get(position);
        
        ImageView imgGlobe = view.findViewById(R.id.imgGlobe);
        TextView tvLanguageCode = view.findViewById(R.id.tvLanguageCode);
        ImageView imgDropdown = view.findViewById(R.id.imgDropdown);
        
        imgGlobe.setImageResource(item.getFlagResId());
        tvLanguageCode.setText(item.getLanguageCode().toUpperCase());
        imgDropdown.setImageResource(R.drawable.ic_dropdown_arrow); // Sử dụng dropdown arrow icon
        
        return view;
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.language_dropdown_item, parent, false);
        }
        
        LanguageItem item = languageItems.get(position);
        
        ImageView imgGlobe = view.findViewById(R.id.imgGlobe);
        TextView tvLanguageName = view.findViewById(R.id.tvLanguageName);
        
        // Sử dụng globe icon màu đen cho dropdown
        imgGlobe.setImageResource(R.drawable.ic_globe_black);
        tvLanguageName.setText(item.getLanguageName());
        
        return view;
    }
    
    /**
     * Model class cho Language Item
     */
    public static class LanguageItem {
        private String languageCode;
        private String languageName;
        private int flagResId;
        
        public LanguageItem(String languageCode, String languageName, int flagResId) {
            this.languageCode = languageCode;
            this.languageName = languageName;
            this.flagResId = flagResId;
        }
        
        public String getLanguageCode() {
            return languageCode;
        }
        
        public String getLanguageName() {
            return languageName;
        }
        
        public int getFlagResId() {
            return flagResId;
        }
    }
}

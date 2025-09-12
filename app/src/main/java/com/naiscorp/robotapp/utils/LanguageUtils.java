package com.naiscorp.robotapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.naiscorp.robotapp.adapter.LanguageSpinnerAdapter;

/**
 * Utility class để quản lý ngôn ngữ
 */
public class LanguageUtils {
    private static final String TAG = "LanguageUtils";
    private static final String PREF_LANGUAGE = "pref_language";
    private static final String PREF_LANGUAGE_CODE = "pref_language_code";
    
    /**
     * Lấy danh sách ngôn ngữ có sẵn
     */
    public static List<LanguageSpinnerAdapter.LanguageItem> getAvailableLanguages() {
        List<LanguageSpinnerAdapter.LanguageItem> languages = new ArrayList<>();
        
        // Vietnam
        languages.add(new LanguageSpinnerAdapter.LanguageItem(
            "vi", 
            "Vietnam", 
            android.R.drawable.ic_menu_day // Tạm thời dùng icon có sẵn
        ));
        
        // English
        languages.add(new LanguageSpinnerAdapter.LanguageItem(
            "en", 
            "English", 
            android.R.drawable.ic_menu_search // Tạm thời dùng icon có sẵn
        ));
        
        return languages;
    }
    
    /**
     * Lưu ngôn ngữ được chọn
     */
    public static void saveLanguage(Context context, String languageCode) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_LANGUAGE, Context.MODE_PRIVATE);
            prefs.edit().putString(PREF_LANGUAGE_CODE, languageCode).apply();
            Log.d(TAG, "Language saved: " + languageCode);
        } catch (Exception e) {
            Log.e(TAG, "Error saving language", e);
        }
    }
    
    /**
     * Lấy ngôn ngữ đã lưu
     */
    public static String getSavedLanguage(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_LANGUAGE, Context.MODE_PRIVATE);
            return prefs.getString(PREF_LANGUAGE_CODE, "vi"); // Default là Vietnam
        } catch (Exception e) {
            Log.e(TAG, "Error getting saved language", e);
            return "vi"; // Fallback
        }
    }
    
    /**
     * Áp dụng ngôn ngữ cho context
     */
    public static void applyLanguage(Context context, String languageCode) {
        try {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            
            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            Log.d(TAG, "Language applied: " + languageCode);
        } catch (Exception e) {
            Log.e(TAG, "Error applying language", e);
        }
    }
    
    /**
     * Khởi tạo ngôn ngữ cho app
     */
    public static void initializeLanguage(Context context) {
        String savedLanguage = getSavedLanguage(context);
        applyLanguage(context, savedLanguage);
    }
    
    /**
     * Lấy vị trí của ngôn ngữ trong danh sách
     */
    public static int getLanguagePosition(String languageCode) {
        List<LanguageSpinnerAdapter.LanguageItem> languages = getAvailableLanguages();
        for (int i = 0; i < languages.size(); i++) {
            if (languages.get(i).getLanguageCode().equals(languageCode)) {
                return i;
            }
        }
        return 0; // Default position
    }
}

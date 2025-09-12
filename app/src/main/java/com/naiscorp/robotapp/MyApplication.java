package com.naiscorp.robotapp;

import android.app.Application;

import com.naiscorp.robotapp.utils.LanguageUtils;

public class MyApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Khởi tạo ngôn ngữ
        LanguageUtils.initializeLanguage(this);
    }
}
package com.naiscorp.robotapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public  class AssetUtils {
    public static void loadImageFromAssets(Context context, ImageView imageView, String assetPath) {
        try {
            InputStream is = context.getAssets().open(assetPath);
            Drawable drawable = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(drawable);
            is.close();
        } catch (Exception e) {
            Log.e("AssetUtils", "Error loading asset image: " + assetPath, e);
        }
    }
}

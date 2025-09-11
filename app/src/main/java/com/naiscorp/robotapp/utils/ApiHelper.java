package com.naiscorp.robotapp.utils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiHelper {
    private static final OkHttpClient client = new OkHttpClient();

    public interface ApiCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }

    public static void get(String url, ApiCallback callback) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onError(new IOException("Unexpected code " + response));
                }
            }
        });
    }
}

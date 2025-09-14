package com.naiscorp.robotapp;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.naiscorp.robotapp.utils.LanguageUtils;
//SDK
import com.naiscorp.robotapp.utils.LogUtil;
import com.naiscorp.robotapp.utils.MqttHandler;
import com.naiscorp.robotapp.utils.PeanutSDKManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    private static final String BROKER_URL = "ssl://emqx.naiscorp.com:8883";
    private static final String CLIENT_ID = "a8faad3e-44fe-41d6-8084-d0ad7ce6dcd9";

    private static final String USER_NAME = "robot01";
    private static final String PASSWORD = "E9SvBhWXK6ZL0z89";
    private String topic = "robot/signal/a8faad3e-44fe-41d6-8084-d0ad7ce6dcd9";

    private MqttHandler mqttHandler;
    private StringBuilder messageLog = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo ngôn ngữ
        LanguageUtils.initializeLanguage(this);
        initSDK();
        initMqtt();
    }

    private void initSDK() {
        PeanutSDKManager.initializeSDK(getApplicationContext(), errorCode -> {
            if (errorCode == com.keenon.sdk.external.PeanutSDK.SDK_INIT_SUCCESS) {
                Log.d(TAG, "✅ SDK Init Success");

            } else {
                LogUtil.d(TAG, "❌ SDK Init Failed: " + errorCode);
            }
        });
    }

    private void initMqtt() {
        mqttHandler = new MqttHandler();

        // Callback khi connect thành công hoặc thất bại
        IMqttActionListener mqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG,"✅ MQTT connect success");
                // Subcribe ngay sau khi connect thành công
                try {
                    mqttHandler.subscribe(topic); // QoS = 1
                    Log.d(TAG,"📡 Subscribed to topic: " + topic);
                    Log.d(TAG, "📡 Subscribed to topic: " + topic);
                } catch (Exception e) {
                    Log.d(TAG,"❌ Subscribe failed: " + e.getMessage());
                    Log.d(TAG, "❌ Subscribe failed: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(TAG,"❌ MQTT connect failed: " + (exception != null ? exception.getMessage() : "Unknown"));
            }
        };

        // Callback xử lý message và connection lost
        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
//        addLog("🚨 MQTT connection lost: " + (cause != null ? cause.getMessage() : "Unknown"));
                Log.d(TAG, "🚨 MQTT connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
//        addLog("📩 Message arrived [" + topic + "]: " + payload);
                Log.d(TAG, "📩 Message arrived [" + topic + "]: " + payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG,"✅ Message delivered");
            }
        };
        String CLIENT_ID = getAndroidId(this);
        Log.d(TAG,"CLIENT_ID: "+ CLIENT_ID);
        // Kết nối MQTT
        mqttHandler.connect(BROKER_URL, CLIENT_ID, USER_NAME, PASSWORD, mqttActionListener, mqttCallback);
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
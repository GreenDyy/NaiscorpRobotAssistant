package com.naiscorp.robotapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.keenon.sdk.component.NavigationComponent;
import com.keenon.sdk.component.runtime.PeanutRuntime;
import com.keenon.sdk.embedded.common.PeanutSensors;
import com.keenon.sdk.external.PeanutSDK;
import com.keenon.sdk.hedera.model.ApiError;
import com.keenon.sdk.robot.ApiCallback;
import com.keenon.sdk.robot.base.BaseResp;
import com.keenon.sdk.sensor.headmotor.HeadMotorInterface;
import com.keenon.sdk.sensor.headmotor.SensorHeadMotor;
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
import org.json.JSONArray;
import org.json.JSONObject;


public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    private static final String BROKER_URL = "ssl://emqx.naiscorp.com:8883";
    private static final String CLIENT_ID = "a8faad3e-44fe-41d6-8084-d0ad7ce6dcd9";

    private static final String USER_NAME = "robot01";
    private static final String PASSWORD = "E9SvBhWXK6ZL0z89";
    private String topic = "robot/signal/a8faad3e-44fe-41d6-8084-d0ad7ce6dcd9";

    private NavigationComponent navigationComponent;


    private MqttHandler mqttHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo ngôn ngữ
        LanguageUtils.initializeLanguage(this);
    }

    public void initSDK() {
        PeanutSDKManager.initializeSDK(getApplicationContext(), statusCode -> {
            switch (statusCode) {
                case PeanutSDK.SDK_INIT_SUCCESS:
                    Log.d(TAG, "✅ SDK Init Success");
                    initRuntime();
                    initHeadMotorSensor();
                    initMap();
                    initNavigation();
                    break;
                case PeanutSDK.SDK_INITIALIZING:
                    Log.d(TAG, "✅ SDK SDK_INITIALIZINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                    break;
                default:
                    Log.d(TAG, "❌ PeanutSDK khởi tạo thất bại với mã: " + statusCode);
                    break;
            }
        });
    }

    public void initRuntime() {
        try {
            if (PeanutRuntime.getInstance() != null) {
                // Sử dụng PeanutSDKManager để khởi tạo Runtime
                PeanutSDKManager.startRuntime(new PeanutRuntime.Listener() {
                    @Override
                    public void onEvent(int event, Object obj) {
                        Log.d("Runtime", "Event: " + event + ", Content: " + obj);
                    }

                    @Override
                    public void onHealth(Object content) {
                        Log.d("Health", "Status: " + content);
                    }

                    @Override
                    public void onHeartbeat(Object content) {
                        // Không log heartbeat để tránh spam
                    }
                });
                Log.d("Runtime", "✅ PeanutRuntime khởi tạo thành công");
            } else {
                Log.d("Runtime", "❌ PeanutRuntime không khả dụng");
            }
        } catch (Exception e) {
            Log.d("Error", "❌ Khởi tạo Runtime thất bại: " + e.getMessage());
        }
    }

    private void initMap() {
        PeanutSDKManager.checkAndLoadMap(getApplicationContext(), new PeanutSDKManager.MapLoadListener() {
            @Override
            public void onMapLoadStart() {
                Log.d(TAG, "Đang load map...");
            }

            @Override
            public void onMapLoadSuccess() {
                Log.d(TAG, "✅ Map load thành công!");
            }

            @Override
            public void onMapLoadError(String error) {
                Log.e(TAG, "❌ Map load thất bại: " + error);
            }

            @Override
            public void onMapLoadProgress(int progress) {
                Log.d(TAG, "Đang tải map: " + progress + "%");
            }
        });
    }

    public void initHeadMotorSensor() {
        try {
            PeanutSensors.getInstance().putSensor(SensorHeadMotor.getInstance());
            SensorHeadMotor.getInstance().setSerialDirect(true);
            Log.d(TAG, "✅ đã put Sensor headmotor vào PeanutSensor & SerialDirect enabled");
            Log.d(TAG, "✅ Head Motor đã sẳn sàng!");
        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to init sensor: " + e.getMessage());
        }
    }

    private void initNavigation() {
        try {
            PeanutSDK sdk = PeanutSDK.getInstance();
            if (sdk != null && sdk.navigation() != null) {
                navigationComponent = sdk.navigation();
                Log.d(TAG, "PeanutSDK NavigationComponent khởi tạo thành công");
            } else {
                Log.d(TAG, "PeanutSDK hoặc Navigation component chưa được khởi tạo");
            }
        } catch (Exception e) {
            Log.d(TAG, "Lỗi khởi tạo NavigationComponent: " + e.getMessage());
        }
    }


    public void initMqtt() {
        mqttHandler = new MqttHandler();

        // Callback khi connect thành công hoặc thất bại
        IMqttActionListener mqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "✅ MQTT connect success");
                // Subcribe ngay sau khi connect thành công
                try {
                    mqttHandler.subscribe(topic); // QoS = 1
                    Log.d(TAG, "📡 Subscribed to topic: " + topic);
                } catch (Exception e) {
                    Log.d(TAG, "❌ Subscribe failed: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(TAG, "❌ MQTT connect failed: " + (exception != null ? exception.getMessage() : "Unknown"));
            }
        };

        // Callback xử lý message và connection lost
        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                // addLog("🚨 MQTT connection lost: " + (cause != null ? cause.getMessage() :
                // "Unknown"));
                Log.d(TAG, "🚨 MQTT connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                Log.d(TAG, "📩 Message arrived [" + topic + "]: " + payload);

                String deviceId = getAndroidId(getApplicationContext());
                String serial = Build.SERIAL;
                Log.d(TAG, "📱 My Serial ID: " + serial);
                Log.d(TAG, "📱 Device ID: " + deviceId);
                try {
                    //convert sang Json
                    JSONObject json = new JSONObject(payload);
                    //field data
                    JSONObject data = json.getJSONObject("data");
                    String type = data.getString("type");
                    Log.d(TAG, "📱 Type: " + type);
                    String name = data.getString("name");
                    Log.d(TAG, "📱 Name: " + name);


                    JSONObject target = json.getJSONObject("target");
                    // Lấy mảng robotIds từ object target
                    JSONArray robotIds = target.getJSONArray("robotIds");
                    //check xem robot id có tồn tại trong robotIds ko?
                    for (int i = 0; i < robotIds.length(); i++) {
                        String id = robotIds.getString(i);
                        Log.d(TAG, "📱 Robot ID in loop: " + id);
                        //de963 tạm fix cunf71 nha
                        if ("a8faad3e-44fe-41d6-8084-d0ad7ce6dcd9".equals(id)) {
                            Log.d(TAG, "✅ robotId có tồn tại: " + id + " trong robotIds, tiến hành thực hiện hành động!");
                            //ACTION
                            if (type.toLowerCase().equals("task")) {
                                switch (name.toLowerCase()) {
                                    case "nod":
                                        Log.d(TAG, "🔄 Tiến hành gật đầu.");
                                        if (SensorHeadMotor.getInstance() != null) {
                                            SensorHeadMotor.getInstance().onControlHeadMotorPlay(HeadMotorInterface.MotorAction.RESET);
                                            Log.d(TAG, "✅Đã thực hiện (PC - Gật đầu)");
                                        } else {
                                            Log.e(TAG, "❌ SensorHeadMotor chưa khởi tạo");
                                        }
                                        break;
                                    case "charge":
                                        Log.d(TAG, "🔄 Tiến hành đi sạc!");
                                        int pointId = 1;//charge tuỳ theo id trong map nha
                                        PeanutSDK.getInstance().navigation().setTarget(pointId, 0, new ApiCallback<BaseResp<String>>() {
                                            @Override
                                            public void onSuccess(BaseResp<String> result) {
                                                Log.d(TAG, "✅ Điều hướng tới điểm đích ID=" + pointId + " thành công");
                                                Log.d(TAG, "📊 Kết quả: " + result.getData());
                                                Log.d(TAG, "🔢 Mã phản hồi: " + result.getCode());
                                            }

                                            @Override
                                            public void onSuccess(String requestId, BaseResp<String> result) {
                                                onSuccess(result);
                                            }

                                            @Override
                                            public void onFail(ApiError error) {
                                                Log.e(TAG, "❌ Điều hướng tới điểm đích ID=" + pointId + " thất bại: " + error.toString());
                                            }
                                        });
                                        break;
                                    default:
                                        Log.d(TAG, "⚠️ Task không xác định: " + name);
                                        break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "❌ Error parsing JSON: " + e.getMessage());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "✅ Message delivered");
            }
        };
        String CLIENT_ID = getAndroidId(this);
        Log.d(TAG, "CLIENT_ID: " + CLIENT_ID);
        // Kết nối MQTT
        mqttHandler.connect(BROKER_URL, CLIENT_ID, USER_NAME, PASSWORD, mqttActionListener, mqttCallback);
    }

    //Xử ly1 Sensor vật thể


    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
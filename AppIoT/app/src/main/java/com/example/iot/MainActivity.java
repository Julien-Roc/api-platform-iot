package com.example.iot;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient mqttAndroidClient;
    private String brokerUrl = "mqtt://test.mosquitto.org:1883";
    private String clientId = MqttAsyncClient.generateClientId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise MQTT client
        mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), brokerUrl, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    // Re-subscribe to topics if connection is lost and then re-established
                    Log.d("MQTT", "Reconnected to : " + serverURI);
                } else {
                    Log.d("MQTT", "Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("MQTT", "Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Handle incoming messages
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Handle message delivery
            }
        });

        // Set connection options
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        // Connect to the broker
        try {
            IMqttToken token = mqttAndroidClient.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT", "Connection success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("MQTT", "Connection failure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

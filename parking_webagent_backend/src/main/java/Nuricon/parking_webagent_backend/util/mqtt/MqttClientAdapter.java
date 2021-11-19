package Nuricon.parking_webagent_backend.util.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MqttClientAdapter implements MqttCallbackExtended {
    private Logger logger = LoggerFactory.getLogger(MqttClientAdapter.class);
    private IMqttAsyncClient _client;
    private String _topic;

    public MqttClientAdapter(String url, String topic) throws MqttException {
        _topic = topic;

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);

        // 접속 및 구독 활성화
        _client = new MqttAsyncClient(url, MqttAsyncClient.generateClientId());
        _client.setCallback(this);
        _client.connect(options);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(message.getPayload().toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            //Very important to resubcribe to the topic after the connection was (re-)estabslished.
            //Otherwise you are reconnected but you don't get any message
            this._client.subscribe(this._topic, 1, (tpic, msg) -> {
                System.out.println("[serverURI : " + serverURI + "] " + msg);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}

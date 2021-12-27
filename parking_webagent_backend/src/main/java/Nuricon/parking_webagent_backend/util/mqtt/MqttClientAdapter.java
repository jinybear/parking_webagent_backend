package Nuricon.parking_webagent_backend.util.mqtt;

import Nuricon.parking_webagent_backend.util.mqtt.strategy.Strategy;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MqttClientAdapter implements MqttCallbackExtended {
    private Logger logger = LoggerFactory.getLogger(MqttClientAdapter.class);
    private IMqttAsyncClient _client;
    private String[] topics;
    private Strategy strategy;
    private MemoryPersistence persistence = new MemoryPersistence();

    public MqttClientAdapter(String url, String[] topics, Strategy strategy) throws MqttException {
        this.topics = topics;
        this.strategy = strategy;

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);

        // 접속 및 구독 활성화
        _client = new MqttAsyncClient(url, MqttAsyncClient.generateClientId(), this.persistence);
        _client.setCallback(this);
        _client.connect(options);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message)  {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            for(String topic: this.topics){
                logger.debug("Subscribe [" + topic + "] from " + serverURI);
                this._client.subscribe(topic, 1, (tpic, msg) -> this.strategy.processMessage(tpic, msg));
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

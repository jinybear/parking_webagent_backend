package Nuricon.parking_webagent_backend.util.mqtt.strategy;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface Strategy {
    void processMessage(String topic, MqttMessage message);
}

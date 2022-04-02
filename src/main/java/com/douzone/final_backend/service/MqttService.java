package com.douzone.final_backend.service;

import com.douzone.final_backend.service.impl.MqttServiceImpl;
import org.eclipse.paho.client.mqttv3.*;

public interface MqttService extends MqttCallback {
    MqttServiceImpl init(String userName, String password, String serverURI, String clientId);

    @Override
    void connectionLost(Throwable throwable);

    @Override
    void messageArrived(String topic, MqttMessage mqttMessage);

    @Override
    void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);

    boolean subscribe(String... topics);

    void sender(String topic, String msg) throws MqttPersistenceException, MqttException;
}

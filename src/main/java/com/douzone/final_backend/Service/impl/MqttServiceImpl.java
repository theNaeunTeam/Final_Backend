package com.douzone.final_backend.service.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class MqttServiceImpl implements com.douzone.final_backend.service.MqttService {

    @Autowired
    private ElasticSearchServiceImpl elasticSearchServiceImpl;

    @Autowired
    private RedisServiceImpl redisServiceImpl;

    private MqttClient client;

    @Override
    public MqttServiceImpl init(String userName, String password, String serverURI, String clientId) {
        MqttConnectOptions option = new MqttConnectOptions();
        option.setCleanSession(true);
        option.setKeepAliveInterval(30);
        option.setUserName(userName);
        option.setPassword(password.toCharArray());
        try {
            client = new MqttClient(serverURI, clientId);

            client.setCallback(this);
            client.connect(option);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("======================= connectionLost");
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        HashMap<String, String> messageMap = new HashMap<>();

        long startTime = System.currentTimeMillis();
        String msg = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);

        System.out.println("======================= messageArrived");
        System.out.println(topic);
        System.out.println(msg);

        messageMap.put("topic", topic);
        messageMap.put("msg", msg);

        JSONObject json = new JSONObject(messageMap);

        try {
            redisServiceImpl.setRedisHashValue(topic, messageMap);
            elasticSearchServiceImpl.createDocument(topic, "received message " + startTime, json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("======================= deliveryComplete");
        try {
            System.out.println(iMqttDeliveryToken.getMessage());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean subscribe(String... topics) {
        try {
            if (topics != null) {
                for (String topic : topics) {
                    client.subscribe(topic, 0);
                }
            }
            System.out.println("======================= MQTT Subscribe SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void sender(String topic, String msg) throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage();
        message.setPayload(msg.getBytes());
        client.publish(topic, message);
        System.out.println("======================= MQTT Publish SUCCESS");
    }

}

package com.douzone.final_backend.config;

import com.douzone.final_backend.service.impl.MqttServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Autowired
    MqttServiceImpl mqttServiceImpl;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.serverURI}")
    private String serverURI;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Bean
    public void mqttInit() {
        mqttServiceImpl.init(userName, password, serverURI, clientId);
        System.out.println("======================= MQTT INIT SUCCESS");
    }

}

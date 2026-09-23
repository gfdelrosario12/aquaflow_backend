package com.aquaflow.backend.infrastructure.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MqttPublishService {

    private static final Logger log = LoggerFactory.getLogger(MqttPublishService.class);

    @Autowired
    private MqttPahoMessageHandler mqttOutboundHandler;

    public void publish(String topic, String payload, int qos) {
        try {
            Message<String> message = MessageBuilder.withPayload(payload)
                    .setHeader("mqtt_topic", topic)
                    .setHeader("mqtt_qos", qos)
                    .build();
            mqttOutboundHandler.handleMessage(message);
            log.info("MQTT message published to topic: {}", topic);
        } catch (Exception e) {
            log.error("Error publishing MQTT message to topic: {}", topic, e);
        }
    }

    public void publishCommand(String deviceId, String command) {
        String topic = "actuator/" + deviceId + "/command";
        publish(topic, command, 1);
    }
}
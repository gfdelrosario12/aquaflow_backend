package com.aquaflow.backend.infrastructure.mqtt;

import com.aquaflow.backend.dto.request.SensorDataRequest;
import com.aquaflow.backend.domain.SensorDataService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MqttMessageListener implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MqttMessageListener.class);

    @Autowired
    private SensorDataService sensorDataService;

    private MqttClient mqttClient;
    private ExecutorService executor;

    @PostConstruct
    public void init() {
        executor = Executors.newFixedThreadPool(4);
        try {
            String broker = System.getenv().getOrDefault("MQTT_BROKER_URL", "tcp://localhost:1883");
            mqttClient = new MqttClient(broker, "aquaflow-backend-listener", new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            mqttClient.setCallback(this);
            mqttClient.connect(options);
            mqttClient.subscribe("sensor/+/data", 1);
            mqttClient.subscribe("device/+/status", 1);
            mqttClient.subscribe("device/+/alert", 1);
            log.info("MQTT listener connected and subscribed");
        } catch (Exception e) {
            log.error("MQTT connection error", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("MQTT connection lost", cause);
        reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        executor.submit(() -> {
            try {
                String payload = new String(message.getPayload());
                log.info("MQTT message received on topic: {}, payload: {}", topic, payload);
                processMessage(topic, payload);
            } catch (Exception e) {
                log.error("Error processing MQTT message", e);
            }
        });
    }

    private void processMessage(String topic, String payload) {
        String[] parts = topic.split("/");
        if (parts.length >= 3 && "sensor".equals(parts[0]) && "data".equals(parts[2])) {
            String deviceId = parts[1];
            SensorDataRequest request = new SensorDataRequest();
            request.setDeviceId(deviceId);
            request.setSensorType(parts.length > 1 ? parts[parts.length - 2] : "temperature");
            try {
                request.setValue(Double.parseDouble(payload));
            } catch (NumberFormatException e) {
                log.warn("Invalid sensor value: {}", payload);
                return;
            }
            sensorDataService.ingestSensorData(request);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("MQTT delivery complete");
    }

    public void reconnect() {
        executor.submit(() -> {
            try {
                if (mqttClient != null && !mqttClient.isConnected()) {
                    MqttConnectOptions options = new MqttConnectOptions();
                    options.setAutomaticReconnect(true);
                    options.setCleanSession(true);
                    mqttClient.reconnect();
                    log.info("MQTT reconnected");
                }
            } catch (Exception e) {
                log.error("MQTT reconnect error", e);
            }
        });
    }
}
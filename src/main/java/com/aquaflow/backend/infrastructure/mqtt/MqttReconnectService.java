package com.aquaflow.backend.infrastructure.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MqttReconnectService {

    private static final Logger log = LoggerFactory.getLogger(MqttReconnectService.class);

    private long lastReconnectAttempt = 0;
    private static final long RECONNECT_DELAY_MS = 5000;

    public boolean canAttemptReconnect() {
        long now = System.currentTimeMillis();
        if (now - lastReconnectAttempt > RECONNECT_DELAY_MS) {
            lastReconnectAttempt = now;
            return true;
        }
        return false;
    }

    public void scheduleReconnect(Runnable action) {
        if (canAttemptReconnect()) {
            log.info("Scheduling MQTT reconnect in {}ms", RECONNECT_DELAY_MS);
            new Thread(() -> {
                try {
                    Thread.sleep(RECONNECT_DELAY_MS);
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
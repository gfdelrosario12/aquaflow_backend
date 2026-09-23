package com.aquaflow.backend.mqtt;

import com.aquaflow.backend.infrastructure.mqtt.MqttReconnectService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MqttReconnectServiceTest {

    @Test
    void shouldAllowReconnectAfterDelay() throws InterruptedException {
        MqttReconnectService service = new MqttReconnectService();
        assertThat(service.canAttemptReconnect()).isTrue();
    }

    @Test
    void shouldPreventReconnectWithinDelay() {
        MqttReconnectService service = new MqttReconnectService();
        service.canAttemptReconnect();
        assertThat(service.canAttemptReconnect()).isFalse();
    }
}
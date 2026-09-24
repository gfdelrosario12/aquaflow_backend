package com.aquaflow.backend.config;

import com.aquaflow.backend.infrastructure.web.TelemetryStreamHandler;
import com.aquaflow.backend.infrastructure.web.WebSocketSecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TelemetryStreamHandler telemetryStreamHandler;
    private final WebSocketSecurityInterceptor webSocketSecurityInterceptor;

    public WebSocketConfig(TelemetryStreamHandler telemetryStreamHandler,
                            WebSocketSecurityInterceptor webSocketSecurityInterceptor) {
        this.telemetryStreamHandler = telemetryStreamHandler;
        this.webSocketSecurityInterceptor = webSocketSecurityInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(telemetryStreamHandler, "/api/v1/telemetry/stream")
                .addInterceptors(webSocketSecurityInterceptor)
                .setAllowedOrigins("*");
    }
}


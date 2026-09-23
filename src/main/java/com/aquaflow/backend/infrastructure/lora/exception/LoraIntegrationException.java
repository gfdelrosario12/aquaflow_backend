package com.aquaflow.backend.infrastructure.lora.exception;

public class LoraIntegrationException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String devEui;

    public LoraIntegrationException(String message) {
        super(message);
        this.statusCode = 500;
        this.errorCode = "LORA_INTEGRATION_ERROR";
        this.devEui = null;
    }

    public LoraIntegrationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
        this.errorCode = "LORA_INTEGRATION_ERROR";
        this.devEui = null;
    }

    public LoraIntegrationException(String message, int statusCode, String errorCode, String devEui) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.devEui = devEui;
    }

    public int getStatusCode() { return statusCode; }
    public String getErrorCode() { return errorCode; }
    public String getDevEui() { return devEui; }
}


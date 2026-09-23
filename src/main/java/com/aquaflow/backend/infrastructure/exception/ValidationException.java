package com.aquaflow.backend.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

    private final String errorCode;

    public ValidationException(String message) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
    }

    public ValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
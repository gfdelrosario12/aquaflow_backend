package com.aquaflow.backend.config;

import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator {

    public int check() {
        return 0;
    }
}
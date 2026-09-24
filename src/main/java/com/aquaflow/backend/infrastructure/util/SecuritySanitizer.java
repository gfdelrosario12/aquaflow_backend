package com.aquaflow.backend.infrastructure.util;

public class SecuritySanitizer {

    private SecuritySanitizer() {
    }

    public static String maskSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            return "****";
        }
        if (secret.length() <= 4) {
            return "****";
        }
        return secret.substring(0, 2) + "****" + secret.substring(secret.length() - 2);
    }

    public static String sanitizeLog(String input) {
        if (input == null) return null;
        return input.replaceAll("(?i)(password|secret|appKey|token|credential)=\"[^\"]*\"", "$1=\"****\"")
                    .replaceAll("(?i)(password|secret|appKey|token|credential)='[^']*'", "$1='****'")
                    .replaceAll("(?i)(\"password\"|\"secret\"|\"appKey\"|\"token\"|\"credential\")\\s*:\\s*\"[^\"]*\"", "$1:\"****\"");
    }
}


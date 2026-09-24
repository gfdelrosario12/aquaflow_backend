## 1. Security Configuration & Role Hardening

- [x] 1.1 Update `SecurityConfig` with explicit authorization rules for `ADMIN`, `OPERATOR`, `VIEWER`, and `EDGE_NODE` roles
- [x] 1.2 Implement `CustomAuthenticationEntryPoint` (401) and `CustomAccessDeniedHandler` (403) returning standardized JSON error payloads
- [x] 1.3 Implement `SecurityHeadersFilter` for HSTS, CSP, X-Frame-Options, and X-Content-Type-Options headers
- [x] 1.4 Externalize security configuration secrets in `application.yml` (`JWT_SECRET`, `LORAWAN_WEBHOOK_SECRET`)

## 2. Webhook & Edge Device Authentication

- [x] 2.1 Implement `WebhookAuthenticationFilter` to validate LoRaWAN webhook secret headers (`X-Downlink-Secret` / `X-Webhook-Secret`)
- [x] 2.2 Register `WebhookAuthenticationFilter` in `SecurityConfig` filter chain
- [x] 2.3 Create `SecuritySanitizer` utility to strip or mask credentials (AppKeys, commissioning tokens, JWT secrets) from logs and DTOs

## 3. Rate Limiting & Protection Filters

- [x] 3.1 Implement `RateLimitingFilter` for authentication endpoints (`/api/v1/auth/login`, `/api/v1/auth/register`)
- [x] 3.2 Add CORS configuration bean in `SecurityConfig`

## 4. Verification & Testing

- [x] 4.1 Write security unit and integration tests verifying 401 Unauthorized for unauthenticated requests and 403 Forbidden for insufficient role permissions
- [x] 4.2 Write webhook authentication test verifying secret header validation
- [x] 4.3 Execute complete clean test suite (`mvn clean test`) to confirm build success

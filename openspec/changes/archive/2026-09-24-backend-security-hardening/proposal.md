## Why

While AquaFlow has initial JWT authentication support, the backend requires comprehensive security hardening across role permissions (`ADMIN`, `OPERATOR`, `VIEWER`, `EDGE_NODE`), resource-level authorization boundaries, LoRaWAN webhook authentication, device credential protection, secret configuration management via environment variables, security headers, rate-limiting boundaries, and prevention of credential leakage in logs and API responses.

## What Changes

- Implement granular Spring Security configuration enforcing explicit role permissions (`ROLE_ADMIN`, `ROLE_OPERATOR`, `ROLE_VIEWER`, `ROLE_EDGE_NODE`).
- Separate human operator JWT authentication from edge-node device token / HMAC payload authentication.
- Secure LoRaWAN webhooks (ChirpStack / TTN) using secret header verification (`X-Downlink-Secret` / `X-Webhook-Secret`).
- Mask sensitive credentials (commissioning tokens, LoRaWAN AppKeys, JWT secrets) so they are never written to logs or returned in normal API responses.
- Configure security headers (HSTS, CSP, X-Frame-Options, X-Content-Type-Options) and restrictive CORS policies.
- Implement rate-limiting protection for authentication endpoints (`POST /api/v1/auth/login`, `POST /api/v1/auth/register`).
- Standardize authentication (401 Unauthorized) and authorization (403 Forbidden) exception handlers and error JSON payloads.
- Externalize all secrets (JWT secret key, webhook tokens, database credentials) to environment variables with fallback defaults for local development.

## Capabilities

### Modified Capabilities
- `auth-security`: Hardens Spring Security configuration, role permissions (`ADMIN`, `OPERATOR`, `VIEWER`, `EDGE_NODE`), webhook authentication, secret masking, security headers, CORS, and externalized secret configuration.

## Impact

- Security & Infrastructure: Updates `SecurityConfig`, `JwtAuthenticationFilter`, `JwtTokenProvider`, and adds `WebhookAuthenticationFilter` and `SecurityHeadersFilter`.
- APIs & Controllers: Applies explicit `@PreAuthorize` or request matcher authority checks on administrative, operator, viewer, and webhook endpoints.
- Configuration: Externalizes secrets in `application.yml` via environment variables (`JWT_SECRET`, `LORAWAN_WEBHOOK_SECRET`).
- Testing: Adds comprehensive Spring Security authorization unit and integration tests.


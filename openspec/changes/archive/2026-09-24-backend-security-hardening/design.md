## Context

See `proposal.md` for motivation. The backend currently uses JWT authentication, but endpoint authorization rules, webhook signature validation, security headers, rate limiting, and secret masking require comprehensive security hardening.

## Goals / Non-Goals

**Goals:**
- Enforce explicit Spring Security authorization rules for `ROLE_ADMIN`, `ROLE_OPERATOR`, `ROLE_VIEWER`, and `ROLE_EDGE_NODE`.
- Implement `WebhookAuthenticationFilter` to validate LoRaWAN webhook headers (`X-Webhook-Secret` / `X-Downlink-Secret`).
- Implement `SecurityHeadersFilter` adding HSTS, CSP, X-Frame-Options, and X-Content-Type-Options headers.
- Implement `CustomAuthenticationEntryPoint` (401) and `CustomAccessDeniedHandler` (403) returning standardized JSON error responses.
- Implement rate limiting boundary on authentication endpoints using in-memory token bucket or request counter.
- Create `SecuritySanitizer` utility to strip/mask sensitive secrets and tokens from logs and API payloads.
- Externalize configuration secrets (`JWT_SECRET`, `LORAWAN_WEBHOOK_SECRET`) to environment variables.

**Non-Goals:**
- Implementing third-party OAuth2/OIDC identity provider integration (maintaining native JWT auth).

## Decisions

### Decision 1: Endpoint Security Matrix & Spring Security Rules
- **Choice**:
  - `/api/v1/auth/**`: `permitAll()` (guarded by `RateLimitingFilter`)
  - `/api/v1/chirpstack/webhook/**`, `/api/v1/lora/webhook/**`: Validated by `WebhookAuthenticationFilter`
  - `/api/v1/audit/**`: `hasRole('ADMIN')`
  - `/api/v1/irrigation/emergency-stop`, `/api/v1/irrigation/manual/**`: `hasAnyRole('ADMIN', 'OPERATOR')`
  - `/api/v1/nodes/*/commission`, `/api/v1/nodes/*/decommission`, `/api/v1/nodes/*/replace`: `hasRole('ADMIN')`
  - `/api/v1/telemetry/uplink`: `hasAnyRole('ADMIN', 'OPERATOR', 'EDGE_NODE')`
  - `GET /api/v1/**`: `hasAnyRole('ADMIN', 'OPERATOR', 'VIEWER')`

### Decision 2: Webhook Authentication Filter (`WebhookAuthenticationFilter`)
- **Choice**: Filter executes before `JwtAuthenticationFilter` on webhook paths, validating `X-Webhook-Secret` or `X-Downlink-Secret` against `${AQUAFLOW_LORAWAN_WEBHOOK_SECRET:your-webhook-secret}`. If header matches, populates `SecurityContext` with `ROLE_EDGE_NODE` authority.

### Decision 3: Custom 401/403 Handling
- **Choice**: Register `CustomAuthenticationEntryPoint` and `CustomAccessDeniedHandler` in `SecurityConfig` to render consistent `ErrorResponse` JSON with 401 and 403 status codes respectively.

### Decision 4: Security Headers & CORS Configuration
- **Choice**: Add `SecurityHeadersFilter` to set:
  - `X-Content-Type-Options: nosniff`
  - `X-Frame-Options: DENY`
  - `X-XSS-Protection: 1; mode=block`
  - `Strict-Transport-Security: max-age=31536000; includeSubDomains`
  - `Content-Security-Policy: default-src 'self'`
  Configure CORS bean allowing configurable origins (`${AQUAFLOW_CORS_ALLOWED_ORIGINS:*}`).

## Risks / Trade-offs

- [Risk] Webhook requests rejected if secret header is misconfigured in network server.
  → **Mitigation**: Provide fallback development secret with warning log when default secret is active.


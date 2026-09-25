package com.aquaflow.backend.security;

import com.aquaflow.backend.infrastructure.security.*;
import com.aquaflow.backend.infrastructure.util.SecuritySanitizer;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BackendSecurityHardeningTest {

    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private SecurityHeadersFilter securityHeadersFilter;
    private WebhookAuthenticationFilter webhookAuthenticationFilter;
    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        authenticationEntryPoint = new CustomAuthenticationEntryPoint();
        accessDeniedHandler = new CustomAccessDeniedHandler();
        securityHeadersFilter = new SecurityHeadersFilter();

        webhookAuthenticationFilter = new WebhookAuthenticationFilter();

        ReflectionTestUtils.setField(
                webhookAuthenticationFilter,
                "configuredWebhookSecret",
                "your-webhook-secret"
        );

        rateLimitingFilter = new RateLimitingFilter();
    }

    @Test
    void testAuthenticationEntryPoint_Returns401Json() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/v1/audit/system");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        authenticationEntryPoint.commence(
                request,
                response,
                new InsufficientAuthenticationException("Access denied")
        );

        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType().contains("application/json"));
        assertTrue(response.getContentAsString().contains("UNAUTHORIZED"));
    }

    @Test
    void testAccessDeniedHandler_Returns403Json() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest("POST", "/api/v1/audit/export");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        accessDeniedHandler.handle(
                request,
                response,
                new AccessDeniedException("Forbidden")
        );

        assertEquals(403, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType().contains("application/json"));
        assertTrue(response.getContentAsString().contains("FORBIDDEN"));
    }

    @Test
    void testSecurityHeadersFilter_AppliesHeaders() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/v1/zones");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        securityHeadersFilter.doFilter(
                request,
                response,
                filterChain
        );

        assertEquals(
                "nosniff",
                response.getHeader("X-Content-Type-Options")
        );

        assertEquals(
                "DENY",
                response.getHeader("X-Frame-Options")
        );

        assertEquals(
                "1; mode=block",
                response.getHeader("X-XSS-Protection")
        );

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    @Test
    void testWebhookAuthenticationFilter_ValidSecret() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "POST",
                        "/api/v1/chirpstack/webhook/uplink"
                );

        request.addHeader(
                "X-Webhook-Secret",
                "your-webhook-secret"
        );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        webhookAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        assertNotNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        assertTrue(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority
                                                .getAuthority()
                                                .equals("ROLE_EDGE_NODE")
                        )
        );

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    @Test
    void testWebhookAuthenticationFilter_InvalidSecret() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "POST",
                        "/api/v1/chirpstack/webhook/uplink"
                );

        request.addHeader(
                "X-Webhook-Secret",
                "wrong-secret"
        );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        webhookAuthenticationFilter.doFilter(
                request,
                response,
                filterChain
        );

        assertEquals(401, response.getStatus());

        assertTrue(
                response
                        .getContentAsString()
                        .contains(
                                "Invalid or missing webhook authentication secret"
                        )
        );

        verify(filterChain, never())
                .doFilter(request, response);
    }

    @Test
    void testRateLimitingFilter_AllowsRequestWithinLimit() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "GET",
                        "/api/v1/zones"
                );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        rateLimitingFilter.doFilter(
                request,
                response,
                filterChain
        );

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    @Test
    void testSecuritySanitizer_MasksSecrets() {
        assertEquals(
                "****",
                SecuritySanitizer.maskSecret("123")
        );

        assertEquals(
                "se****et",
                SecuritySanitizer.maskSecret("secret")
        );

        String json =
                "{\"password\":\"mysecretpassword\",\"user\":\"admin\"}";

        String sanitized =
                SecuritySanitizer.sanitizeLog(json);

        assertTrue(
                sanitized.contains(
                        "\"password\":\"****\""
                )
        );
    }
}
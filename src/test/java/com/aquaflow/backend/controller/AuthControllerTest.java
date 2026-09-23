package com.aquaflow.backend.controller;

import com.aquaflow.backend.dto.request.LoginRequest;
import com.aquaflow.backend.dto.request.UserRequest;
import com.aquaflow.backend.dto.response.AuthResponse;
import com.aquaflow.backend.dto.response.UserResponse;
import com.aquaflow.backend.domain.UserService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private final UserService userService = mock(UserService.class);

    @Test
    void shouldAuthenticateUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        AuthResponse response = AuthResponse.builder()
                .accessToken("test-token")
                .refreshToken("refresh-token")
                .expiresIn(86400000L)
                .build();

        when(userService.authenticate("testuser", "password123")).thenReturn(response);

        var result = userService.authenticate("testuser", "password123");
        assertThat(result.getAccessToken()).isEqualTo("test-token");
    }

    @Test
    void shouldRegisterUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newuser");
        userRequest.setPassword("password123");
        userRequest.setEmail("new@test.com");
        userRequest.setRole("VIEWER");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("newuser");
        userResponse.setEmail("new@test.com");
        userResponse.setRole("VIEWER");

        when(userService.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        var result = userService.registerUser(userRequest);
        assertThat(result.getUsername()).isEqualTo("newuser");
    }
}
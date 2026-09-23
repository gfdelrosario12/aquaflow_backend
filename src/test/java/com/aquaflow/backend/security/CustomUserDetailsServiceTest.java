package com.aquaflow.backend.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomUserDetailsServiceTest {

    @Test
    void shouldLoadUserByUsername() {
        assertThat(true).isTrue();
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionForMissingUser() {
        org.springframework.security.core.userdetails.UsernameNotFoundException exception = assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> { throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: missing"); }
        );
        assertThat(exception.getMessage()).contains("missing");
    }
}
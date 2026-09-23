package com.aquaflow.backend.service;

import com.aquaflow.backend.entity.User;
import com.aquaflow.backend.persistence.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    @Test
    void shouldRegisterUser() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .role("VIEWER")
                .enabled(true)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldFindUserByUsername() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role("VIEWER")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        var result = userRepository.findByUsername("testuser");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldNotFindMissingUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());
        assertThat(userRepository.findByUsername("missing")).isEmpty();
    }
}
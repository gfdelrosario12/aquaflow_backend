package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.RegisterRequest;
import com.aquaflow.backend.dto.request.UserRequest;
import com.aquaflow.backend.dto.response.UserResponse;
import com.aquaflow.backend.dto.response.AuthResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);
    UserResponse registerUser(RegisterRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    AuthResponse authenticate(String username, String password);
    AuthResponse refreshToken(String refreshToken);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
}
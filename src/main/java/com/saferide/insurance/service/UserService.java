package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.LoginRequest;
import com.saferide.insurance.dto.request.PasswordChangeRequest;
import com.saferide.insurance.dto.request.RegisterRequest;
import com.saferide.insurance.dto.response.AuthResponse;
import com.saferide.insurance.entity.user.User;

import java.util.List;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    void changePassword(Long userId, PasswordChangeRequest request);
    void setUserActiveStatus(Long userId, boolean active);
    void deleteUser(Long userId);
}

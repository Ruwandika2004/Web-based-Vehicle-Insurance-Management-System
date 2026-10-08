package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.LoginRequest;
import com.saferide.insurance.dto.request.PasswordChangeRequest;
import com.saferide.insurance.dto.request.RegisterRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.dto.response.AuthResponse;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller: Shared Authentication & User Management
 * Common Component across all group members.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful! Welcome to SafeRide Insurance.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful!", response));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved", users));
    }

    @PostMapping("/change-password/{userId}")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long userId,
                                                            @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable Long userId,
                                                              @RequestParam boolean active) {
        userService.setUserActiveStatus(userId, active);
        String msg = active ? "User account activated" : "User account deactivated";
        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User account deleted successfully", null));
    }
}

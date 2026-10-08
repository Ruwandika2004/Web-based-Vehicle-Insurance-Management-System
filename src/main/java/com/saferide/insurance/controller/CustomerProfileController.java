package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.CustomerProfileRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.entity.customer.CustomerProfile;
import com.saferide.insurance.service.CustomerProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class CustomerProfileController {

    private final CustomerProfileService profileService;

    public CustomerProfileController(CustomerProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerProfile>> createProfile(@Valid @RequestBody CustomerProfileRequest request) {
        CustomerProfile profile = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer profile created successfully", profile));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CustomerProfile>> getProfileByUserId(@PathVariable Long userId) {
        CustomerProfile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerProfile>> getProfileById(@PathVariable Long id) {
        CustomerProfile profile = profileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerProfile>>> getAllProfiles() {
        List<CustomerProfile> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(ApiResponse.success("All customer profiles retrieved", profiles));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CustomerProfile>> updateProfile(@PathVariable Long userId,
                                                                      @Valid @RequestBody CustomerProfileRequest request) {
        CustomerProfile profile = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long userId) {
        profileService.deleteProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile removed/deactivated successfully", null));
    }
}

package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.CustomerProfileRequest;
import com.saferide.insurance.entity.customer.CustomerProfile;

import java.util.List;

public interface CustomerProfileService {
    CustomerProfile createProfile(CustomerProfileRequest request);
    CustomerProfile getProfileByUserId(Long userId);
    CustomerProfile getProfileById(Long id);
    List<CustomerProfile> getAllProfiles();
    CustomerProfile updateProfile(Long userId, CustomerProfileRequest request);
    void deleteProfile(Long userId);
}

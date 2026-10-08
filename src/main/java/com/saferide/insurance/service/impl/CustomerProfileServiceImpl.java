package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.CustomerProfileRequest;
import com.saferide.insurance.entity.customer.CustomerProfile;
import com.saferide.insurance.entity.user.User;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.repository.CustomerProfileRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.service.CustomerProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation: Customer Profile Management (Deshanya T. - IT25102295)
 */
@Service
@Transactional
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerProfileRepository profileRepository;
    private final UserRepository userRepository;

    public CustomerProfileServiceImpl(CustomerProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CustomerProfile createProfile(CustomerProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        if (profileRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new BadRequestException("Profile already exists for this user. Use update instead.");
        }

        if (profileRepository.existsByNicPassport(request.getNicPassport())) {
            throw new BadRequestException("NIC/Passport number is already registered to another profile.");
        }

        if (profileRepository.existsByDrivingLicenseNo(request.getDrivingLicenseNo())) {
            throw new BadRequestException("Driving License number is already registered to another profile.");
        }

        CustomerProfile profile = new CustomerProfile(
                user,
                request.getNicPassport(),
                request.getDateOfBirth(),
                request.getDrivingLicenseNo(),
                request.getLicenseExpiryDate(),
                request.getResidentialAddress(),
                request.getCity(),
                request.getPostalCode(),
                request.getOccupation(),
                request.getEmergencyContactName(),
                request.getEmergencyContactPhone()
        );

        return profileRepository.save(profile);
    }

    @Override
    public CustomerProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user ID: " + userId));
    }

    @Override
    public CustomerProfile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found with ID: " + id));
    }

    @Override
    public List<CustomerProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Override
    public CustomerProfile updateProfile(Long userId, CustomerProfileRequest request) {
        CustomerProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
                    CustomerProfile newP = new CustomerProfile();
                    newP.setUser(user);
                    return newP;
                });

        if (request.getNicPassport() != null) profile.setNicPassport(request.getNicPassport());
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getDrivingLicenseNo() != null) profile.setDrivingLicenseNo(request.getDrivingLicenseNo());
        if (request.getLicenseExpiryDate() != null) profile.setLicenseExpiryDate(request.getLicenseExpiryDate());
        if (request.getResidentialAddress() != null) profile.setResidentialAddress(request.getResidentialAddress());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getPostalCode() != null) profile.setPostalCode(request.getPostalCode());
        if (request.getOccupation() != null) profile.setOccupation(request.getOccupation());
        if (request.getEmergencyContactName() != null) profile.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) profile.setEmergencyContactPhone(request.getEmergencyContactPhone());

        return profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long userId) {
        CustomerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user ID: " + userId));
        profileRepository.delete(profile);
    }
}

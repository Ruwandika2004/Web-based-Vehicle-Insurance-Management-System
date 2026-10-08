package com.saferide.insurance.service.impl;

import com.saferide.insurance.dto.request.LoginRequest;
import com.saferide.insurance.dto.request.PasswordChangeRequest;
import com.saferide.insurance.dto.request.RegisterRequest;
import com.saferide.insurance.dto.response.AuthResponse;
import com.saferide.insurance.entity.customer.CustomerProfile;
import com.saferide.insurance.entity.user.*;
import com.saferide.insurance.exception.BadRequestException;
import com.saferide.insurance.exception.ResourceNotFoundException;
import com.saferide.insurance.exception.UnauthorizedException;
import com.saferide.insurance.repository.CustomerProfileRepository;
import com.saferide.insurance.repository.UserRepository;
import com.saferide.insurance.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerProfileRepository profileRepository;

    public UserServiceImpl(UserRepository userRepository, CustomerProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username '" + request.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' is already registered.");
        }

        UserRole role = request.getRole() != null ? request.getRole() : UserRole.ROLE_CUSTOMER;
        User user;

        if (role == UserRole.ROLE_ADMIN) {
            user = new Admin(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone(), "SUPER_ADMIN", "Executive Administration");
        } else if (role == UserRole.ROLE_INSURANCE_AGENT) {
            user = new InsuranceAgent(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone(), "AG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Headquarters", new java.math.BigDecimal("5.00"));
        } else if (role == UserRole.ROLE_CLAIMS_OFFICER) {
            user = new ClaimsOfficer(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone(), "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Claims Operations", new java.math.BigDecimal("500000.00"));
        } else if (role == UserRole.ROLE_FINANCE_OFFICER) {
            user = new FinanceOfficer(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone(), "FIN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Finance & Payments", new java.math.BigDecimal("2000000.00"));
        } else if (role == UserRole.ROLE_BRANCH_MANAGER) {
            user = new BranchManager(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone(), "MGR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    "Colombo Main Branch", "BR-CMB-001");
        } else {
            user = new Customer(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFullName(), request.getPhone());
        }

        User savedUser = userRepository.save(user);

        // If Customer and profile fields provided, create default profile
        if (role == UserRole.ROLE_CUSTOMER) {
            String nic = (request.getNicPassport() != null && !request.getNicPassport().trim().isEmpty())
                    ? request.getNicPassport() : "NIC-" + savedUser.getId() + "V";
            String license = (request.getDrivingLicenseNo() != null && !request.getDrivingLicenseNo().trim().isEmpty())
                    ? request.getDrivingLicenseNo() : "B" + (1000000 + savedUser.getId());
            String address = (request.getResidentialAddress() != null && !request.getResidentialAddress().trim().isEmpty())
                    ? request.getResidentialAddress() : "Colombo, Sri Lanka";
            String city = (request.getCity() != null && !request.getCity().trim().isEmpty())
                    ? request.getCity() : "Colombo";

            CustomerProfile profile = new CustomerProfile(
                    savedUser,
                    nic,
                    LocalDate.of(1995, 1, 1),
                    license,
                    LocalDate.now().plusYears(5),
                    address,
                    city,
                    "00100",
                    "Professional",
                    "Emergency Contact",
                    savedUser.getPhone()
            );
            profileRepository.save(profile);
        }

        String token = "SR-TOKEN-" + UUID.randomUUID().toString();
        return new AuthResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getFullName(), savedUser.getPhone(), savedUser.getRole(),
                savedUser.getRoleDescription(), token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username/email or password"));

        if (!user.isActive()) {
            throw new UnauthorizedException("Your account is deactivated. Please contact support.");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("Invalid username/email or password");
        }

        String token = "SR-TOKEN-" + UUID.randomUUID().toString();
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getFullName(), user.getPhone(), user.getRole(),
                user.getRoleDescription(), token);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = getUserById(userId);
        if (!user.getPassword().equals(request.getCurrentPassword())) {
            throw new BadRequestException("Current password does not match");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public void setUserActiveStatus(Long userId, boolean active) {
        User user = getUserById(userId);
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}

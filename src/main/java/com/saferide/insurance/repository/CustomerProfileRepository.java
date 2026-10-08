package com.saferide.insurance.repository;

import com.saferide.insurance.entity.customer.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    Optional<CustomerProfile> findByUserId(Long userId);
    Optional<CustomerProfile> findByNicPassport(String nicPassport);
    Optional<CustomerProfile> findByDrivingLicenseNo(String drivingLicenseNo);
    boolean existsByNicPassport(String nicPassport);
    boolean existsByDrivingLicenseNo(String drivingLicenseNo);
}
